package com.mega.hi_pet.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.macroyau.thingspeakandroid.model.Feed;
import com.mega.hi_pet.R;
import java.util.List;

/**
 * Created by Leonardo on 4/21/2018.
 */

public class RFIDHistoryAdapter extends RecyclerView.Adapter<RFIDHistoryAdapter.ViewHolder> {

    //deklarasi context dari activity dan arraylist yang didapatkan
    Context context;
    List<Feed> commentList;
//    RoundedBitmapDrawable rounded;

    //konstruktor utk inisialisasi value pada context dan list dari activity
    public RFIDHistoryAdapter(Context context, List<Feed> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    //view holder untuk deklarasi dan inisialisasi view pada layout comment
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTanggal;
        TextView tvWaktu;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvWaktu = itemView.findViewById(R.id.tvWaktu);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating/set layout comment ke adapter
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Feed comment = commentList.get(position);
        holder.tvTanggal.setText("Tapped");
        holder.tvWaktu.setText(comment.getCreatedAt().toString());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

}