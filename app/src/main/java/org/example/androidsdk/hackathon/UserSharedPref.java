package org.example.androidsdk.hackathon;

import android.content.Context;
import android.content.SharedPreferences;


public class UserSharedPref {

    public User user;
    private Context context;
    private final String UserPreferences = "UserPreferences";
    private final String id = "UserId";
    private final String email = "UserEmail";
    private final String token = "UserToken";
    private final String name = "UserName";
    private final String profilePic = "UserProfilePic";

    public UserSharedPref(Context context) {
        this.context = context;
    }

    public void login(User user) {
        this.user = user;
        /*Save this user in shared preferences*/
        SharedPreferences sharedpreferences = context.getSharedPreferences(UserPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(user.getId()==null||user.getEmail()==null || user.getToken()==null || user.getName()==null || user.getProfilePic()==null)
            throw new NullPointerException();
        editor.putString(id,user.getId());
        editor.putString(email, user.getEmail());
        editor.putString(token, user.getToken());
        editor.putString(name,user.getName());
        editor.putString(profilePic,user.getProfilePic());
        editor.commit();
    }

    public User getCurrentUser(){
        SharedPreferences sharedpreferences = context.getSharedPreferences(UserPreferences, Context.MODE_PRIVATE);
        user=new User(sharedpreferences.getString(id, null),sharedpreferences.getString(email, null),sharedpreferences.getString(token, null),sharedpreferences.getString(name, null),sharedpreferences.getString(profilePic,null));
//        user.setEmail(sharedpreferences.getString(email, null));
//        user.setPassword(sharedpreferences.getString(password, null));
//        user.setName(sharedpreferences.getString(name, null));
//        user.setProfilePic(sharedpreferences.getString(profilePic,null));
        if(user.getId()==null||user.getEmail()==null || user.getToken()==null || user.getName()==null || user.getProfilePic()==null)
            user=null;
        return user;
    }

    public void logout(){
        SharedPreferences sharedpreferences = context.getSharedPreferences(UserPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedin(){
        boolean flag=true;
        SharedPreferences sharedpreferences = context.getSharedPreferences(UserPreferences, Context.MODE_PRIVATE);
        String t1=sharedpreferences.getString(name,null);
        String t2=sharedpreferences.getString(token,null);
        if(t1==null || t2==null)
            flag=false;
        return flag;
    }

}

