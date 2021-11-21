package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements itemClickListener {

    RecyclerView folderRecycler; // folderrecycler thuoc ve activity_main la trang chua hinh anh
    TextView empty;// hien thi text neu ko co hinh
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;// cap quyen truy cap hinh anh

   // Yêu cầu người dùng cấp quyền truy cập các tệp phương tiện và đọc hình ảnh trên thiết bị
   //điều này được áp dụng với api >21
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // yeu cau lay link hinh anh
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        //____________________________________________________________________________________

        empty =findViewById(R.id.empty);


        // hình ảnh được hiển thị theo ngăn
        folderRecycler = findViewById(R.id.folderRecycler);
        folderRecycler.hasFixedSize();

        // lay link hinh anh cho vao danh sach cac thu muc
        ArrayList<imageFolder> folds = getPicturePaths();


        // neu hinh anh la rong
        if(folds.isEmpty()){
            empty.setVisibility(View.VISIBLE);
        }else{
            RecyclerView.Adapter folderAdapter = new pictureFolderAdapter(folds,MainActivity.this,this);
            folderRecycler.setAdapter(folderAdapter);
        }


    }


    // lay link hinh anh
    private ArrayList<imageFolder> getPicturePaths(){
        // chua link hinh anh
        ArrayList<imageFolder> picFolders = new ArrayList<>();
        // chua duong dan hinh anh
        ArrayList<String> picPaths = new ArrayList<>();
        Uri allImagesuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.ImageColumns.DATA ,MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.BUCKET_ID};
        Cursor cursor = this.getContentResolver().query(allImagesuri, projection, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            do{
                imageFolder folds = new imageFolder();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                //String folderpaths =  datapath.replace(name,"");
                String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder+"/"));
                folderpaths = folderpaths+folder+"/";
                if (!picPaths.contains(folderpaths)) {
                    picPaths.add(folderpaths);

                    folds.setPath(folderpaths);
                    folds.setFolderName(folder);
                    folds.setFirstPic(datapath);//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
                    folds.addpics();
                    picFolders.add(folds);
                }else{
                    for(int i = 0;i<picFolders.size();i++){
                        if(picFolders.get(i).getPath().equals(folderpaths)){
                            picFolders.get(i).setFirstPic(datapath);
                            picFolders.get(i).addpics();
                        }
                    }
                }
            }while(cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i = 0;i < picFolders.size();i++){
            Log.d("picture folders",picFolders.get(i).getFolderName()+" and path = "+picFolders.get(i).getPath()+" "+picFolders.get(i).getNumberOfPics());
        }
        return picFolders;
    }


    @Override
    public void onPicClicked(PictureHolder holder, int position, ArrayList<PictureInformation> pics) {

    }

    //Mỗi khi một mục trong RecyclerView được nhấp, phương pháp này từ việc triển khai Listener
    @Override
    public void onPicClicked(String pictureFolderPath,String folderName) {
        Intent move = new Intent(MainActivity.this, ImageOperate.class);
        move.putExtra("folderPath",pictureFolderPath);
        move.putExtra("folderName",folderName);

        //move.putExtra("recyclerItemSize",getCardsOptimalWidth(4));
        startActivity(move);
    }





}
