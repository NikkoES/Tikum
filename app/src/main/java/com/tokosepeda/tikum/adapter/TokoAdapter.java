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

import com.bumptech.glide.Glide;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.activity.toko.DetailTokoActivity;
import com.tokosepeda.tikum.model.Toko;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TokoAdapter extends RecyclerView.Adapter<TokoAdapter.ViewHolder> {

    private Context context;
    private List<Toko> listToko;
    private Location myLocation;

    public TokoAdapter(Context context, List<Toko> listToko, Location myLocation) {
        this.context = context;
        this.listToko = listToko;
        this.myLocation = myLocation;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_toko, null, false);

        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(layoutParams);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Toko toko = listToko.get(position);
        Glide.with(context).load(toko.getImageToko()).into(holder.imageToko);
        holder.txtNamaToko.setText(toko.getNamaToko());
        holder.txtAlamatToko.setText(toko.getAlamatToko());
        Location pesantrenLocation = new Location("");
        pesantrenLocation.setLatitude(Double.parseDouble(toko.getLatToko()));
        pesantrenLocation.setLongitude(Double.parseDouble(toko.getLongToko()));

        final float distanceEuclidean = convertTwo(myLocation.distanceTo(pesantrenLocation) / 1000);

        holder.txtJarakToko.setText(distanceEuclidean + " km");
        holder.cvToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailTokoActivity.class);
                i.putExtra("toko", toko);
                i.putExtra("jarak", String.valueOf(distanceEuclidean));
                context.startActivity(i);
            }
        });
    }

    public float convertTwo(float n) {
        return BigDecimal.valueOf(n).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
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
        @BindView(R.id.txt_jarak_toko)
        TextView txtJarakToko;
        @BindView(R.id.cv_toko)
        CardView cvToko;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
