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

class CartAdapter extends ArrayAdapter<Product> {

    Context context;
    int resource;
    ArrayList<Product> data;

    public CartAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.data = objects;
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(resource, parent, false);
        ImageView imageView = view.findViewById(R.id.imageView);
        final TextView name, price, count, total;
        Button addButton, minusButton, cancel;
        final int num[] = {0};
        num[0] = data.get(position).getCount();

        name = view.findViewById(R.id.name);
        price = view.findViewById(R.id.price);
        total = view.findViewById(R.id.total_price);

        imageView.setImageURI(Uri.parse(data.get(position).getImage()));
        name.setText(data.get(position).getName());
        price.setText("" + data.get(position).getPrice());
        final String url = data.get(position).getImage();
        Picasso.with(context).load(url).into(imageView);

        addButton = view.findViewById(R.id.add_button);
        minusButton = view.findViewById(R.id.minus_button);
        cancel = view.findViewById(R.id.button_cancel);
        count = view.findViewById(R.id.count);
        count.setText("" + num[0]);

        int total_price = num[0]*data.get(position).getPrice();
        total.setText("" + total_price);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num[0]++;
                count.setText("" + num[0]);
                notifyDataSetChanged();

            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num[0] > 0) {
                    num[0]--;
                    count.setText("" + num[0]);
                    notifyDataSetChanged();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
