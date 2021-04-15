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

public class Hobby extends Religion{
    Button nextBtn;
    RadioGroup radioHobby;
    String currentUser, hobby = "";
    int id = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hobby);
        // radio group
        radioHobby = (RadioGroup)findViewById(R.id.radioHobby);
        // button
        nextBtn = (Button)findViewById(R.id.nextBtn);
        // get passed value
        Bundle extra = getIntent().getExtras();
        currentUser = extra.getString("currentUser");
        radioHobby.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                id = radioHobby.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.radioPhotography :
                        hobby = "photography";
                        break;
                    case R.id.radioCooking :
                        hobby = "cooking";
                        break;
                    case R.id.radioDrawing :
                        hobby = "drawing";
                        break;
                    case R.id.radioHiking :
                        hobby = "hiking";
                        break;
                    case R.id.radioDancing :
                        hobby = "dancing";
                        break;
                    case R.id.radioSinging :
                        hobby = "singing";
                        break;
                    case R.id.radioVideoGame :
                        hobby = "video game";
                        break;
                    case R.id.radioOther :
                        hobby = "other";
                        break;
                    default:
                        Toast.makeText(Hobby.this, "Please Check One of the above", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHobby();
            }
        });
    }
    public void addHobby(){
        //final String url = "http://192.168.142.14/dating/addHobby.jsp?ID=" + currentUser + "&hobby=" + hobby;
        final String url = "http://172.30.1.49/dating/addHobby.jsp?ID=" + currentUser + "&hobby=" + hobby;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected addReligion -> " + response);
                        try {
                            Toast.makeText(Hobby.this, "Hobby Added", Toast.LENGTH_SHORT).show();
                            // go to login page
                            Intent intent = new Intent(getApplicationContext(), Priority.class);
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
