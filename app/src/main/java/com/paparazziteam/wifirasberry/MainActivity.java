package com.paparazziteam.wifirasberry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    //UI Element
    Button btnUp;
    Button btnDown;
    EditText txtAddress;
    Socket myAppSocket = null;
    public static String wifiIp = "";
    public static int wifiPort = 0;
    public static String CMD = "0";

    TextView datarecibida;
    String dataRasberry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnUp = (Button) findViewById(R.id.btnUp);
        btnDown = (Button) findViewById(R.id.btnDown);

        txtAddress = (EditText) findViewById(R.id.ipAddress);
        datarecibida = findViewById(R.id.datarecibida);

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getIPandPort();
                CMD = "Up";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();

            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "Down";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
            }
        });


    }


    public void getIPandPort()
    {
        String iPandPort = txtAddress.getText().toString();
        Log.d("MYTEST","IP String: "+ iPandPort);
        String temp[]= iPandPort.split(":");
        wifiIp = temp[0];
        wifiPort = Integer.valueOf(temp[1]);
        Log.d("MY TEST","IP:" +wifiIp);
        Log.d("MY TEST","PORT:"+wifiPort);
    }
    public class Socket_AsyncTask extends AsyncTask<Void,Void,Void>
    {
        Socket socket;

        @Override
        protected Void doInBackground(Void... params){
            try{
                InetAddress inetAddress = InetAddress.getByName(wifiIp);
                socket = new java.net.Socket(inetAddress,MainActivity.wifiPort);
                InputStreamReader dataInputStream = new InputStreamReader(socket.getInputStream());
                dataRasberry= dataInputStream.getEncoding();

                dataInputStream.close();
                socket.close();

            }catch (UnknownHostException e)
            {
                e.printStackTrace();

            }catch (IOException e)
            {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(MainActivity.this, "ERROR"+ e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

                //Log.e("ERROR", ""+ e.getMessage());

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            if(dataRasberry!= null)
            {
                if(!datarecibida.equals(""))
                {
                    datarecibida.setText(dataRasberry);

                }else {
                    datarecibida.setText("Datos recibidos vacios");
                    Toast.makeText(MainActivity.this, "Datos recibidos vacios", Toast.LENGTH_SHORT).show();
                }

            }else {
                datarecibida.setText("Data no recibida, error de conexion");
                Toast.makeText(MainActivity.this, "Data no recibida, error de conexion", Toast.LENGTH_SHORT).show();
            }



        }
    }
}