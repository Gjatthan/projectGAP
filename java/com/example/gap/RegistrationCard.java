package com.example.gap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gap.databinding.ActivityRegisterMblBinding;
import com.example.gap.databinding.ActivityRegistrationCardBinding;
import com.example.gap.misc.MsgDialogSetup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Optional;

public class RegistrationCard extends AppCompatActivity {

    public EditText txtsecans,txtcardnum,txtcardvalid,txtcardpin ;
    //public TextView txtsecqst;
    public String[] qst={"What is the result of 2+2?","Which is the 2 digit\nmaximum number?","What is the result of 2x4x0?"};
    public String[] ans={"4","99","0"};
    public int qst_index=1,prevLength=0,length=0,prevLengthValid=0,lengthValid=0;
    DatabaseReference dbase;

    ActivityRegistrationCardBinding binding;

    MsgDialogSetup msgDialogSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityRegistrationCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        txtsecans=findViewById(R.id.txtsecans);
        //txtsecqst=findViewById(R.id.txtsecqst);
        txtcardnum=findViewById(R.id.txtcardnum);
        txtcardvalid=findViewById(R.id.txtcardvalid);
        txtcardpin=findViewById(R.id.txtcardpin);

        //String tenure[] = new String[]{"Months", "Years"};
        ArrayAdapter<String> adptr;
        adptr = new ArrayAdapter<String>(this, R.layout.spinner_layout2, qst);
        binding.spinnerQst.setAdapter(adptr);

        msgDialogSetup=new MsgDialogSetup(this);

        binding.txtcardnum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(RegistrationCard.this, ""+binding.txtcardnum.getImeOptions(), Toast.LENGTH_SHORT).show();
                if(binding.txtcardnum.getImeOptions()==EditorInfo.IME_ACTION_DONE)
                    Toast.makeText(RegistrationCard.this, "done", Toast.LENGTH_SHORT).show();
                if(binding.txtcardnum.length()>prevLength && binding.txtcardnum.length()%5==0 && binding.txtcardnum.length()!=0)
                {
                    String text,curText,lastText;
                    length = binding.txtcardnum.length() - 1;
                    prevLength=binding.txtcardnum.length();

                    text = binding.txtcardnum.getText().toString();
                    curText = text.substring(0, length);
                    lastText = text.substring(length);

                    binding.txtcardnum.setText(curText + " " + lastText);
                    binding.txtcardnum.setSelection(binding.txtcardnum.length());
                }
                if(binding.txtcardnum.length()==19) {
                    binding.txtcardvalid.requestFocus(View.FOCUS_FORWARD);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(txtcardnum.length()==0) {
                    //txtcardnum.setTextSize(18);
                    prevLength=0;
                }
            }
        });

        txtcardvalid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String date=txtcardvalid.getText().toString();
                //txtcardvalid.setTextSize(24);

                if(txtcardvalid.length()==3 && txtcardvalid.length()>prevLengthValid )
                {
                    prevLengthValid=txtcardvalid.length();
                    lengthValid = txtcardvalid.length() - 1;
                    txtcardvalid.setText(date.substring(0,lengthValid)+"/"+date.substring(lengthValid));
                }
                txtcardvalid.setSelection(txtcardvalid.length());

                if(binding.txtcardvalid.length()==5) {
                    binding.txtcardpin.requestFocus(View.FOCUS_FORWARD);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(txtcardvalid.length()==0) {
                    //txtcardnum.setTextSize(10);
                    prevLengthValid=0;
                }
            }
        });

        txtcardnum.setOnFocusChangeListener((v,hasfocus)->{
            if(!hasfocus&&txtcardnum.getText().length()<1) {
                msgDialogSetup.setupDialog("Please enter card number",Optional.empty());
            }
            else if(!hasfocus&&txtcardnum.getText().length()<19)
                msgDialogSetup.setupDialog("please enter valid card number",Optional.empty());
        });

        txtcardpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(txtcardpin.length()==4)
                    binding.txtsecans.requestFocus(View.FOCUS_FORWARD);
            }
        });
    }

    public void onClickRefresh(View v)
    {
//        if(qst_index>=qst.length)
//            qst_index=0;
//        txtsecqst.setText(qst[qst_index]);
//        qst_index++;
    }

    public void onClickRegister(View v)
    {
        String drnum=txtcardnum.getText().toString();
        String drvalid=txtcardvalid.getText().toString();
        String drpin=txtcardpin.getText().toString();

        if(drnum.length()==0)
            msgDialogSetup.setupDialog("Please enter card number",Optional.empty());
        else if(drnum.length()<19)
            msgDialogSetup.setupDialog("Please enter valid card number",Optional.empty());
        else if(drvalid.length()==0)
            msgDialogSetup.setupDialog("Please enter card validity",Optional.empty());
        else if(drpin.length()==0)
            msgDialogSetup.setupDialog("Please enter card pin",Optional.empty());
        else if(binding.txtsecans.length()==0)
            msgDialogSetup.setupDialog("Please answer the security qst",Optional.empty());
        else if(!binding.txtsecans.getText().toString().equals(ans[binding.spinnerQst.getSelectedItemPosition()])) {
            msgDialogSetup.setupDialog("Wrong answer for security question", Optional.empty());
            msgDialogSetup.layoutDisc.setVisibility(View.GONE);
        }
        else {
            ProgressDialog pbar=ProgressDialog.show(this,"","\nPlease Wait...");
            dbase = FirebaseDatabase.getInstance().getReference().child("ac_details");
            dbase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Boolean found = false;
                    for (DataSnapshot dshot : snapshot.getChildren()) {
                        if (dshot.child("drno").getValue().toString().equals(drnum)) {
                            if (dshot.child("drvalid").getValue().toString().equals(drvalid) &&
                                    dshot.child("drpin").getValue().toString().equals(drpin)) {
                                checkLogin(dshot.child("phno").getValue().toString(), dshot.child("name").getValue().toString(), dshot.getKey().toString());
                            } else {
                                msgDialogSetup.setupDialog("Wrong Credential", Optional.empty());
                                msgDialogSetup.layoutDisc.setVisibility(View.GONE);
                            }
                            found = true;

                        }
                    }
                    if (!found)
                        msgDialogSetup.setupDialog("Debit card not present", Optional.empty());
                    pbar.hide();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void checkLogin(String ph,String name,String ac){
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("RegisterInfo");
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
                    Intent i=new Intent(RegistrationCard.this, TpinMpin.class);
                    i.putExtra("phone_no",ph);
                    i.putExtra("ac_no",ac);
                    i.putExtra("name",name);
                    startActivity(i);
                    finish();
                }
                else {
                    msgDialogSetup.setupDialog("Please log out from other device", Optional.empty());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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