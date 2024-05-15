package com.example.gap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.gap.misc.MsgDialogSetup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

import pl.droidsonroids.gif.GifImageView;

public class TpinMpin extends AppCompatActivity {

    TextInputEditText txtTp,txtTpC,txtLp,txtLpC;
    DatabaseReference dref;
    Dialog msg_dialog;
    DatabaseReference firebaseDatabase,firebaseDatabase2;
    MsgDialogSetup msgDialogSetup;

    String ph,name,ac,adrs,gender,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tpin_mpin);
        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink_animation);
        findViewById(R.id.textView4).startAnimation(animation);
        txtTp=findViewById(R.id.txttpin);
        txtTpC=findViewById(R.id.txttpinc);
        txtLp=findViewById(R.id.txtlpin);
        txtLpC=findViewById(R.id.txtlpinc);

        ph=getIntent().getExtras().getString("phone_no");
        name=getIntent().getExtras().getString("name");
        ac=getIntent().getExtras().getString("ac_no");


        msgDialogSetup=new MsgDialogSetup(this);

        //msgDialogSetup.setupDialog(ph+"\n"+name+"\n"+ac,Optional.empty());

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

//        msg_dialog=new Dialog(this);
//        msg_dialog.setContentView(R.layout.message_dialog);
//        msg_dialog.setCancelable(false);
//
//        msg_dialog.findViewById(R.id.msg_box_ok).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                msg_dialog.dismiss();
//            }
//        });

        txtLpC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(txtLpC.getText().length()==4&&!txtLpC.getText().toString().equals(Objects.requireNonNull(txtLp.getText()).toString())) {
                    txtLpC.startAnimation(AnimationUtils.loadAnimation(TpinMpin.this, R.anim.shake_animation));
                    txtLpC.setText("");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        v.vibrate(500);
                    }
                    msgDialogSetup.setupDialog("Login Pin does not match",Optional.of(R.drawable.info_icn));
                }

            }
        });

        txtTpC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txtTpC.getText().length() == 4 && !txtTpC.getText().toString().equals(Objects.requireNonNull(txtTp.getText()).toString())) {
                    {
                    txtTpC.startAnimation(AnimationUtils.loadAnimation(TpinMpin.this, R.anim.shake_animation));
                    txtTpC.setText("");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        v.vibrate(500);
                    }
                        msgDialogSetup.setupDialog("Transaction Pin does not match",Optional.of(R.drawable.info_icn));
                }
            }
        }
        });

        txtTp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(txtTp.getText().toString().equals(txtLp.getText().toString())&&txtTp.getText().toString().length()>0) {
                    txtTp.startAnimation(AnimationUtils.loadAnimation(TpinMpin.this, R.anim.shake_animation));
                    txtTp.setText("");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        v.vibrate(500);
                    }
                    msgDialogSetup.setupDialog("Tpin and Lpin cannot be same", Optional.of(R.drawable.info_icn));

                }

                if(txtTp.getText().toString().length()==0)
                    txtTpC.setText("");
            }
        });

        txtLp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txtLp.getText().toString().equals(txtTp.getText().toString()) && txtLp.getText().toString().length() > 0) {
                    txtLp.startAnimation(AnimationUtils.loadAnimation(TpinMpin.this, R.anim.shake_animation));
                    txtLp.setText("");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        v.vibrate(500);
                    }
                    msgDialogSetup.setupDialog("Lpin and Tpin cannot be same", Optional.of(R.drawable.info_icn));
                }

                if(txtLp.getText().toString().length()==0)
                    txtLpC.setText("");
            }
        });

    }

//    public void onClickShowHide(View v){
//        ToggleButton tb=(ToggleButton) v;
//        TextInputEditText txt;
//        if(tb.getId()==R.id.tbtpin)
//            txt=txtTp;
//        else if(tb.getId()==R.id.tbtpinc)
//            txt=txtTpC;
//        else if(tb.getId()==R.id.tblpin)
//            txt=txtLp;
//        else
//            txt=txtLpC;
//
//        if(tb.isChecked()){
//            tb.setBackground(getDrawable(R.drawable.close_eye));
//            txt.setInputType(InputType.TYPE_CLASS_NUMBER);
//        }
//        else {
//            tb.setBackground(getDrawable(R.drawable.eye));
//            txt.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
//        }
//        txt.setSelection(txt.length());
//    }

    public void onClickProceed(View v)
    {
        String tp,tpc,lp,lpc;

        tp=txtTp.getText().toString();
        tpc=txtTpC.getText().toString();
        lp=txtLp.getText().toString();
        lpc=txtLpC.getText().toString();

        findViewById(R.id.textView4).setFocusable(true);


//        SharedPreferences u_details=getSharedPreferences("user_details",MODE_PRIVATE);
//        u_details.edit()
//                .putBoolean("registered",true)
//                .commit();
        if(lp.equals("")){
            msgDialogSetup.setupDialog("Please enter Login Pin",Optional.empty());
        }
        else if(tp.equals("")){
            msgDialogSetup.setupDialog("Please enter Transaction Pin",Optional.empty());
        }
        else if(lp.length()<4){
            msgDialogSetup.setupDialog("Login Pin must be 4 digit long",Optional.empty());
        }
        else if(tp.length()<4){
            msgDialogSetup.setupDialog("Transaction Pin must be 4 digit long",Optional.empty());
        }
        else if(tpc.length()<4){
            msgDialogSetup.setupDialog("Please verify Transaction Pin",Optional.empty());
        }
        else if(lpc.length()<4){
            msgDialogSetup.setupDialog("Please verify Login Pin",Optional.empty());
        }
        else
        {
            ProgressDialog pbar=ProgressDialog.show(this,"Registering","Please wait\nIt may take a while...");
            pbar.show();
            firebaseDatabase=FirebaseDatabase.getInstance().getReference("RegisterInfo");
             HashMap<String, String> hashMap = new HashMap<>();
             hashMap.put(ph, "");
             firebaseDatabase.child(ph).setValue(hashMap);

            firebaseDatabase2=FirebaseDatabase.getInstance().getReference("PinInfo");
            hashMap.clear();
            hashMap.put("LPin",txtLpC.getText().toString());
            hashMap.put("TPin",txtTpC.getText().toString());
            firebaseDatabase2.child(ph).setValue(hashMap);

            SharedPreferences u_details=getSharedPreferences("user_details",MODE_PRIVATE);
            u_details.edit()
                    .putString("name",name)
                    .putString("ac_no",ac)
                    .putString("phno",ph)
                    .putBoolean("isregistered",true)
                    .putString("tpin",txtTpC.getText().toString())
                    .commit();
            new Handler().postDelayed(()->{
                pbar.hide();
                startActivity(new Intent(this,Success_screen.class));
                finish();
            },5000);

        }
    }

//    public void setupDialog(String msg, Optional<Integer> img_src){
//        TextView txt=msg_dialog.findViewById(R.id.msg_box_text);
//        GifImageView iview=msg_dialog.findViewById(R.id.msg_img);
//
//        iview.setImageResource(img_src.orElse(R.drawable.information));
//
//        txt.setText(msg);
//        msg_dialog.show();
//    }

    @Override
    public void onBackPressed() {
        msgDialogSetup.setupDialog("Registration is in Progress\nDo you want to quit",Optional.empty());
        msgDialogSetup.layoutDisc.setVisibility(View.VISIBLE);
        msgDialogSetup.txtsave.setText("Yes");
        msgDialogSetup.txtdiscard.setText("No");

        msgDialogSetup.layoutSave.setOnClickListener(v->{
            super.onBackPressed();
        });
        msgDialogSetup.layoutDisc.setOnClickListener(v->{
            msgDialogSetup.msg_dialog.hide();
        });
    }

}