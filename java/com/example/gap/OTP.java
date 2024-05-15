package com.example.gap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gap.misc.GenerateOTP;
import com.example.gap.misc.MsgDialogSetup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class OTP extends AppCompatActivity {
EditText otp1,otp2,otp3,otp4,otp5,otp6;
TextView txttimer,txtph;
String otp,eOtp="",phnum,gender,email,adrs;
Boolean timerStop=false;
DatabaseReference dref;
MsgDialogSetup msgDialogSetup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otp1=findViewById(R.id.otp1);
        otp2=findViewById(R.id.otp2);
        otp3=findViewById(R.id.otp3);
        otp4=findViewById(R.id.otp4);
        otp5=findViewById(R.id.otp5);
        otp6=findViewById(R.id.otp6);
        txttimer=findViewById(R.id.txttimer);
        txtph=findViewById(R.id.txtphonenum);

        msgDialogSetup=new MsgDialogSetup(this);

        phnum=getIntent().getExtras().getString("phonenum");
        otp=getIntent().getExtras().getString("otp");

        txtph.setText(phnum);

        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //eOtp=s+eOtp.substring(1);
                concatOTP();
                otp2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //eOtp=eOtp.substring(0,1)+s+eOtp.substring(2);
                concatOTP();
                otp3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //eOtp=eOtp.substring(0,2)+s+eOtp.substring(3);
                concatOTP();
                otp4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //eOtp=eOtp.substring(0,3)+s+eOtp.substring(4);
                concatOTP();
                otp5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //eOtp=eOtp.substring(0,4)+s+eOtp.substring(5);
                concatOTP();
                otp6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                concatOTP();
                //eOtp=eOtp.substring(0,5)+s;
                //verifyOTP(eOtp);
                //Toast.makeText(OTP.this, ""+concatOTP(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                txttimer.setText(f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                txttimer.setText("0:00");
                timerStop=true;
                otp="";
            }
        }.start();

    }

    private void verifyOTP(String o)
    {
            ProgressDialog pr = ProgressDialog.show(OTP.this, "", "Verifying");
            if(otp.equals(o)){
                pr.hide();
                dref= FirebaseDatabase.getInstance().getReference().child("ac_details");
                dref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dshot : snapshot.getChildren()) {
                            if (dshot.child("phno").getValue().toString().equals(phnum)) {
//                                        SharedPreferences u_details=getSharedPreferences("user_details",MODE_PRIVATE);
//                                        u_details.edit()
//                                                .putString("name",dshot.child("name").getValue().toString())
//                                                .putString("ac_no",dshot.getKey())
//                                                .putString("phno",phnum)
//                                                .commit();
                                Intent i = new Intent(OTP.this, TpinMpin.class);
                                i.putExtra("phone_no", phnum);
                                i.putExtra("ac_no", dshot.getKey());
                                i.putExtra("name", dshot.child("name").getValue().toString());
                                startActivity(i);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
            else{
                pr.hide();
                AlertDialog.Builder builder = new AlertDialog.Builder(OTP.this);
                builder.setMessage("Wrong OTP "+o);
                builder.setPositiveButton("OK", (d, v) -> {
                });
                builder.show();
            }

            //PhoneAuthCredential p = PhoneAuthProvider.getCredential(otp, o);
//            FirebaseAuth.getInstance().signInWithCredential(p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()) {
//                        pr.hide();
//                        dref= FirebaseDatabase.getInstance().getReference().child("ac_details");
//                        dref.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                for(DataSnapshot dshot:snapshot.getChildren())
//                                {
//                                    if(dshot.child("phno").getValue().toString().equals(phnum))
//                                    {
////                                        SharedPreferences u_details=getSharedPreferences("user_details",MODE_PRIVATE);
////                                        u_details.edit()
////                                                .putString("name",dshot.child("name").getValue().toString())
////                                                .putString("ac_no",dshot.getKey())
////                                                .putString("phno",phnum)
////                                                .commit();
//                                        Intent i=new Intent(OTP.this, TpinMpin.class);
//                                        i.putExtra("phone_no",phnum);
//                                        i.putExtra("ac_no",dshot.getKey());
//                                        i.putExtra("name",dshot.child("name").getValue().toString());
//                                        startActivity(i);
//                                        finish();
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//                    } else {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(OTP.this);
//                        builder.setMessage("Wrong OTP");
//                        builder.setPositiveButton("OK", (d, v) -> {
//                        });
//                        builder.show();
//                    }
//                }
//            });
    }

    public void onClickResend(View v)
    {
        if(timerStop) {
            AlertDialog.Builder ab=new AlertDialog.Builder(this);
            SpannableString msg = new SpannableString("Do you want to edit the Phone Number\n"+phnum);
            msg.setSpan(new StyleSpan(Typeface.BOLD), 36, 47, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ab.setMessage(msg);
            ab.setPositiveButton("Edit",(d,m)->{
                startActivity(new Intent(this,RegisterMbl.class));
                finish();
            });
            ab.setNegativeButton("No",(d,n)->{
                new CountDownTimer(60000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        // Used for formatting digit to be in 2 digits only
                        NumberFormat f = new DecimalFormat("00");
                        long min = (millisUntilFinished / 60000) % 60;
                        long sec = (millisUntilFinished / 1000) % 60;
                        txttimer.setText(f.format(min) + ":" + f.format(sec));
                    }
                    // When the task is over it will print 00:00:00 there
                    public void onFinish() {
                        txttimer.setText("0:00");
                        timerStop=true;
                        otp="";
                    }
                }.start();
                otp=GenerateOTP.getOtp();
                timerStop=false;
                SmsManager smsManager = SmsManager.getDefault();
                ArrayList<String> parts = smsManager.divideMessage(otp + " " + " is ur otp");
                smsManager.sendMultipartTextMessage(txtph.getText().toString(),"",parts,null,null);
//                ProgressDialog pbar=ProgressDialog.show(this,"","Connecting");
//                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+phnum, 10, TimeUnit.SECONDS, OTP.this,
//                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                            @Override
//                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//
//                            }
//
//                            @Override
//                            public void onVerificationFailed(@NonNull FirebaseException e) {
//                                Toast.makeText(OTP.this, "fail "+e.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//
//                            @Override
//                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                //super.onCodeSent(s, forceResendingToken);
//                                pbar.hide();
//                                timerStop=false;
//                            }
//                        });
            });
            ab.show();
        }
        else
            Toast.makeText(this, "Please Wait ...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        msgDialogSetup.setupDialog("Registration is in Progress\nDo you want to quit", Optional.empty());
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

    public void concatOTP(){
        if(otp1.getText().length()>0&&otp2.getText().length()>0&&otp3.getText().length()>0
                &&otp4.getText().length()>0&&otp5.getText().length()>0&&otp6.getText().length()>0) {
            eOtp= otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString()
                    + otp4.getText().toString() + otp5.getText().toString() + otp6.getText().toString();
            verifyOTP(eOtp);
        }
    }
}