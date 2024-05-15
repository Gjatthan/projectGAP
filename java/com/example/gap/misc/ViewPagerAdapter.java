package com.example.gap.misc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.gap.R;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    Context mcont;
    ArrayList<IntroModalClass> arrayList;

    public ViewPagerAdapter(Context mcont, ArrayList<IntroModalClass> arrayList) {
        this.mcont = mcont;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcont).inflate(R.layout.pager_screen,parent,false);
        ViewHolder vh=new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewHolder holder, int position) {
        holder.gifImageView.setImageResource(arrayList.get(position).img_id);
        holder.txtttl.setText(arrayList.get(position).title);
        holder.txtdesc.setText(arrayList.get(position).desc);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        GifImageView gifImageView;
        TextView txtttl,txtdesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gifImageView=itemView.findViewById(R.id.intro_img_view);
            txtttl=itemView.findViewById(R.id.intro_txt_title);
            txtdesc=itemView.findViewById(R.id.intro_txt_desc);
        }
    }
}
