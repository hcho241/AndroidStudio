package com.semirus.dating;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Priority extends Religion{Button submitBtn;
    RadioGroup radioPriority;
    String currentUser, priority = "";
    int id = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.priority);
        // button
        submitBtn = (Button)findViewById(R.id.submitBtn);
        // radio group
        radioPriority = (RadioGroup)findViewById(R.id.radioPriority);
        // get passed value
        Bundle extra = getIntent().getExtras();
        currentUser = extra.getString("currentUser");
        radioPriority.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                id = radioPriority.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.radioAge :
                        priority = "A";
                        break;
                    case R.id.radioDistance :
                        priority = "D";
                        break;
                    case R.id.radioDrink :
                        priority = "DR";
                        break;
                    case R.id.radioSmoke :
                        priority = "S";
                        break;
                    case R.id.radioReligion :
                        priority = "R";
                        break;
                    case R.id.radioHobby :
                        priority = "H";
                        break;
                    default:
                        Toast.makeText(Priority.this, "Please Check One of the above", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPriority();
            }
        });
    }
    public void addPriority(){
        //final String url = "http://192.168.142.14/dating/addPriority.jsp?ID=" + currentUser + "&priority=" + priority;
        final String url = "http://172.30.1.49/dating/addPriority.jsp?ID=" + currentUser + "&priority=" + priority;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected addPriority -> " + response);
                        try {
                            Toast.makeText(Priority.this, "Priority Added", Toast.LENGTH_SHORT).show();
                            // go to login page
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
