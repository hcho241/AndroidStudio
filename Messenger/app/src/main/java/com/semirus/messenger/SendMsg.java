package com.semirus.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendMsg extends ListActivity {
    EditText msgReceiverEditTxt, msgSubjectEditTxt, msgEditTxt;
    Button sendBtn, cancel;
    String subject, message, receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_msg);
        msgReceiverEditTxt = (EditText) findViewById(R.id.msgReceiverEditTxt);
        msgSubjectEditTxt = (EditText) findViewById(R.id.msgSubjectEditTxt);
        msgEditTxt = (EditText) findViewById(R.id.msgEditTxt);
        sendBtn = (Button) findViewById(R.id.sendBtn);
        cancel = (Button) findViewById(R.id.cancel);
        // get username from ListActivity
        Bundle extra = getIntent().getExtras();
        currentUser = extra.getString("currentUser");
        // send
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiver = msgReceiverEditTxt.getText().toString();
                subject = msgSubjectEditTxt.getText().toString();
                message = msgEditTxt.getText().toString();
                addMsg();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });
    }
    public void addMsg(){
        final String ID = currentUser;
        //msgID, subject, date, sender, message, receiver
        //final String url = "http://192.168.142.13/messenger/addMsg.jsp?subject=" + subject + "&sender=" + currentUser
         //       + "&message=" + message + "&receiver=" + receiver;
        final String url = "http://172.30.1.35/messenger/addMsg.jsp?subject=" + subject + "&sender=" + currentUser
                + "&message=" + message + "&receiver=" + receiver;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected -> " + response);
                        try {
                            System.out.println("Sent Msg Successfully Added");
                            Toast.makeText(SendMsg.this, "MESSAGE SENT", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                            intent.putExtra("currentUser", currentUser);
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
