package com.semirus.timestampworker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
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
    EditText taskEditTxt;
    Button startBtn, addBtn;
    ListView taskList;
    String macAddress, savedSSID, startSSID, currentSSID, totalWorkHr, toDoList, toDoListDate = "";
    //String savedSSID = "KT_GiGA_5G_Wave2_75B5";
    // handle timer
    Timer timer;
    TimerTask timerTask;
    String lunchStartStr, lunchEndStr = "";
    //  use handler to be able to run in our TimerTask
    final Handler handler = new Handler();
    // for time in
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    long now = System.currentTimeMillis();
    ArrayList<String> timeStampList = new ArrayList<String>();
    int trial, index = 0;
    String name, date, timeIN, timeOUT, current_timeIN, current_timeOUT, totalTime = "";
    static RequestQueue requestQueue;
    // for task
    ArrayList adapterList = new ArrayList();
    String task, taskNoSpace = "";
    int taskLen, spaceIndex = 0;
    boolean addedTask = false;
    // for task list to compute only once
    int counter = 0;
    String selectedTask, selectedTaskName, selectedTaskDate = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // text view
        trialTxtView = (TextView)findViewById(R.id.trialTxtView);
        timeInTxtView = (TextView)findViewById(R.id.timeInTxtView);
        timeOutTxtView = (TextView)findViewById(R.id.timeOutTxtView);
        totalWorkHrTxtView = (TextView)findViewById(R.id.totalWorkHrTxtView);
        // edit text
        taskEditTxt = (EditText)findViewById(R.id.taskEditTxt);
        // button
        startBtn = (Button) findViewById(R.id.startBtn);
        addBtn = (Button) findViewById(R.id.addBtn);
        // list view
        taskList = (ListView)findViewById(R.id.taskList);
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
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSSID();
            }
        });
        // click button automatically
        startBtn.performClick();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });
        // selected item of taskList
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTask = taskList.getItemAtPosition(position).toString();
                String[] arrOfStr = selectedTask.split(":");
                System.out.println("array of selectedTask " + arrOfStr[0]);
                selectedTaskName = arrOfStr[0];
                selectedTaskDate = arrOfStr[1];
                Intent intent = new Intent(getApplicationContext(), TaskActivity.class);
                intent.putExtra("selectedTaskName", selectedTaskName);
                intent.putExtra("selectedTaskDate", selectedTaskDate);
                intent.putExtra("macAddress", macAddress);
                startActivity(intent);
            }
        });
    }
    // get SSID
    public void getSSID(){
        final String url = "https://hcho241.freehongs.net/getWorkerSSID.jsp?macAddress=" + macAddress;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected getSSID -> " + response);
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray jsonArray = json.getJSONArray("Data Sent");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                json = jsonArray.getJSONObject(i);
                                name = json.getString("name");
                                savedSSID = json.getString("ssid");
                            }
                            if (startSSID.equals(savedSSID)){
                                startTimer();
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
        request.setShouldCache(false);    // disable cache
        requestQueue.add(request);
    }
    // add task
    public void addTask(){
        task = taskEditTxt.getText().toString();
        taskLen = task.length();
        for (int i = 0; i < taskLen; i++){
            if (task.charAt(i) == ' '){
                // find index of space
                spaceIndex = task.indexOf(' ');
                taskNoSpace = task.substring(0, spaceIndex) + "_" + task.substring(spaceIndex+1);
                System.out.println("taskNoSpace " + taskNoSpace);
            }
            else{
                taskNoSpace = task;
            }
        }
        final String url = "https://hcho241.freehongs.net/addTask.jsp?name=" + name + "&macAddress=" + macAddress +
                "&date=" + date + "&task=" + taskNoSpace;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected addTask -> " + response);
                        try {
                            System.out.println("Add Task");
                            adapterList.clear();
                            getTaskList();
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
        request.setShouldCache(false);    // disable cache
        requestQueue.add(request);
    }
    // 9) log each time
    public void addTimeLog(){
        final String url = "https://hcho241.freehongs.net/addTimeLog.jsp?name=" + name + "&macAddress=" + macAddress +
                "&date=" + date + "&timeIN=" + current_timeIN + "&timeOUT=" + current_timeOUT + "&totalWorkHr=" + totalWorkHr;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected addTimeLog -> " + response);
                        try {
                            System.out.println("Add Time Log");
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
        request.setShouldCache(false);    // disable cache
        requestQueue.add(request);
    }
    // 8) show total work hr
    public void displayTotalWorkHr(){
        final String url = "https://hcho241.freehongs.net/getTotalWorkHr.jsp?macAddress=" + macAddress;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected displayTotalWorkHr -> " + response);
                        try {
                            System.out.println("Display Total Work Hr");
                            JSONObject json = new JSONObject(response);
                            JSONArray jsonArray = json.getJSONArray("Data Sent");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                json = jsonArray.getJSONObject(i);
                                totalWorkHr = json.getString("totalWorkHr");
                                totalTime = totalWorkHr;
                            }
                            totalWorkHrTxtView.setText("Total Work Hr : " + totalWorkHr);
                            if (counter < 1){               // for some reason time log added twice, so I added this line
                                addTimeLog();
                                counter ++;
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
        request.setShouldCache(false);    // disable cache
        requestQueue.add(request);
    }
    // 7) calculate total work hr
    public void calcTotalWorkHr(){
        System.out.println("timeIN " + current_timeIN + " timeOUT " + current_timeOUT);
        final String url = "https://hcho241.freehongs.net/addTotalWorkHr.jsp?macAddress=" + macAddress + "&timeIN=" + current_timeIN + "&timeOUT=" + current_timeOUT;
        System.out.println(url);
        displayTotalWorkHr();
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        System.out.println("Connected calcTotalWorkHr -> " + response);
                        try {
                            System.out.println("Total Work Hr added");
                            displayTotalWorkHr();
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
        request.setShouldCache(false);    // disable cache
        requestQueue.add(request);
    }
    // 6) add TimeOUT & reset everything
    public void addTimeOUT(){
        System.out.println("TIME OUT " + macAddress);
        index = timeOUT.indexOf(" ");
        current_timeOUT = timeOUT.substring(index+1);
        final String url = "https://hcho241.freehongs.net/addTimeOUT.jsp?macAddress=" + macAddress + "&timeOUT=" + current_timeOUT;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
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
                            totalWorkHr = "";
                            System.out.println("List : " + timeStampList + ", # trial : " + trial + ", current ssid : " + currentSSID + ", timeIN : " + timeIN + ", timeOUT : " + timeOUT);
                            calcTotalWorkHr();
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
        request.setShouldCache(false);    // disable cache
        requestQueue.add(request);
    }
    // 5) add TimeIN
    public void addTimeIN(String time){
        index = timeIN.indexOf(" ");
        date = timeIN.substring(0, index);
        current_timeIN = timeIN.substring(index+1);
        final String url = "https://hcho241.freehongs.net/addTimeIN.jsp?macAddress=" + macAddress + "&ssid=" + currentSSID + "&date=" + date + "&timeIN=" + current_timeIN;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected addTimeIN -> " + response);
                        try {
                            System.out.println("timeIN added " + macAddress);
                            // after get in, check state every 10 min for 6 times
                            if (addedTask == false) {
                                getTaskList();
                                checkState(time);
                            }
                            else{
                                checkState(time);
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
        request.setShouldCache(false);    // disable cache
        requestQueue.add(request);
    }
    // timeIN helper function - get task list right after worker time in
    public void getTaskList(){
        final String url = "https://hcho241.freehongs.net/getTaskList.jsp?macAddress=" + macAddress + "&date=" + date;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected getTaskList -> " + response);
                        try {
                            System.out.println("Get Task List");
                            JSONObject json = new JSONObject(response);
                            JSONArray jsonArray = json.getJSONArray("Data Sent");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                json = jsonArray.getJSONObject(i);
                                toDoList = json.getString("task");
                                toDoListDate = json.getString("date");
                                adapterList.add(toDoList + ":" + toDoListDate);
                            }
                            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, adapterList);
                            taskList.setAdapter(adapter);
                            //checkState(time);
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
        request.setShouldCache(false);    // disable cache
        requestQueue.add(request);
    }
    // 4) repeat 6 times to check state == LEFT SEAT, LUNCH, OUT
    public void checkState(String time){
        System.out.println("mac address in checkState : " + macAddress);
        addedTask = true;
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
            // stop timer
            if (timer != null) {
                timer.cancel();
                timer.purge();
                timer = null;
            }
            // set time out
            timeOUT = timeStampList.get(0);
            trialTxtView.setText("Trial : " + trial);
            timeOutTxtView.setText("TIME OUT : " + timeOUT);
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
            Toast.makeText(MainActivity.this, "Please try to connect other wifi", Toast.LENGTH_SHORT).show();
            checkState(time);
        }
    }
    // 2) initialize timer task
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        // get the current timeStamp
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        final String strNow = simpleDateFormat.format(calendar.getTime());
                        System.out.println("strNow " + strNow);
                        // set lunch time start
                        calendar.set(Calendar.HOUR_OF_DAY, 12);
                        calendar.set(Calendar.MINUTE, 00);
                        lunchStartStr = simpleDateFormat.format(calendar.getTime());
                        System.out.println("lunch start " + lunchStartStr);
                        // set lunch time end
                        calendar.set(Calendar.HOUR_OF_DAY, 13);
                        calendar.set(Calendar.MINUTE, 00);
                        lunchEndStr = simpleDateFormat.format(calendar.getTime());
                        System.out.println("lunch end " + lunchEndStr);
                        // check ssid connectivity
                        if (strNow.equals(lunchStartStr)){  // lunch time
                            Toast.makeText(MainActivity.this, "Lunch time", Toast.LENGTH_SHORT).show();
                            if (strNow.equals(lunchEndStr)){
                                checkSSID(strNow);
                            }
                        }
                        else {  // before & after lunch time
                            checkSSID(strNow);
                            Toast.makeText(MainActivity.this, strNow, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
    }
    // 1) schedule the timer, right after user starts the app the TimerTask will run every 600000ms(10min)
    public void startTimer() throws InterruptedException {
        timer = new Timer();    //set a new Timer
        initializeTimerTask();  //initialize the TimerTask's job
        timer.schedule(timerTask, 0, 3000); // RIGHT NOW : run every 3 sec to test
    }
}