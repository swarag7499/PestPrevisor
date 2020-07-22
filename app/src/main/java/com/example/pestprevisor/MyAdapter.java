package com.example.pestprevisor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    Context c;
    ArrayList<Model> models;

    public MyAdapter(Context c, ArrayList<Model> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, viewGroup, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int position) {
        Model m = models.get(position);
        myHolder.mTitle.setText(m.getTitle());
        myHolder.mDes.setText(models.get(position).getDescription());
        myHolder.mpest.setText(models.get(position).getPesticides());
        myHolder.mImaeView.setImageResource(models.get(position).getImg());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder{

        ImageView mImaeView;
        TextView mTitle, mDes,mpest;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            mImaeView = itemView.findViewById(R.id.imageIv);
            mTitle = itemView.findViewById(R.id.titleTv);
            mDes = itemView.findViewById(R.id.description);
            mpest = itemView.findViewById(R.id.pest);

        }
    }

}
