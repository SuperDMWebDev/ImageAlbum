package com.example.myapplication;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.transition.Fade;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;



public class ImageOperate extends AppCompatActivity implements itemClickListener {

    RecyclerView imageRecycler;
    ArrayList<PictureInformation> pictureInformation;
    ProgressBar NoPictureShow;
    String folderPath;
    TextView folderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        folderName = findViewById(R.id.foldername);
        folderName.setText(getIntent().getStringExtra("folderName"));

        folderPath =  getIntent().getStringExtra("folderPath");
        pictureInformation = new ArrayList<>();
        imageRecycler = findViewById(R.id.recycler);
        // addItem decoration
        imageRecycler.hasFixedSize();
        NoPictureShow = findViewById(R.id.loader);


        if(pictureInformation.isEmpty()){
            NoPictureShow.setVisibility(View.VISIBLE);
            pictureInformation = getAllImagesByFolder(folderPath);
            imageRecycler.setAdapter(new picture_Adapter(pictureInformation, ImageOperate.this,this));
            NoPictureShow.setVisibility(View.GONE);
        }else{

        }
    }

    @Override
    public void onPicClicked(PictureHolder holder, int position, ArrayList<PictureInformation> pics) {
        ImageSlider browser = ImageSlider.newInstance(pics,position, ImageOperate.this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            browser.setEnterTransition(new Slide());
//            browser.setExitTransition(new Slide()); //uncomment this to use slide transition and comment the two lines below
            browser.setEnterTransition(new Fade());
            browser.setExitTransition(new Fade());
        }

        getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(holder.picture, position+"picture")
                .add(R.id.displayContainer, browser)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onPicClicked(String pictureFolderPath,String folderName) {

    }


    public ArrayList<PictureInformation> getAllImagesByFolder(String path){
        ArrayList<PictureInformation> images = new ArrayList<>();
        Uri allVideosuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.ImageColumns.DATA ,MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor cursor = ImageOperate.this.getContentResolver().query( allVideosuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[] {"%"+path+"%"}, null);
        try {
            cursor.moveToFirst();
            do{
                PictureInformation pic = new PictureInformation();

                pic.setPicturName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));

                pic.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

                pic.setPictureSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));

                images.add(pic);
            }while(cursor.moveToNext());
            cursor.close();
            ArrayList<PictureInformation> reSelection = new ArrayList<>();
            for(int i = images.size()-1;i > -1;i--){
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }


}
