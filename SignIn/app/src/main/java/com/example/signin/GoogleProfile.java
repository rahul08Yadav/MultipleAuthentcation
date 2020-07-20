package com.example.signin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class GoogleProfile extends AppCompatActivity {
    TextView name,email;
    ImageView google_img;
    Button signOut;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        name = findViewById(R.id.name);
        google_img = findViewById(R.id.google_img);
        email = findViewById(R.id.mail);
        signOut = findViewById(R.id.logout);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null){
            name.setText(signInAccount.getDisplayName());
            email.setText(signInAccount.getEmail());
            Glide.with(GoogleProfile.this).load(signInAccount.getPhotoUrl()).into(google_img);
        }

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(GoogleProfile.this,MainActivity.class);
                startActivity(i);
            }
        });



    }
}
