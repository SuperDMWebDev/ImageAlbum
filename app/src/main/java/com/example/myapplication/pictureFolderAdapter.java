package com.example.myapplication;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;


public class pictureFolderAdapter extends RecyclerView.Adapter<pictureFolderAdapter.FolderHolder>{

    private ArrayList<imageFolder> imageFolder;
    private Context folderContex;
    private itemClickListener itemClick;

    public pictureFolderAdapter(ArrayList<imageFolder> imageFolder, Context folderContex, itemClickListener listen) {
        this.imageFolder = imageFolder;
        this.folderContex = folderContex;
        this.itemClick = listen;
    }

    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell = inflater.inflate(R.layout.picture_folder_item, parent, false);
        return new FolderHolder(cell);

    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolder holder, int position) {
        final imageFolder folder = imageFolder.get(position);

        Glide.with(folderContex)
                .load(folder.getFirstPic())
                .apply(new RequestOptions().centerCrop())
                .into(holder.folderPic);

        //setting the number of images
        String text = ""+folder.getFolderName();
        String folderSizeString=""+folder.getNumberOfPics()+" Media";
        holder.folderSize.setText(folderSizeString);
        holder.folderName.setText(text);

        holder.folderPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onPicClicked(folder.getPath(),folder.getFolderName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageFolder.size();
    }


    public class FolderHolder extends RecyclerView.ViewHolder{
        ImageView folderPic;
        TextView folderName;
        TextView folderSize;
        public FolderHolder(@NonNull View itemView) {
            super(itemView);
            folderPic = itemView.findViewById(R.id.folderPic);
            folderName = itemView.findViewById(R.id.folderName);
            folderSize=itemView.findViewById(R.id.folderSize);
        }
    }

}
