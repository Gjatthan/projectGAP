package com.example.gap.misc;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.gap.R;

import java.util.ArrayList;
import java.util.List;

public class Drawer {
    //double amt1,amt2,amt3;
    double amt1,amt2;
    String msg[],caption,famt1,famt2,famt3;
    public Drawer(String famt1,String famt2,String famt3,double amt1,double amt2,String msg[],String caption){
        this.famt1=famt1;
        this.famt2=famt2;
        this.famt3=famt3;
        this.amt1=amt1;
        this.amt2=amt2;
        this.msg=msg;
        this.caption=caption;
    }
    public void showDialog(Context c) {
        AnyChartView any;
        TextView txtemi,txtint,txtamt,txtcaption;

        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_pie);

        any=dialog.findViewById(R.id.pie);
        txtemi=dialog.findViewById(R.id.txtemi);
        txtint=dialog.findViewById(R.id.txtiamt);
        txtamt=dialog.findViewById(R.id.txttotal);
        txtcaption=dialog.findViewById(R.id.txtcaption);

        txtcaption.setText(caption);
        txtemi.setText(famt1);
        txtint.setText(famt2);
        txtamt.setText(famt3);

        TextView txt;
        txt=dialog.findViewById(R.id.txtmsg1);
        txt.setText(msg[0]);
        txt=dialog.findViewById(R.id.txtmsg2);
        txt.setText(msg[1]);
        txt=dialog.findViewById(R.id.txtmsg3);
        txt.setText(msg[2]);
        setUpPieChart(any,amt1,amt2);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    public void setUpPieChart(AnyChartView any,double emi,double yearInt){
        String m[]={"EMI Amt","Total Interest"};
        double earn[]={Math.round(emi),Math.round(yearInt)};
        Pie p= AnyChart.pie();
        p.legend(false);

        List<DataEntry> de=new ArrayList<>();
        for(int i=0;i<2;i++)
        {
            de.add(new ValueDataEntry(msg[i],earn[i]));
        }
        p.data(de);
        any.setChart(p);
    }
}
