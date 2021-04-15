package com.semirus.volleytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText editText1;
    TextView textView1,textView2,textView3,textView4,textView5;

    static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = findViewById(R.id.editText1);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRequest();
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }

    public void makeRequest() {
        String url = editText1.getText().toString();
        String add = "http://192.168.142.13/login/loginAndroid.jsp?ID=";
        String msg = add+url;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                msg,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {
                        //println("응답 -> " + response1);
                        String response = response1;
                        //textView1.setText(response);
                        try {
                            int index1, index2, index3, index4, index5;
                            //int len, len2, len3, len4;
                            String sender, receiver, send_date, msg, recv_date;
                            index1 = response.indexOf("sender:");
                            index2 = response.indexOf("receiver:");
                            index3 = response.indexOf("send_date:");
                            index4 = response.indexOf("msg:");
                            index5 = response.indexOf("recv_date:");

                            /*
                            len = index2 - (index1 + 7);
                            len2 = index3 - (index2 + 9);
                            len3 = index4 - (index3 + 10);
                            len4 = index5 - (index4 + 4);
                            */

                            // substring(가져오고자 하는 문자열의 시작점의 위치 index, 문자열의 끝 index)
                            // 이 때, 끝점의 index는 전체 문자열에서의 위치값으로 한다.
                            sender = response.substring(index1 + 7, index2);
                            receiver = response.substring(index2 + 9, index3);
                            send_date = response.substring(index3 + 10, index4);
                            msg = response.substring(index4 + 4, index5);
                            recv_date = response.substring(index5 + 10);
                            //sendmsg(Send, Receive, Msg, Send_Date);

                            println(sender, receiver, send_date, msg, recv_date);

                            //textView1.setText(sender);
                            //textView2.setText(receiver);
                            //textView3.setText(send_date);
                            //textView4.setText(msg);
                            //textView5.setText(recv_date);

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("에러 -> " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();

                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
        //println("요청 보냄.");
    }

    public void println(String data) {
        textView1.append(data + "\n");
    }
    public void println(String data1,String data2,String data3,String data4,String data5) {

        textView1.append(data1 + "\n");
        textView2.append(data2 + "\n");
        textView3.append(data3 + "\n");
        textView4.append(data4 + "\n");
        textView5.append(data5 + "\n");
    }
}
