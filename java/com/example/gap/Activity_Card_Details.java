package com.example.gap;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.RenderEffect;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.CompoundButton;

public class Activity_Card_Details extends AppCompatActivity {
    AnimatorSet back_anim,front_anim;
    boolean isfront=true;
    com.example.gap.databinding.ActivityCardDetailsBinding binder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder= com.example.gap.databinding.ActivityCardDetailsBinding.inflate(getLayoutInflater());
        setContentView(binder.getRoot());

        float scale=getApplicationContext().getResources().getDisplayMetrics().density;
        binder.txtforeground.setCameraDistance(8000*scale);
        binder.txtbackground.setCameraDistance(8000*scale);

        front_anim= (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),R.animator.animator_foreground);
        back_anim=(AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),R.animator.animator_background);

       binder.switchFlip.setOnCheckedChangeListener((buttonView,isChecked)-> {
               flip_card();
               binder.switchFlip2.setChecked(true);
           });

        binder.switchFlip2.setOnCheckedChangeListener((view,ischecked)->{
            flip_card();
            binder.switchFlip.setChecked(false);
        });
    }

    void flip_card(){
        if(isfront){
            front_anim.setTarget(binder.txtforeground);
            back_anim.setTarget(binder.txtbackground);
            front_anim.start();
            back_anim.start();
            isfront=false;
        }
        else{
            front_anim.setTarget(binder.txtbackground);
            back_anim.setTarget(binder.txtforeground);
            back_anim.start();
            front_anim.start();
            isfront=true;
        }
    }
}