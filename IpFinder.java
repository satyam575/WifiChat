package com.newton.wifichat;

import android.os.AsyncTask;
import android.os.Handler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class IpFinder extends AsyncTask<Void,Void,Void> {

    String ip = "http://ipv4bot.whatismyipaddress.com/";
    String result = "";

    Handler handler = new Handler();
    @Override
    protected Void doInBackground(Void... voids) {

        try {
            URL url = new URL(ip);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            InputStream is = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = "" ;
            while ((line = reader.readLine()) != null){

                result += line ;
            }
            httpURLConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
