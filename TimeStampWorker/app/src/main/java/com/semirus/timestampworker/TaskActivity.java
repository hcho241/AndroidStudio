package com.semirus.timestampworker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TaskActivity extends MainActivity{
    TextView taskNameTxtView, taskDateTxtView;
    Button doneBtn, ongoingBtn;
    String selectedTaskName, selectedTaskDate, macAddress, done = "";
    String taskNameTxt, taskDateTxt = "";
    String year, month, day, taskNoSpace = "";
    int index, taskLen = 0 ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);
        // text view
        taskNameTxtView = (TextView) findViewById(R.id.taskNameTxtView);
        taskDateTxtView = (TextView) findViewById(R.id.taskDateTxtView);
        // button
        doneBtn = (Button) findViewById(R.id.doneBtn);
        ongoingBtn = (Button) findViewById(R.id.ongoingBtn);
        // get passed values
        Bundle extra = getIntent().getExtras();
        selectedTaskName = extra.getString("selectedTaskName");
        selectedTaskDate = extra.getString("selectedTaskDate");
        macAddress = extra.getString("macAddress");
        // set text
        taskNameTxt = taskNameTxtView.getText().toString() + " " + selectedTaskName;
        taskNameTxtView.setText(taskNameTxt);
        taskDateTxt = taskDateTxtView.getText().toString() + " " + selectedTaskDate;
        taskDateTxtView.setText(taskDateTxt);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                done = "Y";
                doneBtn.setBackgroundColor(Color.RED);
                addDone();
            }
        });
        ongoingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                done = "N";
                ongoingBtn.setBackgroundColor(Color.RED);
                addDone();
            }
        });
    }
    public void postponeTask(){
        final String url = "https://hcho241.freehongs.net/postponeTask.jsp?macAddress=" + macAddress + "&task=" + taskNoSpace
                + "&date=" + selectedTaskDate + "&year=" + year + "&month=" + month + "&day=" + day;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected postponeTask -> " + response);
                        try {
                            System.out.println("Task postponed");
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
    public void addDone(){
        final String url = "https://hcho241.freehongs.net/addDone.jsp?macAddress=" + macAddress + "&task=" + selectedTaskName +
                "&done=" + done;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected addDone -> " + response);
                        try {
                            System.out.println("Done answer added");
                            if (done.equals("N")){
                                String[] arrOfStr = selectedTaskDate.split("-");
                                year = arrOfStr[0];
                                month = arrOfStr[1];
                                day = arrOfStr[2];
                                taskLen = selectedTaskName.length();
                                for (int i = 0; i < taskLen; i++){
                                    if (selectedTaskName.charAt(i) == ' '){
                                        // find index of space
                                        index = selectedTaskName.indexOf(' ');
                                        taskNoSpace = selectedTaskName.substring(0, index) + "_" + selectedTaskName.substring(index+1);
                                    }
                                    else{
                                        taskNoSpace = task;
                                    }
                                }
                                postponeTask();
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
