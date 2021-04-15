package com.semirus.dating;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.slider.RangeSlider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends MainActivity{
    TextView sexPrefTxtView;
    EditText idEditTxt, pwEditTxt;
    Button earlyBtn, midBtn, lastBtn, earlyPrefBtn, midPrefBtn, lastPrefBtn, nextBtn;
    RadioGroup radioSex, radioAge, radioPrefAge;
    RangeSlider distRangeSlider;
    static RequestQueue requestQueue;
    String sex, prefSex, sexPrefTxt, age, ageRange, prefAge, prefAgeRange, distance = "";
    int id, distanceInt = 0;
    ArrayList<Float> distValues = new ArrayList<Float>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        // text view
        sexPrefTxtView = (TextView)findViewById(R.id.sexPrefTxtView);
        // edit text
        idEditTxt = (EditText)findViewById(R.id.idEditTxt);
        pwEditTxt = (EditText)findViewById(R.id.pwEditTxt);
        // button
        earlyBtn = (Button)findViewById(R.id.earlyBtn);
        midBtn = (Button)findViewById(R.id.midBtn);
        lastBtn = (Button)findViewById(R.id.lastBtn);
        earlyPrefBtn = (Button)findViewById(R.id.earlyPrefBtn);
        midPrefBtn = (Button)findViewById(R.id.midPrefBtn);
        lastPrefBtn = (Button)findViewById(R.id.lastPrefBtn);
        nextBtn = (Button)findViewById(R.id.nextBtn);
        // radio group
        radioSex = (RadioGroup)findViewById(R.id.radioSex);
        radioAge = (RadioGroup)findViewById(R.id.radioAge);
        radioPrefAge = (RadioGroup)findViewById(R.id.radioPrefAge);
        // range slider
        distRangeSlider = (RangeSlider)findViewById(R.id.distRangeSlider);
        radioSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                id = radioSex.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.radioMale :
                        sex = "M";
                        prefSex = "Female";
                        sexPrefTxt = sexPrefTxtView.getText().toString() + " " + prefSex;
                        sexPrefTxtView.setText(sexPrefTxt);
                        //sexPrefTxtView.setText(prefSex);
                        break;
                    case R.id.radioFemale :
                        sex = "F";
                        prefSex = "Male";
                        sexPrefTxt = sexPrefTxtView.getText().toString() + " " + prefSex;
                        sexPrefTxtView.setText(sexPrefTxt);
                        //sexPrefTxtView.setText(prefSex);
                        break;
                    default:
                        Toast.makeText(SignUpActivity.this, "Please Check One of the above", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        radioAge.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                id = radioAge.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.radio20 :
                        age = "20";
                        break;
                    case R.id.radio30 :
                        age = "30";
                        break;
                    case R.id.radio40 :
                        age = "40";
                        break;
                    case R.id.radio50 :
                        age = "50";
                        break;
                    default:
                        Toast.makeText(SignUpActivity.this, "Please Check One of the above", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        earlyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                earlyBtn.setBackgroundColor(Color.RED);
                ageRange = "early";
            }
        });
        midBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                midBtn.setBackgroundColor(Color.RED);
                ageRange = "mid";
            }
        });
        lastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastBtn.setBackgroundColor(Color.RED);
                ageRange = "late";
            }
        });
        // ========== Preference ==========
        radioPrefAge.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                id = radioPrefAge.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.radioPref20 :
                        prefAge = "20";
                        break;
                    case R.id.radioPref30 :
                        prefAge = "30";
                        break;
                    case R.id.radioPref40 :
                        prefAge = "40";
                        break;
                    case R.id.radioPref50 :
                        prefAge = "50";
                        break;
                    default:
                        Toast.makeText(SignUpActivity.this, "Please Check One of the above", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        earlyPrefBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                earlyPrefBtn.setBackgroundColor(Color.RED);
                prefAgeRange = "early";
            }
        });
        midPrefBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                midPrefBtn.setBackgroundColor(Color.RED);
                prefAgeRange = "mid";
            }
        });
        lastPrefBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastPrefBtn.setBackgroundColor(Color.RED);
                prefAgeRange = "late";
            }
        });
        distRangeSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                //System.out.println("start tracking " + slider.getValues());
            }
            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                System.out.println("dist range stop tracking " + slider.getValues());
                distValues = (ArrayList<Float>) slider.getValues();
                distanceInt = Math.round(distValues.get(1));
                distance = String.valueOf(distanceInt);
                System.out.println("max distance " + distance);
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }
    public void signUp(){
        final String ID = idEditTxt.getText().toString();
        final String PW = pwEditTxt.getText().toString();
        /*
        final String url = "http://192.168.142.14/dating/signUp.jsp?ID=" + ID + "&PW=" + PW +
                "&sex=" + sex + "&age=" + age + "&ageRange=" + ageRange + "&prefAge=" + prefAge + "&prefAgeRange=" + prefAgeRange +
                "&distance=" + distance;
         */
        final String url = "http://172.30.1.49/dating/signUp.jsp?ID="  + ID + "&PW=" + PW +
                "&sex=" + sex + "&age=" + age + "&ageRange=" + ageRange + "&prefAge=" + prefAge + "&prefAgeRange=" + prefAgeRange +
                "&distance=" + distance;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Connected signUp -> " + response);
                try {
                    Toast.makeText(SignUpActivity.this, "SIGN UP COMPLETE", Toast.LENGTH_SHORT).show();
                    // go to ideal setting page
                    Intent intent = new Intent(getApplicationContext(), DrinkSmoke.class);
                    intent.putExtra("currentUser", ID);
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
