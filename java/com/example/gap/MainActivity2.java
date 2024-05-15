package com.example.gap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
AnyChartView any;
String m[]={"Jan","feb","mar"};
int earn[]={500,600,600};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie);
        //any=findViewById(R.id.pie);
        setUpPieChart();
    }
    public void setUpPieChart(){
        //any=findViewById(R.id.pie);
        Pie p= AnyChart.pie();
        List<DataEntry> de=new ArrayList<>();
        for(int i=0;i<3;i++)
        {
            de.add(new ValueDataEntry(m[i],earn[i]));
        }
        p.data(de);
        //any.setChart(p);
    }
}