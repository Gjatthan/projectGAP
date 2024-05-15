package com.example.gap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.gap.databinding.ActivityVideoPlayerBinding;

public class Video_Player extends AppCompatActivity {

    ActivityVideoPlayerBinding binder;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder=ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binder.getRoot());

        binder.spinguid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                    playVideo(R.raw.how_to_login);
                else if(position==1)
                    playVideo(R.raw.how_to_register);
                else
                    playVideo(R.raw.how_to_ft);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Video_Player.this, "noting selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        binder.spinguid.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binder.spinguid.setVisibility(View.GONE);
                binder.videoView.setHasTransientState(true);
                binder.videoView.setTop(5);
            }
        },10000);
        return super.onTouchEvent(event);

    }

    public void playVideo(int id)
    {
        String videoUrl="android.resource://"+getPackageName()+"/"+id;
        Uri uri= Uri.parse(videoUrl);
        binder.videoView.setVideoURI(uri);
        MediaController mc=new MediaController(this);
        binder.videoView.start();
        binder.videoView.setMediaController(mc);
        mc.setAnchorView(binder.videoView);
    }
}