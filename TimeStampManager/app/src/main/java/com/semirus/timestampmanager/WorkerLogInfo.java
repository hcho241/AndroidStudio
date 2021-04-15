package com.semirus.timestampmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorkerLogInfo extends MainActivity{
    TextView nameTxtView;
    Button editBtn, taskBtn;
    ListView timeLogListView;
    String workerName, workerMAC, date, timeIN, timeOUT = "";
    String dateLog, timeINlog, timeOUTlog, totalWorkHrLog = "";
    String nameTxt = "";
    // adapter list
    ArrayList adapterList = new ArrayList();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_log_info);
        // text view
        nameTxtView = (TextView)findViewById(R.id.nameTxtView);
        timeLogListView = (ListView)findViewById(R.id.timeLogListView);
        // button
        editBtn = (Button)findViewById(R.id.editBtn);
        taskBtn = (Button)findViewById(R.id.taskBtn);
        // get passed values
        Bundle extra = getIntent().getExtras();
        workerName = extra.getString("workerName");
        workerMAC = extra.getString("workerMAC");
        //System.out.println("Worker loginfo workerMAC" + workerMAC);
        date = extra.getString("date");
        timeIN = extra.getString("timeIN");
        timeOUT = extra.getString("timeOUT");
        // set text view
        nameTxt = nameTxtView.getText().toString() + workerName;
        nameTxtView.setText(nameTxt);
        getTimeLogList();
        editBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditWorkHr.class);
                intent.putExtra("workerName", workerName);
                intent.putExtra("workerMAC", workerMAC);
                intent.putExtra("date", date);
                intent.putExtra("timeIN", timeIN);
                intent.putExtra("timeOUT", timeOUT);
                startActivity(intent);
            }
        });
        taskBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WorkerTaskInfo.class);
                intent.putExtra("workerName", workerName);
                intent.putExtra("workerMAC", workerMAC);
                startActivity(intent);
            }
        });
    }
    public void getTimeLogList(){
        final String url = "https://hcho241.freehongs.net/getTimeLogList.jsp?macAddress=" + workerMAC;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected getTimeLogList -> " + response);
                        try {
                            System.out.println("Get Time Log List");
                            JSONObject json = new JSONObject(response);
                            JSONArray jsonArray = json.getJSONArray("Data Sent");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                json = jsonArray.getJSONObject(i);
                                dateLog = json.getString("date");
                                timeINlog = json.getString("timeIN");
                                timeOUTlog = json.getString("timeOUT");
                                totalWorkHrLog = json.getString("totalWorkHr");
                                // to see whether the client received the data from server or not
                                String data = "AdapterList Data : " + dateLog + " " + timeINlog + " " + timeOUTlog + " " + totalWorkHrLog;
                                System.out.println(data);
                                adapterList.add(dateLog + " " + timeINlog + " " + timeOUTlog + " " + totalWorkHrLog);
                            }
                            ArrayAdapter adapter = new ArrayAdapter(WorkerLogInfo.this, android.R.layout.simple_list_item_1, adapterList);
                            timeLogListView.setAdapter(adapter);
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

