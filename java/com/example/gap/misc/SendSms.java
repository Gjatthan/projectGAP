package com.example.gap.misc;

import android.os.AsyncTask;

import com.example.gap.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendSms extends AsyncTask<Void,Void,String> {
    protected void onPreExecute() {
        //display progress dialog.

    }
    @Override
    protected String doInBackground(Void... params) {
        try {
            // Construct data
            String apiKey = "apikey=" + "MzA2NzZjNGQ1Nzc3NjE2NjQzNDY2ZTM5NDkzNzM5NDQ=";
            String message = "&message=" + "This is your message";
            String sender = "&sender=" + "TXTLCL";
            String numbers = "&numbers=" + "918904300710";

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
            String data = apiKey + message+ sender + numbers;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
                System.out.println(line);
            }
            rd.close();

            return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS "+e);
            return "Error "+e;
        }
    }



    protected void onPostExecute(Void result) {
        // dismiss progress dialog and update ui
    }
}
