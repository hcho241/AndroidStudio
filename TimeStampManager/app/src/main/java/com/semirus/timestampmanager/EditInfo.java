package com.semirus.timestampmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class EditInfo extends MainActivity{
    TextView nameTxtView, originMaddressTxtView, originSSIDtxtView;
    EditText newMaddressEditTxt, newSSIDeditTxt;
    Button editBtn, delBtn, cancelBtn;
    static RequestQueue requestQueue;
    String originMaddress, originSSID, macAddress, ssid;
    String nameTxt, mAddressTxt, ssidTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_info);
        originMaddressTxtView = (TextView) findViewById(R.id.originMaddressTxtView);
        originSSIDtxtView = (TextView) findViewById(R.id.originSSIDtxtView);
        nameTxtView = (TextView) findViewById(R.id.nameTxtView);
        newMaddressEditTxt = (EditText)findViewById(R.id.newMaddressEditTxt);
        newSSIDeditTxt = (EditText)findViewById(R.id.newSSIDeditTxt);
        editBtn = (Button)findViewById(R.id.editBtn);
        delBtn = (Button)findViewById(R.id.delBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);
        // get passed values
        Bundle extra = getIntent().getExtras();
        name = extra.getString("name");
        macAddress = extra.getString("macAddress");
        ssid = extra.getString("ssid");
        // set text view
        nameTxt = nameTxtView.getText().toString() + name;
        nameTxtView.setText(nameTxt);
        originMaddress = originMaddressTxtView.getText().toString() + macAddress;
        originMaddressTxtView.setText(originMaddress);
        originSSID = originSSIDtxtView.getText().toString() + ssid;
        originSSIDtxtView.setText(originSSID);
        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (newMaddressEditTxt.getText().toString().equals("")) {
                    mAddressTxt = macAddress;
                }
                else{
                    mAddressTxt = newMaddressEditTxt.getText().toString();
                }
                if (newSSIDeditTxt.getText().toString().equals("")) {
                    ssidTxt = ssid;
                }
                else{
                    ssidTxt = newSSIDeditTxt.getText().toString();
                }
                updateManager();
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delManager();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }
    public void delManager(){
        final String url = "https://hcho241.freehongs.net/delManager.jsp?macAddress=" + macAddress;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected del Manager -> " + response);
                        try {
                            System.out.println("Manager info deleted");
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
    // 2) then change worker's ssid
    public void updateWorker(){
        final String url = "https://hcho241.freehongs.net/updateWorker.jsp?originSSID=" + ssid + "&newSSID=" + ssidTxt;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected workerUpdate -> " + response);
                        try {
                            System.out.println("Worker info updated");
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
    // 1) change manager's info first
    public void updateManager(){
        final String url = "https://hcho241.freehongs.net/updateManager.jsp?name=" + name + "&originMacAddress=" + macAddress + "&newMacAddress=" + mAddressTxt +
                "&originSSID=" + ssid + "&newSSID=" + ssidTxt;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected managerUpdate -> " + response);
                        try {
                            System.out.println("Manager info updated");
                            // now update worker's ssid
                            updateWorker();
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
