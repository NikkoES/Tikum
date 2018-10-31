package com.tokosepeda.tikum.activity.friend;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.adapter.TemanAdapter;
import com.tokosepeda.tikum.firebase.FirebaseApplication;
import com.tokosepeda.tikum.model.Teman;
import com.tokosepeda.tikum.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CariTemanActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @BindView(R.id.rv_teman)
    RecyclerView rvTeman;
    @BindView(R.id.search_id)
    SearchView searchView;
    @BindView(R.id.txt_info)
    TextView txtInfo;

    DatabaseReference dbUser, dbTeman;
    FirebaseAuth mAuth;

    String id, idUser;

    private TemanAdapter mAdapter;
    List<User> listUser = new ArrayList<>();
    List<User> listPertemanan = new ArrayList<>();
    List<Teman> listTeman = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari_teman);
        ButterKnife.bind(this);

        mAuth = ((FirebaseApplication) getApplication()).getFirebaseAuth();
        id = mAuth.getCurrentUser().getUid();

        dbUser = FirebaseDatabase.getInstance().getReference("users");
        dbTeman = FirebaseDatabase.getInstance().getReference("friends").child(id);

        initToolbar();
        initSearchView();
        initRecyclerView();
    }

    private void initSearchView() {
        searchView.setQueryHint("Pencarian ID");
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.clearFocus();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchTeman(query);
        return false;
    }

    private void searchTeman(final String query) {
        for (int i = 0; i < listTeman.size(); i++) {
            Teman teman = listTeman.get(i);
            if (teman.getIdUser().equalsIgnoreCase(query)) {
                showDialogUnfollow(teman);
            }
        }
        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    if (user.getIdUser().equalsIgnoreCase(query)) {
                        showDialogFollow(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showDialogFollow(final User user) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog_follow, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        final ImageView imageFoto = promptView.findViewById(R.id.image_foto);
        final TextView txtNama = promptView.findViewById(R.id.txt_nama);
        final TextView txtIdUser = promptView.findViewById(R.id.txt_id_user);
        final TextView txtJenisKelamin = promptView.findViewById(R.id.txt_jenis_kelamin);
        final Button btnAction = promptView.findViewById(R.id.btn_action);

        Glide.with(this).load(user.getFoto()).into(imageFoto);
        txtNama.setText(user.getNamaUser());
        txtIdUser.setText(user.getIdUser());
        txtJenisKelamin.setText(user.getJenisKelamin());
        btnAction.setText("Follow");

        alertDialogBuilder.setCancelable(true);

        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = dbTeman.push().getKey();
                Teman teman = new Teman(
                        user.getId(),
                        user.getIdUser(),
                        user.getNamaUser(),
                        user.getEmail(),
                        user.getNomorHp(),
                        user.getJenisKelamin(),
                        user.getAlamat(),
                        user.getTempatLahir(),
                        user.getTanggalLahir(),
                        user.getFoto(),
                        user.getSepeda(),
                        user.getOutfit()
                );
                teman.setIdFriend(key);
                dbTeman.child(key).setValue(teman);
                alert.dismiss();
            }
        });
    }

    private void showDialogUnfollow(final Teman teman) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog_unfollow, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        final ImageView imageFoto = promptView.findViewById(R.id.image_foto);
        final TextView txtNama = promptView.findViewById(R.id.txt_nama);
        final TextView txtIdUser = promptView.findViewById(R.id.txt_id_user);
        final TextView txtJenisKelamin = promptView.findViewById(R.id.txt_jenis_kelamin);
        final Button btnDetail = promptView.findViewById(R.id.btn_detail);
        final Button btnUnfollow = promptView.findViewById(R.id.btn_unfollow);

        Glide.with(this).load(teman.getFoto()).into(imageFoto);
        txtNama.setText(teman.getNamaUser());
        txtIdUser.setText(teman.getIdUser());
        txtJenisKelamin.setText(teman.getJenisKelamin());
        btnDetail.setText("Detail");
        btnUnfollow.setText("Unfollow");

        dbTeman.child(teman.getIdFriend());

        alertDialogBuilder.setCancelable(true);

        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        btnUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbTeman.removeValue();
                mAdapter.notifyDataSetChanged();
                alert.dismiss();
            }
        });
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CariTemanActivity.this, DetailTemanActivity.class);
                i.putExtra("teman", teman);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void initRecyclerView() {
        rvTeman.setHasFixedSize(true);
        rvTeman.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TemanAdapter(this, listPertemanan);
        rvTeman.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbTeman.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listTeman.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Teman teman = postSnapshot.getValue(Teman.class);
                    listTeman.add(teman);
                }
                dbUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listPertemanan.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            User user = postSnapshot.getValue(User.class);
                            for (int i = 0; i < listTeman.size(); i++) {
                                Teman teman = listTeman.get(i);
                                if (teman.getId().equalsIgnoreCase(user.getId())) {
                                    listPertemanan.add(user);
                                }
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mAdapter.notifyDataSetChanged();
                if (listTeman.size() == 0) {
                    txtInfo.setVisibility(View.VISIBLE);
                } else {
                    txtInfo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Daftar Teman");
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
