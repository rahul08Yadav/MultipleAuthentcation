package com.example.signin;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;

public class TwitterProfile extends AppCompatActivity {
    TextView twitterName;
    Button twitterLogout;
    ImageView img;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_profile);
        String username = getIntent().getStringExtra("username");
        String url = getIntent().getStringExtra("image_url");

        twitterName = findViewById(R.id.twitter_name);
        img = findViewById(R.id.twt_img);
        twitterName.setText(username);
       // Glide.with(TwitterProfile.this).load(url).into(img);

        twitterLogout = findViewById(R.id.twitter_logout);

        twitterLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CookieSyncManager.createInstance(TwitterProfile.this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeSessionCookie();
                TwitterCore.getInstance().getSessionManager().clearActiveSession();

                Intent i = new Intent(TwitterProfile.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
}
