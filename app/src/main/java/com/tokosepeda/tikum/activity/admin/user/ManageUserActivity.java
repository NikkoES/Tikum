package com.tokosepeda.tikum.activity.admin.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.adapter.UserAdminAdapter;
import com.tokosepeda.tikum.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageUserActivity extends AppCompatActivity {

    @BindView(R.id.rv_user)
    RecyclerView rvUser;

    private UserAdminAdapter mAdapter;
    List<User> listAllUser = new ArrayList<>();

    DatabaseReference dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);

        ButterKnife.bind(this);

        dbUser = FirebaseDatabase.getInstance().getReference("users");

        initToolbar();
        initRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listAllUser.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    listAllUser.add(user);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.btn_add)
    public void buttonAdd(){
        Intent i = new Intent(this, FormUserActivity.class);
        i.putExtra("mode", "add");
        startActivity(i);
    }

    private void initToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Manage User");
    }

    private void initRecyclerView() {
        rvUser.setHasFixedSize(true);
        rvUser.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new UserAdminAdapter(this, listAllUser);
        rvUser.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
