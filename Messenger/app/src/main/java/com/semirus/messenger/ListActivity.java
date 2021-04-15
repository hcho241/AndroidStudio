package com.semirus.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListActivity extends MainActivity{
    TextView welcomeTxtView;
    Button sendBtn, logoutBtn;
    ListView msgList;
    ArrayList adapterList = new ArrayList();
    String currentUser, msgID, subject, date, sender, message, receiver, selectedMsg;
    String msgContent = ""; // to avoid printing 'null'
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list);
        welcomeTxtView = (TextView)findViewById(R.id.welcomeTxtView);
        sendBtn = (Button)findViewById(R.id.sendBtn);
        logoutBtn = (Button)findViewById(R.id.logoutBtn);
        msgList = (ListView)findViewById(R.id.msgList);
        // get username from MainActivity after user login
        Bundle extra = getIntent().getExtras();
        currentUser = extra.getString("currentUser");
        welcomeTxtView.setText("WELCOME BACK USER '" + currentUser +"'");
        // show chat list
        getMsgList();
        // start chat
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SendMsg.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });
        // logout
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void getMsgList(){
        final String ID = currentUser;
        //final String url = "http://192.168.142.13/messenger/getMsgList.jsp?ID=" + ID;
        final String url = "http://172.30.1.35/messenger/getMsgList.jsp?ID=" + ID;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected -> " + response);
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray jsonArray = json.getJSONArray("Data Sent");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                json = jsonArray.getJSONObject(i);
                                msgID = json.getString("msgID");
                                subject = json.getString("subject");
                                date = json.getString("date");
                                sender = json.getString("sender");
                                message = json.getString("message");
                                receiver = json.getString("receiver");
                                adapterList.add(msgID + " " + subject + " " + date + " " + sender + " " + message);
                            }
                            // to see whether the client received the data from server or not
                            String data = "Received Data : " + msgID + " " + subject + " " + date + " " + sender + " " + message;
                            System.out.println(data);
                           // if ((ID.equals(receiver))) {
                               // System.out.println(ID);
                                ArrayAdapter adapter = new ArrayAdapter(ListActivity.this, android.R.layout.simple_list_item_1, adapterList);
                                msgList.setAdapter(adapter);
                                // selected item of chatList
                                msgList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        selectedMsg = msgList.getItemAtPosition(position).toString();
                                        String[] arrOfStr = selectedMsg.split(" ");
                                        System.out.println("array " + arrOfStr);
                                        msgID = arrOfStr[0];
                                        subject = arrOfStr[1];
                                        date = arrOfStr[2] + arrOfStr[3];
                                        sender = arrOfStr[4];
                                        for (int i = 5; i < arrOfStr.length; i++){
                                            System.out.println("MSG : " + arrOfStr[i]);
                                            msgContent += arrOfStr[i];
                                            msgContent += " ";
                                            message = msgContent;
                                        }
                                        Intent intent = new Intent(getApplicationContext(), ReceivedMsg.class);
                                        intent.putExtra("currentUser", currentUser);
                                        intent.putExtra("msgID", msgID);
                                        intent.putExtra("subject", subject);
                                        intent.putExtra("sender", sender);
                                        intent.putExtra("message", message);
                                        intent.putExtra("receiver", currentUser); // id == receiver
                                        startActivity(intent);
                                    }
                                });
                           // } else {
                             //   Toast.makeText(ListActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                           // }
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
