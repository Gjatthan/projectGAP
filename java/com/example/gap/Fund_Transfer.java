package com.example.gap;

import static com.example.gap.misc.GenerateOTP.getOtp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gap.databinding.ActivityFundTransferBinding;
import com.example.gap.misc.Decoration;
import com.example.gap.misc.MsgDialogSetup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class Fund_Transfer extends AppCompatActivity {

    ActivityFundTransferBinding binding;
    String phno,acno,name,imgurl,acno_sender,drpin,sPhno;
    double bal_sender,bal_recv;
    SharedPreferences u_details;
    public static Activity activity;
    MsgDialogSetup msgDialogSetup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFundTransferBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity=this;

        msgDialogSetup=new MsgDialogSetup(this);

        phno=getIntent().getExtras().getString("phno");
        acno=getIntent().getExtras().getString("acno");

        u_details=getSharedPreferences("user_details",MODE_PRIVATE);
        acno_sender=u_details.getString("ac_no","");
        sPhno=u_details.getString("phno","");

        binding.txtamt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //binding.txtamt.setText(Decoration.setComma(s.toString()));
                //new DownloadFilesTask().execute(s.toString());
                if(s.length()>0)
                    binding.btnProceed.setVisibility(View.VISIBLE);
                else
                    binding.btnProceed.setVisibility(View.INVISIBLE);
            }
        });

        FirebaseDatabase.getInstance().getReference("ac_details").child(acno_sender).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bal_sender=Double.parseDouble(snapshot.child("savings").getValue().toString());
                drpin= Objects.requireNonNull(snapshot.child("drpin").getValue()).toString();
                //Toast.makeText(Fund_Transfer.this, ""+drpin, Toast.LENGTH_SHORT).show();
                //FirebaseDatabase.getInstance().getReference("ac_details").child(ac_recv).child("savings").setValue(u_amt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference("ac_details").child(acno).child("savings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bal_recv=Double.parseDouble(snapshot.getValue().toString());
                //FirebaseDatabase.getInstance().getReference("ac_details").child(ac_recv).child("savings").setValue(u_amt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("ac_details").child(acno).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.txtName.setText(snapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("RegisterInfo").child(phno).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("ImageUrl")){
                    imgurl=snapshot.child("ImageUrl").getValue().toString();
                    binding.iviewUser.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Picasso.get().load(imgurl).into(binding.iviewUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.txtAcno.setText(acno);

        binding.imgBack.setOnClickListener(v->{
            startActivity(new Intent(this,Home_Screen.class));
            finish();
        });

        binding.btnProceed.setOnClickListener(v->{
            String amt=binding.txtamt.getText().toString();
            Intent i=new Intent(this,Transaction_Pin.class);

            if(Long.parseLong(amt)>bal_sender)
                msgDialogSetup.setupDialog("Insufficient balance", Optional.empty());
            else if(bal_sender-Long.parseLong(amt)<500)
                msgDialogSetup.setupDialog("Please maintain minimum balance", Optional.empty());
            else {
                if(Long.parseLong(amt)>100000){
                    String sec_pin=getOtp().substring(0,2);
                    SmsManager smsManager = SmsManager.getDefault();
                    ArrayList<String> parts = smsManager.divideMessage(String.format(getResources().getString(R.string.otp_sms),sec_pin));
                    smsManager.sendMultipartTextMessage(sPhno,"",parts,null,null);
                    sec_pin=sec_pin+drpin.substring(2);
                    //Toast.makeText(activity, ""+drpin.substring(2), Toast.LENGTH_SHORT).show();
                    i.putExtra("sec_pin",sec_pin);
                }
                i.putExtra("amt", amt);
                i.putExtra("ac_recv", acno);
                i.putExtra("phno", phno);
                i.putExtra("bal_recv", bal_recv);
                i.putExtra("bal_sender", bal_sender);
                startActivity(i);
            }
        });
    }

//    private class DownloadFilesTask extends AsyncTask<String, Integer, Void> {
//        String amtn;
//        protected Void doInBackground(String... amt) {
//            Toast.makeText(Fund_Transfer.this, ""+amt[0], Toast.LENGTH_SHORT).show();
//            amtn=Decoration.setComma(amt[0]);
//            binding.txtamt.setText(amtn);
//            return null;
//        }
//
//        protected void onProgressUpdate(Integer... progress) {
//
//        }
//
//    }
}