package com.example.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

public class itemShow extends AppCompatActivity {
    ImageView imageView;
    TextView name, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_show);

        imageView = findViewById(R.id.image_item);
        name = findViewById(R.id.txt_display_name);
        price = findViewById(R.id.txt_price);

        Intent intent = getIntent();

        Picasso.with(itemShow.this).load(intent.getStringExtra("itemImage")).into(imageView);
        name.setText(intent.getStringExtra("itemName"));
        price.setText(""+intent.getIntExtra("itemPrice", 0));

    }

}
