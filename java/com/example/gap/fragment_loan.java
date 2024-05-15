package com.example.gap;


import static com.example.gap.misc.Decoration.setComma;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.gap.misc.Decoration;
import com.example.gap.misc.Drawer;
import com.example.gap.misc.InputFilterMinMax;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class fragment_loan extends Fragment {
    Context context;
    View view;
    SeekBar skbar;
    TextInputEditText txtamt;
    Spinner spin;
    EditText txtintrst, txttenure;
    TextView txtemi,txtiamt,txttotal,txtvmore,txttenurecaption;
    LinearLayout layoutVMore;
    double amt,roi,rate,emi,yearInt;
    double ten;
    RadioButton radpersonal,radhome;
    long min=50000;
    String name1,name2,tenCaption;
    String msg1[]={"Invested amount","Est. return","Total amount"},
            msg2[]={"EMI Amount","Interest","Total Amoount\nPayable"};

    Drawer draw;


    public fragment_loan(String s1,String s2,String caption,Context context) {
        name1=s1;
        name2=s2;
        this.context=context;
        this.tenCaption=caption;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_loan, container, false);
        skbar = view.findViewById(R.id.amtseekbar);
        txtamt = view.findViewById(R.id.txtamt);
        spin = view.findViewById(R.id.spintenure);
        txtintrst = view.findViewById(R.id.txtinterest);
        txttenure = view.findViewById(R.id.txttenure);
        txtvmore=view.findViewById(R.id.viewmore);
        radpersonal=view.findViewById(R.id.radpersonal);
        radhome=view.findViewById(R.id.radhousing);
        radpersonal.setText(name1);
        radhome.setText(name2);
        layoutVMore=view.findViewById(R.id.layoutloandetail);
        txttenurecaption=view.findViewById(R.id.txttenurecaption);

        txttenurecaption.setText(tenCaption);

        txtamt.setFilters(new InputFilter[]{new InputFilterMinMax("1", "5000000")});

        String tenure[] = new String[]{"Months", "Years"};
        ArrayAdapter<String> adptr;
        adptr = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_layout, tenure);
        spin.setAdapter(adptr);

        radpersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radpersonal.getText().toString().equals("Personal")) {
//                    skbar.setMax(5000000);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        skbar.setMin(50000);
//                    }
//                    txtamt.setText("");
//                    txtamt.setFilters(new InputFilter[]{new InputFilterMinMax("1", "5000000")});
//                    view.findViewById(R.id.layoutloandetail).setVisibility(View.INVISIBLE);
//                    min = 50000;
                    setupTextBox(50000,5000000);
                }
                else
                {
//                    skbar.setMax(2000000000);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        skbar.setMin(25);
//                    }
//                    txtamt.setText("");
//                    txtamt.setFilters(new InputFilter[]{new InputFilterMinMax("1", "2000000000")});
//                    view.findViewById(R.id.layoutloandetail).setVisibility(View.INVISIBLE);
//                    min = 25;
                    setupTextBox(25,2000000000);
                }
            }
        });

        radhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radhome.getText().toString().equals("Housing")) {
//                    skbar.setMax(100000000);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        skbar.setMin(100000);
//                    }
//                    txtamt.setText("");
//                    txtamt.setFilters(new InputFilter[]{new InputFilterMinMax("1", "100000000")});
//                    view.findViewById(R.id.layoutloandetail).setVisibility(View.INVISIBLE);
//                    min = 100000;
                    setupTextBox(100000,100000000);
                }
                else
                {
//                    skbar.setMax(1000000000);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        skbar.setMin(5000);
//                    }
//                    txtamt.setText("");
//                    txtamt.setFilters(new InputFilter[]{new InputFilterMinMax("1", "100000000")});
//                    view.findViewById(R.id.layoutloandetail).setVisibility(View.INVISIBLE);
//                    min = 5000;
                    setupTextBox(5000,1000000000);
                }
            }
        });

        txtamt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    try {
//                        if(Long.parseLong(txtamt.getText().toString())>=min) {
//                            String a, r;
//                            a=txtamt.getText().toString();
//                            r=Decoration.setComma(a);
//                            Toast.makeText(getContext(), ""+r, Toast.LENGTH_SHORT).show();
//                            //txtamt.setInputType(InputType.);
//                            txtamt.setText(r.toString());
//                        }
//                        else
                        if(Long.parseLong(txtamt.getText().toString())<min)
                            txtamt.setText(Long.toString(min));
                    }
                    catch (NumberFormatException e)
                    {
                        txtamt.setText(Long.toString(min));
                    }
                }
            }
        });

        txtamt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()<=0){
                    layoutVMore.setVisibility(View.INVISIBLE);
                }
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                                        String text=s.toString();
//                text=text.replaceAll("[^0-9]","");
//                txtamt.setText(setComma(text));
//                    }
//                }).start();
            }
        });
        view.findViewById(R.id.btnmin).setOnClickListener(new View.OnClickListener() {
                                                              @Override
                                                              public void onClick(View v) {float interest = Float.parseFloat(txtintrst.getText().toString());
                                                                  float inc = interest - 0.3f;
                                                                  txtintrst.setText(String.format("%.2f",inc));
                                                              }
                                                          }
        );
        view.findViewById(R.id.btncalculate).setOnClickListener(new View.OnClickListener() {
            String frmt_amt1,frmt_amt2,frmt_amt3;
            @Override
            public void onClick(View v) {
                try
                {
                    amt = Float.parseFloat(txtamt.getText().toString());
                    roi = Float.parseFloat(txtintrst.getText().toString());
                    ten = Integer.parseInt(txttenure.getText().toString());
                    txtemi = view.findViewById(R.id.txtemi);
                    txtiamt = view.findViewById(R.id.txtiamt);
                    txttotal = view.findViewById(R.id.txttotal);

                    if (radhome.getText().toString().equals("FD")) {
                        RadioGroup rgrp = view.findViewById(R.id.radgrpfrgmnt);
                        if (rgrp.getCheckedRadioButtonId() == R.id.radpersonal) {
                            if (spin.getSelectedItem().toString() == "Years") {
                                ten = ten * 12;
                                amt /= ten;
                            }
                            double n = amt * ten;
                            double est = amt * ten * (ten + 1) * roi / 100 / 24;

                            frmt_amt1=formatAmount(String.format("%.2f", n));
                            frmt_amt2=formatAmount(String.format("%.2f", est));
                            frmt_amt3=formatAmount(String.format("%.2f", (n + est)));

                            txtemi.setText(frmt_amt1);
                            txtiamt.setText(frmt_amt2);
                            txttotal.setText(frmt_amt3);
                            draw = new Drawer(frmt_amt1, frmt_amt2, frmt_amt3,n,est, msg1,"Investment Details");
                        } else {
                            if (spin.getSelectedItem().toString() == "Months")
                                ten = ten / 12;

                            double p = amt + (amt * ten * roi / 100);
                            frmt_amt1=formatAmount(String.format("%.2f", amt));
                            frmt_amt2=formatAmount(String.format("%.2f", p - amt));
                            frmt_amt3=formatAmount(String.format("%.2f", (p)));

                            txtemi.setText(frmt_amt1);
                            txtiamt.setText(frmt_amt2);
                            txttotal.setText(frmt_amt3);
                            draw = new Drawer(frmt_amt1, frmt_amt2, frmt_amt3,amt,p-amt, msg1,"Investment Details");
                        }
                        TextView txt;
                        txt = view.findViewById(R.id.txtmsg1);
                        txt.setText(msg1[0]);
                        txt = view.findViewById(R.id.txtmsg2);
                        txt.setText(msg1[1]);
                        txt = view.findViewById(R.id.txtmsg3);
                        txt.setText(msg1[2]);

                    } else {
                        if (spin.getSelectedItem().toString() == "Years")
                            ten = ten * 12;

                        rate = roi / 12 / 100;

                        yearInt = rate * ten * amt;
                        emi = amt * rate * (Math.pow((1 + rate), ten)) / (Math.pow((1 + rate), ten) - 1);

                        frmt_amt1=formatAmount(String.format("%.2f", emi));
                        frmt_amt2=formatAmount(String.format("%.2f", yearInt));
                        frmt_amt3=formatAmount(String.format("%.2f", (amt + yearInt)));

                        txtemi.setText(frmt_amt1);
                        txtiamt.setText(frmt_amt2);
                        txttotal.setText(frmt_amt3);
                        TextView txt;
                        txt = view.findViewById(R.id.txtmsg1);
                        txt.setText(msg2[0]);
                        txt = view.findViewById(R.id.txtmsg2);
                        txt.setText(msg2[1]);
                        txt = view.findViewById(R.id.txtmsg3);
                        txt.setText(msg2[2]);
                        draw = new Drawer(frmt_amt1, frmt_amt2, frmt_amt3,emi,yearInt, msg2,"Loan Details");
                    }

                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

//                txtemi.setText(String.format("%.2f",emi));
//                txtiamt.setText(String.format("%.2f",yearInt));
//                txttotal.setText(String.format("%.2f",(amt+yearInt)));
                    layoutVMore.setVisibility(View.VISIBLE);
                }
                catch (NullPointerException | NumberFormatException n){
                    Toast.makeText(context, "Please enter all numericals", Toast.LENGTH_SHORT).show();
                }
            }
        });
        view.findViewById(R.id.btnmax).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                float interest = Float.parseFloat(txtintrst.getText().toString());
                float inc = interest + 0.3f;
                txtintrst.setText(String.format("%.2f",inc));
            }
        });

        //txtintrst.setFilters(new InputFilter[]{new InputFilterMinMax("0.1", "50")});

        txtintrst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                minmax(txtintrst);
            }
        });

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                RadioGroup rgrp=view.findViewById(R.id.radgrpfrgmnt);
                RadioButton rbtn=view.findViewById(rgrp.getCheckedRadioButtonId());
                if(radpersonal.getText().toString().equals("RD")&&rbtn.isChecked())
                    if(position==0)
                        setupTextBox(25,2000000000);
                    else
                        setupTextBox(300,2000000000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        skbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtamt.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        txtvmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Drawer draw=new Drawer();
                draw.showDialog(getContext());
            }
        });


        return view;
    }

    public void setupTextBox(int min,int max){
        skbar.setMax(max);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            skbar.setMin(min);
        }
        txtamt.setText(Integer.toString(min));
        txtamt.setFilters(new InputFilter[]{new InputFilterMinMax(1, max)});
        view.findViewById(R.id.layoutloandetail).setVisibility(View.INVISIBLE);
        this.min = min;
    }

    public void minmax(EditText txt)
    {
        if(txt.getText().toString().equals(""))
            txt.setText("0.1");
        float val=Float.parseFloat(txt.getText().toString());
        Log.d("text", ""+val);
        if(val>50.0f)
            txt.setText("50.0");
        else if(val<0.1f)
            txt.setText("0.1");
    }

    String formatAmount(String amt){
        String[] split_amt=amt.split("\\.");
        return  Decoration.setComma(split_amt[0])+"."+split_amt[1];

    }

}