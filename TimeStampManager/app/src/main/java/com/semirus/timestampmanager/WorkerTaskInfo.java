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

public class WorkerTaskInfo extends WorkerLogInfo{
    TextView nameTxtView, taskTxtView;
    ListView taskListView;
    String workerName, workerMAC, task, done, taskDate = "";
    String nameTxt = "";
    // adapter list
    ArrayList adapterList = new ArrayList();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_task_info);
        // text view
        nameTxtView = (TextView)findViewById(R.id.nameTxtView);
        taskTxtView = (TextView)findViewById(R.id.taskTxtView);
        // list view
        taskListView = (ListView)findViewById(R.id.taskListView);
        // get passed values
        Bundle extra = getIntent().getExtras();
        workerName = extra.getString("workerName");
        workerMAC = extra.getString("workerMAC");
        // set text view
        nameTxt = nameTxtView.getText().toString() + workerName;
        nameTxtView.setText(nameTxt);
        getTaskList();
    }
    public void getTaskList(){
        final String url = "https://hcho241.freehongs.net/getAllTaskList.jsp?macAddress=" + workerMAC;
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
                                taskDate = json.getString("date");
                                task = json.getString("task");
                                done = json.getString("done");
                                if (done.equals("N")){
                                    done = "In progress";
                                }
                                else {
                                    done = "Complete";
                                }
                                // to see whether the client received the data from server or not
                                String data = "AdapterList Data : " + taskDate + " " + task + " " + done;
                                System.out.println(data);
                                adapterList.add(taskDate + " " + task + " " + done);
                            }
                            ArrayAdapter adapter = new ArrayAdapter(WorkerTaskInfo.this, android.R.layout.simple_list_item_1, adapterList);
                            taskListView.setAdapter(adapter);
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
