package com.example.codingtr.lolquery;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.codingtr.lolquery.Activity.AboutUs_Activity;
import com.example.codingtr.lolquery.Activity.GameSettings_Activity;
import com.example.codingtr.lolquery.Activity.Login_Activity;
import com.example.codingtr.lolquery.Activity.UserSettings_Activity;
import com.example.codingtr.lolquery.Activity.Welcome_Activity;
import com.example.codingtr.lolquery.Fragment.CanliMac_Fragment;
import com.example.codingtr.lolquery.Fragment.Duo_Fragment;
import com.example.codingtr.lolquery.Fragment.Home_Fragment;
import com.example.codingtr.lolquery.Fragment.Profil_Fragment;
import com.example.codingtr.lolquery.Management.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SessionManagement sessionManagement;

    private NavigationView nav_view;
    private Toolbar toolbar;
    private Fragment fragment;
    private DrawerLayout drawer;
    private CircleImageView circleImageView;

    private String getEmail;
    private String getName;
    private String getId;

    private TextView navName;
    private TextView navEmail;

    private final static String URL_READ = "http://lolquery.codingtr.com/webservice/logout.php";
    private final static String URL_DETAIL = "http://lolquery.codingtr.com/webservice/read_detail.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManagement = new SessionManagement(this);
        sessionManagement.checkLogin();

        sessionManagement.getUserDetail();

        HashMap<String, String> user  = sessionManagement.getUserDetail();
        getEmail = user.get(sessionManagement.EMAIL);
        getName = user.get(sessionManagement.NAME);
        getId = user.get(sessionManagement.ID);

        loadNavigation();
        getDetail();
        loadDrawerInformation();


    }

    private void getDetail() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DETAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("read");

                    int success = jsonObject.getInt("success");

                    if(success > 0){

                        for(int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String jsonFullName = jsonObject1.getString("user_fullname").trim();
                            String jsonEmail = jsonObject1.getString("user_email").trim();
                            String jsonPhoto = jsonObject1.getString("user_photo").trim();

                            String photo = "http://lolquery.codingtr.com/img/"+jsonPhoto;

                            Glide.with(Home_Activity.this)
                                    .load(photo)
                                    .placeholder(R.drawable.ic_account_circle_black_24dp)
                                    .into(circleImageView);

                            navEmail.setText(jsonEmail);
                            navName.setText(jsonFullName);

                        }
                    }
                } catch (JSONException e) {
                    Log.e("Exception Parse", response);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse", error.toString());

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", getId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadDrawerInformation() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navName = headerView.findViewById(R.id.navbaslikad);
        navEmail = headerView.findViewById(R.id.navbaslikemail);
        circleImageView = headerView.findViewById(R.id.navBaslikCircleImageView);
        navName.setText(getName);
        navEmail.setText(getEmail);
    }

    private void loadNavigation() {
        fragment = new Home_Fragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_tutucu, fragment).commit();

        nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer);
        toolbar =  findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, 0, 0);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        View baslik = nav_view.inflateHeaderView(R.layout.nav_baslik);

        nav_view.setNavigationItemSelectedListener(this);
    }

    private void logoutOperation(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_email", getEmail);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if(id == R.id.action_anasayfa){
            fragment = new Home_Fragment();
        }
        if(id == R.id.action_canli_mac){
            fragment = new CanliMac_Fragment();
        }
        if(id == R.id.action_duo_bul){
            fragment = new Duo_Fragment();
        }
        if(id == R.id.action_profil_sorgu){
            fragment = new Profil_Fragment();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_tutucu, fragment).commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_oyun :
                startActivity(new Intent(getApplicationContext(), GameSettings_Activity.class));
                return true;

            case R.id.action_kisisel :
                startActivity(new Intent(this, UserSettings_Activity.class));
                return true;

            case R.id.action_hakkinda :
                startActivity(new Intent(this, AboutUs_Activity.class));
                return true;

            case R.id.action_cikis:
                logoutOperation();
                sessionManagement.logOut();
                finish();
                startActivity(new Intent(this, Welcome_Activity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        }

        logoutOperation();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
