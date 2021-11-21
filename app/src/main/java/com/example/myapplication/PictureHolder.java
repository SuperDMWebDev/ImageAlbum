package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import com.example.myapplication.R;

/*picture_Adapter's ViewHolder*/

public class PictureHolder extends RecyclerView.ViewHolder{

    public ImageView picture;

    PictureHolder(@NonNull View itemView) {
        super(itemView);

        picture = itemView.findViewById(R.id.image);
    }
}
