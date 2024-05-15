package com.example.gap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.gap.databinding.ActivityFundTransferBeneficiaryBinding;
import com.example.gap.misc.AllConstants;
import com.example.gap.misc.BeneficiaryAdapter;
import com.example.gap.misc.BeneficiaryModel;
import com.example.gap.misc.ChatBotAdapter;
import com.example.gap.misc.ChatBotModalClass;
import com.example.gap.misc.MsgDialogSetup;
import com.example.gap.misc.SelectInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class FundTransferBeneficiary extends AppCompatActivity implements SelectInterface {

    ActivityFundTransferBeneficiaryBinding binding;
    ArrayList<BeneficiaryModel> arrayList=new ArrayList<>();
    public static BeneficiaryAdapter adapter;
    SharedPreferences u_details;
    String acno;
    //public static Activity ftb;
    boolean hasBeneficiary;
    ProgressDialog progressDialog;

    DatabaseReference dref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFundTransferBeneficiaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Fund Transfer");


        //ftb=this;
        binding.recyclerViewBenefiary.setLayoutManager(new LinearLayoutManager(this));
        u_details=getSharedPreferences("user_details",MODE_PRIVATE);
        acno=u_details.getString("ac_no","");
        //hasBeneficiary=u_details.getBoolean("hasBeneficiary",false);
        adapter=new BeneficiaryAdapter(arrayList,this,acno,this);
        binding.recyclerViewBenefiary.setAdapter(adapter);

        progressDialog=new ProgressDialog(new ContextThemeWrapper(this, R.style.CustomFontDialog));
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        dref=FirebaseDatabase.getInstance().getReference("Beneficiary").child(acno);
        dref.keepSynced(true);
        updateBeneficiary();

    }

    public void updateBeneficiary(){
        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    if(snapshot1.exists()&&snapshot1.hasChild("name")&&snapshot1.hasChild("ph")){
                        String ac_rec,name,ph,img_url=null;
                        ac_rec= snapshot1.getKey();
                        name= Objects.requireNonNull(snapshot1.child("name").getValue()).toString();
                        ph= Objects.requireNonNull(snapshot1.child("ph").getValue()).toString();
                        if(snapshot1.hasChild("ImageUrl"))
                            img_url=Objects.requireNonNull(snapshot1.child("ImageUrl").getValue()).toString();
                        arrayList.add(new BeneficiaryModel(name,ph,ac_rec,img_url));
                    }
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
                if(arrayList.size()>0)
                    binding.txtnobenifmsg.setVisibility(View.INVISIBLE);
                else
                    binding.txtnobenifmsg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        arrayList.clear();
        updateBeneficiary();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_benificiary_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this,Add_Beneficiary_ac.class));
        return false;
    }

    @Override
    public void onItemClicked(int action_code,BeneficiaryModel beneficiaryModel) {
        if(action_code== AllConstants.DELETE){
            MsgDialogSetup dialog = new MsgDialogSetup(this);
            dialog.setupDialog("Do you want to remove " + beneficiaryModel.name + " from beneficiary list?", Optional.empty());
            dialog.layoutDisc.setVisibility(View.VISIBLE);
            dialog.txtdiscard.setText("No");
            dialog.layoutDisc.setOnClickListener(v -> {
                dialog.msg_dialog.dismiss();
            });

            dialog.txtsave.setText("YES");
            dialog.layoutSave.setOnClickListener(v -> {
                FirebaseDatabase.getInstance().getReference("Beneficiary").child(acno).child(beneficiaryModel.ac).removeValue();
                MsgDialogSetup d = new MsgDialogSetup(this);
                d.setupDialog("Removed Successfully", Optional.empty());
                d.layoutSave.setOnClickListener(x -> {
                    arrayList.remove(beneficiaryModel);
                    if (arrayList.size() > 0)
                        binding.txtnobenifmsg.setVisibility(View.INVISIBLE);
                    else
                        binding.txtnobenifmsg.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
//                    for(int i=0;i<arrayList.size();i++){
//                        if(arrayList.get(i).ac.equals(beneficiaryModel.ac)){
//                            arrayList.remove(i);
//                            adapter.notifyDataSetChanged();
//                            d.msg_dialog.dismiss();
//                            break;
//                        }
//                    }
                    d.msg_dialog.dismiss();
                });
                dialog.msg_dialog.dismiss();

                //this.onCreate(bundle);
            });
        }
        else if(action_code==AllConstants.SEND){
            Intent i=new Intent(this,Fund_Transfer.class);
            i.putExtra("phno",beneficiaryModel.ph);
            i.putExtra("acno",beneficiaryModel.ac);
            startActivity(i);
            finish();
        }
    }
}