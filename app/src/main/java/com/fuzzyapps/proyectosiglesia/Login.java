package com.fuzzyapps.proyectosiglesia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class Login extends AppCompatActivity {
    //UI variables
    private EditText user,password;
    private Button loginwithEPButton;
    private TextView errorTextView;
    //Facebook variables
    private CallbackManager mCallbackManager;
    private AccessTokenTracker tracker;
    private ProfileTracker profileTracker;
    private LoginButton loginButton;
    private String TAG = "@MyGvs";
    private FacebookCallback<LoginResult>  mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            //Profile profile = Profile.getCurrentProfile();
            //almacenarDatosMomentaneamente(profile);
            if(Profile.getCurrentProfile() == null) {
                profileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                        // profile2 is the new profile
                        almacenarDatosMomentaneamente(profile2);
                        profileTracker.stopTracking();
                    }
                };
                profileTracker.startTracking();
            }else {
                Profile profile = Profile.getCurrentProfile();
                almacenarDatosMomentaneamente(profile);
            }
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        AppEventsLogger.activateApp(getApplication());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        mCallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginwithEPButton = (Button) findViewById(R.id.loginButton);
        user = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.password);
        errorTextView = (TextView) findViewById(R.id.errorText);
        user.setText("mygvs.mh@gmail.com");
        password.setText("anitalavalatina");
        loginButton.setReadPermissions("public_profile");
        tracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                almacenarDatosMomentaneamente(newProfile);
            }
        };
        tracker.startTracking();
        profileTracker.startTracking();
        loginwithEPButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String getUser = user.getText().toString();
                String getPassword = password.getText().toString();
                if(getUser.equals("mygvs.mh@gmail.com") && getPassword.equals("anitalavalatina")){
                    redirigirMapa();
                }else{
                    errorTextView.setText("¡Datos incorrectos!");
                    errorTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        almacenarDatosMomentaneamente(profile);
    }
    @Override
    protected void onStop() {
        super.onStop();
        tracker.stopTracking();
        profileTracker.stopTracking();
    }
    private void almacenarDatosMomentaneamente(Profile profile){
        if(profile!= null){
            //texto.setText("welcome "+profile.getName());
            redirigirMapa();
        }
    }
    private void redirigirMapa() {
        Intent i = new Intent(Login.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
