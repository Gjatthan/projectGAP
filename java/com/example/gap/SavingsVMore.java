package com.example.gap;

import static com.example.gap.misc.Decoration.setComma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.Objects;

public class SavingsVMore extends AppCompatActivity {
    TextView txtname,txtdeposit,txtbranch,txtacno,txtactype,txtifsc;
    SharedPreferences u_details;
    String ac,name,deposit,branch,actype,ifsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savings_vmore);

        ProgressDialog dialog = new ProgressDialog(new ContextThemeWrapper(this, R.style.CustomFontDialog));
        dialog.setMessage("Please wait..");
        dialog.show();

        txtname=findViewById(R.id.txtcustname);
        txtdeposit=findViewById(R.id.txtdeposit);
        txtbranch=findViewById(R.id.txtbadrs);
        txtacno=findViewById(R.id.txtacno);
        txtactype=findViewById(R.id.txtactype);
        txtifsc=findViewById(R.id.txtifsc);

        u_details=getSharedPreferences("user_details",MODE_PRIVATE);
        ac=u_details.getString("ac_no","");

        txtacno.setText(ac);

        FirebaseDatabase.getInstance().getReference("ac_details").child(ac).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name= Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                //Toast.makeText(SavingsVMore.this, snapshot.child("name").getValue().toString(), Toast.LENGTH_SHORT).show();
                deposit=Objects.requireNonNull(snapshot.child("savings").getValue()).toString();
                branch=Objects.requireNonNull(snapshot.child("branch").getValue()).toString();
                actype=Objects.requireNonNull(snapshot.child("actype").getValue()).toString();
                ifsc=Objects.requireNonNull(snapshot.child("ifsc").getValue()).toString();

                txtname.setText(name);
                txtdeposit.setText("₹ "+setComma(deposit));
                txtbranch.setText(branch);
                txtactype.setText(actype);
                txtifsc.setText(ifsc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        findViewById(R.id.btnback).setOnClickListener(v->{finish();});

        new Handler().postDelayed(()->{
            dialog.hide();
            findViewById(R.id.layout).setVisibility(View.VISIBLE);
        }, 5000);
    }
}