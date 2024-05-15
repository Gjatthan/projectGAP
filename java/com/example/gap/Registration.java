package com.example.gap;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Registration extends AppCompatActivity {
    SharedPreferences u_details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT < 16) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
        setContentView(R.layout.activity_registration);


    }

    public void onClick(View v)
    {
        RadioGroup rgrp=findViewById(R.id.radioGroup);
        Intent i=null;
        if(rgrp.getCheckedRadioButtonId()==R.id.radioButtonDebit)
            i=new Intent(this,RegistrationCard.class);
        else if(rgrp.getCheckedRadioButtonId()==R.id.radioButtonMbl)
            i=new Intent(this,RegisterMbl.class);
        else
            Toast.makeText(this, "Please select the mode of registration", Toast.LENGTH_SHORT).show();
        if(i!=null) {
            startActivity(i);
        }
    }

    @Override
    protected void onRestart() {
        u_details=getSharedPreferences("user_details",MODE_PRIVATE);
        if(u_details.getBoolean("isregistered",false))
            finish();
        super.onRestart();
    }
}