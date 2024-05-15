package com.example.gap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gap.databinding.ActivityOwnOtpBinding;
import com.example.gap.misc.SendSms;

import java.security.SecureRandom;
import java.util.ArrayList;

public class OwnOtp extends AppCompatActivity {

    String otp,ph;
    ImageView imageView;
    LinearLayout bounceBallImage;
    ActivityOwnOtpBinding activityOwnOtpBinding;
    AnimatedVectorDrawableCompat animdraw;
    AnimatedVectorDrawable animdraw2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityOwnOtpBinding=ActivityOwnOtpBinding.inflate(getLayoutInflater());
        setContentView(activityOwnOtpBinding.getRoot());

        imageView=activityOwnOtpBinding.txtotp1;
        bounceBallImage=activityOwnOtpBinding.bouncelayout;

        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.scale_reduce);
        activityOwnOtpBinding.llround.startAnimation(animation);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bounceBallImage.clearAnimation();
                bounceBallImage.setVisibility(View.VISIBLE);
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
                        final int left = bounceBallImage.getLeft();
                        final int top = bounceBallImage.getTop();
                        final int right = bounceBallImage.getRight();
                        final int bottom = bounceBallImage.getBottom();
                        bounceBallImage.layout(left, top, right, bottom);

                    }
                });
                bounceBallImage.startAnimation(transAnim);

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
                        activityOwnOtpBinding.txtsuccessmsg.setVisibility(View.VISIBLE);
                        activityOwnOtpBinding.txtsuccessmsg.startAnimation(AnimationUtils.loadAnimation(OwnOtp.this, android.R.anim.fade_in));
                    }
                },4000);


            }
        },3000);
    }
}
