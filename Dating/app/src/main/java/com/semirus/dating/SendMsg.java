package com.semirus.dating;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SendMsg extends MatchingList{
    TextView receiverTextView;
    EditText msgEditTxt;
    Button sendBtn, cancelBtn;
    String matchedUser, message, receiverTxt;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_msg);
        receiverTextView = (TextView) findViewById(R.id.receiverTxtView);
        msgEditTxt = (EditText) findViewById(R.id.msgEditTxt);
        sendBtn = (Button) findViewById(R.id.sendBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        // get passed values from MatchingList
        Bundle extra = getIntent().getExtras();
        currentUser = extra.getString("currentUser");
        matchedUser = extra.getString("receiver");
        // show receiver == matchedUser
        receiverTxt = receiverTextView.getText().toString() + matchedUser;
        receiverTextView.setText(receiverTxt);
        // send
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = msgEditTxt.getText().toString();
                sendMsg();
            }
        });
        // go back to matching list
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MatchingList.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });
    }
    public void sendMsg(){
        //final String url = "http://192.168.142.14/dating/sendMsg.jsp?sender=" + currentUser
        //       + "&message=" + message + "&receiver=" + matchedUser;
        final String url = "http://172.30.1.49/dating/sendMsg.jsp?sender=" + currentUser
                + "&message=" + message + "&receiver=" + matchedUser;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected -> " + response);
                        try {
                            System.out.println("Sent Msg!");
                            Toast.makeText(SendMsg.this, "MESSAGE SENT", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MatchingList.class);
                            intent.putExtra("currentUser", currentUser);
                            intent.putExtra("priority", priority);
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
