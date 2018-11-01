package com.tokosepeda.tikum.activity.admin.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailUserActivity extends AppCompatActivity {

    @BindView(R.id.image_foto)
    CircularImageView imageFoto;
    @BindView(R.id.txt_nama)
    TextView txtNama;
    @BindView(R.id.txt_id)
    TextView txtId;
    @BindView(R.id.txt_jenis_kelamin)
    TextView txtJenisKelamin;
    @BindView(R.id.txt_ttl)
    TextView txtTTL;
    @BindView(R.id.txt_no_hp)
    TextView txtNoHp;
    @BindView(R.id.txt_email)
    TextView txtEmail;
    @BindView(R.id.txt_alamat)
    TextView txtAlamat;
    @BindView(R.id.txt_sepeda)
    TextView txtSepeda;
    @BindView(R.id.txt_outfit)
    TextView txtOutfit;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        ButterKnife.bind(this);

        user = (User) getIntent().getSerializableExtra("user");

        initToolbar();
        initUI();
    }

    private void initToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail User");
        getSupportActionBar().setSubtitle(user.getNamaUser());
    }

    private void initUI() {
        Glide.with(getApplicationContext()).load(user.getFoto()).into(imageFoto);
        txtNama.setText(user.getNamaUser());
        txtId.setText("#"+user.getIdUser());
        txtJenisKelamin.setText(user.getJenisKelamin());
        txtTTL.setText(user.getTempatLahir()+", "+user.getTanggalLahir());
        txtNoHp.setText(user.getNomorHp());
        txtEmail.setText(user.getEmail());
        txtAlamat.setText(user.getAlamat());
        txtSepeda.setText(user.getSepeda());
        txtOutfit.setText(user.getOutfit());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
