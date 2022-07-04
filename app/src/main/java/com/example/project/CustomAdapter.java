package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CustomAdapter extends ArrayAdapter<Product> {
    Context context;
    int resource;
    ArrayList<Product> data;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.data = objects;
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(resource, parent, false);
        ImageView imageView = view.findViewById(R.id.imageView);
        final TextView name, price, count;
        Button addButton, minusButton, shopCart;
        final int num[] = {0};

        name = view.findViewById(R.id.name);
        price = view.findViewById(R.id.price);

        imageView.setImageURI(Uri.parse(data.get(position).getImage()));
        name.setText(data.get(position).getName());
        price.setText(""+data.get(position).getPrice());
        final String url = data.get(position).getImage();
        Picasso.with(context).load(url).into(imageView);

        addButton = view.findViewById(R.id.add_button);
        minusButton = view.findViewById(R.id.minus_button);
        shopCart = view.findViewById(R.id.shop_cart);
        count = view.findViewById(R.id.count);
        count.setText("" + num[0]);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num[0]++;
                count.setText(""+num[0]);
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num[0] > 0) {
                    num[0]--;
                    count.setText("" + num[0]);
                }
            }
        });

        LinearLayout ln = view.findViewById(R.id.click_item);
        ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, itemShow.class);
                intent.putExtra("itemImage", url);
                intent.putExtra("itemName", data.get(position).getName());
                intent.putExtra("itemPrice", data.get(position).getPrice());
                context.startActivity(intent);
            }
        });

        shopCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData(num[0], data.get(position).getName());
                num[0] = 0;
                count.setText("" + num[0]);
            }
        });

        return view;
    }

    private void updateData(final int i, final String name) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://10.0.2.2/flatfrom/update.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("quality", String.valueOf(i));
                params.put("name", name);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
