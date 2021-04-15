package com.semirus.dating;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Religion extends DrinkSmoke{
    Button nextBtn;
    RadioGroup radioReligion;
    String currentUser, religion = "";
    int id = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.religion);
        // button
        nextBtn = (Button)findViewById(R.id.nextBtn);
        // radio group
        radioReligion = (RadioGroup)findViewById(R.id.radioReligion);
        // get passed value
        Bundle extra = getIntent().getExtras();
        currentUser = extra.getString("currentUser");
        radioReligion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                id = radioReligion.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.radioNoReligion :
                        religion = "none";
                        break;
                    case R.id.radioChristian :
                        religion = "christian";
                        break;
                    case R.id.radioBuddhist :
                        religion = "buddhist";
                        break;
                    case R.id.radioCatholic :
                        religion = "catholic";
                        break;
                    case R.id.radioMuslim :
                        religion = "muslim";
                        break;
                    case R.id.radioHindu :
                        religion = "hindu";
                        break;
                    case R.id.radioJewish :
                        religion = "jewish";
                        break;
                    case R.id.radioOther :
                        religion = "other";
                        break;
                    default:
                        Toast.makeText(Religion.this, "Please Check One of the above", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReligion();
            }
        });
    }
    public void addReligion(){
        //final String url = "http://192.168.142.14/dating/addReligion.jsp?ID=" + currentUser + "&religion=" + religion;
        final String url = "http://172.30.1.49/dating/addReligion.jsp?ID=" + currentUser + "&religion=" + religion;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected addReligion -> " + response);
                        try {
                            Toast.makeText(Religion.this, "Religion Added", Toast.LENGTH_SHORT).show();
                            // go to login page
                            Intent intent = new Intent(getApplicationContext(), Hobby.class);
                            intent = intent.putExtra("currentUser", currentUser);
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
