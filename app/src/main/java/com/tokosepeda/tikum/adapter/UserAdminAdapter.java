package com.tokosepeda.tikum.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.activity.admin.toko.DetailTokoAdminActivity;
import com.tokosepeda.tikum.activity.admin.toko.FormTokoActivity;
import com.tokosepeda.tikum.activity.admin.user.DetailUserActivity;
import com.tokosepeda.tikum.activity.admin.user.FormUserActivity;
import com.tokosepeda.tikum.activity.friend.DetailTemanActivity;
import com.tokosepeda.tikum.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserAdminAdapter extends RecyclerView.Adapter<UserAdminAdapter.ViewHolder> {

    private Context context;
    private List<User> listTeman;

    public UserAdminAdapter(Context context, List<User> listTeman) {
        this.context = context;
        this.listTeman = listTeman;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_teman, null, false);

        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(layoutParams);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final User user = listTeman.get(position);
        final DatabaseReference dbUser = FirebaseDatabase.getInstance().getReference("user").child(user.getId());

        Glide.with(context).load(user.getFoto()).into(holder.imageFoto);
        holder.txtNamaTeman.setText(user.getNamaUser());
        holder.txtNomorTeman.setText(user.getNomorHp());
        holder.cvTeman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailUserActivity.class);
                i.putExtra("user", user);
                context.startActivity(i);
//                PopupMenu popup = new PopupMenu(context, holder.cvTeman);
//                popup.inflate(R.menu.context_menu);
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.action_detail:
//                                Intent i = new Intent(context, DetailUserActivity.class);
//                                i.putExtra("user", user);
//                                context.startActivity(i);
//                                break;
//                            case R.id.action_edit:
//                                Intent intent = new Intent(context, FormUserActivity.class);
//                                intent.putExtra("id_user", user.getId());
//                                intent.putExtra("mode", "edit");
//                                context.startActivity(intent);
//                                break;
//                            case R.id.action_hapus:
//                                dbUser.removeValue();
//
//                                listTeman.remove(position);
//                                notifyDataSetChanged();
//                                break;
//                        }
//                        return false;
//                    }
//                });
//                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTeman.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_foto)
        ImageView imageFoto;
        @BindView(R.id.txt_nama_teman)
        TextView txtNamaTeman;
        @BindView(R.id.txt_nomor_teman)
        TextView txtNomorTeman;
        @BindView(R.id.cv_teman)
        CardView cvTeman;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
