package com.tokosepeda.tikum.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.activity.friend.DetailTemanActivity;
import com.tokosepeda.tikum.adapter.TemanAdapter;
import com.tokosepeda.tikum.firebase.FirebaseApplication;
import com.tokosepeda.tikum.model.Teman;
import com.tokosepeda.tikum.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TemanFragment extends Fragment implements SearchView.OnQueryTextListener {

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

    public TemanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_teman, container, false);

        ButterKnife.bind(this, v);

        mAuth = ((FirebaseApplication) getActivity().getApplication()).getFirebaseAuth();
        id = mAuth.getCurrentUser().getUid();

        dbUser = FirebaseDatabase.getInstance().getReference("users");
        dbTeman = FirebaseDatabase.getInstance().getReference("friends").child(id);

        initTemanData();
        initSearchView();
        initRecyclerView();

        return v;
    }

    private void initSearchView() {
        searchView.setQueryHint("Pencarian ID Teman");
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.clearFocus();
    }

    private void initRecyclerView() {
        rvTeman.setHasFixedSize(true);
        rvTeman.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new TemanAdapter(getContext(), listPertemanan);
        rvTeman.setAdapter(mAdapter);
    }

    private void initTemanData() {
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchTeman(query);
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void searchTeman(final String query) {
        for (int i = 0; i < listTeman.size(); i++) {
            Teman teman = listTeman.get(i);
            if (teman.getIdUser().equalsIgnoreCase(query)) {
                showUnfollow(teman);
                break;
            }
        }
        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    if (user.getIdUser().equalsIgnoreCase(query)) {
                        showFollow(user);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showFollow(User user) {
        Intent i = new Intent(getContext(), DetailTemanActivity.class);
        i.putExtra("user", user);
        startActivity(i);
    }

    private void showUnfollow(Teman teman) {
        Intent i = new Intent(getContext(), DetailTemanActivity.class);
        i.putExtra("teman", teman);
        startActivity(i);
    }

}
