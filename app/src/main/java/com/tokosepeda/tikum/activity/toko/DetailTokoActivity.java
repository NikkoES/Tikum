package com.tokosepeda.tikum.activity.toko;

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
import com.bumptech.glide.util.Util;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.model.Toko;
import com.tokosepeda.tikum.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailTokoActivity extends AppCompatActivity {

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
    String jarak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_toko);
        ButterKnife.bind(this);

        toko = (Toko) getIntent().getSerializableExtra("toko");
        jarak = getIntent().getStringExtra("jarak");

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
        txtAlamat.setText(toko.getAlamatToko() + "\n(Jarak : " + jarak + "km)");
        txtSparepart.setText(toko.getSparePart());
    }

    @OnClick({R.id.btn_kontak, R.id.btn_whatsapp})
    public void actionButton(View v) {
        switch (v.getId()) {
            case R.id.btn_kontak:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+toko.getNoHp()));
                startActivity(intent);
                break;
            case R.id.btn_whatsapp:
                String phone = toko.getNoHp();
                String waPhone;
                if(phone.substring(0,1).equalsIgnoreCase("0")){
                    waPhone = "62" + phone.substring(1);
                }
                else{
                    waPhone = phone;
                }
                String urlWhatsapp = "https://api.whatsapp.com/send?phone="+waPhone;
                Intent waIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlWhatsapp));
                startActivity(waIntent);
                break;
            case R.id.btn_navigasi:
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+toko.getLatToko()+","+toko.getLongToko()+"");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;
        }
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
