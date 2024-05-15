package com.example.gap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

public class Payment_QR extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_qr);
        replaceFragment(new Scan_QR(Payment_QR.this));
    }

    public void onClickScanQr(View v)
    {
//        if(v.getId()==R.id.radioloan)
            replaceFragment(new Scan_QR(Payment_QR.this));
//        else
//            replaceFragment(new fragment_loan("RD","FD"));
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fmanager=getSupportFragmentManager();
        FragmentTransaction ftransaction=fmanager.beginTransaction();
        ftransaction.add(R.id.qr,fragment);
        ftransaction.commit();
    }
}