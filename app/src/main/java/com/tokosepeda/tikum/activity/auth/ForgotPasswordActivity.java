package com.tokosepeda.tikum.activity.auth;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.firebase.FirebaseApplication;
import com.tokosepeda.tikum.utils.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.et_email)
    EditText etEmail;

    private ProgressDialog loading;

    private FirebaseAuth mAuth;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ButterKnife.bind(this);
        loading = DialogUtils.showProgressDialog(this, "Loading", "Reseting your password");

        mAuth = ((FirebaseApplication)getApplication()).getFirebaseAuth();
    }

    @OnClick(R.id.btn_kirim)
    public void resetPassword(){
        loading.show();
        if(TextUtils.isEmpty(etEmail.getText().toString())){
            Toast.makeText(getApplicationContext(), getString(R.string.data_belum_lengkap), Toast.LENGTH_SHORT).show();
            loading.dismiss();
        }
        else{
            new AlertDialog.Builder(ForgotPasswordActivity.this)
                    .setTitle(getString(R.string.yakin_ingin_reset_password))
                    .setPositiveButton(getString(R.string.ya), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            email = etEmail.getText().toString();
                            ((FirebaseApplication)getApplication()).resetPassword(ForgotPasswordActivity.this, email);
                            loading.dismiss();
                        }
                    })
                    .setNegativeButton(getString(R.string.batal), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    @OnClick(R.id.btn_to_login)
    public void toLogin(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
