package com.tokosepeda.tikum.activity.friend;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.firebase.FirebaseApplication;
import com.tokosepeda.tikum.model.Teman;
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
    @BindView(R.id.txt_no_hp)
    TextView txtNoHp;
    @BindView(R.id.txt_email)
    TextView txtEmail;
    @BindView(R.id.txt_sepeda)
    TextView txtSepeda;

    @BindView(R.id.btn_follow_unfollow)
    Button btnFollowUnFollow;

    FirebaseAuth mAuth;

    String id;

    DatabaseReference dbTeman;
    Teman teman;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_teman);
        ButterKnife.bind(this);

        if(getIntent().hasExtra("teman")){
            teman = (Teman) getIntent().getSerializableExtra("teman");
            btnFollowUnFollow.setText("Unfollow");
        }
        else if(getIntent().hasExtra("user")){
            user = (User) getIntent().getSerializableExtra("user");
            btnFollowUnFollow.setText("Follow");
        }
        else if(getIntent().hasExtra("detail_teman")){
            user = (User) getIntent().getSerializableExtra("detail_teman");
            btnFollowUnFollow.setText("Unfollow");
        }

        mAuth = ((FirebaseApplication) getApplication()).getFirebaseAuth();
        id = mAuth.getCurrentUser().getUid();
        dbTeman = FirebaseDatabase.getInstance().getReference("friends").child(id);

        initToolbar();
        initUI();
    }

    private void initToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Bikers");
        if(getIntent().hasExtra("teman")){
            getSupportActionBar().setSubtitle(teman.getNamaUser());
        }
        else if(getIntent().hasExtra("user") || getIntent().hasExtra("detail_teman")){
            getSupportActionBar().setSubtitle(user.getNamaUser());
        }
    }

    private void initUI() {
        if(getIntent().hasExtra("teman")){
            Glide.with(getApplicationContext()).load(teman.getFoto()).into(imageFoto);
            txtNama.setText(teman.getNamaUser());
            txtId.setText("#"+teman.getIdUser());
            txtNoHp.setText(teman.getNomorHp());
            txtEmail.setText(teman.getEmail());
            txtSepeda.setText(teman.getSepeda());
        }
        else if(getIntent().hasExtra("user") || getIntent().hasExtra("detail_teman")){
            Glide.with(getApplicationContext()).load(user.getFoto()).into(imageFoto);
            txtNama.setText(user.getNamaUser());
            txtId.setText("#"+user.getIdUser());
            txtNoHp.setText(user.getNomorHp());
            txtEmail.setText(user.getEmail());
            txtSepeda.setText(user.getSepeda());
        }
    }

    @OnClick(R.id.btn_follow_unfollow)
    public void actionFollowUnfollow(){
        if(btnFollowUnFollow.getText().toString().equalsIgnoreCase("unfollow")){ //sudah difollow
            if(getIntent().hasExtra("detail_teman")){
                dbTeman = dbTeman.child(user.getId());
            }
            else{
                dbTeman = dbTeman.child(teman.getIdFriend());
            }
            dbTeman.removeValue();
            btnFollowUnFollow.setText("follow");
            finish();
        }
        else if(btnFollowUnFollow.getText().toString().equalsIgnoreCase("follow")){ //belum difollow
            String key = dbTeman.push().getKey();
            Teman teman = new Teman(
                    user.getId(),
                    user.getIdUser(),
                    user.getNamaUser(),
                    user.getEmail(),
                    user.getNomorHp(),
                    user.getFoto(),
                    user.getSepeda()
            );
            teman.setIdFriend(user.getId());
            dbTeman.child(user.getId()).setValue(teman);
            btnFollowUnFollow.setText("unfollow");
            finish();
        }
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
                String phone = teman.getNomorHp();
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
