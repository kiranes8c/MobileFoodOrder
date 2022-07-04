package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class sign_up extends AppCompatActivity {
    EditText signName, signUser, signPass;
    Button save, back;
    private Object MainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signName = findViewById(R.id.sign_up_name);
        signUser = findViewById(R.id.sign_up_username);
        signPass = findViewById(R.id.sign_up_password);

        save = findViewById(R.id.sign_up_save);
        back = findViewById(R.id.sign_up_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sign_up.this, MainActivity.class);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signName.length() == 0) {
                    signName.setError("Must enter name!");
                }
                if (signUser.length() == 0) {
                    signUser.setError("Must enter username or gmail!");
                }
                if (signPass.length() == 0) {
                    signPass.setError("Must enter password!");
                }
                if (signName.length() > 0 && signUser.length() > 0 && signPass.length() > 0) {
                    String username = signUser.getText().toString().trim();
                    String password = signPass.getText().toString();
                    String name = signName.getText().toString();
                    checkUser(username, password, name);

                }
            }
        });
    }

    private void checkUser(final String username, final String password, final String name) {
        String url = "http://10.0.2.2/flatfrom/login.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse", response);
                        boolean temp = false;
                        try {
                            JSONObject root = new JSONObject(response);
                            Boolean status = root.getBoolean("status");
                            if (status == true) {
                                JSONArray us = root.getJSONArray("data");
                                for (int i = 0; i < us.length(); i++) {
                                    JSONObject jsonList = us.getJSONObject(i);
                                    String user = jsonList.getString("username");

                                    if (username.equals(user)){
                                        temp = true;
                                        break;
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (temp == false) {
                            signUp(username, password, name);

                            signUser.setText("");
                            signPass.setText("");
                            signName.setText("");

                            String result = "Sign up successful!!!";
                            Intent intent = new Intent();
                            intent.putExtra("result", result);
                            setResult(1, intent);
                            finish();
                        } else {
                            signUser.setText("");
                            signUser.setError("Username already exit!!!");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("onErrorResponse", error.getMessage());
                    }
                });
        queue.add(stringRequest);
    }

    private void signUp(final String username, final String password, final String name) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2/flatfrom/sign.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("name", name);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
