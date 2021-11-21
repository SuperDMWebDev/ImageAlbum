package com.example.myapplication;

import java.util.ArrayList;

public interface itemClickListener {

        void onPicClicked(PictureHolder holder, int position, ArrayList<PictureInformation> pics);
        void onPicClicked(String pictureFolderPath,String folderName);
}
