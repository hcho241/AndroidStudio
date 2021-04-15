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

public class DrinkSmoke extends SignUpActivity{
    Button nextBtn;
    RadioGroup radioDrink, radioSmoke;
    String drink, smoke = "";
    int id = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drink_smoke);
        // radio group
        radioDrink = (RadioGroup)findViewById(R.id.radioDrink);
        radioSmoke = (RadioGroup)findViewById(R.id.radioSmoke);
        // button
        nextBtn = (Button)findViewById(R.id.nextBtn);
        // get passed value
        Bundle extra = getIntent().getExtras();
        currentUser = extra.getString("currentUser");
        radioDrink.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                id = radioDrink.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.radioDrinkYes :
                        drink = "Y";
                        break;
                    case R.id.radioFemale :
                        drink = "N";
                        break;
                    default:
                        Toast.makeText(DrinkSmoke.this, "Please Check One of the above", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        radioSmoke.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                id = radioSmoke.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.radioSmokeYes :
                        smoke = "Y";
                        break;
                    case R.id.radioSmokeNo :
                        smoke = "N";
                        break;
                    default:
                        Toast.makeText(DrinkSmoke.this, "Please Check One of the above", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDrinkSmoke();
            }
        });
    }
    public void addDrinkSmoke(){
        //final String url = "http://192.168.142.14/dating/addDrinkSmoke.jsp?ID=" + currentUser + "&drink=" + drink + "&smoke=" + smoke;
        final String url = "http://172.30.1.49/dating/addDrinkSmoke.jsp?ID=" + currentUser + "&drink=" + drink + "&smoke=" + smoke;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected addDrinkSmoke -> " + response);
                        try {
                            Toast.makeText(DrinkSmoke.this, "ADD DRINK SMOKE", Toast.LENGTH_SHORT).show();
                            // go to ideal setting page
                            Intent intent = new Intent(getApplicationContext(), Religion.class);
                            intent.putExtra("currentUser", currentUser);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
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
