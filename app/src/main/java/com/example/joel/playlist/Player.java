package com.example.joel.playlist;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener {
    static MediaPlayer mp;
    ArrayList<File> mySongs;
    int position;
    SeekBar sb;
    ImageButton btnPlay,btnFF,btnFB,btnNxt,btnPrev;
    Uri u;
    Thread updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btnPlay =(ImageButton) findViewById(R.id.btnPlay);
        btnFF =(ImageButton) findViewById(R.id.btnFF);
        btnFB =(ImageButton) findViewById(R.id.btnFB);
        btnNxt =(ImageButton) findViewById(R.id.btnNxt);
        btnPrev =(ImageButton) findViewById(R.id.btnPrev);

        btnPlay.setOnClickListener(this);
        btnFF.setOnClickListener(this);
        btnFB.setOnClickListener(this);
        btnNxt.setOnClickListener(this);
        btnPrev.setOnClickListener(this);

        if (mp!= null){
            mp.stop();
            mp.release();
        }
        sb = (SeekBar) findViewById(R.id.sb);
        updateSeekBar= new Thread(){
            @Override
            public void run() {
                int totalDuration = mp.getDuration();
                int currentPosition =0;
                sb.setMax(totalDuration);
                while (currentPosition < totalDuration){
                    try {
                        sleep(500);
                        currentPosition = mp.getCurrentPosition();
                        sb.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

               // super.run();
            }
        };


        Intent i = getIntent();
        Bundle b = i.getExtras();
         mySongs = (ArrayList) b.getParcelableArrayList("songlist");
         position = b.getInt("pos",0);


        u = Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(),u);
        mp.start();
        updateSeekBar.start();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());

            }
        });


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btnPlay:
                if (mp.isPlaying()){
                    btnPlay.setImageResource(R.mipmap.playbutton);
                    mp.pause();
                }
                else{
                btnPlay.setImageResource(R.mipmap.pausebutton);
                mp.start();
                }
                break;
            case R.id.btnFF:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.btnFB:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.btnNxt:
                mp.stop();
                mp.release();
                position = (position+1)%mySongs.size();
                u = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                break;
            case R.id.btnPrev:
                mp.stop();
                mp.release();
                position = (position-1<0)?mySongs.size()-1:position-1;
                /*if (position-1<0){
                    position = mySongs.size()-1;
                }
                else{
                    position= position-1;
                }*/
                u = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                break;

        }
    }
}
