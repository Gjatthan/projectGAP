package com.example.gap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gap.databinding.ActivityAddBeneficiaryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Add_Beneficiary_ac extends AppCompatActivity {

    ActivityAddBeneficiaryBinding binding;
    boolean ac_found=false;
    String rec_name,rec_ifsc,rec_ph,img_url=null;
    public static Activity add_beneficiary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddBeneficiaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        add_beneficiary=this;

        binding.txtacno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>=5){
                    String ac_no=binding.txtacno.getText().toString();
                    DatabaseReference dref=FirebaseDatabase.getInstance().getReference("ac_details");
                    FirebaseDatabase.getInstance().getReference("ac_details")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.hasChild(ac_no)){
                                        rec_ifsc=snapshot.child(ac_no).child("ifsc").getValue().toString();
                                        rec_name=snapshot.child(ac_no).child("name").getValue().toString();
                                        rec_ph=snapshot.child(ac_no).child("phno").getValue().toString();
                                        ac_found=true;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                }
                else{
                    binding.layoutifsc.setVisibility(View.GONE);
                    binding.layoutname.setVisibility(View.GONE);
                    binding.lyoutBtn.setVisibility(View.GONE);
                }
            }
        });
        binding.txtacno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    new Handler().postDelayed(()->{
                        if(ac_found){
                            binding.txtifsc.setText(rec_ifsc);
                            binding.txtname.setText(rec_name);
                            binding.layoutifsc.setVisibility(View.VISIBLE);
                            binding.layoutname.setVisibility(View.VISIBLE);
                            binding.lyoutBtn.setVisibility(View.VISIBLE);
                            binding.txtacno.setError(null);
                            FirebaseDatabase.getInstance().getReference("RegisterInfo").child(rec_ph).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.hasChild("ImageUrl"))
                                        img_url=snapshot.child("ImageUrl").getValue().toString();
                                    else
                                        img_url=null;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        else
                            binding.txtacno.setError("Please check account number");
                    },4000);
                }
            }
        });

        binding.btnptoceed.setOnClickListener(r->{
            TextView txt;
            Dialog d=new Dialog(this);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            d.setContentView(R.layout.beneficiary_dialog);
            txt=d.findViewById(R.id.txtpname);
            txt.setText(binding.txtname.getText());
            txt=d.findViewById(R.id.txtpacno);
            txt.setText(binding.txtacno.getText());
            txt=d.findViewById(R.id.txtpifsc);
            txt.setText(binding.txtifsc.getText());
            d.show();

            d.findViewById(R.id.btnclose).setOnClickListener(v->{
                d.dismiss();
            });

            d.findViewById(R.id.btnptoceed).setOnClickListener(v->{
                Intent i=new Intent(this,BeneficiaryVerifyTpin.class);
                i.putExtra("r_acno",binding.txtacno.getText());
                i.putExtra("r_phno",rec_ph);
                i.putExtra("r_name",rec_name);
                i.putExtra("r_img",img_url);
                d.dismiss();
                startActivity(i);
            });
        });
    }
}