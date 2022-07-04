package com.example.project;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.content.Intent;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editUser, editPass;
    Button login, signUp;
    ArrayList<Profile> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editUser = findViewById(R.id.username);
        editPass = findViewById(R.id.password);
        login = findViewById(R.id.button_login);
        signUp = findViewById(R.id.button_sign);
        data = new ArrayList<>();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, sign_up.class);
                startActivityForResult(intent, 1);
            }
        });

        //run();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editUser.length() == 0) {
                    editUser.setError("Enter username/gmail");
                }
                if (editPass.length() == 0) {
                    editPass.setError("Enter password");
                }
                if (editUser.length() > 0 && editPass.length() > 0) {
                    profileLogin();
                }
            }
        });
    }

    private void run() {
        Intent intent = new Intent(MainActivity.this, home_page.class);
        intent.putExtra("name", "asada");
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String message = data.getStringExtra("result");
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void profileLogin() {
        data.clear();
        final String username = editUser.getText().toString().trim();
        final String password = editPass.getText().toString();

        String url = "http://10.0.2.2/flatfrom/login.php";

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse", response);
                        Boolean result = false;
                        try {
                            JSONObject root = new JSONObject(response);
                            Boolean status = root.getBoolean("status");
                            String message = root.getString("message");
                            if (status == true) {
                                JSONArray us = root.getJSONArray("data");
                                for (int i = 0; i < us.length(); i++) {
                                    result = false;
                                    JSONObject jsonList = us.getJSONObject(i);
                                    String user = jsonList.getString("username");
                                    String pass = jsonList.getString("password");
                                    String name = jsonList.getString("name");

                                    if (username.equals(user) && password.equals(pass)) {
                                        data.add(new Profile(user, pass, name));
                                        result = true;
                                        break;
                                    }
                                }
                                if (result) {
                                    Intent intent = new Intent(MainActivity.this, home_page.class);
                                    intent.putExtra("name", data.get(0).getName());
                                    intent.putExtra("username", data.get(0).getUsername());
                                    intent.putExtra("password", data.get(0).getPassword());
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "Wrong Username or Password!!!", Toast.LENGTH_SHORT).show();
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
        queue.add(stringRequest);
    }
}
