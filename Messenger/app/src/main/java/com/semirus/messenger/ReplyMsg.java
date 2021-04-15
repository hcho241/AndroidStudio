package com.semirus.messenger;

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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReplyMsg extends ReceivedMsg{
    TextView reSubjectTxtView, reReceiverTxtView;
    EditText msgReplyEditTxt;
    Button sendBtn, cancelBtn;
    String replySubject, newReceiver, reply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply_msg);
        reSubjectTxtView = (TextView) findViewById(R.id.reSubjectTxtView);
        reReceiverTxtView = (TextView) findViewById(R.id.reReceiverTxtView);
        msgReplyEditTxt = (EditText) findViewById(R.id.msgReplyEditTxt);
        sendBtn = (Button) findViewById(R.id.sendBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        // subject = [RE:] + subject
        Bundle extra = getIntent().getExtras();
        currentUser = extra.getString("currentUser");
        // reSubject
        replySubject = extra.getString("reSubject");
        reSubjectTxtView.setText("Subject : " + replySubject);
        // receiver = sender
        newReceiver = extra.getString("receiver");
        reReceiverTxtView.setText("Receiver: " + newReceiver);
        // send msg
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // msg written on EditText area becomes msg
                reply = msgReplyEditTxt.getText().toString();
                replyMsg();
            }
        });
        // cancel
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });
    }
    public void replyMsg(){
        final String ID = currentUser;
        //subject, sender, message, receiver
        //final String url = "http://192.168.142.13/messenger/addMsg.jsp?subject=" + replySubject + "&sender=" + currentUser
          //      + "&message=" + reply + "&receiver=" + newReceiver;
        final String url = "http://172.30.1.35/messenger/addMsg.jsp?subject=" + replySubject + "&sender=" + currentUser
                + "&message=" + reply + "&receiver=" + newReceiver;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected -> " + response);
                        try {
                            System.out.println("Reply Msg Successfully Added");
                            Toast.makeText(ReplyMsg.this, "SENT REPLY", Toast.LENGTH_SHORT).show();
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
