package com.tokosepeda.tikum.activity.admin.toko;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.model.Toko;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailTokoAdminActivity extends AppCompatActivity {

    @BindView(R.id.image_toko)
    ImageView imageFoto;
    @BindView(R.id.txt_nama_toko)
    TextView txtNamaToko;
    @BindView(R.id.txt_email_toko)
    TextView txtEmailToko;
    @BindView(R.id.txt_nomor_toko)
    TextView txtNomorToko;
    @BindView(R.id.txt_alamat_toko)
    TextView txtAlamat;
    @BindView(R.id.txt_sparepart)
    TextView txtSparepart;

    Toko toko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_toko2);
        ButterKnife.bind(this);

        toko = (Toko) getIntent().getSerializableExtra("toko");

        initToolbar();
        initUI();
    }

    private void initToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Toko");
        getSupportActionBar().setSubtitle(toko.getNamaToko());
    }

    private void initUI() {
        Glide.with(getApplicationContext()).load(toko.getImageToko()).into(imageFoto);
        txtNamaToko.setText(toko.getNamaToko());
        txtEmailToko.setText(toko.getEmailToko());
        txtNomorToko.setText(toko.getNoHp());
        txtAlamat.setText(toko.getAlamatToko());
        txtSparepart.setText(toko.getSparePart());
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
