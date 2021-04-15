package com.semirus.worker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TextView trialTxtView, timeInTxtView, timeOutTxtView, totalWorkHrTxtView;
    String macAddress, savedSSID, startSSID, currentSSID, totalWorkHr = "";
    // timer
    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();
    // for time in
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    long now = System.currentTimeMillis();
    String timeIN, timeOUT = "";
    ArrayList<String> timeStampList = new ArrayList<String>();
    int trial = 0;
    static RequestQueue requestQueue;
    String date, current_timeIN, current_timeOUT = "";
    int index = 0;
    boolean start = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // text view
        trialTxtView = (TextView)findViewById(R.id.trialTxtView);
        timeInTxtView = (TextView)findViewById(R.id.timeInTxtView);
        timeOutTxtView = (TextView)findViewById(R.id.timeOutTxtView);
        totalWorkHrTxtView = (TextView)findViewById(R.id.totalWorkHrTxtView);
        // if permission is not granted, ask
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "Please allow access", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        // get the mac address & currently connected network
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifiMgr.isWifiEnabled()){   // if wifi is connected
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            startSSID = wifiInfo.getSSID();              // get the current wifi name -> this includes ""
            startSSID = startSSID.replace("\"", "");  // exclude ""
            macAddress = wifiInfo.getBSSID();
        }
        else{
            Toast.makeText(MainActivity.this, "Please Connect to wifi", Toast.LENGTH_SHORT).show();
        }
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }
    // onResume we start our timer so it can start when the app comes from the background
    @Override
    protected void onResume() {
        super.onResume();
        if (start) {
            getSSID();
        }
    }
    // handler
    // Define the code block to be executed
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            //get the current timeStamp
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final String strDate = simpleDateFormat.format(calendar.getTime());
            // check ssid connectivity
            checkSSID(strDate);
            //show the toast
            Toast.makeText(MainActivity.this, strDate, Toast.LENGTH_SHORT).show();
            // Repeat this the same runnable code block again another 2 second
            // 'this' is referencing the Runnable object
            handler.postDelayed(this, 10000);
        }
    };
    // 6) add TimeOUT & reset everything
    public void addTimeOUT(){
        System.out.println("TIME OUT " + macAddress);
        // remove timer delaying
        if (start == false){
            handler.removeCallbacks(runnableCode);
            System.out.println("stop timer");
        }
        index = timeOUT.indexOf(" ");
        current_timeOUT = timeOUT.substring(index+1);
        final String url = "https://hcho241.freehongs.net/addTimeOUT.jsp?macAddress=" + macAddress + "&timeOUT=" + current_timeOUT;
        System.out.println(url);
        StringRequest requestOUT = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected addTimeOUT -> " + response);
                        try {
                            System.out.println("timeOUT added");
                            // reset list
                            timeStampList.clear();
                            trial = 0;
                            currentSSID = "";
                            timeIN = "";
                            timeOUT = "";
                            System.out.println("List : " + timeStampList + ", # trial : " + trial + ", current ssid : " + currentSSID + ", timeIN : " + timeIN + ", timeOUT : " + timeOUT);
                            //calcTotalWorkHr();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error : " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                return params;
            }
        };
        requestOUT.setShouldCache(false);    // disable cache
        requestQueue.add(requestOUT);
    }
    // 5) add TimeIN
    public void addTimeIN(String time){
        index = timeIN.indexOf(" ");
        date = timeIN.substring(0, index);
        current_timeIN = timeIN.substring(index+1);
        final String url = "https://hcho241.freehongs.net/addTimeIN.jsp?macAddress=" + macAddress + "&ssid=" + currentSSID + "&date=" + date + "&timeIN=" + current_timeIN;
        System.out.println(url);
        StringRequest requestIN = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected addTimeIN -> " + response);
                        try {
                            System.out.println("timeIN added " + macAddress);
                            // after get in, check state every 10 min for 6 times
                            checkState(time);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error : " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                return params;
            }
        };
        requestIN.setShouldCache(false);    // disable cache
        requestQueue.add(requestIN);
    }
    // 4) repeat 6 times to check state == LEFT SEAT, LUNCH, OUT
    public void checkState(String time){
        System.out.println("mac address in checkState : " + macAddress);
        // if worker took a break / lunch
        if (trial > 0 && trial < 6 && !currentSSID.equals(savedSSID)){
            timeStampList.add(time);
            trialTxtView.setText("Trial : " + trial + "\n Break Time List : " + timeStampList);
        }
        // if worker took a break / lunch and come back to work
        else if (trial > 0 && trial < 6 && currentSSID.equals(savedSSID)){
            trial = 0;
            timeStampList.clear();
        }
        // if worker is out of work place
        else if (trial == 6){
            System.out.println("mAddress if trial == 6 " + macAddress);
            // Removes pending code execution
            //handler.removeCallbacks(runnableCode);
            // set time out
            timeOUT = timeStampList.get(0);
            trialTxtView.setText("Trial : " + trial);
            timeOutTxtView.setText("TIME OUT : " + timeOUT);
            start = false;
            addTimeOUT();
        }
    }
    // 3) keep checking current ssid
    public void checkSSID(String time){
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        currentSSID = wifiInfo.getSSID();              // get the current wifi name -> this includes ""
        currentSSID = currentSSID.replace("\"", "");  // exclude ""
        // if check ssid == semiruskorea
        if (currentSSID.equals(savedSSID)){
            System.out.println("ssid == semiruskorea " + macAddress);
            timeIN = dateFormat.format(new Date(now));
            timeInTxtView.setText("TIME IN : " + timeIN);
            addTimeIN(time);
        }
        // if ssid != semiruskorea
        else {
            trial += 1;
            System.out.println("ssid != semiruskorea " + macAddress);
            checkState(time);
            //Toast.makeText(MainActivity.this, "Please try to connect other wifi", Toast.LENGTH_SHORT).show();
        }
    }

    public void getSSID(){
        final String url = "https://hcho241.freehongs.net/getWorkerSSID.jsp?macAddress=" + macAddress;
        System.out.println(url);
        StringRequest requestSSID = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected getSSID -> " + response);
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray jsonArray = json.getJSONArray("Data Sent");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                json = jsonArray.getJSONObject(i);
                                savedSSID = json.getString("ssid");
                            }
                            System.out.println("getSSID " + savedSSID);
                            if (startSSID.equals(savedSSID)){
                                //startTimer();
                                // Start the initial runnable task by posting through the handler
                                handler.post(runnableCode);
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Please Ask Manager to Register", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error : " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                return params;
            }
        };
        requestSSID.setShouldCache(false);    // disable cache
        requestQueue.add(requestSSID);
    }
}