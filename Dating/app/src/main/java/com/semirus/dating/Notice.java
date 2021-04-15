package com.semirus.dating;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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

public class Notice extends MainActivity {
    ListView noticeListView;
    Button startBtn;
    // will be used for adapter list
    ArrayList adapterList = new ArrayList();
    ArrayList saveInfo = new ArrayList();
    String title, date, writer, contents = "";
    String selectedNotice, selectedTitle, selectedDate, selectedWriter, selectedContents = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);
        // list view
        noticeListView = (ListView)findViewById(R.id.noticeListView);
        // button
        startBtn = (Button)findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                getNotice();
            }
        });
        startBtn.performClick();
    }
    // get notice list
    public void getNotice(){
        //final String url = "http://192.168.142.14/dating/getNotice.jsp";
        final String url = "http://172.30.1.49/dating/getNotice.jsp";
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected getNotice -> " + response);
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray jsonArray = json.getJSONArray("Data Sent");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                json = jsonArray.getJSONObject(i);
                                title = json.getString("title");
                                date = json.getString("date");
                                writer = json.getString("writer");
                                contents = json.getString("contents");
                                // to see whether the client received the data from server or not
                                String data = "AdapterList Data : " + title + " " + date + " " + writer + " " + contents;
                                System.out.println(data);
                                adapterList.add(title + "  " + date + "  " + writer);
                                saveInfo.add(contents);
                            }
                            ArrayAdapter adapter = new ArrayAdapter(Notice.this, android.R.layout.simple_list_item_1, adapterList);
                            noticeListView.setAdapter(adapter);
                            // selected item of chatList
                            noticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    selectedNotice = noticeListView.getItemAtPosition(position).toString();
                                    String[] arrOfStr = selectedNotice.split("  ");
                                    //System.out.println("array of selectedNotice" + arrOfStr[0]);
                                    selectedTitle = arrOfStr[0];
                                    selectedDate = arrOfStr[1];
                                    selectedWriter = arrOfStr[2];
                                    selectedContents = saveInfo.get(0).toString();
                                    //System.out.println("array of selected contents" + saveInfo.get(0).toString());
                                    Intent intent = new Intent(getApplicationContext(), NoticeInfo.class);
                                    intent.putExtra("selectedTitle", selectedTitle);
                                    intent.putExtra("selectedDate", selectedDate);
                                    intent.putExtra("selectedWriter", selectedWriter);
                                    intent.putExtra("selectedContents", selectedContents);
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
