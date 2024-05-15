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

import com.example.gap.databinding.ActivityBorrowingVmoreBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class BorrowingVMore extends AppCompatActivity {

    ActivityBorrowingVmoreBinding binding;
    SharedPreferences u_details;
    String name,lac_no,loan_type,lodate,nemidate,borrow,ldesc,roi,emi_Amt,tenure,ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityBorrowingVmoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        u_details=getSharedPreferences("user_details",MODE_PRIVATE);
        ac=u_details.getString("ac_no","");

        ProgressDialog dialog = new ProgressDialog(new ContextThemeWrapper(this, R.style.CustomFontDialog));
        dialog.setMessage("Please wait..");
        dialog.show();

        FirebaseDatabase.getInstance().getReference("Loan").child(ac).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name= Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                loan_type= Objects.requireNonNull(snapshot.child("type").getValue()).toString();
                lac_no=Objects.requireNonNull(snapshot.child("AcNo").getValue()).toString();
                lodate= Objects.requireNonNull(snapshot.child("OpenDate").getValue()).toString();
                nemidate= Objects.requireNonNull(snapshot.child("NED").getValue()).toString();
                borrow= Objects.requireNonNull(snapshot.child("Borrow").getValue()).toString();
                ldesc= Objects.requireNonNull(snapshot.child("description").getValue()).toString();
                roi= Objects.requireNonNull(snapshot.child("ROI").getValue()).toString();
                emi_Amt= Objects.requireNonNull(snapshot.child("emi_amt").getValue()).toString();
                tenure=Objects.requireNonNull(snapshot.child("Tenure").getValue()).toString();

                binding.txtcustname.setText(name);
                binding.txtemi.setText("₹ "+setComma(emi_Amt));
                binding.txtlacno.setText(lac_no);
                binding.txtloandesc.setText(ldesc);
                binding.txtnemid.setText(nemidate);
                binding.txtroi.setText(roi);
                binding.txtsanction.setText("₹ "+setComma(borrow));
                binding.txtloantype.setText(loan_type);
                binding.txtodate.setText(lodate);
                binding.txttenure.setText(tenure);
                binding.txtactype.setText("Loan");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.btnback.setOnClickListener(v->{finish();});

        new Handler().postDelayed(()->{
            dialog.hide();
            binding.layout.setVisibility(View.VISIBLE);
            }, 5000);
    }
}