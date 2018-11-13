package com.tokosepeda.tikum.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.activity.friend.DetailTemanActivity;
import com.tokosepeda.tikum.activity.grup.DetailGroupActivity;
import com.tokosepeda.tikum.model.Grup;
import com.tokosepeda.tikum.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GrupAdapter extends RecyclerView.Adapter<GrupAdapter.ViewHolder> {

    private Context context;
    private List<Grup> listGrup;

    public GrupAdapter(Context context, List<Grup> listGrup) {
        this.context = context;
        this.listGrup = listGrup;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_grup, null, false);

        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(layoutParams);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Grup grup = listGrup.get(position);
        holder.txtNamaGrup.setText(grup.getNamaGrup());
        holder.txtDeskripsiGrup.setText(grup.getDeskripsiGrup());
        holder.cvGrup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailGroupActivity.class);
                i.putExtra("grup", grup);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listGrup.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_foto)
        ImageView imageFoto;
        @BindView(R.id.txt_nama_grup)
        TextView txtNamaGrup;
        @BindView(R.id.txt_deskripsi_grup)
        TextView txtDeskripsiGrup;
        @BindView(R.id.cv_grup)
        CardView cvGrup;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
