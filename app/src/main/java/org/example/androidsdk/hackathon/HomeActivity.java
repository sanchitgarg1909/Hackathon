package org.example.androidsdk.hackathon;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header_layout = navigationView.getHeaderView(0);
        TextView profile_name = (TextView) header_layout.findViewById(R.id.profile_name);
        profile_name.setText(new UserSharedPref(this).getCurrentUser().getName());
        ImageView profile_pic = (ImageView) header_layout.findViewById(R.id.profile_pic);
        Picasso.with(this).load(new UserSharedPref(this).getCurrentUser().getProfilePic()).into(profile_pic);
        exploreFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_explore) {
            exploreFragment();
        } else if (id == R.id.nav_profile) {
            profileFragment();
        } else if (id == R.id.nav_group) {
            groupFragment();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void exploreFragment(){
        if (!(fragment instanceof MapFragment)) {
            fragment = new MapFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container_body, fragment).commit();
        }else{
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }
    public void profileFragment(){
        if (!(fragment instanceof FriendsFragment)) {
            fragment = new FriendsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container_body, fragment).commit();
        }else{
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }
    public void groupFragment(){
//        fragment = new GroupFragment();
//        Bundle bundle = new Bundle();
////        bundle.putString("loginToken",loginToken);
////        bundle.putString("userId", userId);
//        fragment.setArguments(bundle);
//        if (fragment != null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.container_body, fragment).commit();
//        }
    }
}
