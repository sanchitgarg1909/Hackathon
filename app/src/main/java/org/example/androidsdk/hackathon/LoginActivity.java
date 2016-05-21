package org.example.androidsdk.hackathon;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private String loginToken,userId,email,profilePic,profileName;
    CallbackManager callbackManager;
    UserSharedPref userSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        userSharedPref = new UserSharedPref(LoginActivity.this);
        LoginButton fblogin = (LoginButton)findViewById(R.id.fblogin);
        callbackManager = CallbackManager.Factory.create();
        fblogin.setReadPermissions(Arrays.asList("public_profile, email, user_friends"));
        fblogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.d("here", "success");
                loginToken = loginResult.getAccessToken().getToken();
                userId = loginResult.getAccessToken().getUserId();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                Log.d("LoginActivity", response.toString());
                                Log.d("LoginActivity", String.valueOf(object));
                                try {
                                    JSONObject jsonObject = response.getJSONObject();
                                    //JSONObject graphObject = jsonObject.getJSONObject("graphObject");
                                    profileName = jsonObject.getString("name");
                                    profilePic = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
                                    email = jsonObject.getString("email");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                fetchId();
//                                startApp();
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,picture.height(400){url},friends");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                //info.setText("Cancelled");
                Log.d("here", "cancel");
            }

            @Override
            public void onError(FacebookException e) {
                //info.setText("Failed");
                e.printStackTrace();
                Log.d("here", "error");
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("here","activityresult");
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void fetchId(){
        ServerRequest request = new ServerRequest(this);
        request.loginFB(userId, loginToken, profileName, email, profilePic, new GetResponseCallback() {
            @Override
            public void callback(String resp) {
//                Log.d("token",response);
//                JSONObject response = null;
                try {
                    JSONObject response = new JSONObject(resp);
                    if(response.getBoolean("success")){
                        loginToken = response.getString("token");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                loginToken = response;
                User user = new User(userId, email, loginToken, profileName, profilePic);
                userSharedPref.login(user);
                startApp();
            }
        });
    }
    private void startApp(){
//        User user = new User(userId, email, loginToken, profileName, profilePic);
//        userSharedPref.login(user);
        startActivity(new Intent(this,AddFriendsActivity.class));
    }
}
