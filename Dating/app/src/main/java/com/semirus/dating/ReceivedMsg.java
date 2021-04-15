package com.semirus.dating;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReceivedMsg extends MatchingList {
    TextView receivedMsgTxtView;
    Button backBtn;
    ListView msgList;
    // will be used in getReceivedMsg
    ArrayList adapterList = new ArrayList();
    String msgID, sender, date, message;
    String selectedMsg;
    String msgContent = ""; // to avoid printing 'null'
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.received_msg);
        // text view
        receivedMsgTxtView = (TextView) findViewById(R.id.receivedMsgTxtView);
        // button
        backBtn = (Button) findViewById(R.id.backBtn);
        // list view
        msgList = (ListView)findViewById(R.id.msgList);
        // get username from MatchingList
        Bundle extra = getIntent().getExtras();
        currentUser = extra.getString("currentUser");
        // set textview's text
        receivedMsgTxtView.setText(currentUser + "'s INBOX");
        // get received msg
        getReceivedMsg();
        // back to matching list
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MatchingList.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });
    }
    public void getReceivedMsg(){
        //final String url = "http://192.168.142.14/dating/getReceivedMsg.jsp?receiver=" + currentUser;
        final String url = "http://172.30.1.49/dating/getReceivedMsg.jsp?receiver=" + currentUser;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected -> " + response);
                        try {
                            System.out.println("Get Received Msg");
                            JSONObject json = new JSONObject(response);
                            JSONArray jsonArray = json.getJSONArray("Data Sent");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                json = jsonArray.getJSONObject(i);
                                msgID = json.getString("msgID");
                                sender = json.getString("sender");
                                date = json.getString("date");
                                message = json.getString("message");
                                date = date.substring(0, 19);
                                //System.out.println("new date is " + date);
                                adapterList.add(sender + "   " + date + "   " + message);
                            }
                            // to see whether the client received the data from server or not
                            String data = "Received Msg Info : " + msgID + " " + sender + " " + date + " " +  message;
                            System.out.println(data);
                            ArrayAdapter adapter = new ArrayAdapter(ReceivedMsg.this, android.R.layout.simple_list_item_1, adapterList);
                            msgList.setAdapter(adapter);
                            // selected item of msgList
                            msgList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    selectedMsg = msgList.getItemAtPosition(position).toString();
                                    String[] arrOfStr = selectedMsg.split(" ");
                                    System.out.println("array " + arrOfStr);
                                    sender = arrOfStr[0];
                                    date = arrOfStr[3] + " " + arrOfStr[4];
                                    System.out.println("DATE : " + date);
                                    for (int i = 6; i < arrOfStr.length; i++){
                                        System.out.println("MSG : " + arrOfStr[i]);
                                        msgContent += arrOfStr[i];
                                        msgContent += " ";
                                        message = msgContent;
                                    }
                                    System.out.println("MESSAGE : " + message);
                                    Intent intent = new Intent(getApplicationContext(), ReplyMsg.class);
                                    intent.putExtra("currentUser", currentUser);
                                    intent.putExtra("msgID", msgID);
                                    intent.putExtra("sender", sender);
                                    intent.putExtra("date", date);
                                    intent.putExtra("message", message);
                                    startActivity(intent);
                                }
                            });
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
