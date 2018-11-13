package com.tokosepeda.tikum.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.activity.account.GantiPasswordActivity;
import com.tokosepeda.tikum.activity.account.UbahProfilActivity;
import com.tokosepeda.tikum.activity.auth.LoginActivity;
import com.tokosepeda.tikum.firebase.FirebaseApplication;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private FirebaseAuth mAuth;

    DatabaseReference dbUser;
    String id;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, v);

        mAuth = ((FirebaseApplication) getActivity().getApplication()).getFirebaseAuth();
        id = mAuth.getCurrentUser().getUid();
        dbUser = FirebaseDatabase.getInstance().getReference("users").child(id);

        return v;
    }

    @OnClick({R.id.btn_edit_profile, R.id.btn_change_password, R.id.btn_keluar})
    public void actionButton(View v){
        switch (v.getId()){
            case R.id.btn_edit_profile : {
                startActivity(new Intent(getContext(), UbahProfilActivity.class));
                break;
            }
            case R.id.btn_change_password : {
                startActivity(new Intent(getContext(), GantiPasswordActivity.class));
                break;
            }
            case R.id.btn_keluar : {
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.yakin_ingin_keluar))
                        .setPositiveButton(getString(R.string.ya), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbUser.child("latitude").setValue(null);
                                dbUser.child("longitude").setValue(null);
                                mAuth.signOut();
                                getActivity().finish();
                                Intent i = new Intent(getContext(), LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton(getString(R.string.tidak), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setCancelable(false)
                        .show();
                break;
            }
        }
    }

}
