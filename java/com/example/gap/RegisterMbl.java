package com.example.gap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gap.misc.GenerateOTP;
import com.example.gap.misc.MsgDialogSetup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterMbl extends AppCompatActivity {
    EditText txtph;
    DatabaseReference dref;
    String phone;
    ProgressDialog pbar;
    MsgDialogSetup msgDialogSetup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mbl);
        txtph=findViewById(R.id.txtphnum);

        msgDialogSetup=new MsgDialogSetup(this);
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==100&&ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED)
            getOtp();
        else
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
    }

    public void onClickGetOTP(View v){
        phone=txtph.getText().toString();

        Pattern pattern=Pattern.compile("^[6,7,8,9]+[0-9]{9}");
        Matcher matcher=pattern.matcher(phone);
        if(!matcher.find()){
            msgDialogSetup.setupDialog("Enter valid phone number",Optional.empty());
        }
        else{
            pbar=ProgressDialog.show(this,"","Connecting ...");
            checkForNumberPresent();
        }
        //dref= FirebaseDatabase.getInstance().getReference("login");
//        dref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                boolean found=false;
//                for(DataSnapshot snapshot1:snapshot.getChildren()){
//                    if(snapshot1.getKey().toString().equals(phone)) {
//                        found = true;
//                        break;
//                    }
//                }
//                if(!found){
//                    dref.setValue(phone);
//                    prepareForOtp();
//                }
//                else
//                {
//                    pbar.hide();
//                    Toast.makeText(RegisterMbl.this, "Please log out rom other device", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    public void checkForNumberPresent(){
        dref= FirebaseDatabase.getInstance().getReference("ac_details");
        dref.addValueEventListener(new ValueEventListener() {
            boolean numfound=false;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ac_no:snapshot.getChildren()){
                    if(ac_no.child("phno").getValue().toString().equals(phone))
                    {
                        setLogin(phone);
                        numfound=true;
                        break;
                    }
                }
                if(!numfound){
                    pbar.hide();
                    msgDialogSetup.setupDialog("Phone number is not registered", Optional.empty());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        dref.child(phone).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if(task.isSuccessful())
//                {
//                    if(task.getResult().exists())
//                    {
//                        if(Objects.requireNonNull(task.getResult().child("login").getValue()).toString().equals("false"))
//                        {
//                            PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+txtph.getText().toString(), 10, TimeUnit.SECONDS, RegisterMbl.this,
//                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                                        @Override
//                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//
//                                        }
//
//                                        @Override
//                                        public void onVerificationFailed(@NonNull FirebaseException e) {
//                                            Toast.makeText(RegisterMbl.this, "fail "+e.getMessage(), Toast.LENGTH_LONG).show();
//                                        }
//
//                                        @Override
//                                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                            //super.onCodeSent(s, forceResendingToken);
//                                            String otp=s;
//                                            pbar.hide();
//                                            Intent i=new Intent(RegisterMbl.this,OTP.class);
//                                            i.putExtra("phonenum",txtph.getText().toString());
//                                            i.putExtra("otp",otp);
//                                            startActivity(i);
//                                            finish();
//                                        }
//                                    });
//
//                        }
//                        else
//                        {
//                            pbar.hide();
//                            AlertDialog.Builder ad=new AlertDialog.Builder(RegisterMbl.this);
//                            ad.setMessage("Your already loged in using other device");
//                            ad.setPositiveButton("Log out",(d,u)->{});
//                            ad.setNegativeButton("Cancel",(d,u)->{});
//                            ad.show();
//                        }
//                    }
//                    else
//                    {
//                        pbar.hide();
//                        AlertDialog.Builder ad=new AlertDialog.Builder(RegisterMbl.this);
//                        ad.setMessage("Phone number is not registered");
//                        ad.setPositiveButton("Ok",(d,u)->{});
//                        ad.show();
//                    }
//                }
//            }
//        });
    }

    public void setLogin(String ph){
        pbar.hide();
        dref = FirebaseDatabase.getInstance().getReference("RegisterInfo");
        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean exists=false;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dshot:snapshot.getChildren()){
                    if(dshot.getKey().equals(ph)){
                        exists=true;
                        break;
                    }
                }

                if(!exists){
                    if(ActivityCompat.checkSelfPermission(RegisterMbl.this, android.Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED)
                        getOtp();
                    else{
                        ActivityCompat.requestPermissions(RegisterMbl.this,new String[]{Manifest.permission.SEND_SMS},100);
                    }
                }
                else {
                    pbar.hide();
                    msgDialogSetup.setupDialog("Please log out from other device", Optional.empty());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getOtp(){
        String otp= GenerateOTP.getOtp();
        pbar.hide();
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(otp + " " + " is ur otp");
        smsManager.sendMultipartTextMessage(txtph.getText().toString(),"",parts,null,null);
        Intent i=new Intent(RegisterMbl.this,OTP.class);
        i.putExtra("phonenum",txtph.getText().toString());
        i.putExtra("otp",otp);

        startActivity(i);
        finish();
//        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+txtph.getText().toString(), 10, TimeUnit.SECONDS, RegisterMbl.this,
//                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//
//            }
//
//            @Override
//            public void onVerificationFailed(@NonNull FirebaseException e) {
//                Toast.makeText(RegisterMbl.this, "fail "+e.getMessage(), Toast.LENGTH_LONG).show();
//                pbar.hide();
//            }
//
//            @Override
//            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                super.onCodeSent(s, forceResendingToken);
//                String otp=s;
//                pbar.hide();
//                Intent i=new Intent(RegisterMbl.this,OTP.class);
//                i.putExtra("phonenum",txtph.getText().toString());
//                i.putExtra("otp",otp);
//                startActivity(i);
//                finish();
//            }
//        });
    }

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