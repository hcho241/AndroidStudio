package com.semirus.timestampmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView welcomeTxtView, timeSheetTxtView;
    Button startBtn, addWorkerBtn, editInfoBtn;
    ListView timeSheetList;
    String ssid, macAddress;
    String name, registeredMacAddress, registeredSSID;
    static RequestQueue requestQueue;
    ArrayList adapterList = new ArrayList();
    ArrayList saveInfo = new ArrayList();
    String  workerName, workerMAC, date, timeIN, timeOUT, totalWorkHr = "";
    String selectedWorker, selectedWorkerName, selectedWorkerMAC, selectedDate, selectedTimeIN, selectedTimeOUT = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        welcomeTxtView = (TextView)findViewById(R.id.welcomeTxtView);
        timeSheetTxtView = (TextView)findViewById(R.id.timeSheetTxtView);
        startBtn = (Button) findViewById(R.id.startBtn);
        addWorkerBtn = (Button) findViewById(R.id.addWorkerBtn);
        editInfoBtn = (Button) findViewById(R.id.editInfoBtn);
        timeSheetList = (ListView)findViewById(R.id.timeSheetList);
        // grant wifi network permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "Please allow access", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        ssid = wifiInfo.getSSID();              // get the current wifi name -> this includes ""
        ssid = ssid.replace("\"", "");  // exclude ""
        macAddress = wifiInfo.getBSSID();               // get the mac address of the currently connected network
        System.out.println("mac address is " + macAddress);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkManagerExist();
            }
        });
        addWorkerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddWorker.class);
                intent.putExtra("ssid", ssid);
                startActivity(intent);
            }
        });
        editInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditInfo.class);
                intent.putExtra("name", name);
                intent.putExtra("macAddress", macAddress);
                intent.putExtra("ssid", ssid);
                startActivity(intent);
            }
        });
        // automatically click the start button
        startBtn.performClick();
    }
    // 2) display time sheet of workers
    public void displayTimeSheet(){
        final String url = "https://hcho241.freehongs.net/displayTime.jsp?ssid=" + ssid;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected displayTime -> " + response);
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray jsonArray = json.getJSONArray("Data Sent");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                json = jsonArray.getJSONObject(i);
                                workerName = json.getString("name");
                                workerMAC = json.getString("macAddress");
                                date = json.getString("date");
                                timeIN = json.getString("timeIN");
                                timeOUT = json.getString("timeOUT");
                                totalWorkHr = json.getString("totalWorkHr");
                                // to see whether the client received the data from server or not
                                String data = "AdapterList Data : " + workerName + " " + date + " " + timeIN + " " + timeOUT + " " + totalWorkHr;
                                System.out.println(data);
                                adapterList.add(workerName + " " + date + " " + timeIN + " " + timeOUT + " " + totalWorkHr);
                                saveInfo.add(workerName + " " + workerMAC + " " + date + " " + timeIN + " " + timeOUT + " " + totalWorkHr);
                            }
                            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, adapterList);
                            timeSheetList.setAdapter(adapter);
                            // selected item of timesheet list
                            timeSheetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    selectedWorker = timeSheetList.getItemAtPosition(position).toString();
                                    String[] arrOfStr = selectedWorker.split(" ");
                                    //System.out.println("array of selectedUser " + arrOfStr[0]);
                                    selectedWorkerName = arrOfStr[0];
                                    selectedDate = arrOfStr[1];
                                    selectedTimeIN = arrOfStr[2];
                                    selectedTimeOUT = arrOfStr[3];
                                    //System.out.println("workerMac " + workerMAC);
                                    //System.out.println("saveInfo" + saveInfo.get(position).toString());
                                    String [] arrOfMAC = saveInfo.get(position).toString().split(" ");
                                    selectedWorkerMAC = arrOfMAC[1];
                                    //System.out.println("Main activity workerMAC " + selectedWorkerMAC);
                                    Intent intent = new Intent(getApplicationContext(), WorkerLogInfo.class);
                                    intent.putExtra("workerName", selectedWorkerName);
                                    //intent.putExtra("workerMAC", workerMAC);
                                    intent.putExtra("workerMAC", selectedWorkerMAC);
                                    intent.putExtra("date", selectedDate);
                                    intent.putExtra("timeIN", selectedTimeIN);
                                    intent.putExtra("timeOUT", selectedTimeOUT);
                                    startActivity(intent);
                                }
                            });
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
    // 1) check the manager's mac address to see whether she/he already registered or not
    public void checkManagerExist(){
        final String url = "https://hcho241.freehongs.net/checkManagerExist.jsp?macAddress=" + macAddress;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected checkManagerExist -> " + response);
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray jArray = json.getJSONArray("Data Sent");
                            for (int i = 0; i < jArray.length(); i++) {
                                json = jArray.getJSONObject(i);
                                name = json.getString("name");
                                registeredMacAddress = json.getString("macAddress");
                                registeredSSID = json.getString("ssid");
                            }
                            System.out.println("name " + name + " &registeredMacAddress " + registeredMacAddress + " & mac address " + macAddress);
                            System.out.println(" &registeredSSID " + registeredSSID + " & ssid " + ssid);
                            // if the manager's mac address already registered, show time sheet of each worker
                            if (registeredMacAddress.equals(macAddress)) {
                                welcomeTxtView.setText("Welcome, " + name);
                                startBtn.setVisibility(View.GONE);
                                timeSheetTxtView.setVisibility(View.VISIBLE);
                                timeSheetList.setVisibility(View.VISIBLE);
                                addWorkerBtn.setVisibility(View.VISIBLE);
                                editInfoBtn.setVisibility(View.VISIBLE);
                                displayTimeSheet();
                            }
                            else {
                                startBtn.setOnClickListener(null);
                                startBtn.setEnabled(false);
                                startBtn.setClickable(false);
                                Toast.makeText(MainActivity.this, "Please Register", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                                intent.putExtra("macAddress", macAddress);
                                intent.putExtra("ssid", ssid);
                                startActivity(intent);
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
}