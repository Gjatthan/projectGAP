package com.example.gap;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gap.databinding.ActivityUserProfileBinding;
import com.example.gap.misc.MsgDialogSetup;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.Optional;

public class UserProfile extends AppCompatActivity {

    ActivityUserProfileBinding binding;
    String ph,name,ac,adrs,gender,email;
    SharedPreferences u_details;
    DatabaseReference firebaseDatabase;
    ActivityResultLauncher<String> launcher;
    FirebaseStorage storage;
    MsgDialogSetup msgDialogSetup;
    Uri uri;
    ProgressDialog dialog;
    boolean save_from_back=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        msgDialogSetup=new MsgDialogSetup(this);
        u_details=getSharedPreferences("user_details",MODE_PRIVATE);
        ph=u_details.getString("phno","");
        ac=u_details.getString("ac_no","");

        dialog = new ProgressDialog(new ContextThemeWrapper(this, R.style.CustomFontDialog));

        binding.imageViewUProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
        FirebaseDatabase.getInstance().getReference("RegisterInfo").child(ph).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("ImageUrl")){
                    String imgurl=snapshot.child("ImageUrl").getValue(String.class);
                    Picasso.get().load(imgurl).into(binding.imageViewUProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        storage=FirebaseStorage.getInstance();
        launcher=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                binding.imageViewUProfile.setImageURI(result);
                uri=result;
                if(uri!=null)
                    binding.btnsave.setVisibility(View.VISIBLE);
            }
        });

        binding.btnback.setOnClickListener(v->{
            finish();
        });


        name=getIntent().getExtras().get("name").toString();
        adrs=getIntent().getExtras().get("adrs").toString();
        gender=getIntent().getExtras().get("gender").toString();
        email=getIntent().getExtras().get("email").toString();

        binding.txtadrs.setText(adrs);
        binding.txtname.setText(name);
        binding.txtgender.setText(gender);
        binding.txtemail.setText(email);
        binding.txtphone.setText(ph);
    }

    public void onClickSave(View v){
        dialog.setMessage("Updating profile pic..");
        dialog.show();
        StorageReference storageReference=storage.getReference().child(name+"-"+ph).child(ph);
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        firebaseDatabase=FirebaseDatabase.getInstance().getReference("RegisterInfo").child(ph);
                        firebaseDatabase.child("ImageUrl").setValue(uri.toString());
                        dialog.hide();
                        v.setVisibility(View.GONE);
                        u_details.edit().putString("image",uri.toString()).commit();
                        if(save_from_back){
                            save_from_back=false;
                            finish();
                        }
                    }
                });
            }
        });
    }

    public void onClickEditProfile(View v){
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this);
//        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.change_profile_bottom_nav);
        bottomSheetDialog.show();
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        bottomSheetDialog.findViewById(R.id.btnChangePic).setOnClickListener(m->{
            launcher.launch("image/*");
            bottomSheetDialog.hide();
        });

        bottomSheetDialog.findViewById(R.id.btnRemovePic).setOnClickListener(m->{
            u_details.edit().remove("image").commit();
            binding.imageViewUProfile.setImageResource(R.drawable.user);
            FirebaseDatabase.getInstance().getReference("RegisterInfo").child(ph).child("ImageUrl").removeValue();
            bottomSheetDialog.hide();
        });
    }


    public void cardZoom(View v){
        CardView c=(CardView)v;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                c.animate().scaleX(1).scaleY(1).setDuration(135);
//                c.setBackgroundColor(getResources().getColor(R.color.white));
//                c.setRadius(10);
            }
        },2000);
        v.animate().scaleX(1.05f).scaleY(1.05f).setDuration(135);
        //v.setBackgroundColor(getResources().getColor(R.color.transparent1));
    }

    @Override
    public void onBackPressed() {
        if(binding.btnsave.getVisibility()==View.VISIBLE){
            msgDialogSetup.layoutDisc.setVisibility(View.VISIBLE);
            msgDialogSetup.txtsave.setText("Save");

            msgDialogSetup.layoutSave.setOnClickListener(v->{
                save_from_back=true;
                msgDialogSetup.msg_dialog.hide();
                onClickSave(binding.btnsave);
            });
            msgDialogSetup.layoutDisc.setOnClickListener(v->{
                super.onBackPressed();
            });

            msgDialogSetup.setupDialog("Profile pic is not updated...\nDo you want to update", Optional.empty());
        }
        else{
            super.onBackPressed();
        }
    }

    public void onClickDeregister(View v)
    {
        msgDialogSetup.layoutDisc.setVisibility(View.VISIBLE);
        msgDialogSetup.txtdiscard.setText("No");
        msgDialogSetup.txtsave.setText("Yes");
        msgDialogSetup.setupDialog("Do you want to De-Register from the app?\nThis will lead to deletion of all the data associated with the app",Optional.of(R.drawable.info_icn));

        msgDialogSetup.layoutDisc.setOnClickListener(m->{
            msgDialogSetup.msg_dialog.hide();
        });

        msgDialogSetup.layoutSave.setOnClickListener(m->{
            dialog=ProgressDialog.show(this,"","De-Registering ...\nPlease wait");
            FirebaseDatabase.getInstance().getReference("RegisterInfo").child(ph).removeValue();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.hide();
                    u_details.edit()
//                            .putBoolean("isregistered",false)
//                            .putString("image","")
                            .clear()
                            .commit();
                    startActivity(new Intent(UserProfile.this,Registration.class));
                    finishAffinity();   //finishes all parent acctivity
                }
            },5000);
        });
    }

    public void onClickProfile(View v){
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.zoom_user_profile);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView imageView=dialog.findViewById(R.id.userProfile);
        String img_url=u_details.getString("image","");
        if(img_url!="")
            Picasso.get().load(img_url).into(imageView);
        else
            imageView.setImageResource(R.drawable.user);
        dialog.show();
    }
}