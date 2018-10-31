package com.tokosepeda.tikum.activity.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.activity.admin.toko.ManageTokoActivity;
import com.tokosepeda.tikum.activity.admin.user.ManageUserActivity;
import com.tokosepeda.tikum.activity.auth.LoginActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);
        ButterKnife.bind(this);

        initToolbar();
    }

    private void initToolbar() {
        getSupportActionBar().setTitle("Admin Tikum");
    }

    @OnClick(R.id.btn_user)
    public void buttonUser(){
        Intent i = new Intent(this, ManageUserActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btn_toko)
    public void buttonToko(){
        Intent i = new Intent(this, ManageTokoActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btn_keluar)
    public void buttonKeluar(){
        SharedPreferences.Editor editor = getSharedPreferences("session", MODE_PRIVATE).edit();
        editor.putString("user", "user");
        editor.apply();

        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
