package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class HomeFragment extends Fragment {
    ListView listView;
    CustomAdapter adapter;
    ArrayList<Product> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.home_listView);
        data = new ArrayList<>();

        //mockData();

        adapter = new CustomAdapter(getContext(),
                R.layout.list_item,
                data
        );

        listView.setAdapter(adapter);
        sqlData();

    }

    private void mockData() {
        data.add(new Product("tttt", 123455, "http://10.0.2.2/flatfrom/img/currentTitle.jpg"));
    }

    private void sqlData() {
        String url = "http://10.0.2.2/flatfrom/product.php";

        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                                    String name = jsonList.getString("name");
                                    int price = jsonList.getInt("price");
                                    String image = "http://10.0.2.2/flatfrom/" + jsonList.getString("image");

                                    data.add(new Product(name, price, image));
                                    int count = 0;
                                    updateSql(count, name);
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

    private void updateSql(final int count, final String name) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://10.0.2.2/flatfrom/update.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
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


}
