package com.tokosepeda.tikum.activity.friend;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailTemanActivity extends AppCompatActivity {

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

    User teman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_teman);
        ButterKnife.bind(this);

        teman = (User) getIntent().getSerializableExtra("teman");

        initToolbar();
        initUI();
    }

    private void initToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Teman");
        getSupportActionBar().setSubtitle(teman.getNamaUser());
    }

    private void initUI() {
        Glide.with(this).load(teman.getFoto()).into(imageFoto);
        txtNama.setText(teman.getNamaUser());
        txtId.setText("#"+teman.getIdUser());
        txtJenisKelamin.setText(teman.getJenisKelamin());
        txtTTL.setText(teman.getTempatLahir()+", "+teman.getTanggalLahir());
        txtNoHp.setText(teman.getNomorHp());
        txtEmail.setText(teman.getEmail());
        txtAlamat.setText(teman.getAlamat());
        txtSepeda.setText(teman.getSepeda());
        txtOutfit.setText(teman.getOutfit());
    }

    @OnClick({R.id.btn_kontak, R.id.btn_whatsapp})
    public void actionButton(View v) {
        switch (v.getId()) {
            case R.id.btn_kontak:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+teman.getNomorHp()));
                startActivity(intent);
                break;
            case R.id.btn_whatsapp:
                Intent sendIntent =new Intent("android.intent.action.MAIN");
                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT,"");
                sendIntent.putExtra("jid", teman.getNomorHp() +"@s.whatsapp.net");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home : {
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
