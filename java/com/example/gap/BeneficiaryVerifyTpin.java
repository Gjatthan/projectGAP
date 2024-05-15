package com.example.gap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gap.databinding.ActivityTransactionPinBinding;
import com.example.gap.databinding.MessageDialogBinding;
import com.example.gap.misc.MsgDialogSetup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class BeneficiaryVerifyTpin extends AppCompatActivity {

    ActivityTransactionPinBinding binding;
    SharedPreferences u_details;
    String tpin,r_acno,acno_rec,r_ph,acno,r_name,r_img;
    MsgDialogSetup msg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityTransactionPinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        shuffelBtn();

        msg=new MsgDialogSetup(this);
        u_details=getSharedPreferences("user_details",MODE_PRIVATE);
        tpin=u_details.getString("tpin","");
        acno=u_details.getString("ac_no","");

        r_acno=getIntent().getExtras().get("r_acno").toString();
        r_ph=getIntent().getExtras().get("r_phno").toString();
        r_name=getIntent().getExtras().get("r_name").toString();
        if(getIntent().getExtras().get("r_img")!=null)
            r_img=getIntent().getExtras().get("r_img").toString();
        else
            r_img=null;

    }

    public void onClickPay(View v){
        if(binding.txtPin.getText().toString().equals(tpin))
        {
            DatabaseReference dref=FirebaseDatabase.getInstance().getReference("Beneficiary").child(acno).child(r_acno);
            dref.child("ph").setValue(r_ph);
            dref.child("name").setValue(r_name);
            dref.child("ImageUrl").setValue(r_img);
            AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.CustomFontDialog));
            dialog.setMessage("Beneficiary Account added successfuly");
            dialog.setTitle(null);

            dialog.setPositiveButton("OK",(w,m)->{
                w.dismiss();
                Add_Beneficiary_ac.add_beneficiary.finish();
                finish();
            });
            dialog.show();
        }
        else {
            msg.setupDialog("Wrong TPin", Optional.empty());
            binding.txtPin.setText("");
        }
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

}
