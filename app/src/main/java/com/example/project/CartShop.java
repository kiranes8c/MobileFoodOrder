package com.example.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearSmoothScroller;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CartShop extends AppCompatActivity {

    ListView listView;
    TextInputLayout textInputLayout;
    TextInputEditText textInputEditText;
    CartAdapter adapter;
    ArrayList<Product> data;
    TextView textTotal;
    String username;
    int dem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_shop);

        listView = findViewById(R.id.listCartView);
        textInputLayout = findViewById(R.id.textInputLayout);
        textInputEditText = findViewById(R.id.textInput);
        textTotal = findViewById(R.id.total);

        Intent getintent = getIntent();
        username = getintent.getStringExtra("user");

        data = new ArrayList<>();

        adapter = new CartAdapter(this,
                R.layout.cart_item,
                data
        );

        listView.setAdapter(adapter);
        sqlData();

        dem = 0;
        for (int i = 0; i < data.size(); i++) {
            int number1 = data.get(i).getPrice();
            int number2 = data.get(i).getPrice();
            dem = dem + (number1 * number2);
        }

        textTotal.setText("" + dem);
        adapter.notifyDataSetChanged();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void sqlData() {
        data.clear();
        String url = "http://10.0.2.2/flatfrom/product.php";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse", response);
                        Integer result = null;
                        try {
                            JSONObject root = new JSONObject(response);
                            Boolean status = root.getBoolean("status");

                            if (status == true) {
                                JSONArray jsonArray = root.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonList = jsonArray.getJSONObject(i);
                                    int count = jsonList.getInt("quality");
                                    if (count > 0) {
                                        String name = jsonList.getString("name");
                                        int price = jsonList.getInt("price");
                                        String image = "http://10.0.2.2/flatfrom/" + jsonList.getString("image");

                                        data.add(new Product(name, price, image, count));
                                    }
                                }
                                adapter.notifyDataSetChanged();
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

    private void sqlUpdate(final int count, final String name) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2/flatfrom/update.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CartShop.this, "error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("quality", String.valueOf(count));
                params.put("name", name);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case R.id.purchase:
                if (textInputEditText.length() == 0) {
                    textInputEditText.setError("Table must choose!!!");
                } else {
                    for (int i = 0; i < data.size(); i++) {
                        String name = data.get(i).getName();
                        int price = data.get(i).getPrice();
                        int count = data.get(i).getCount();
                        int total = price * count;
                        int ban = Integer.parseInt(textInputEditText.getText().toString());

                        transperData(username, name, price, count, total, ban);
                        sqlUpdate(0, name);
                    }
                    data.clear();
                    adapter.notifyDataSetChanged();
                    CartShop.this.finish();
                }

                break;

        }
        onBackPressed();
        return true;
    }

    private void transperData(final String username, final String name, final int price, final int count, final int total, final int ban) {
        RequestQueue queue = Volley.newRequestQueue(CartShop.this);
        String url = "http://10.0.2.2/flatfrom/insert_cartshop.php";

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
                params.put("name", name);
                params.put("price", String.valueOf(price));
                params.put("count", String.valueOf(count));
                params.put("total", String.valueOf(total));
                params.put("ban", String.valueOf(ban));
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
