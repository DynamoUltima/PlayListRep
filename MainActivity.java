package com.example.joel.playlist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    String[] items;
    ArrayAdapter<String> adp;
    ArrayList<File> mySongs;

    public static final int MY_PERMISSIONS_REQUEST_READ_STORAGE=8;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==MY_PERMISSIONS_REQUEST_READ_STORAGE&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            mySongs = findSongs(Environment.getExternalStorageDirectory());
            items = new String[mySongs.size()];

            for (int i = 0;i<mySongs.size();i++){
                //toast(mySongs.get(i).getName().toString());
                items[i] = mySongs.get(i).getName().toString();

            }

            adp = new ArrayAdapter<String>(getApplicationContext(),R.layout.song_layout,R.id.textView,items);

            lv = (ListView) findViewById(R.id.lvPlayList);


            lv.setAdapter(adp);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    startActivity(new Intent(getApplicationContext(),Player.class).putExtra("pos",position).putExtra("songlist",mySongs));

                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //permit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();

    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_STORAGE);
        } else {
            mySongs = findSongs(Environment.getExternalStorageDirectory());
            items = new String[mySongs.size()];

            for (int i = 0;i<mySongs.size();i++){
                //toast(mySongs.get(i).getName().toString());
                items[i] = mySongs.get(i).getName().toString();

            }

            adp = new ArrayAdapter<String>(getApplicationContext(),R.layout.song_layout,R.id.textView,items);

            lv = (ListView) findViewById(R.id.lvPlayList);


            lv.setAdapter(adp);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    startActivity(new Intent(getApplicationContext(),Player.class).putExtra("pos",position).putExtra("songlist",mySongs));

                }
            });
        }
    }
//    private void permit() {
//        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (shouldShowRequestPermissionRationale(
//                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                // Explain to the user why we need to read the contacts
//            }
//
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    MY_PERMISSIONS_REQUEST_READ_STORAGE);
//
//            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//            // app-defined int constant
//
//            return;
//        }


    public ArrayList<File>findSongs( File root){
        ArrayList<File> al = new ArrayList<File>();
        File file[] = root.listFiles();
        for (File singleFile : file){
            if (singleFile.isDirectory() && !singleFile.isHidden()){
                al.addAll(findSongs(singleFile));

            }else{
                if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                    al.add(singleFile);

                }
            }
        }
        return al;

    }
    public  void toast(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }
}
