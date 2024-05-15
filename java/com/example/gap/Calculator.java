package com.example.gap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

public class Calculator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        replaceFragment(new fragment_loan("Personal","Housing","Loan Tenure",this));
    }

    public void onClick(View v)
    {
        if(v.getId()==R.id.radioloan)
            replaceFragment(new fragment_loan("Personal","Housing","Loan Tenure",this));
        else
            replaceFragment(new fragment_loan("RD","FD","Investment Tenure",this));
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fmanager=getSupportFragmentManager();
        FragmentTransaction ftransaction=fmanager.beginTransaction();
        ftransaction.replace(R.id.calculation_detail,fragment);
        ftransaction.commit();
    }
}