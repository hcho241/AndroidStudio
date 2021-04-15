package com.semirus.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText idEditTxt, pwEditTxt;
    Button loginBtn;
    //TextView result;
    static RequestQueue requestQueue;
    String id, pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        idEditTxt = (EditText)findViewById(R.id.idEditTxt);
        pwEditTxt = (EditText)findViewById(R.id.pwEditTxt);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        //result = (TextView)findViewById(R.id.result);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeLogin();
            }
        });
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }
    public void executeLogin(){
        final String ID = idEditTxt.getText().toString();
        final String PW = pwEditTxt.getText().toString();
        //final String url = "http://192.168.142.13/messenger/loginAndroid.jsp?ID=" + ID + "&PW=" + PW;
        final String url = "http://172.30.1.35/messenger/loginAndroid.jsp?ID=" + ID + "&PW=" + PW;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected -> " + response);
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray jArray = json.getJSONArray("Data Sent");
                            for (int i = 0; i < jArray.length(); i++) {
                                json = jArray.getJSONObject(i);
                                id = json.getString("ID");
                                pw = json.getString("PW");
                            }
                            // to see whether the client received the data from server or not
                            String data = "Received Data : " + id + " " + pw;
                            System.out.println(data);
                            //result.setText(data + "\n");
                            if ((ID.equals(id)) && (PW.equals(pw))) {
                                System.out.println(ID);
                                //result.setText(data + "\n");
                                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                                intent.putExtra("currentUser", id);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "Check ID and/or PW again", Toast.LENGTH_SHORT).show();
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