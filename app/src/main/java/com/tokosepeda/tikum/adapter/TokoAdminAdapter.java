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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.activity.admin.toko.DetailTokoAdminActivity;
import com.tokosepeda.tikum.activity.admin.toko.FormTokoActivity;
import com.tokosepeda.tikum.model.Toko;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TokoAdminAdapter extends RecyclerView.Adapter<TokoAdminAdapter.ViewHolder> {

    private Context context;
    private List<Toko> listToko;

    public TokoAdminAdapter(Context context, List<Toko> listToko) {
        this.context = context;
        this.listToko = listToko;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_admin_toko, null, false);

        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(layoutParams);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Toko toko = listToko.get(position);
        final DatabaseReference dbToko = FirebaseDatabase.getInstance().getReference("toko").child(toko.getIdToko());

        Glide.with(context).load(toko.getImageToko()).into(holder.imageToko);
        holder.txtNamaToko.setText(toko.getNamaToko());
        holder.txtAlamatToko.setText(toko.getAlamatToko());
        holder.cvToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.cvToko);
                popup.inflate(R.menu.context_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_detail:
                                Intent i = new Intent(context, DetailTokoAdminActivity.class);
                                i.putExtra("toko", toko);
                                context.startActivity(i);
                                break;
                            case R.id.action_edit:
                                Intent intent = new Intent(context, FormTokoActivity.class);
                                intent.putExtra("id_toko", toko.getIdToko());
                                intent.putExtra("mode", "edit");
                                context.startActivity(intent);
                                break;
                            case R.id.action_hapus:
                                dbToko.removeValue();

                                listToko.remove(position);
                                notifyDataSetChanged();
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listToko.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_toko)
        ImageView imageToko;
        @BindView(R.id.txt_nama_toko)
        TextView txtNamaToko;
        @BindView(R.id.txt_alamat_toko)
        TextView txtAlamatToko;
        @BindView(R.id.cv_toko)
        CardView cvToko;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
