package com.example.project;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class getData {

    public static Boolean checkLogin(String username, String password) {
        final Boolean[] result = new Boolean[1];
        final String user = username;
        final String pass = password;
        String url = "http://10.0.2.2/flatfrom/login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse", response);
                        try {
                            JSONObject root = new JSONObject(response);
                            Boolean status = root.getBoolean("status");
                            String message = root.getString("message");
                            if (status == true) {
                                JSONArray data = root.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject jsonList = data.getJSONObject(i);
                                    String username = jsonList.getString("username");
                                    String password = jsonList.getString("password");
                                    String name = jsonList.getString("name");

                                    if (user == username && pass == password) {
                                        result[0] = true;
                                        break;
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("onErrorResponse", error.getMessage());
                    }
                });

        return result[0];
    }
}
