package com.example.veloxigami.btp;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private TextView time, turnAroundTime, serverTime;
    private Button compute,offloadButton;
    private EditText dim;

    private double matrix1[][], matrix2[][];
    private String server_url = "http://52.15.113.236:5000/data";
    private long serverSendTime;
    private long numOfProc;
    private double freeRAM;
    private long netLatency;
    private int taskSize;
    private long timeInvested;
    private int batteryLevel;
    private OutputObject x;
    private long timeStamp;
    private DatabaseReference databaseReference;
    private int count;
    private boolean offload;
    private boolean offline;
    private boolean mulDone;
    private boolean asyncDone;
    private long offloadTime;
    private long offlineTime;
    private int optionSelect;
    private double totalRAM;
    private int biggerCount;
    private static int OFFLINE = 0;
    private static int OFFLOAD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = findViewById(R.id.textView);
        compute = findViewById(R.id.button);
        dim = findViewById(R.id.editText);
        turnAroundTime = findViewById(R.id.textView2);
        serverTime =  findViewById(R.id.textView3);
        count = 0;
        registerBatteryLevelBroadcast();
        netLatency = 0;
        optionSelect = 0;
        offlineTime = 0;
        offloadTime = 0;

        databaseReference = FirebaseDatabase.getInstance().getReference();
        registerNextOpBroadcastReceiver();

        compute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncDone = false;
                offload = false;
                offline = false;
                count = 0;
                biggerCount = 0;


               // new NetworkLatency().execute();
                numOfProc = numberOfProcesses();
                freeRAM = getFreeRAM();
                totalRAM = getTotalRam();
                taskSize = Integer.parseInt(dim.getText().toString());

                serverSendTime = System.currentTimeMillis();
                volleyTask();

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                            offline = offlineTask(taskSize);
                    }
                };

                thread.start();


                while (true) {
                    if(offload && offline)
                        break;
                }

                //while(!asyncDone);

                if (offloadTime >= offlineTime) {
                    optionSelect = OFFLINE;
                    timeInvested = offlineTime;
                    Log.v("dasdsa", " offline");
                } else if(offloadTime < offlineTime) {
                    optionSelect = OFFLOAD;
                    timeInvested = offloadTime;
                    Log.v("sadas", " offload");
                }

                x = new OutputObject(taskSize,
                        numOfProc, timeInvested, freeRAM, totalRAM,
                        batteryLevel, optionSelect);

                sendDataToFirebase(x);
                count++;
                if (count <= 1000){
                    Intent intentx = new Intent("com.veloxigami.btp.nextop");
                    sendBroadcast(intentx);
                    Log.v("Sent", "Broadcast offline");
                }
            }
        });
    }



    private BroadcastReceiver nextOpReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("OP","Broadcast " + count + " received");
            asyncDone = false;
            offload = false;
            offline = false;

            dim.setText(""+(Integer.parseInt(dim.getText().toString())+1));

            numOfProc = numberOfProcesses();
            freeRAM = getFreeRAM();
            totalRAM = getTotalRam();
            taskSize = Integer.parseInt(dim.getText().toString());

            serverSendTime = System.currentTimeMillis();
            volleyTask();

            Thread thread = new Thread() {
                @Override
                public void run() {
                    offline = offlineTask(taskSize);
                }
            };

            thread.start();


            while (true) {
                if(offload && offline)
                    break;
            }

            //while(!asyncDone);

            if (offloadTime >= offlineTime) {
                optionSelect = OFFLINE;
                timeInvested = offlineTime;
                Log.v("dasdsa", " offline");
            } else if(offloadTime < offlineTime) {
                optionSelect = OFFLOAD;
                timeInvested = offloadTime;
                Log.v("sadas", " offload");
            }

            x = new OutputObject(taskSize,
                    numOfProc, timeInvested, freeRAM, totalRAM,
                    batteryLevel, optionSelect);


            x = new OutputObject(taskSize,
                    numOfProc, timeInvested, freeRAM, totalRAM,
                    batteryLevel, optionSelect);

            sendDataToFirebase(x);
            count++;
            if (count <= 300) {
                Intent intentx = new Intent("com.veloxigami.btp.nextop");
                sendBroadcast(intentx);
                Log.v("Sent", "Broadcast offline");
            } else if (count > 300 ){
                if(biggerCount != 7){
                    biggerCount++;
                    count = 0;
                    dim.setText(""+1);
                    Intent intentx = new Intent("com.veloxigami.btp.nextop");
                    sendBroadcast(intentx);
                }
                else
                    count = 0;
            }
        }
    };

    private void registerNextOpBroadcastReceiver(){
        IntentFilter filter = new IntentFilter("com.veloxigami.btp.nextop");
        registerReceiver(nextOpReceiver,filter);
    }

    private double[][] matrixGenerator(int n){
        double matrix[][]= new double[n][n];
        for(int i=0; i<n;i++){
            for(int j=0; j<n;j++){
                matrix[i][j]= Math.random();
            }
        }
        return matrix;
    }

    public void multiply(double mat1[][],double mat2[][], int N)    {
        double res[][]=new double[N][N];
        int i, j, k;
        for (i = 0; i < N; i++)
        {

            for (j = 0; j < N; j++)
            {
                res[i][j] = 0;
                for (k = 0; k < N; k++)
                    res[i][j] += mat1[i][k]
                            * mat2[k][j];
            }
        }
    }

    private long numberOfProcesses(){
        List<AndroidAppProcess> processes = AndroidProcesses.getRunningAppProcesses();
        long number = processes.size();

        /**Number of processes*/
        Log.v("TAG","number of processes "+number);
        return number;
    }

    public double getTotalRam(){
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        Log.v("total RAM: ", "" + ((mi.totalMem/1024)/1024));
        return ((mi.totalMem/1024)/1024);
    }

    public double getFreeRAM() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double mem =  100*(((double)mi.totalMem-(double)mi.availMem)/(double)(mi.totalMem));
        Log.v("Available RAM: ", ""+ ((mi.availMem/1024)/1024));
        return mem;
    }

    BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            context.unregisterReceiver(this);
            int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int level = -1;
            if (rawlevel >= 0 && scale > 0) {
                level = (rawlevel * 100) / scale;
            }
            batteryLevel = level;
            Log.v("Batter%",level + "");
        }
    };

    private void registerBatteryLevelBroadcast(){
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }

    class NetworkLatency extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
          /*  netLatency = 0;
            int timeout = 3000;
            long beforeTime = System.currentTimeMillis();
            InetAddress ip = null;
            try {
                ip = InetAddress.getByName("52.15.113.236");
                Log.v("netlat","host reached");
            } catch (UnknownHostException e) {
                e.printStackTrace();

                Log.v("netlat","host unreached");
            }
            byte[] bytes = ip.getAddress();
            boolean reachable = true;
            try {
                reachable =  InetAddress.getByAddress(bytes).isReachable(timeout);
            } catch (IOException e) {
                e.printStackTrace();
            }
            long afterTime = System.currentTimeMillis();
            long timeDifference = afterTime - beforeTime;

            Log.v("latency",timeDifference+"");
            netLatency = timeDifference;
            asyncDone = true;*/
            inSomeWhere();
            return null;

        }
    }

    private void volleyTask(){



        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        long serverResponseTime = System.currentTimeMillis();
                        offloadTime = serverResponseTime - serverSendTime;
                        Log.v("timestart",""+serverSendTime);
                        Log.v("timeend",""+serverResponseTime);

                        Log.v("offloadtime",offloadTime+"");
                        turnAroundTime.setText(""+offloadTime+ " ms");

                        serverTime.setText(response + " s");
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                turnAroundTime.setText("Error occurred");
                serverTime.setText("Error occurred");
                requestQueue.stop();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("size",""+taskSize);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

        offload = true;

    }

    private boolean offlineTask(int dimension){
        multiply(matrixGenerator(dimension),
                matrixGenerator(dimension),
                dimension);
        offlineTime = System.currentTimeMillis() - serverSendTime;
        Log.v("offlinetime",offlineTime+"");
        return true;
    }

    private void sendDataToFirebase(OutputObject x){
        timeStamp = System.currentTimeMillis();
        databaseReference.child(""+timeStamp).setValue(x);
    }






    public static long inSomeWhere()
    {
        long start = System.currentTimeMillis();
        String pingResult = getPingResult("52.15.113.236");
        long latency = System.currentTimeMillis() - start;
        boolean isNetOk = true;
        if (pingResult == null) {
            // not reachable!!!!!
            isNetOk = false;
        }
        return latency;
    }


    public static String getPingResult(String a) {
        String str = "";
        String result = "";
        BufferedReader reader = null;
        char[] buffer = new char[4096];
        StringBuffer output = new StringBuffer();

        try {
            Runtime r = Runtime.getRuntime();
            Process process = r.exec("/system/bin/ping -c 3 " + a);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            int i;

            while ((i = reader.read(buffer)) > 0)
                output.append(buffer, 0, i);


            str = output.toString();

            final String[] b = str.split("---");
            final String[] c = b[2].split("rtt");

            if (b.length == 0 || c.length == 0)
                return null;

            if(b.length == 1 || c.length == 1)
                return null;

            result = b[1].substring(1, b[1].length()) + c[0] + c[1].substring(1, c[1].length());

        } catch (IOException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
        finally
        {
            if(reader != null)
            {
                try{reader.close();}catch(IOException ie){}
            }
        }

        return result;
    }
}
