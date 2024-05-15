package com.example.gap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.gap.databinding.ActivityMyQrBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class My_QR extends AppCompatActivity {

    SharedPreferences u_details;
    ActivityMyQrBinding binder;
    String acno,ph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder=ActivityMyQrBinding.inflate(getLayoutInflater());
        setContentView(binder.getRoot());

        u_details=getSharedPreferences("user_details",MODE_PRIVATE);
        acno=u_details.getString("ac_no","");
        ph=u_details.getString("phno","");

        MultiFormatWriter m=new MultiFormatWriter();

        try {
            BitMatrix bitMatrix=m.encode(acno+" "+ph, BarcodeFormat.QR_CODE,300,300);
            BarcodeEncoder bcodeen=new BarcodeEncoder();
            Bitmap bmap=bcodeen.createBitmap(bitMatrix);
            binder.qriview.setImageBitmap(bmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }


    }
}