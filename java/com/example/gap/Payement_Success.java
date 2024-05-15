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

import com.example.gap.databinding.ActivityPayementSuccessBinding;

public class Payement_Success extends AppCompatActivity {
    ActivityPayementSuccessBinding binding;
    MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPayementSuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.scale_reduce);
        binding.llround.startAnimation(animation);
        music= MediaPlayer.create(this, R.raw.notification4);
        music.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.lottiePay.setVisibility(View.VISIBLE);
                binding.lottiePay.playAnimation();
            }
        },3000);
    }
}