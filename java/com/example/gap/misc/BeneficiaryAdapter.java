package com.example.gap.misc;

import static com.example.gap.misc.Decoration.setComma;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gap.FundTransferBeneficiary;
import com.example.gap.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Optional;

public class BeneficiaryAdapter extends RecyclerView.Adapter<BeneficiaryAdapter.ViewHolderClass>{

    ArrayList<BeneficiaryModel> arrayList;
    Context mcontext;
    MsgDialogSetup dialog;
    String acno;
    SelectInterface selectInterface;
    public BeneficiaryAdapter(ArrayList<BeneficiaryModel> arrayList, Context mcontext, String acno,SelectInterface selectInterface) {
        this.arrayList = arrayList;
        this.mcontext = mcontext;
        dialog=new MsgDialogSetup(mcontext);
        this.selectInterface=selectInterface;
        //u_details=mcontext.getSharedPreferences("user_details",MODE_PRIVATE);
        //tpin=u_details.getString("tpin","");
        this.acno=acno;
    }

    @NonNull
    @Override
    public BeneficiaryAdapter.ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.beneficiary_ac_layout,parent,false);
        BeneficiaryAdapter.ViewHolderClass viewhold=new BeneficiaryAdapter.ViewHolderClass(view);
        return viewhold;
    }

    @Override
    public void onBindViewHolder(@NonNull BeneficiaryAdapter.ViewHolderClass holder, int position) {
        holder.txtname.setText(arrayList.get(position).name);
        holder.txtac.setText(arrayList.get(position).ac);

        if(arrayList.get(position).img_url!=null){
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.get().load(arrayList.get(position).img_url).into(holder.imageView);
        }
        holder.btndlt.setOnClickListener(r->{
            selectInterface.onItemClicked(AllConstants.DELETE,arrayList.get(position));
//            dialog.setupDialog("Do you want to remove "+arrayList.get(position).name+" from beneficiary list?", Optional.empty());
//            dialog.layoutDisc.setVisibility(View.VISIBLE);
//            dialog.txtdiscard.setText("No");
//            dialog.layoutDisc.setOnClickListener(v->{
//                dialog.msg_dialog.dismiss();
//            });
//
//            dialog.txtsave.setText("YES");
//            dialog.layoutSave.setOnClickListener(v->{
//                FirebaseDatabase.getInstance().getReference("Beneficiary").child(acno).child(arrayList.get(position).ac).setValue(null);
//                MsgDialogSetup d=new MsgDialogSetup(mcontext);
//                d.setupDialog("Removed Successfully",Optional.empty());
//                dialog.msg_dialog.dismiss();
//
//                FundTransferBeneficiary.ftb.startActivity(new Intent(mcontext,FundTransferBeneficiary.class));
//                FundTransferBeneficiary.ftb.finish();
//            });
        });
        holder.card.setOnClickListener(r->{
            selectInterface.onItemClicked(AllConstants.SEND,arrayList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{
        TextView txtname,txtac;
        View btndlt;
        LinearLayoutCompat card;
        ImageView imageView;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            txtname= itemView.findViewById(R.id.txtname);
            txtac=itemView.findViewById(R.id.txtac);
            btndlt=itemView.findViewById(R.id.btnremove);
            card=itemView.findViewById(R.id.cardviewBeneficiary);
            imageView=itemView.findViewById(R.id.userpic);
        }
    }
}