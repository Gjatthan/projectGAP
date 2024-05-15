package com.example.gap.misc;

import java.security.SecureRandom;

public class GenerateOTP {
    public static String getOtp(){
        SecureRandom secureRandom=new SecureRandom();
        String otp="";
        for(int i=0;i<6;i++){
            int num=secureRandom.nextInt(9);
            if(num==0){
                i--;
                continue;
            }
            otp+=Integer.toString(num);
        }
        return otp;
    }
}
