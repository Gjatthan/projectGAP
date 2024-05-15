package com.example.gap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.gap.databinding.ActivityOwnOtpBinding;
import com.example.gap.databinding.ActivitySuccessScreenBinding;

public class Success_screen extends AppCompatActivity {

    ImageView imageView;
    LinearLayout bounceBallLayout;
    ActivitySuccessScreenBinding successScreenBinding;
    AnimatedVectorDrawableCompat animdraw;
    AnimatedVectorDrawable animdraw2;
    MediaPlayer music;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        successScreenBinding=ActivitySuccessScreenBinding.inflate(getLayoutInflater());
        setContentView(successScreenBinding.getRoot());

        imageView=successScreenBinding.txtotp1;
        bounceBallLayout=successScreenBinding.bouncelayout;

        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.scale_reduce);
        successScreenBinding.llround.startAnimation(animation);
        music=MediaPlayer.create(Success_screen.this, R.raw.notification4);
        music.start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bounceBallLayout.clearAnimation();
                bounceBallLayout.setVisibility(View.VISIBLE);
                TranslateAnimation transAnim = new TranslateAnimation(0, 0, -400,
                        450);
                transAnim.setStartOffset(500);
                transAnim.setDuration(3000);
                transAnim.setFillAfter(true);
                transAnim.setInterpolator(new BounceInterpolator());
                transAnim.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //bounceBallImage.clearAnimation();
                        final int left = bounceBallLayout.getLeft();
                        final int top = bounceBallLayout.getTop();
                        final int right = bounceBallLayout.getRight();
                        final int bottom = bounceBallLayout.getBottom();
                        bounceBallLayout.layout(left, top, right, bottom);

                    }
                });
                bounceBallLayout.startAnimation(transAnim);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Drawable drawable=imageView.getDrawable();
                        if(drawable instanceof AnimatedVectorDrawableCompat){
                            animdraw=(AnimatedVectorDrawableCompat)drawable;
                            animdraw.start();
                        }else if(drawable instanceof AnimatedVectorDrawable){
                            animdraw2=(AnimatedVectorDrawable)drawable;
                            animdraw2.start();
                        }
                        successScreenBinding.txtsuccessmsg.setVisibility(View.VISIBLE);
                        successScreenBinding.txtsuccessmsg.startAnimation(AnimationUtils.loadAnimation(Success_screen.this, android.R.anim.fade_in));

                        music=MediaPlayer.create(Success_screen.this, R.raw.notification1);
                        music.start();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                music.stop();
                                Intent myIntent = new Intent(Success_screen.this, Login.class);
                                ActivityOptions options =
                                        ActivityOptions.makeCustomAnimation(Success_screen.this, android.R.anim.fade_in, android.R.anim.fade_out);
                                startActivity(myIntent, options.toBundle());
                                finish();
                            }
                        },2000);
                    }
                },4000);

            }
        },3000);
    }
}
