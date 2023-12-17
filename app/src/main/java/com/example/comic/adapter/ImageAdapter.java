package com.example.comic.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.comic.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private ArrayList<String> arrUrlAnh;
    private int soTrangDangDoc = 0;

    public ImageAdapter(ArrayList<String> arrUrlAnh) {
        if (arrUrlAnh == null) {
            this.arrUrlAnh = new ArrayList<>();
        } else {
            this.arrUrlAnh = arrUrlAnh;
        }
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String urlAnh = arrUrlAnh.get(position);
        Glide.with(holder.itemView.getContext()).load(urlAnh).into(holder.imgAnh);
        holder.itemView.setBackgroundColor(position == soTrangDangDoc ? Color.GRAY : Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return arrUrlAnh.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAnh;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnh = itemView.findViewById(R.id.imgAnh);
        }
    }
}
