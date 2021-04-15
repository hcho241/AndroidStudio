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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends MainActivity{
    TextView mAddressTxtView, ssidTxtView;
    EditText nameEditTxt;
    Button registerBtn, cancelBtn;
    static RequestQueue requestQueue;
    String macAddress, ssid;
    String mAddressTxt, ssidTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        nameEditTxt = (EditText) findViewById(R.id.nameEditTxt);
        mAddressTxtView = (TextView)findViewById(R.id.mAddressTxtView);
        ssidTxtView = (TextView)findViewById(R.id.ssidTxtView);
        registerBtn = (Button)findViewById(R.id.registerBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);
        // get passed values
        Bundle extra = getIntent().getExtras();
        macAddress = extra.getString("macAddress");
        ssid = extra.getString("ssid");
        // set text view
        mAddressTxt = mAddressTxtView.getText().toString() + macAddress;
        mAddressTxtView.setText(mAddressTxt);
        ssidTxt = ssidTxtView.getText().toString() + ssid;
        ssidTxtView.setText(ssidTxt);

        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                register();
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
    public void register(){
        final String name = nameEditTxt.getText().toString();
        final String url = "https://hcho241.freehongs.net/registerManager.jsp?name=" + name + "&macAddress=" + macAddress + "&ssid=" + ssid;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected register -> " + response);
                        try {
                            System.out.println("Manager Register Complete");
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
