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

public class ReplyMsg extends ReceivedMsg{
    TextView receiverTxtView, receivedMsgTxtView, sentDateTxtView;
    EditText msgEditTxt;
    Button replyBtn, delBtn, cancelBtn;
    String receiverTxt, receivedMsgTxt, sentDateTxt,  reply;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply_msg);
        // text view
        receivedMsgTxtView = (TextView) findViewById(R.id.receivedMsgTxtView);
        sentDateTxtView = (TextView) findViewById(R.id.sentDateTxtView);
        receiverTxtView = (TextView) findViewById(R.id.receiverTxtView);
        // edit text
        msgEditTxt = (EditText) findViewById(R.id.msgEditTxt);
        // button
        replyBtn = (Button) findViewById(R.id.replyBtn);
        delBtn = (Button) findViewById(R.id.delBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        // get passed values from LoginMatchingList
        Bundle extra = getIntent().getExtras();
        currentUser = extra.getString("currentUser");
        // will be used in this class
        msgID = extra.getString("msgID");
        sender = extra.getString("sender");
        date = extra.getString("date");
        message = extra.getString("message");
        // display received message
        receivedMsgTxt = receivedMsgTxtView.getText().toString() + message;
        receivedMsgTxtView.setText(receivedMsgTxt);
        // display sent date
        sentDateTxt = sentDateTxtView.getText().toString() + date;
        sentDateTxtView.setText(sentDateTxt);
        // show receiver == sender
        receiverTxt = receiverTxtView.getText().toString() + sender;
        receiverTxtView.setText(receiverTxt);
        // reply
        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reply = msgEditTxt.getText().toString();
                replyMsg();
            }
        });
        // delete
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delMsg();
            }
        });
        // go back to message list
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReceivedMsg.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });
    }
    // reply message
    public void replyMsg(){
        //final String url = "http://192.168.142.14/dating/sendMsg.jsp?sender=" + currentUser + "&message=" + reply
        //      + "&receiver=" + sender;
        final String url = "http://172.30.1.49/dating/sendMsg.jsp?sender=" + currentUser + "&message=" + reply
                + "&receiver=" + sender;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected -> " + response);
                        try {
                            System.out.println("Reply Msg Successfully Added");
                            Toast.makeText(ReplyMsg.this, "SENT REPLY", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MatchingList.class);
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
    // delete message
    public void delMsg(){
        //final String url = "http://192.168.142.14/dating/delMsg.jsp?msgID=" + msgID;
        final String url = "http://172.30.1.49/dating/delMsg.jsp?msgID=" + msgID;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected -> " + response);
                        try {
                            System.out.println("Msg Successfully Deleted");
                            Toast.makeText(ReplyMsg.this, "DELETE MESSAGE", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ReceivedMsg.class);
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
