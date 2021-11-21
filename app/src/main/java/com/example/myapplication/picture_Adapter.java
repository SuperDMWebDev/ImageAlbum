package com.example.myapplication;

import static androidx.core.view.ViewCompat.setTransitionName;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class picture_Adapter extends RecyclerView.Adapter<PictureHolder> {

    private ArrayList<PictureInformation> pictureList;
    private Context pictureContx;
    private final itemClickListener picListerner;


    public picture_Adapter(ArrayList<PictureInformation> pictureList, Context pictureContx, itemClickListener picListerner) {
        this.pictureList = pictureList;
        this.pictureContx = pictureContx;
        this.picListerner = picListerner;
    }

    @NonNull
    @Override
    public PictureHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell = inflater.inflate(R.layout.picture_holder_item, container, false);
        return new PictureHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull final PictureHolder holder, @SuppressLint("RecyclerView") final int position) {

        final PictureInformation image = pictureList.get(position);

        Glide.with(pictureContx)
                .load(image.getPicturePath())
                .apply(new RequestOptions().centerCrop())
                .into(holder.picture);

        setTransitionName(holder.picture, String.valueOf(position) + "_image");

        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picListerner.onPicClicked(holder, position, pictureList);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }

}