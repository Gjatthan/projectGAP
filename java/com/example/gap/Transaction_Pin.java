package com.example.gap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gap.databinding.ActivityTransactionPinBinding;
import com.example.gap.misc.MsgDialogSetup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

public class Transaction_Pin extends AppCompatActivity {

    ActivityTransactionPinBinding binding;
    SharedPreferences u_details;
    MsgDialogSetup msgDialogSetup;
    double amt_send,avail_bal_sender,avail_bal_recv;
    double updateAmt;
    double updateAmtRec;
    int tpin_attempts;
    String ac_sender,sphno,rphno,ac_recv,sec_pin;

    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityTransactionPinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vibrator= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        msgDialogSetup=new MsgDialogSetup(this);
        u_details=getSharedPreferences("user_details",MODE_PRIVATE);

        ac_sender=u_details.getString("ac_no","");
        sphno=u_details.getString("phno","");
        tpin_attempts=u_details.getInt("tpin_attempts",0);

        avail_bal_sender=Double.parseDouble(getIntent().getExtras().get("bal_sender").toString());
        ac_recv=getIntent().getExtras().get("ac_recv").toString();
        avail_bal_recv=Double.parseDouble(getIntent().getExtras().get("bal_recv").toString());
        rphno=getIntent().getExtras().get("phno").toString();
        //rec_amt=Double.parseDouble(getIntent().getExtras().get("rec_amt").toString());

        shuffelBtn();

        amt_send=Double.parseDouble(getIntent().getExtras().get("amt").toString());


//        FirebaseDatabase.getInstance().getReference("ac_details").child(ac_sender).child("savings").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                avail_bal_sender=Double.parseDouble(snapshot.getValue().toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        FirebaseDatabase.getInstance().getReference("ac_details").child(ac_recv).child("savings").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                avail_bal_recv=Double.parseDouble(snapshot.getValue().toString());
//                //FirebaseDatabase.getInstance().getReference("ac_details").child(ac_recv).child("savings").setValue(u_amt);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    public void onClickClear(View v){
        String number=binding.txtPin.getText().toString();
        int length=number.length()-1;
        if(length>=0)
            binding.txtPin.setText(number.substring(0,length));
        else
            shuffelBtn();
    }

    public void onClickPin(View v){
        String text=binding.txtPin.getText().toString();
        Button btn=(Button)v;
        binding.txtPin.setText(text+btn.getText().toString());
    }

    void shuffelBtn(){
        Button button[]={binding.btn0,binding.btn1,binding.btn2,
                binding.btn3,binding.btn4,binding.btn5,binding.btn6,
                binding.btn7,binding.btn8,binding.btn9};
        ArrayList<Integer> gen_num=new ArrayList<>();
        Random random=new Random();

        for(int i=0;i<=9;i++){
            int num=random.nextInt(10);
            if(gen_num.contains(num)){
                i--;
                continue;
            }
            gen_num.add(num);
            button[i].setText(Integer.toString(num));
        }

    }

    @SuppressLint("SuspiciousIndentation")
    public void onClickPay(View v){
        String tpin;
        if(amt_send>100000)
            tpin=getIntent().getExtras().get("sec_pin").toString();
        else
            tpin=u_details.getString("tpin","");
        //Toast.makeText(this, ""+tpin, Toast.LENGTH_SHORT).show();
        String eTpin=binding.txtPin.getText().toString();
        if(eTpin.equals("")){
            msgDialogSetup.setupDialog("Please enter pin", Optional.empty());
        }
        else
        if(!tpin.equals("")){ //remove @ if error occurs
            if(tpin.equals(eTpin)){
//                if(amt_send>avail_bal_sender)
//                    msgDialogSetup.setupDialog("Insufficient balance",Optional.empty());
//                else
//                {
                    u_details.edit().putInt("tpin_attempts", 0).commit();
                    sendMoney();
//                }
            }
            else{
                tpin_attempts += 1;
                if(tpin_attempts>=3){
                    u_details.edit().putInt("tpin_attempts", 0).commit();
                    FirebaseDatabase.getInstance().getReference("ac_details").child(ac_sender).child("block").setValue(true);
                    msgDialogSetup.setupDialog("Out of attempts\nYour account is temporarily blocked",Optional.empty());
                    msgDialogSetup.layoutSave.setOnClickListener(m->{
                        Intent i=new Intent(getApplicationContext(),Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("blocked",true);
                        startActivity(i);
                    });
                }
                else
                {
                    binding.txtPin.startAnimation(AnimationUtils.loadAnimation(Transaction_Pin.this, R.anim.shake_animation));
                    binding.txtPin.setText("");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    }

                    u_details.edit().putInt("tpin_attempts", tpin_attempts).commit();
                    msgDialogSetup.setupDialog("Wrong Transaction Pin\nYou have only "+(3-tpin_attempts)+" attempts left", Optional.empty());
                }
            }
        }
        else
            msgDialogSetup.setupDialog("null tpin", Optional.empty());

    }

    void sendMoney(){
        ProgressDialog pbar=ProgressDialog.show(this,"","Transaction is in progress");
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        String send_msg,recv_msg;
        updateAmt=avail_bal_sender-amt_send;
        updateAmtRec=amt_send+avail_bal_recv;

        send_msg=String.format(getResources().getString(R.string.bank_sms),ac_sender," debited",amt_send,timeStamp,updateAmt);
        recv_msg=String.format(getResources().getString(R.string.bank_sms),ac_recv," creadited",amt_send,timeStamp,updateAmtRec);
        FirebaseDatabase.getInstance().getReference("ac_details").child(ac_sender).child("savings").setValue(updateAmt);
        FirebaseDatabase.getInstance().getReference("ac_details").child(ac_recv).child("savings").setValue(updateAmtRec);
        updateTransaction();

        new Handler().postDelayed(()->{
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> parts = smsManager.divideMessage(send_msg);
            smsManager.sendMultipartTextMessage(sphno,"",parts,null,null);

            ArrayList<String> parts2 = smsManager.divideMessage(recv_msg);
            smsManager.sendMultipartTextMessage(rphno,"",parts2,null,null);

            pbar.hide();

            Fund_Transfer.activity.finish();
            startActivity(new Intent(Transaction_Pin.this,Payement_Success.class));
            finish();
        },1000);

//        parts = smsManager.divideMessage("money revd successfully");
//        smsManager.sendMultipartTextMessage(rphno,"",parts,null,null);


    }

    void updateTransaction(){
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        DatabaseReference dbref=FirebaseDatabase.getInstance().getReference("Transaction");
        String key=dbref.push().getKey();
        HashMap<String,Object> map=new HashMap<>();
        map.put("Description","Payment through mobile banking App");
        map.put("Date",timeStamp);
        map.put("Amount",amt_send);
        dbref.child(key).setValue(map);

        map.clear();
        map.put("Bal",updateAmt);
        map.put("Tdetails","Debited");
        map.put("ac",ac_recv);
        dbref.child(key).child(ac_sender).setValue(map);

        map.clear();
        map.put("Bal",updateAmtRec);
        map.put("Tdetails","Credited");
        map.put("ac",ac_sender);
        dbref.child(key).child(ac_recv).setValue(map);
    }
}