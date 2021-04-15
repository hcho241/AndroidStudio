package com.semirus.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import java.util.List;
import java.util.Map;

public class ReceivedMsg extends ListActivity {
    TextView senderTxtView, subjectTxtView, msgTxtView;  // change variable name
    Button replyBtn, delBtn, backBtn;
    String msgID, msgSubject, msgSender, msgDate, message, receiver, reSub, selectedMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.received_msg);
        senderTxtView = (TextView) findViewById(R.id.senderTxtView);
        subjectTxtView = (TextView) findViewById(R.id.subjectTxtView);
        msgTxtView = (TextView) findViewById(R.id.msgTxtView);
        replyBtn = (Button) findViewById(R.id.replyBtn);
        delBtn = (Button) findViewById(R.id.delBtn);
        backBtn = (Button) findViewById(R.id.backBtn);

        // get msgInfo from ListActivity
        Bundle extra = getIntent().getExtras();
        currentUser = extra.getString("currentUser");
        // msgID
        msgID = extra.getString("msgID");
        System.out.println("pass to receivedMsg " + selectedMsg);
        // show subject
        msgSubject = extra.getString("subject");
        String title = subjectTxtView.getText().toString() + msgSubject;
        subjectTxtView.setText(title);
        // get date
        msgDate = extra.getString("date");
        // show sender
        msgSender = extra.getString("sender");
        String sentPerson = senderTxtView.getText().toString() + msgSender;
        senderTxtView.setText(sentPerson);
        // show message
        message = extra.getString("message");
        String content = msgTxtView.getText().toString() + message;
        msgTxtView.setText(content);
        // receiver
        receiver = extra.getString("receiver");
        // reply
        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // subject : [RE:] + subject
                reSub = "RE:" + msgSubject;
                // new receiver : sender
                Intent intent = new Intent(getApplicationContext(), ReplyMsg.class);
                intent.putExtra("currentUser", currentUser);
                intent.putExtra("reSubject", reSub);
                intent.putExtra("receiver", msgSender);
                startActivity(intent);
            }
        });
        // delete
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delMsg();
            }
        });
        // back
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });
    }
    public void delMsg(){
        final String ID = currentUser;
        //msgID, subject, date, sender, message, receiver
        //final String url = "http://192.168.142.13/messenger/delMsg.jsp?msgID=" + msgID;
        final String url = "http://172.30.1.35/messenger/delMsg.jsp?msgID=" + msgID;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected -> " + response);
                        try {
                            System.out.println("Msg Successfully Deleted");
                            Toast.makeText(ReceivedMsg.this, "MESSAGE DELETED", Toast.LENGTH_SHORT).show();
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
