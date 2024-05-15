package com.example.gap;

import static com.example.gap.misc.Decoration.setComma;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.tv.TvContract;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.gap.databinding.ActivityHomeScreenBinding;
import com.example.gap.misc.MsgDialogSetup;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.Intents;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

public class Home_Screen extends AppCompatActivity {
    int TAB1=1,TAB2=2,TAB3=3;
    ActivityHomeScreenBinding binder;
    SharedPreferences u_details;
    DatabaseReference firebaseDatabase;
    MsgDialogSetup msgDialogSetup;
    ActivityResultLauncher<String> launcher;

    String ph,name,ac,adrs,gender,email,borrow="loading...",saving="loading...",image;
    Intent i;
    ProgressDialog progressBar;
    boolean isfront=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder=ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binder.getRoot());

        msgDialogSetup=new MsgDialogSetup(this);
//        String ans="10,10,100".replaceAll("[^0-9]","");
//        Toast.makeText(this, ""+ans, Toast.LENGTH_SHORT).show();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        int time=calendar.get(Calendar.HOUR_OF_DAY);


        u_details=getSharedPreferences("user_details",MODE_PRIVATE);
        ph=u_details.getString("phno","");
        ac=u_details.getString("ac_no","");
        name=u_details.getString("name","");

        setImage();

        binder.fundtransfer.setOnClickListener(r->{
            startActivity(new Intent(this,FundTransferBeneficiary.class));
        });
        binder.epass.setOnClickListener(r->{
            startActivity(new Intent(this,E_Pass.class));
        });
        binder.btnsaveVmore.setOnClickListener(r->{startActivity(new Intent(this,SavingsVMore.class));});
        binder.btnborrowVmore.setOnClickListener(r->{startActivity(new Intent(this,BorrowingVMore.class));});


        binder.myQr.setOnClickListener(v->{
            startActivity(new Intent(this,My_QR.class));
        });


        launcher=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                binder.btmNav.show(TAB1,false);
                if(result!=null)
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),result);
                        String qrCodeData=decodeQRCode(bitmap);
                        if(qrCodeData!=null){
                            startNewActivity(qrCodeData);
                        }
                        else{
                            Toast.makeText(Home_Screen.this, "Please select Correct QR Code", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(Home_Screen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            }
        });
        firebaseDatabase= FirebaseDatabase.getInstance().getReference("ac_details").child(ac);
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                adrs = Objects.requireNonNull(snapshot.child("adrs").getValue()).toString();
                gender = Objects.requireNonNull(snapshot.child("gender").getValue()).toString();
                email = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                borrow=Objects.requireNonNull(snapshot.child("borrowing").getValue()).toString();
                saving=Objects.requireNonNull(snapshot.child("savings").getValue()).toString();

                if(borrow.equals("0")||borrow.equals(""))
                    binder.btnborrowVmore.setVisibility(View.INVISIBLE);
                else
                    binder.btnborrowVmore.setVisibility(View.VISIBLE);

                i=new Intent(Home_Screen.this,UserProfile.class);
                i.putExtra("name",name);
                i.putExtra("adrs",adrs);
                i.putExtra("gender",gender);
                i.putExtra("email",email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binder.txtname.setText(name);

        if(time<12){
            binder.txtgreet.setText("Good Morning");
        binder.ripple.setAnimation(R.raw.ripple2);}
        else if(time<16){
            binder.txtgreet.setText("Good Afternoon");
            binder.ripple.setAnimation(R.raw.ripple_after);}
        else{
            binder.txtgreet.setText("Good Evening");
            binder.ripple.setAnimation(R.raw.ripple3);
        }

        binder.btmNav.add(new MeowBottomNavigation.Model(TAB1, R.drawable.ic_home_gif));
        binder.btmNav.add(new MeowBottomNavigation.Model(TAB2, R.drawable.ic_scan));
        binder.btmNav.add(new MeowBottomNavigation.Model(TAB3, R.drawable.ic_card));

        binder.btmNav.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch(item.getId()){
                    case 1:
                        //Toast.makeText(Home_Screen.this, "Home fragment", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        IntentIntegrator intentIntegrator = new IntentIntegrator(Home_Screen.this);
                        intentIntegrator.setOrientationLocked(false);
                        intentIntegrator.setCameraId(0);
                        intentIntegrator.setPrompt("Scan QR Code");
                        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);

                        Dialog dialog=new Dialog(Home_Screen.this);
                        dialog.setContentView(R.layout.qrcode_selection_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                        dialog.findViewById(R.id.btndscan).setOnClickListener(r->{
                            dialog.dismiss();
                            intentIntegrator.initiateScan();
                        });

                        dialog.findViewById(R.id.btngscan).setOnClickListener(r->{
                            dialog.dismiss();
                            launcher.launch("image/*");
                        });
                        break;
                    case 3:
                        showCard();
                        break;
                }
            }
        });

        binder.btmNav.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

            }
        });

        binder.btmNav.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                System.out.println("id"+item.getId());
            }
        });

        binder.btmNav.show(TAB1,false);

        binder.userprofileimg.setOnClickListener(v->{
            //Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
            startActivity(i);
        });

//        binder.iviewCard.setOnClickListener(v->{
//            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
//            startActivity(i);
//        });
    }

    void showCard(){
        AnimatorSet back_anim,front_anim;

        CardView txtforeground,txtbackground;
        Switch switchFlip,switchFlip2;
        ImageView img_sign;
        TextView txtcardno,txtvalid,txtholder,txtccv;

        Dialog d=new Dialog(this,R.style.cardDialogStyle);
        d.setContentView(R.layout.activity_card_details);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        d.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                binder.btmNav.show(TAB1,false);
            }
        });

        txtforeground=d.findViewById(R.id.txtforeground);
        txtcardno=d.findViewById(R.id.txtcardnum);
        txtvalid=d.findViewById(R.id.txtvalid);
        txtholder=d.findViewById(R.id.txtholdername);
        txtccv=d.findViewById(R.id.txtccv);
        img_sign=d.findViewById(R.id.holdersign);
        txtbackground=d.findViewById(R.id.txtbackground);
        switchFlip=d.findViewById(R.id.switch_flip);
        switchFlip2=d.findViewById(R.id.switch_flip2);
        switchFlip2.setChecked(true);

        txtcardno.setText(u_details.getString("drno",""));
        txtvalid.setText(u_details.getString("drvalid",""));
        txtholder.setText(name);
        txtccv.setText(u_details.getString("ccv",""));
        Picasso.get().load(u_details.getString("sign","")).into(img_sign);
        float scale=getApplicationContext().getResources().getDisplayMetrics().density;
        txtforeground.setCameraDistance(8000*scale);
        txtbackground.setCameraDistance(8000*scale);

        front_anim= (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),R.animator.animator_foreground);
        back_anim=(AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),R.animator.animator_background);

        switchFlip.setOnCheckedChangeListener((buttonView,isChecked)-> {
            if(isChecked) {
                switchFlip2.setChecked(true);
                flip_card(front_anim, back_anim, txtforeground, txtbackground);
            }
        });

        switchFlip2.setOnCheckedChangeListener((view,ischecked)->{
            if(!ischecked) {
                flip_card(front_anim, back_anim, txtforeground, txtbackground);
                switchFlip.setChecked(false);
            }
        });
        d.show();

    }
    void flip_card(AnimatorSet front_anim, AnimatorSet back_anim,CardView txtforeground,CardView txtbackground){
        if(isfront){
            front_anim.setTarget(txtforeground);
            back_anim.setTarget(txtbackground);
            front_anim.start();
            back_anim.start();
            isfront= false;
        }
        else{
            front_anim.setTarget(txtbackground);
            back_anim.setTarget(txtforeground);
            back_anim.start();
            front_anim.start();
            isfront=true;
        }
    }

    private String decodeQRCode(Bitmap bitmap) {
        try{
            int[] pixels=new int[bitmap.getWidth()*bitmap.getHeight()];
            bitmap.getPixels(pixels,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
            RGBLuminanceSource source=new RGBLuminanceSource(bitmap.getWidth(),bitmap.getHeight(),pixels);
            BinaryBitmap binaryBitmap=new BinaryBitmap(new HybridBinarizer(source));
            Result result=new MultiFormatReader().decode(binaryBitmap);
            return result.getText();
        } catch (NotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "QR"+e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    protected void onRestart() {
        setImage();
        super.onRestart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult iresult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(iresult!=null)
        {
            String result=iresult.getContents();
            if(result!=null) {
                startNewActivity(result);
                //Toast.makeText(this, "Ac " + res[0] + "\nPh " + res[1], Toast.LENGTH_SHORT).show();
            }
                binder.btmNav.show(TAB1,false);
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    public void onClickShowBalance(View v){
        ToggleButton tb=(ToggleButton) v;
        if(tb.getId()==R.id.savings_eye){
            if(tb.isChecked()){
                binder.txtsavings.setText(getResources().getString(R.string.ruppes)+" "+setComma(saving)); //static method in custom class decorator in misc pckg
                v.setBackground(getResources().getDrawable(R.drawable.close_eye));
            }
            else{
                binder.txtsavings.setText("XXXXXX");
                v.setBackground(getResources().getDrawable(R.drawable.eye));
            }
        }
        else
        {
            if(tb.isChecked()){
                binder.txtborrow.setText(getResources().getString(R.string.ruppes)+setComma(borrow));
                v.setBackground(getResources().getDrawable(R.drawable.close_eye));
            }
            else{
                binder.txtborrow.setText("XXXXXX");
                v.setBackground(getResources().getDrawable(R.drawable.eye));
            }
        }
    }

    void setImage(){
        binder.userprofileimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        image=u_details.getString("image","");
        if(image.equals(""))
            binder.userprofileimg.setImageResource(R.drawable.user);
        else
            Picasso.get().load(image).into(binder.userprofileimg);
    }

    @Override
    public void onBackPressed() {
        msgDialogSetup.txtsave.setText("Yes");
        msgDialogSetup.txtdiscard.setText("No");
        msgDialogSetup.layoutDisc.setVisibility(View.VISIBLE);
        msgDialogSetup.setupDialog("Do u want to log out", Optional.empty());

        msgDialogSetup.layoutDisc.setOnClickListener(v->{
            msgDialogSetup.msg_dialog.hide();
        });

        msgDialogSetup.layoutSave.setOnClickListener(v->{
            super.onBackPressed();
        });
    }

    void startNewActivity(String result){
        String[] res = result.split(" ");
        Intent i=new Intent(this,Fund_Transfer.class);
        i.putExtra("acno",res[0]);
        i.putExtra("phno",res[1]);
        startActivity(i);
    }
}