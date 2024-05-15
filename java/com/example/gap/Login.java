package com.example.gap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.IntentCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gap.misc.AllConstants;
import com.example.gap.misc.Biometric;
import com.example.gap.misc.MsgDialogSetup;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

public class Login extends AppCompatActivity {
    BottomSheetDialog bs;
    TextView txtname,txtlog;

    private FusedLocationProviderClient fusedLocationClient;
    double lat,lng;

    FirebaseUser user=null;

    int cur_time,cur_min,hr,min;

    Calendar calendar=Calendar.getInstance();

    Dialog dialog;

    BiometricPrompt bioprompt;
    BiometricPrompt.PromptInfo promptInfo;

    ImageView fingerPrint;

    private VideoView videoView;
    private String videoUrl,phno,email,acno,ph,map_url;

    DatabaseReference dref;

    Vibrator v;
    MsgDialogSetup msgDialogSetup;

    int attempt;
    SharedPreferences u_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logincreate);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        calendar.setTime(new Date());

        u_details=getSharedPreferences("user_details",MODE_PRIVATE);
        acno=u_details.getString("ac_no","");
        attempt=u_details.getInt("attempt",0);
        hr=u_details.getInt("hr",0);
        min=u_details.getInt("min",0);
        ph=u_details.getString("phno","");

        txtname=findViewById(R.id.txtname);
        fingerPrint=findViewById(R.id.gifImageView4);
        txtlog=findViewById(R.id.txtlogpin);

        SharedPreferences uref=getSharedPreferences("user_details",MODE_PRIVATE);
        txtname.setText(uref.getString("name",""));
        phno=uref.getString("phno","");


        v= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        msgDialogSetup=new MsgDialogSetup(this);

        dref= FirebaseDatabase.getInstance().getReference("PinInfo");

        boolean blocked=getIntent().getBooleanExtra("blocked",false);
        if(!blocked)
        {
            attemptChecker();

            FirebaseDatabase.getInstance().getReference("ac_details").child(acno).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    email = snapshot.child("email").getValue().toString();
                    u_details.edit()
                            .putString("sign", snapshot.child("sign").getValue().toString())
                            .putString("drno", snapshot.child("drno").getValue().toString())
                            .putString("drvalid", snapshot.child("drvalid").getValue().toString())
                            .putString("ccv", snapshot.child("ccv").getValue().toString())
                            .commit();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            txtlog.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (txtlog.getText().length() == 4) {
                        ProgressDialog dialog = new ProgressDialog(new ContextThemeWrapper(Login.this, R.style.CustomFontDialog));
                        dialog.setMessage("Logging in..");
                        dialog.show();
                        dref.child(phno).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        if (task.getResult().child("LPin").getValue().equals(txtlog.getText().toString())) {
                                            Intent i = new Intent(Login.this, Home_Screen.class);
                                            u_details.edit().putInt("attempt", 0).commit();
                                            dialog.dismiss();
                                            startActivity(i);
                                            finish();
                                        } else {
                                            txtlog.startAnimation(AnimationUtils.loadAnimation(Login.this, R.anim.shake_animation));
                                            txtlog.setText("");
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                            } else {
                                                v.vibrate(500);
                                            }
                                            dialog.dismiss();
                                            attempt++;
                                            //msgDialogSetup.setupDialog("Wrong Pin\nYou have only "+(3-attempt)+" attempts", Optional.of(R.drawable.info_icn));
                                            u_details.edit().putInt("attempt", attempt).commit();

                                            attemptChecker();
                                            updateAttemptTime();
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }
        else
        {
            msgDialogSetup.setupDialog("Your account is temporarily blocked\nPlease visit the bank to unblock the account",Optional.empty());
            msgDialogSetup.msg_dialog.setCancelable(false);
            msgDialogSetup.layoutSave.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==AllConstants.EMAIL_VERIFY){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,"1234567890")
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                Dialog dialog=new Dialog(Login.this);
                                dialog.setContentView(R.layout.dialog_new_lpin);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.setCancelable(false);

                                dialog.findViewById(R.id.lpin_close).setOnClickListener(r->{
                                    dialog.dismiss();
                                });

                                Button btnreset=dialog.findViewById(R.id.btnreset);
                                TextView txtnlpin,txtclpin;
                                txtnlpin=dialog.findViewById(R.id.txtnlpin);
                                txtclpin=dialog.findViewById(R.id.txtclpin);

                                txtclpin.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        if(s.length()>=4){
                                            if(s.toString().equals(txtnlpin.getText().toString()))
                                                btnreset.setVisibility(View.VISIBLE);
                                            else
                                            {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                    txtclpin.startAnimation(AnimationUtils.loadAnimation(Login.this, R.anim.shake_animation));
                                                    s.clear();
                                                } else {
                                                    v.vibrate(500);
                                                }
                                            }
                                        }
                                    }
                                });

                                btnreset.setOnClickListener(r->{
                                    AlertDialog.Builder alert=new AlertDialog.Builder(Login.this,R.style.CustomFontDialog);
                                    FirebaseDatabase.getInstance().getReference("PinInfo").child(ph).child("LPin")
                                            .setValue(txtclpin.getText().toString())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    dialog.dismiss();
                                                    alert.setMessage("Login pin updated successfully");
                                                    alert.setPositiveButton("Ok",(k,l)->{
                                                        k.dismiss();
                                                    });
                                                    alert.show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    alert.setMessage(e.getMessage());
                                                    alert.setPositiveButton("Dismiss",(k,l)->{
                                                        k.dismiss();
                                                    });
                                                    alert.show();
                                                }
                                            });
                                });
                                FirebaseAuth.getInstance().getCurrentUser().delete();
                                dialog.show();
                            }
                            else
                                Toast.makeText(Login.this, "Not verified", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Login.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClickCalculator(View v)
    {
        startActivity(new Intent(this,Calculator.class));
    }

    public void onClikcFingerPrint(View v) {
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this, "hardware not found", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(this, "hardware not enrolled", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(this, "no hardware", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_SUCCESS:
                Executor executor = ContextCompat.getMainExecutor(this);
                bioprompt = new BiometricPrompt(Login.this, executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        startActivity(new Intent(Login.this, Home_Screen.class));
                        finish();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        attempt++;
                        u_details.edit().putInt("attempt",attempt).commit();

                        attemptChecker();
                        updateAttemptTime();
                        super.onAuthenticationFailed();
                    }
                });
                promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Login using Biometric")
                        .setDeviceCredentialAllowed(true)
                        .build();
                bioprompt.authenticate(promptInfo);
        }
    }

    public void onClikcHelp(View v)
    {
        bs=new BottomSheetDialog(this);
        View z=getLayoutInflater().inflate(R.layout.activity_help_support,null,false);
        bs.setContentView(z);
        bs.show();
        bs.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        bs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bs.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        bs.getWindow().setGravity(Gravity.BOTTOM);
        bs.setCanceledOnTouchOutside(false);
        bs.findViewById(R.id.btnchatbot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,ChatBot.class));
            }
        });
        bs.findViewById(R.id.btnvideoguid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Video_Player.class));
            }
        });
    }

    public void onClickFindAtm(View v){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point(); display. getSize(size);
        int width = size. x;
        int height = size.y;
        dialog=new Dialog(this,R.style.PauseDialog);
        dialog.setContentView(R.layout.dialog_atm_selector);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        RadioGroup rgrp=dialog.findViewById(R.id.radatmtype);
        rgrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            LottieAnimationView lview;
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                dialog.dismiss();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.getWindow().setLayout(width,height);
                dialog.setContentView(R.layout.dialog_loading_atm);
                lview=dialog.findViewById(R.id.atm_lottie);
                if(checkedId==R.id.radnearby)
                {
                    lview.setAnimation(R.raw.allatm);
                    map_url="https://www.google.com/maps/search/atm/@"+lat+","+lng+",11z/data=!3m1!4b1?entry=ttu";

                }
                else {
                    lview.setAnimation(R.raw.nearestatm);
                    map_url="google.navigation:q="+lat+"+"+lng+",atm&avoid=tf";
                }
                dialog.show();
                getCurrentLocation();
            }
        });
    }

    private void getCurrentLocation(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},AllConstants.REQUEST_LOCATION);
        }
        else {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setNumUpdates(1);
            locationRequest.setInterval(0);

            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    fusedLocationClient.removeLocationUpdates(this);
                    lat=location.getLatitude();
                    lng=location.getLongitude();
                    //String url="https://www.google.com/maps/search/atm/@"+lat+","+lng+",11z/data=!3m1!4b1?entry=ttu";
                    Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse(map_url));
                    //i.setData(Uri.parse(url));
                    startActivity(i);
                    dialog.dismiss();
//                    Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat+"+"+lng+",atm&avoid=tf");
//                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                    mapIntent.setPackage("com.google.android.apps.maps");
//                    startActivity(mapIntent);
                }
            }, Looper.myLooper());

        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case AllConstants.REQUEST_LOCATION:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    getCurrentLocation();
                }
                else
                {
                    dialog.dismiss();
                }
        }
    }

    void attemptChecker(){
        if(attempt<3){
            txtlog.setVisibility(View.VISIBLE);
            fingerPrint.setVisibility(View.VISIBLE);
            if(attempt!=0)
                msgDialogSetup.setupDialog("Wrong Pin\nYou have only "+(3-attempt)+" attempts", Optional.of(R.drawable.info_icn));
        }
        else
        {
            calendar.setTime(new Date());
            cur_time=calendar.get(Calendar.HOUR_OF_DAY);
            cur_min=calendar.get(Calendar.MINUTE);

            hr=u_details.getInt("hr",0);
            min=u_details.getInt("min",0);

            if((cur_time==hr&&cur_min-min==0)||cur_time>=hr)
            {
                txtlog.setVisibility(View.VISIBLE);
                fingerPrint.setVisibility(View.VISIBLE);
                u_details
                        .edit()
                        .putInt("hr",0)
                        .putInt("min",0)
                        .putInt("attempt",0)
                        .commit();
            }
            else {
                fingerPrint.setVisibility(View.INVISIBLE);
                txtlog.setVisibility(View.INVISIBLE);
                msgDialogSetup.setupDialog("Out of attempts\nWait for "+Math.abs(cur_time-hr)+" hours", Optional.empty());
            }
        }
    }

    void updateAttemptTime()
    {
        if(attempt>=3){
            calendar.setTime(new Date());
            int time=calendar.get(Calendar.HOUR_OF_DAY)+3;
            if(time>=24)
                time-=24;
            int min=calendar.get(Calendar.MINUTE);

            u_details
                    .edit()
                    .putInt("hr",time)
                    .putInt("min",min)
                    .commit();
        }
    }

    public void onClickforgetPin(View v){
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.dialog_gmail_password);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtemail;
        ProgressBar pbar=dialog.findViewById(R.id.progress);
        Button btn=dialog.findViewById(R.id.btnopenmail);
        txtemail=dialog.findViewById(R.id.txtemail);

        txtemail.setText(email);

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(txtemail.getText().toString(), "1234567890")
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                          @Override
                                          public void onSuccess(AuthResult authResult) {
                                              authResult.getUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                  @Override
                                                  public void onSuccess(Void unused) {
                                                      user = authResult.getUser();
                                                      pbar.setVisibility(View.GONE);
                                                      btn.setVisibility(View.VISIBLE);
                                                  }
                                              }).addOnFailureListener(new OnFailureListener() {
                                                  @Override
                                                  public void onFailure(@NonNull Exception e) {
                                                      Toast.makeText(Login.this, "Failed", Toast.LENGTH_SHORT).show();
                                                  }
                                              });
                                          }
                                      }
                )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        btn.setOnClickListener(r->{
            dialog.dismiss();
            String url="https://mail.google.com/mail/";
            Intent i=new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivityForResult(i, AllConstants.EMAIL_VERIFY);
        });

        dialog.show();
    }
}