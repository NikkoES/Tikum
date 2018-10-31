package com.tokosepeda.tikum.adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.activity.friend.DetailTemanActivity;
import com.tokosepeda.tikum.model.Teman;
import com.tokosepeda.tikum.model.Toko;
import com.tokosepeda.tikum.model.User;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TemanAdapter extends RecyclerView.Adapter<TemanAdapter.ViewHolder> {

    private Context context;
    private List<User> listTeman;

    public TemanAdapter(Context context, List<User> listTeman) {
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final User teman = listTeman.get(position);
        Glide.with(context).load(teman.getFoto()).into(holder.imageFoto);
        holder.txtNamaTeman.setText(teman.getNamaUser());
        holder.txtNomorTeman.setText(teman.getNomorHp());
        holder.cvTeman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailTemanActivity.class);
                i.putExtra("teman", teman);
                context.startActivity(i);
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
