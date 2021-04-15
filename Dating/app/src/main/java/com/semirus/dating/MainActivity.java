package com.semirus.dating;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {
    EditText idEditTxt, pwEditTxt;
    Button loginBtn, signUpBtn;
    static RequestQueue requestQueue;
    // store current user's info
    String currentUser, pw, sex, age, ageRange, prefAge, prefAgeRange, latitude, longitude, distance, drink, smoke,
            religion, hobby, priority = "";
    double lat, lon = 0;
    // store opposite sex user's info
    String matchedUserID, matchedAge, matchedAgeRange, matchedLatitude, matchedLongitude, matchedReligion,
            matchedDrink, matchedSmoke, matchedHobby = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // edit text
        idEditTxt = (EditText)findViewById(R.id.idEditTxt);
        pwEditTxt = (EditText)findViewById(R.id.pwEditTxt);
        // button
        loginBtn = (Button)findViewById(R.id.loginBtn);
        signUpBtn = (Button)findViewById(R.id.signUpBtn);
        // login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check location permission has granted or not
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    Toast.makeText(MainActivity.this, "Please allow access", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions( MainActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                            0 );
                }
                else {
                    LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    lat = location.getLatitude();   // 위도
                    lon = location.getLongitude();  // 경도
                    System.out.println("위도 : " + lat + " 경도 : " + lon);
                }
                addLocation();
            }
        });
        // sign up
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to signUp
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }
    // =============== 4) add scores according to purpose ===============
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
                System.out.println("Connected addMatchingScore -> " + response);
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

    // =============== 3) get other user's info based on different sex & filtering same answers ===============
    public void getOtherUserInfo(){
        //final String url = "http://192.168.142.14/dating/getOtherUserInfo.jsp?ID=" + currentUser + "&sex=" + sex;
        final String url = "http://172.30.1.49/dating/getOtherUserInfo.jsp?ID=" + currentUser + "&sex=" + sex;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Connected getOtherUserInfo FD -> " + response);
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

    // ================================= 2) login to get current user's info =================================
    public void executeLogin(){
        final String ID = idEditTxt.getText().toString();
        final String PW = pwEditTxt.getText().toString();
        //final String url = "http://192.168.142.14/dating/loginAndroid.jsp?ID=" + ID + "&PW=" + PW;
        final String url = "http://172.30.1.49/dating/loginAndroid.jsp?ID=" + ID + "&PW=" + PW;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected executeLogin -> " + response);
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
                            if ((ID.equals(currentUser)) && (PW.equals(pw))) {
                                getOtherUserInfo();
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Check ID & PW again", Toast.LENGTH_SHORT).show();
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
    // ================================= 1) add latitude, longitude of login user =================================
    public void addLocation(){
        final String ID = idEditTxt.getText().toString();
        final String PW = pwEditTxt.getText().toString();
        final String latStr = String.valueOf(lat);
        final String lonStr = String.valueOf(lon);
        //final String url = "http://192.168.142.14/dating/addLocation.jsp?ID=" + ID + "&PW=" + PW + "&lat=" + latStr + "&lon=" + lonStr;
        final String url = "http://172.30.1.49/dating/addLocation.jsp?ID=" + ID + "&PW=" + PW + "&lat=" + latStr + "&lon=" + lonStr;
        System.out.println(url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected addLocation -> " + response);
                        try {
                            System.out.println("Location added");
                            executeLogin();
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