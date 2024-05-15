package com.example.gap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.gap.databinding.ActivityEpassBinding;
import com.example.gap.misc.TransactionHistoryAdapter;
import com.example.gap.misc.TransactionHistoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class E_Pass extends AppCompatActivity {

    TransactionHistoryAdapter adapter;
    ArrayList<TransactionHistoryModel> arrayList;
    ActivityEpassBinding binding;
    SharedPreferences u_details;
    String my_acno,name,ifsc,branch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEpassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        u_details=getSharedPreferences("user_details",MODE_PRIVATE);
        my_acno=u_details.getString("ac_no","");
        name=u_details.getString("name","");

        arrayList=new ArrayList<>();
        adapter=new TransactionHistoryAdapter(arrayList,this);
        binding.epassrecycle.setLayoutManager(new LinearLayoutManager(this));
        binding.epassrecycle.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference("ac_details").child(my_acno).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                branch= Objects.requireNonNull(snapshot.child("branch").getValue()).toString();
                ifsc= Objects.requireNonNull(snapshot.child("ifsc").getValue()).toString();
                binding.txtacno.setText(my_acno);
                binding.txtbranch.setText(branch);
                binding.txtname.setText(name);
                binding.txtifsc.setText(ifsc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("Transaction").addValueEventListener(new ValueEventListener() {
            String desc,recv_ac,drcrmsg,drcramt,date,bal;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dshot:snapshot.getChildren()){
                    if(dshot.hasChild(my_acno)){
                        desc= Objects.requireNonNull(dshot.child("Description").getValue()).toString();
                        date=Objects.requireNonNull(dshot.child("Date").getValue()).toString();
                        drcramt=Objects.requireNonNull(dshot.child("Amount").getValue()).toString();
                        recv_ac=Objects.requireNonNull(dshot.child(my_acno).child("ac").getValue()).toString();
                        drcrmsg=Objects.requireNonNull(dshot.child(my_acno).child("Tdetails").getValue()).toString();
                        bal=Objects.requireNonNull(dshot.child(my_acno).child("Bal").getValue()).toString();

                        arrayList.add(new TransactionHistoryModel(desc,date,drcramt,bal,drcrmsg,recv_ac));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}