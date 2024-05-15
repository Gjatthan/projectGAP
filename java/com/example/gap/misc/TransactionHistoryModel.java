package com.example.gap.misc;

public class TransactionHistoryModel {
    String desc,date,drcramt,tamt,drcrmsg,acno;

    public TransactionHistoryModel(String desc, String date, String drcramt, String tamt, String drcrmsg,String acno) {
        this.desc = desc;
        this.date = date;
        this.drcramt = drcramt;
        this.tamt = tamt;
        this.drcrmsg = drcrmsg;
        this.acno=acno;
    }
}
