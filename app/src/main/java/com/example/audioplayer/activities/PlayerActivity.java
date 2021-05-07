package com.example.audioplayer.activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.audioplayer.R;
import com.example.audioplayer.models.MusicFiles;
import com.example.audioplayer.utils.AppUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

import static com.example.audioplayer.activities.MainActivity.musicFiles;
import static com.example.audioplayer.activities.MainActivity.repeatBoolean;
import static com.example.audioplayer.activities.MainActivity.shuffleBoolean;

public class PlayerActivity extends BaseActivity {

    TextView songName,songArtist,durationPlayed,durationTotal,shuffleStatus,repeatStatus;
    ImageView coverArt;
    ImageView returnToList,menuList,shuffleSongs,previousSong,nextSong,repeatSong;
    FloatingActionButton playPause;
    SeekBar seekBar;
    int position=-1;
    static ArrayList<MusicFiles>listSongs=new ArrayList<>();
    Uri uri;
    MediaPlayer mediaPlayer;
    private Handler handler=new Handler();
    private Thread playThread,prevThread,nextThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
        getIntentMethod();
        seekBarMethod();
        onClickMethods();

    }

    private void onClickMethods() {

        returnToList.setOnClickListener(v -> {
            Intent intent=new Intent(PlayerActivity.this,MainActivity.class);
            startActivity(intent);
        });

        shuffleSongs.setOnClickListener(v -> {
            if(shuffleBoolean){
                shuffleBoolean=false;
                shuffleSongs.setImageResource(R.drawable.ic_shuffle_off);
                shuffleStatus.setText(R.string.shuffleOff);

            }else{
                shuffleBoolean=true;
                shuffleSongs.setImageResource(R.drawable.ic_shuffle_on);
                shuffleStatus.setText(R.string.shuffleOn);
            }
        });
        repeatSong.setOnClickListener(v -> {
            if(repeatBoolean){
                repeatBoolean=false;
                repeatSong.setImageResource(R.drawable.ic_repeat_off);
                repeatStatus.setText(R.string.repeatOff);

            }else{
                repeatBoolean=true;
                repeatSong.setImageResource(R.drawable.ic_repeat_on);
                repeatStatus.setText(R.string.repeatOn);
            }
        });
    }

    @Override
    protected void onResume() {
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    private void prevThreadBtn() {

        prevThread=new Thread(){

            @Override
            public void run() {
                super.run();
                previousSong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prevBtnClicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void prevBtnClicked() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean && !repeatBoolean){
                position=getRandom(listSongs.size()-1);
            }else if(!shuffleBoolean && !repeatBoolean){
                position=((position-1) <0 ? (listSongs.size()-1): (position-1));
            }
            uri=Uri.parse(listSongs.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            getSongDetails();
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!= null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            playPause.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();

        }else{
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean && !repeatBoolean){
                position=getRandom(listSongs.size()-1);
            }else if(!shuffleBoolean && !repeatBoolean){
                position=((position-1) <0 ? (listSongs.size()-1): (position-1));
            }
            uri=Uri.parse(listSongs.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            getSongDetails();
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!= null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            playPause.setBackgroundResource(R.drawable.ic_play);
            mediaPlayer.start();


        }
    }

    private void nextThreadBtn() {
        nextThread=new Thread(){

            @Override
            public void run() {
                super.run();
                nextSong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextBtnClicked();
                    }
                });
            }
        };
        nextThread.start();

    }

    private void nextBtnClicked() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean && !repeatBoolean){
                position=getRandom(listSongs.size()-1);
            }else if(!shuffleBoolean && !repeatBoolean){
                position=((position+1) %listSongs.size());
            }
            uri=Uri.parse(listSongs.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            getSongDetails();
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!= null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            playPause.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();

        }else{
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean && !repeatBoolean){
                position=getRandom(listSongs.size()-1);
            }else if(!shuffleBoolean && !repeatBoolean){
                position=((position+1) %listSongs.size());
            }            uri=Uri.parse(listSongs.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            getSongDetails();
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!= null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            playPause.setBackgroundResource(R.drawable.ic_play);
            mediaPlayer.start();


        }
    }

    private int getRandom(int i) {
        Random random=new Random();
        return random.nextInt(i+1);
    }

    private void playThreadBtn() {

        playThread=new Thread(){

            @Override
            public void run() {
                super.run();
                playPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playPauseBtnClicked() {

        if(mediaPlayer.isPlaying()){
            playPause.setImageResource(R.drawable.ic_play);
            seekBar.setMax(mediaPlayer.getDuration()/1000);

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!= null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.pause();

        }else {
            mediaPlayer.start();
            playPause.setBackgroundResource(R.drawable.ic_pause);
            seekBar.setMax(mediaPlayer.getDuration()/1000);

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!= null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });

        }

    }

    private void seekBarMethod() {

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!= null){
                    int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                    seekBar.setProgress(mCurrentPosition);
                    durationPlayed.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this,1000);
            }
        });
    }

    private void metaData(Uri uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        byte[]art=retriever.getEmbeddedPicture();
        Bitmap bitmap;
        bitmap=BitmapFactory.decodeByteArray(art,0,art.length);

        AppUtils.getInstance().ImageAnimation(this,coverArt,bitmap);


        if(art != null){
            Glide.with(this)
                    .asBitmap()
                    .load(art)
                    .into(coverArt);
        }else{
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.music_empty_album)
                    .into(coverArt);
        }

    }

    private String formattedTime(int mCurrentPosition) {
        String totalOut = "";
        String totalNew = "";
        String minutes = String.valueOf(mCurrentPosition / 60);
        String seconds = String.valueOf(mCurrentPosition % 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }
    }

    private void getIntentMethod() {
        position=getIntent().getIntExtra("position",-1);
        listSongs=musicFiles;
        if(listSongs!= null){
            playPause.setBackgroundResource(R.drawable.ic_pause);
            uri = Uri.parse(listSongs.get(position).getPath());
        }
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer= MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }else{
            mediaPlayer= MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        seekBar.setMax(mediaPlayer.getDuration());
        metaData(uri);
        getSongDetails();
    }

    private void getSongDetails() {
        songName.setText(listSongs.get(position).getTitle());
        songArtist.setText(listSongs.get(position).getArtist());
        int totalDuration= Integer.parseInt(listSongs.get(position).getDuration()) /1000;
        durationTotal.setText(formattedTime(totalDuration));
    }

    private void initView() {
        songName=findViewById(R.id.songName);
        songArtist=findViewById(R.id.songArtist);
        durationPlayed=findViewById(R.id.durationPlayed);
        durationTotal=findViewById(R.id.durationTotal);
        coverArt=findViewById(R.id.coverArt);
        returnToList=findViewById(R.id.returnToList);
        menuList=findViewById(R.id.menuList);
        shuffleSongs=findViewById(R.id.shuffleSongs);
        previousSong=findViewById(R.id.previousSong);
        nextSong=findViewById(R.id.nextSong);
        repeatSong=findViewById(R.id.repeatSong);
        playPause=findViewById(R.id.playPause);
        seekBar=findViewById(R.id.seekBar);
        shuffleStatus=findViewById(R.id.shuffleStatus);
        repeatStatus=findViewById(R.id.repeatStatus);

    }
}