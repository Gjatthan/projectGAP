package com.example.gap.misc;

import android.widget.Toast;

import java.util.regex.Pattern;

public class Decoration {
    public static String setComma(String amt){
        int len,dif;
        String disp="";

        len=amt.length();

        if(len>3&&Pattern.compile("[0-9]+").matcher(amt).matches())
        {
            if(len%2==0) {
                dif = len - 3;
                disp = amt.substring(dif - 1, dif) + "," + amt.substring(dif, len);

                while (dif > 2) {
                    dif -= 2;
                    disp = amt.substring(dif - 1, dif) + "," + amt.substring(dif, dif + 1) + disp;
                }
            }
            else
            {
                dif = len - 3;
                disp = amt.substring(dif - 2, dif) + "," + amt.substring(dif, len);

                while (dif > 2) {
                    dif -= 2;
                    disp = amt.substring(dif - 2, dif) + "," + disp;
                }
            }
            return disp;
        }
        else
            return amt;


    }
}
