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

import java.util.HashMap;
import java.util.Map;

public class AddWorker extends RegisterActivity{
    EditText nameEditTxt, mAddressEditTxt;
    TextView ssidTxtView;
    Button registerBtn, cancelBtn;
    static RequestQueue requestQueue;
    String ssidTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_worker);
        nameEditTxt = (EditText) findViewById(R.id.nameEditTxt);
        mAddressEditTxt = (EditText)findViewById(R.id.mAddressEditTxt);
        ssidTxtView = (TextView)findViewById(R.id.ssidTxtView);
        registerBtn = (Button)findViewById(R.id.registerBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);
        // get passed values
        Bundle extra = getIntent().getExtras();
        ssid = extra.getString("ssid");
        // set text view
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
        final String mAddress = mAddressEditTxt.getText().toString();
        final String url = "https://hcho241.freehongs.net/registerWorker.jsp?name=" + name + "&macAddress=" + mAddress + "&ssid=" + ssid;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected checkRegistration -> " + response);
                        try {
                            System.out.println("Worker Register Complete");
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
