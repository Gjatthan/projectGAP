package com.example.gap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.SavedStateHandle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences u_details;
    String acno;
    Intent i;
    boolean blocked=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        u_details=getSharedPreferences("user_details",MODE_PRIVATE);
        acno=u_details.getString("ac_no","");

        if(!acno.equals(""))
        {
            FirebaseDatabase.getInstance().getReference("ac_details").child(acno).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild("block"))
                        blocked=true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.splash_zoom_in);
        findViewById(R.id.logo).startAnimation(animation);

//        HashMap<String,Object> map=new HashMap<>();
//        map.put("actype","Current");
//        map.put("adrs","Laxminagar 5th cross");
//        map.put("borrowing","");
//        map.put("branch","Santhekatte");
//        map.put("ccv",234);
//        map.put("drno","1212 1223 1223 1223");
//        map.put("drpin",5678);
//        map.put("drvalid","12/27");
//        map.put("email","pranamskumar821@gmail.com");
//        map.put("gender","male");
//        map.put("actype","Current");
//        map.put("ifsc","TAB20231048");
//        map.put("name","Pranam S kumar");
//        map.put("savings",20500);
//        map.put("phno","9353162467");
//
//        FirebaseDatabase.getInstance().getReference("ac_details").child("67812").setValue(map);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.splash_move_in);
                TextView txt=findViewById(R.id.textView5);
                txt.setVisibility(View.VISIBLE);
                txt.startAnimation(animation);
            }
        }, 1500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent i;
                //SharedPreferences u_details=getSharedPreferences("user_details", Context.MODE_PRIVATE);
                //Toast.makeText(SplashScreen.this, ""+u_details.getBoolean("isregistered",false), Toast.LENGTH_SHORT).show();
                if(!u_details.getBoolean("isregistered", false))
                    i = new Intent(SplashScreen.this, Introduction.class);
                else {
                    i = new Intent(SplashScreen.this, Login.class);
                    i.putExtra("blocked",blocked);
                }
                startActivity(i);
                finish();
            }
        }, 6000);
    }
}