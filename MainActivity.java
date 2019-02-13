package com.newton.wifichat;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText e1,e2 ;
    Button sendBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);

       Log.i("Ip address",ipAddress);

       e1 = findViewById(R.id.myip);
       e1.setText(ipAddress);
       e2 = findViewById(R.id.message );
       sendBtn = findViewById(R.id.sendBtn);

       sendBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               BackgroundTask backgroundTask = new BackgroundTask();
               backgroundTask.execute(e1.getText().toString(),e2.getText().toString());
           }
       });

       Thread myThread = new Thread(new MyServer());
       myThread.start();


    }

    class MyServer implements Runnable {

        ServerSocket ss ;
        Socket mySocket ;
        DataInputStream dis ;
        String incomingMessage ;
        Handler handler = new Handler();

        @Override
        public void run() {

            Log.i("My Server","Listening");
            try {

                ss = new ServerSocket(9700);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Waiting for the client", Toast.LENGTH_SHORT).show();
                    }
                });

                while(true){

                    mySocket = ss.accept();

                    dis = new DataInputStream(mySocket.getInputStream());
                    incomingMessage = dis.readUTF();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    class BackgroundTask extends AsyncTask<String,Void,String>{

        Socket s ;
        DataOutputStream dos ;
        String ip,message ;

        @Override
        protected String doInBackground(String... strings) {

            ip = strings[0];
            message = strings[1];

            try {
                s= new Socket(ip, Integer.parseInt("9700"));
                dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(message);
                dos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
