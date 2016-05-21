package org.example.androidsdk.hackathon;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerRequest {
    Context context;
    public static final int CONNECTION_TIMEOUT = 15 * 1000;
    ProgressDialog progressDialog;
    HashMap<String,String> params;
    String url;
    String SERVER_ADDRESS = "http://ec2-54-187-141-92.us-west-2.compute.amazonaws.com/api/";

    public ServerRequest(Context context) {
        this.context = context;
        params = new HashMap<>();
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please Wait...");
    }

    public void loginFB(String userId,String loginToken,String profileName,String email,String profilePic, GetResponseCallback getResponseCallback) {
        progressDialog.show();
        url = "login";
//        Log.d("params",userId);
        HashMap<String,String> params = new HashMap<String, String>();
        params.put("id", userId);
        params.put("token", loginToken);
        params.put("name", profileName);
        params.put("email", email);
        params.put("pictureUrl", profilePic);
        serverRequest(url, params, getResponseCallback);
    }

    public void addFriend(String fbId,GetResponseCallback getResponseCallback){
        progressDialog.show();
        url = "addFriend";
        HashMap<String,String> params = new HashMap<String, String>();
        params.put("friendid", fbId);
        params.put("token", new UserSharedPref(context).getCurrentUser().getToken());
        serverRequest(url, params, getResponseCallback);
    }

    public void removeFriend(String fbId,GetResponseCallback getResponseCallback){
        progressDialog.show();
        url = "removeFriend";
        HashMap<String,String> params = new HashMap<String, String>();
        params.put("friendid", fbId);
        params.put("token", new UserSharedPref(context).getCurrentUser().getToken());
        serverRequest(url, params, getResponseCallback);
    }

    public void serverRequest(String url,HashMap<String,String> params, final GetResponseCallback getResponseCallback){
        url = SERVER_ADDRESS + url;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    VolleyLog.v("Response:%n%s", response.toString(4));
                    getResponseCallback.callback(response.toString());
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(CONNECTION_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(request);
    }
}
