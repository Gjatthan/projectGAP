package com.example.gap.misc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gap.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatBotAdapter extends RecyclerView.Adapter<ChatBotAdapter.ViewHolderClass>{

    ArrayList<ChatBotModalClass> arrayList;
    Context mcontext;

    public ChatBotAdapter(ArrayList<ChatBotModalClass> arrayList, Context mcontext) {
        this.arrayList = arrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mcontext).inflate(R.layout.layout_send_recieve,parent,false);
        ViewHolderClass viewhold=new ViewHolderClass(view);
        return viewhold;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {
        if(arrayList.get(position).send.length()>0)
            holder.layout.setVisibility(View.VISIBLE);
        else
            holder.layout.setVisibility(View.GONE);
        holder.txtsend.setText(arrayList.get(position).send);
        holder.txtrev.setText(arrayList.get(position).recv);
        holder.ic_bot.setPadding(holder.ic_bot.getPaddingLeft(),60,holder.ic_bot.getPaddingRight(),holder.ic_bot.getPaddingBottom());
        if(arrayList.get(position).img.equals(""))
            holder.img.setImageResource(R.drawable.user);
        else
            Picasso.get().load(arrayList.get(position).img).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{
        TextView txtrev,txtsend;
        RelativeLayout layout;
        CardView ic_user,ic_bot;
        ImageView img;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            txtrev= itemView.findViewById(R.id.txtrecieve);
            txtsend=itemView.findViewById(R.id.txtsend);
            layout=itemView.findViewById(R.id.layout_send);
            ic_user=(CardView) itemView.findViewById(R.id.icnu);
            ic_bot=itemView.findViewById(R.id.icnb);
            img=itemView.findViewById(R.id.sendImg);
        }
    }
}
