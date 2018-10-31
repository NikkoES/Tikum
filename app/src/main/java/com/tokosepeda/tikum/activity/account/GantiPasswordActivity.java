package com.tokosepeda.tikum.activity.account;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.firebase.FirebaseApplication;
import com.tokosepeda.tikum.model.User;
import com.tokosepeda.tikum.utils.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GantiPasswordActivity extends AppCompatActivity {

    @BindView(R.id.et_password_lama)
    EditText etPasswordLama;
    @BindView(R.id.et_password_baru)
    EditText etPasswordBaru;
    @BindView(R.id.et_konfirmasi_password)
    EditText etKonfirmasiPassword;

    FirebaseAuth mAuth;

    private ProgressDialog loading;

    String email, passwordLama, passwordBaru, konfirmasiPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);
        ButterKnife.bind(this);

        loading = DialogUtils.showProgressDialog(this, "Loading", "Changing Password..");

        mAuth = ((FirebaseApplication) getApplication()).getFirebaseAuth();
        email = mAuth.getCurrentUser().getEmail();

        initToolbar();
    }

    @OnClick(R.id.btn_simpan)
    public void buttonSimpan() {
        loading.show();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (!TextUtils.isEmpty(etPasswordLama.getText().toString())) {
            passwordLama = etPasswordLama.getText().toString();
        } else {
            Toast.makeText(this, "Password Lama tidak boleh kosong !", Toast.LENGTH_SHORT).show();
        }
        if (!TextUtils.isEmpty(etPasswordBaru.getText().toString())) {
            passwordBaru = etPasswordBaru.getText().toString();
        } else {
            Toast.makeText(this, "Password Baru tidak boleh kosong !", Toast.LENGTH_SHORT).show();
        }
        if (!TextUtils.isEmpty(etKonfirmasiPassword.getText().toString())) {
            konfirmasiPassword = etKonfirmasiPassword.getText().toString();
        } else {
            Toast.makeText(this, "Konfirmasi Password tidak boleh kosong !", Toast.LENGTH_SHORT).show();
        }
        if (!passwordBaru.equalsIgnoreCase(konfirmasiPassword)) {
            Toast.makeText(this, "Password tidak match !", Toast.LENGTH_SHORT).show();
        } else {
            AuthCredential credential = EmailAuthProvider.getCredential(email, passwordLama);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.updatePassword(passwordBaru).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    loading.dismiss();
                                    Toast.makeText(GantiPasswordActivity.this, "Password berhasil diubah..", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(GantiPasswordActivity.this, "Password gagal diubah !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(GantiPasswordActivity.this, "Password Lama salah !", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void initToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ganti Password");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
