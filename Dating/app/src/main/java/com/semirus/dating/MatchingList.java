package com.semirus.dating;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MatchingList extends MainActivity {
    TextView welcomeTxtView, listTxtView;
    Button inboxBtn, logoutBtn, settingBtn, noticeBtn;
    ListView matchingListView;
    // store passed values
    String currentUser, pw, age, prefAge, prefAgeRange, distance, drink, smoke, religion, hobby, priority = "";
    // will be used for adapter list
    ArrayList adapterList = new ArrayList();
    String matchingUser, matchingAge, matchingDistance = "";
    String selectedUser, selectedUserID = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_list);
        // text view
        welcomeTxtView = (TextView)findViewById(R.id.welcomeTxtView);
        listTxtView = (TextView)findViewById(R.id.listTxtView);
        // button
        inboxBtn = (Button)findViewById(R.id.inboxBtn);
        logoutBtn = (Button)findViewById(R.id.logoutBtn);
        settingBtn = (Button)findViewById(R.id.settingBtn);
        noticeBtn = (Button)findViewById(R.id.noticeBtn);
        // list view
        matchingListView = (ListView)findViewById(R.id.matchingListView);
        // get passed values
        Bundle extra = getIntent().getExtras();
        currentUser = extra.getString("currentUser");
        pw = extra.getString("PW");
        sex = extra.getString("sex");
        prefAge = extra.getString("prefAge");
        prefAgeRange = extra.getString("prefAgeRange");
        distance = extra.getString("distance");
        drink = extra.getString("drink");
        smoke = extra.getString("smoke");
        religion = extra.getString("religion");
        hobby = extra.getString("hobby");
        priority = extra.getString("priority");
        System.out.println("matching list bundle values : " + currentUser + " " + pw + " " + sex + " " + prefAge + " " +
                prefAgeRange + " " + distance + " " + drink + " " + smoke + " " + religion + " " + hobby + " " + priority);
        // set textview's text
        welcomeTxtView.setText("Welcome back '" + currentUser + "'");
        listTxtView.setText("******* Matching List *******");
        // get matching list of current user
        getMatchingList();
        // show received message
        inboxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), ReceivedMsg.class);
                intent.putExtra("currentUser", currentUser);
                intent.putExtra("receiver", selectedUserID);
                startActivity(intent);
            }
        });
        // setting
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChangeSetting.class);
                intent.putExtra("currentUser", currentUser);
                intent.putExtra("PW", pw);
                intent.putExtra("sex", sex);
                intent.putExtra("prefAge", prefAge);
                intent.putExtra("prefAgeRange", prefAgeRange);
                intent.putExtra("distance", distance);
                intent.putExtra("drink", drink);
                intent.putExtra("smoke", smoke);
                intent.putExtra("religion", religion);
                intent.putExtra("hobby", hobby);
                intent.putExtra("priority", priority);
                startActivity(intent);
            }
        });
        // go to notice
        noticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Notice.class);
                startActivity(intent);
            }
        });
        // logout & del score table
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delMatching();
            }
        });
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }
    // get matching list
    public void getMatchingList(){
        //final String url = "http://192.168.142.14/dating/getMatchingList.jsp?ID=" + currentUser;
        final String url = "http://172.30.1.49/dating/getMatchingList.jsp?ID=" + currentUser;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected loginMatchingList -> " + response);
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray jsonArray = json.getJSONArray("Data Sent");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                json = jsonArray.getJSONObject(i);
                                matchingUser = json.getString("matchingUser");
                                matchingAge = json.getString("matchingAge");
                                matchingDistance = json.getString("matchingDistance");
                                // to see whether the client received the data from server or not
                                String data = "AdapterList Data : " + matchingUser + " " + matchingAge + " " + matchingDistance;
                                System.out.println(data);
                                adapterList.add(matchingUser + " " + matchingAge + " " + matchingDistance + "km");
                            }
                            ArrayAdapter adapter = new ArrayAdapter(MatchingList.this, android.R.layout.simple_list_item_1, adapterList);
                            matchingListView.setAdapter(adapter);
                            // selected item of chatList
                            matchingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    selectedUser = matchingListView.getItemAtPosition(position).toString();
                                    String[] arrOfStr = selectedUser.split(" ");
                                    //System.out.println("array of selectedUser" + arrOfStr[0]);
                                    selectedUserID = arrOfStr[0];
                                    Intent intent = new Intent(getApplicationContext(), SendMsg.class);
                                    intent.putExtra("currentUser", currentUser);
                                    intent.putExtra("receiver", selectedUserID);
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
    // delete matching info
    public void delMatching(){
        //final String url = "http://192.168.142.14/dating/delMatching.jsp?ID=" + currentUser;
        final String url = "http://172.30.1.49/dating/delMatching.jsp?ID=" + currentUser;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected delMatchingTable -> " + response);
                        try {
                            System.out.println("Matching Table deleted");
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
