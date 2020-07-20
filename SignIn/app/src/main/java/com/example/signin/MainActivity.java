package com.example.signin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 123;
    LoginButton facebookButton;
    Button verify;
    String TAG = "TAG";
    FirebaseAuth mAuth;
    TwitterLoginButton twitterLoginButton;
    private  CallbackManager callbackManager;
    private static final String EMAIL = "email";
    public  String email,fname,lname,id;

    private static final String TWITTER_KEY = "p6ut5nxQxMFvytiIftzimqY7Y";
    private static final String TWITTER_SECRET = "uipSW8EOOsLgkECYRciwu9h4NX3ebSztLvAeMZAEaBHLLY69pP";

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!= null){
            Intent i = new Intent(MainActivity.this,GoogleProfile.class);
            startActivity(i);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);



        facebookButton = (LoginButton) findViewById(R.id.facebookbutton);

        facebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                Log.v("LoginActivity", object.toString());

                                // Application code
                                try {
                                    //email = object.getString("email");
                                     id = object.getString("id");
                                     fname = object.getString("first_name");
                                     lname = object.getString("last_name");
                                    Log.d(TAG,lname);
                                    Log.d(TAG,fname);

                                    Intent i = new Intent(MainActivity.this,FacebookProfile.class);

                                    String image_url = "https://graph.facebook.com/"+id+"/picture?type=normal";
                                    Log.d("TAG",image_url);
                                    Log.d("TAG",id);
                                    i.putExtra("image_url",image_url);
                                    i.putExtra("name", fname+" "+ lname);
                                    //i.putExtra("email",email);

                                    startActivity(i);// 01/31/1980 format
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email");
                request.setParameters(parameters);
                request.executeAsync();

                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        verify = findViewById(R.id.google_signIn);
        mAuth = FirebaseAuth.getInstance();
        verify.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        //Twitter


        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.login_button);
//        Twitter twitter = new TwitterFactory().getInstance();
//        User user = twitter.showUser(twitter.getScreenName());
//        String profileImage = user.getProfileImageURL();
//        System.out.println("Profile Image URL : " +profileImage);

        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                    TwitterSession session = result.data;
                    login(session);


            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Toast.makeText(MainActivity.this, "Twitter not Authenticated", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void login(TwitterSession session) {
        String username = session.getUserName();
        String id = String.valueOf(session.getUserId());

        String url = "http://pbs.twimg.com/profile_images/"+id+"/7df3h38zabcvjylnyfe3_normal.png";

        //String profileImage = session.profileImageUrl.replace("_normal", "");
        Intent intent = new Intent(MainActivity.this,TwitterProfile.class);
        intent.putExtra("username",username);
        intent.putExtra("image_url",url);
        startActivity(intent);

    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.google_signIn:
                signIn();
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);





//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                // ...
            }
        }


    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i = new Intent(MainActivity.this,GoogleProfile.class);
                            startActivity(i);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();


                        }

                        // ...
                    }
                });
    }


}