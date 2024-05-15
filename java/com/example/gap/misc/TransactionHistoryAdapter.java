package com.example.gap.misc;

import static com.example.gap.misc.Decoration.setComma;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gap.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolderClass>{

    ArrayList<TransactionHistoryModel> arrayList;
    Context mcontext;

    public TransactionHistoryAdapter(ArrayList<TransactionHistoryModel> arrayList, Context mcontext) {
        this.arrayList = arrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public TransactionHistoryAdapter.ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.detailed_e_pass,parent,false);
        TransactionHistoryAdapter.ViewHolderClass viewhold=new TransactionHistoryAdapter.ViewHolderClass(view);
        return viewhold;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {
        holder.txtdesc.setText(arrayList.get(position).desc);
        holder.txtamt.setText("₹ "+setComma(arrayList.get(position).tamt));
        holder.txtdrcrmsg.setText(arrayList.get(position).drcrmsg);
        holder.txtac.setText(arrayList.get(position).acno);
        holder.txtdate.setText(arrayList.get(position).date);

        if(arrayList.get(position).drcrmsg.equals("Debited")) {
            holder.txtdrcr.setText("- ₹ "+setComma(arrayList.get(position).drcramt));
            holder.txtdrcr.setTextColor(Color.RED);
        }
        else {
            holder.txtdrcr.setText("+ ₹ "+setComma(arrayList.get(position).drcramt));
            holder.txtdrcr.setTextColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{
        TextView txtdesc,txtac,txtdrcrmsg,txtdrcr,txtamt,txtdate;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            txtdesc= itemView.findViewById(R.id.txtdescription);
            txtac=itemView.findViewById(R.id.txtacno);
            txtdrcrmsg=itemView.findViewById(R.id.msgdrcr);
            txtdrcr=itemView.findViewById(R.id.txtdrcr);
            txtamt=itemView.findViewById(R.id.txttotal);
            txtdate=itemView.findViewById(R.id.txtdate);
        }
    }
}

