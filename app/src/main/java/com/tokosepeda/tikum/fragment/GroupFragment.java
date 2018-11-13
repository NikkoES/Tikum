package com.tokosepeda.tikum.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.activity.grup.DetailGroupActivity;
import com.tokosepeda.tikum.activity.grup.FormGroupActivity;
import com.tokosepeda.tikum.adapter.GrupAdapter;
import com.tokosepeda.tikum.adapter.TemanAdapter;
import com.tokosepeda.tikum.firebase.FirebaseApplication;
import com.tokosepeda.tikum.model.Grup;
import com.tokosepeda.tikum.model.Teman;
import com.tokosepeda.tikum.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment implements SearchView.OnQueryTextListener {

    @BindView(R.id.rv_grup)
    RecyclerView rvGrup;
    @BindView(R.id.search_id)
    SearchView searchView;
    @BindView(R.id.txt_info)
    TextView txtInfo;

    DatabaseReference dbGroup, dbAnggota;
    FirebaseAuth mAuth;

    String id;

    private GrupAdapter mAdapter;
    List<Grup> listGrup = new ArrayList<>();

    public GroupFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group, container, false);

        ButterKnife.bind(this, v);

        mAuth = ((FirebaseApplication) getActivity().getApplication()).getFirebaseAuth();
        id = mAuth.getCurrentUser().getUid();

        dbGroup = FirebaseDatabase.getInstance().getReference("groups");

        initGrupData();
        initSearchView();
        initRecyclerView();

        return v;
    }

    @OnClick(R.id.btn_add)
    public void buttonAddGrup(){
        Intent i = new Intent(getContext(), FormGroupActivity.class);
        i.putExtra("mode", "add");
        startActivity(i);
    }

    private void initSearchView() {
        searchView.setQueryHint("Pencarian ID Grup");
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
    }

    private void initRecyclerView() {
        rvGrup.setHasFixedSize(true);
        rvGrup.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new GrupAdapter(getContext(), listGrup);
        rvGrup.setAdapter(mAdapter);
    }

    private void initGrupData() {
        dbGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listGrup.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Grup grup = postSnapshot.getValue(Grup.class);
                    listGrup.add(grup);
                }
                mAdapter.notifyDataSetChanged();
                if (listGrup.size() == 0) {
                    txtInfo.setVisibility(View.VISIBLE);
                } else {
                    txtInfo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchGrup(query);
        searchView.clearFocus();
        return false;
    }

    private void searchGrup(final String query) {
        dbGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Grup grup = postSnapshot.getValue(Grup.class);
                    if (grup.getIdGrup().equalsIgnoreCase(query)) {
                        Intent i = new Intent(getContext(), DetailGroupActivity.class);
                        i.putExtra("grup", grup);
                        startActivity(i);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

}
