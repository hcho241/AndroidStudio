package com.semirus.timestampmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class EditWorkHr extends MainActivity{
    TextView nameTxtView, originMaddressTxtView, originTimeINtxtView, originTimeOUTtxtView;
    EditText newMaddressEditTxt, newTimeInEditTxt, newTimeOutEditTxt;
    Button editBtn, delBtn, cancelBtn;
    String nameTxt, originMaddressTxt, originTimeINtxt, originTimeOUTtxt, newTimeIN, newTimeOUT, newMacAddress = "";
    String workerName, macAddress, date, timeIN, timeOUT = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_work_hr);
        nameTxtView = (TextView)findViewById(R.id.nameTxtView);
        originMaddressTxtView = (TextView)findViewById(R.id.originMaddressTxtView);
        originTimeINtxtView = (TextView)findViewById(R.id.originTimeINtxtView);
        originTimeOUTtxtView = (TextView)findViewById(R.id.originTimeOUTtxtView);
        newMaddressEditTxt = (EditText)findViewById(R.id.newMaddressEditTxt);
        newTimeInEditTxt = (EditText)findViewById(R.id.newTimeInEditTxt);
        newTimeOutEditTxt = (EditText)findViewById(R.id.newTimeOutEditTxt);
        editBtn = (Button) findViewById(R.id.editBtn);
        delBtn = (Button) findViewById(R.id.delBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        // get passed values
        Bundle extra = getIntent().getExtras();
        workerName = extra.getString("workerName");
        macAddress = extra.getString("workerMAC");
        date = extra.getString("date");
        timeIN = extra.getString("timeIN");
        timeOUT = extra.getString("timeOUT");
        // set text view
        nameTxt = nameTxtView.getText() + workerName;
        nameTxtView.setText(nameTxt);
        originMaddressTxt = originMaddressTxtView.getText() + macAddress;
        originMaddressTxtView.setText(originMaddressTxt);
        originTimeINtxt = originTimeINtxtView.getText().toString() + timeIN;
        originTimeINtxtView.setText(originTimeINtxt);
        originTimeOUTtxt = originTimeOUTtxtView.getText().toString() + timeOUT;
        originTimeOUTtxtView.setText(originTimeOUTtxt);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newTimeInEditTxt.getText().toString().equals("")){
                    newTimeIN = timeIN;
                }
                else{
                    newTimeIN = newTimeInEditTxt.getText().toString();
                }
                if (newTimeOutEditTxt.getText().toString().equals("")){
                    newTimeOUT = timeOUT;
                }
                else{
                    newTimeOUT = newTimeOutEditTxt.getText().toString();
                }
                if (newMaddressEditTxt.getText().toString().equals("")){
                    newMacAddress = macAddress;
                }
                else{
                    newMacAddress = newMaddressEditTxt.getText().toString();
                }
                updateWorkHr();
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delWorker();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void updateWorkHr(){
        final String url = "https://hcho241.freehongs.net/editWorkHr.jsp?originMaddress=" + macAddress
                + "&newMacAddress=" + newMacAddress + "&date=" + date + "&originTimeIN=" + timeIN + "&newTimeIN="
                + newTimeIN + "&originTimeOUT=" + timeOUT + "&newTimeOUT=" + newTimeOUT;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected updateWorkHr -> " + response);
                        try {
                            System.out.println("Work hr edited");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
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
    public void delWorker(){
        final String url = "https://hcho241.freehongs.net/delWorker.jsp?macAddress=" + macAddress;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected delWorker -> " + response);
                        try {
                            System.out.println("Delete worker");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
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
