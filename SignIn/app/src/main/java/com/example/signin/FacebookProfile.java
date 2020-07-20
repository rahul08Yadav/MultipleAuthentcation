package com.example.signin;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.facebook.login.widget.LoginButton;


public class FacebookProfile extends AppCompatActivity {
    TextView name;
    ImageView img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebook_profile);
        String img_url = getIntent().getStringExtra("image_url");
        String full_name = getIntent().getStringExtra("name");
        //String email = getIntent().getStringExtra("email");
        name = findViewById(R.id.facebook_name);


        img = findViewById(R.id.facebook_pic);

       // name.setText(full_name);
       // emailid.setText(email);


        //Glide.with(FacebookProfile.this).load(img_url).into(img);

    }
}
