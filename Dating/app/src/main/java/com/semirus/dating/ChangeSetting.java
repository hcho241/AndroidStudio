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

public class ChangeSetting extends MatchingList{
    Button earlyPrefBtn, midPrefBtn, lastPrefBtn, submitBtn;
    RadioGroup radioPrefAge, radioDrink, radioSmoke, radioReligion, radioHobby, radioPriority;
    RangeSlider distRangeSlider;
    String currentUser, pw, sex, prefAge, prefAgeRange, distance, drink, smoke, religion, hobby, priority = "";
    static RequestQueue requestQueue;
    int id, distanceInt = 0;
    ArrayList<Float> distValues = new ArrayList<Float>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_setting);
        // button
        earlyPrefBtn = (Button)findViewById(R.id.earlyPrefBtn);
        midPrefBtn = (Button)findViewById(R.id.midPrefBtn);
        lastPrefBtn = (Button)findViewById(R.id.lastPrefBtn);
        submitBtn = (Button)findViewById(R.id.submitBtn);
        // radio group
        radioPrefAge = (RadioGroup)findViewById(R.id.radioPrefAge);
        radioDrink = (RadioGroup)findViewById(R.id.radioDrink);
        radioSmoke = (RadioGroup)findViewById(R.id.radioSmoke);
        radioReligion = (RadioGroup)findViewById(R.id.radioReligion);
        radioHobby = (RadioGroup)findViewById(R.id.radioHobby);
        radioPriority = (RadioGroup)findViewById(R.id.radioPriority);
        // range slider
        distRangeSlider = (RangeSlider)findViewById(R.id.distRangeSlider);
        // get username from MatchingList
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
        System.out.println("change setting bundle values : " + currentUser + " " + pw + " " + sex + " " + prefAge + " " +
                prefAgeRange + " " + distance + " " + drink + " " + smoke + " " + religion + " " + hobby + " " + priority);
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
                        Toast.makeText(ChangeSetting.this, "Please Check One of the above", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ChangeSetting.this, "Please Check One of the above", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ChangeSetting.this, "Please Check One of the above", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
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
                        Toast.makeText(ChangeSetting.this, "Please Check One of the above", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
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
                        Toast.makeText(ChangeSetting.this, "Please Check One of the above", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
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
                        Toast.makeText(ChangeSetting.this, "Please Check One of the above", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSetting();
            }
        });
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }
    public void addMatchingScores(){
        /*
        final String url = "http://192.168.142.14/dating/addMatchingScore.jsp?ID=" + currentUser + "&age=" + age +
                "&ageRange=" + ageRange + "&prefAge=" + prefAge + "&prefAgeRange=" + prefAgeRange +"&latitude=" + latitude +
                "&longitude=" + longitude + "&distance=" + distance + "&drink=" + drink + "&smoke=" + smoke + "&religion=" + religion +
                "&hobby=" + hobby + "&priority="+ priority + "&matchedUserID=" + matchedUserID + "&matchedAge=" + matchedAge +
                "&matchedAgeRange=" + matchedAgeRange + "&matchedLatitude=" + matchedLatitude + "&matchedLongitude=" + matchedLongitude +
                "&matchedDrink=" + matchedDrink + "&matchedSmoke=" + matchedSmoke + "&matchedReligion=" + matchedReligion + "&matchedHobby=" + matchedHobby;
        */
        final String url = "http://172.30.1.49/dating/addMatchingScore.jsp?ID=" +  currentUser + "&age=" + age +
                "&ageRange=" + ageRange + "&prefAge=" + prefAge + "&prefAgeRange=" + prefAgeRange +"&latitude=" + latitude +
                "&longitude=" + longitude + "&distance=" + distance + "&drink=" + drink + "&smoke=" + smoke + "&religion=" + religion +
                "&hobby=" + hobby + "&priority="+ priority + "&matchedUserID=" + matchedUserID + "&matchedAge=" + matchedAge +
                "&matchedAgeRange=" + matchedAgeRange + "&matchedLatitude=" + matchedLatitude + "&matchedLongitude=" + matchedLongitude +
                "&matchedDrink=" + matchedDrink + "&matchedSmoke=" + matchedSmoke + "&matchedReligion=" + matchedReligion + "&matchedHobby=" + matchedHobby;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected Update Currentuser Info addMatchingScore -> " + response);
                        try {
                            System.out.println("added matching score");
                            //addAutoMatchingList();
                            Intent intent = new Intent(getApplicationContext(), MatchingList.class);
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

    public void getOtherUserInfo(){
        //final String url = "http://192.168.142.14/dating/getOtherUserInfo.jsp?ID=" + currentUser + "&sex=" + sex;
        final String url = "http://172.30.1.49/dating/getOtherUserInfo.jsp?ID=" + currentUser + "&sex=" + sex;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected Update Currentuser Info getOtherUserInfo FD -> " + response);
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray jsonArray = json.getJSONArray("Data Sent");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                json = jsonArray.getJSONObject(i);
                                matchedUserID = json.getString("ID");
                                matchedAge = json.getString("age");
                                matchedAgeRange = json.getString("ageRange");
                                matchedLatitude = json.getString("latitude");
                                matchedLongitude = json.getString("longitude");
                                matchedDrink = json.getString("drink");
                                matchedSmoke = json.getString("smoke");
                                matchedReligion = json.getString("religion");
                                matchedHobby = json.getString("hobby");
                                addMatchingScores();
                            }
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

    public void delMatching(){
        //final String url = "http://192.168.142.14/dating/delMatching.jsp?ID=" + currentUser;
        final String url = "http://172.30.1.49/dating/delMatching.jsp?ID=" + currentUser;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected Update Currentuser Info delMatchingTable -> " + response);
                        try {
                            System.out.println("Matching Table deleted");
                            getOtherUserInfo();
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

    public void getCurrentUserInfo(){
        //final String url = "http://192.168.142.14/dating/loginAndroid.jsp?ID=" + currentUser + "&PW=" + pw;
        final String url = "http://172.30.1.49/dating/loginAndroid.jsp?ID=" + currentUser + "&PW=" + pw;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected Update Setting Currentuser Info -> " + response);
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray jArray = json.getJSONArray("Data Sent");
                            for (int i = 0; i < jArray.length(); i++) {
                                json = jArray.getJSONObject(i);
                                currentUser = json.getString("ID");
                                pw = json.getString("PW");
                                sex = json.getString("sex");
                                age = json.getString("age");
                                ageRange = json.getString("ageRange");
                                prefAge = json.getString("prefAge");
                                prefAgeRange = json.getString("prefAgeRange");
                                latitude = json.getString("latitude");
                                longitude = json.getString("longitude");
                                distance = json.getString("distance");
                                drink = json.getString("drink");
                                smoke = json.getString("smoke");
                                religion = json.getString("religion");
                                hobby = json.getString("hobby");
                                priority = json.getString("priority");
                            }
                            delMatching();
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

    public void updateSetting(){
        /*
        final String url = "http://192.168.142.14/dating/updatePref.jsp?ID=" + currentUser + "&PW=" + pw +
                "&prefAge=" + prefAge + "&prefAgeRange=" + prefAgeRange + "&distance=" + distance +
                "&drink=" + drink + "&smoke=" + smoke + "&religion=" + religion + "&hobby=" + hobby + "&priority=" + priority;
         */
        final String url = "http://172.30.1.49/dating/updatePref.jsp?ID=" + currentUser + "&prefAge=" + prefAge +
                "&prefAgeRange=" + prefAgeRange + "&distance=" + distance + "&drink=" + drink + "&smoke=" + smoke +
                "&religion=" + religion + "&hobby=" + hobby + "&priority=" + priority;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected updatePref -> " + response);
                        try {
                            Toast.makeText(ChangeSetting.this, "CHANGED SETTING", Toast.LENGTH_SHORT).show();
                            getCurrentUserInfo();
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
