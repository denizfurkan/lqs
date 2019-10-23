package com.example.codingtr.lolquery.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.codingtr.lolquery.Home_Activity;
import com.example.codingtr.lolquery.Management.SessionManagement;
import com.example.codingtr.lolquery.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class GameSettings_Activity extends AppCompatActivity {

    private SessionManagement sessionManagement;

    private Spinner spinnerServer;
    private Button buttonKaydet;
    private TextInputLayout textInputLayoutUserName;
    private TextView textViewOnay;
    private static String status = "0";
    private String[] server;
    private String getEmail;
    private String serverItem;

    private final static String URL_SELECT= "http://lolquery.codingtr.com/webservice/gamer_read_detail.php";
    private final static String URL_INSERT = "http://lolquery.codingtr.com/webservice/game_insert.php";
    private final static String URL_UPDATE = "http://lolquery.codingtr.com/webservice/game_update.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);

        setSessionManagement();
        setReferences();
        spinnerLoad();
        gamerSelect();
        alertDialog();

        buttonKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(serverCheck() && userNameCheck()){
                    gamerInsert();
                    gamerUpdate();
                }
            }
        });
    }

    private void gamerSelect() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SELECT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject  = new JSONObject(response);
                    int success = jsonObject.getInt("success");

                    if(success == 1){
                        JSONArray jsonArray = jsonObject.getJSONArray("read");
                        for(int i =0; i<jsonArray.length(); i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String game_username = jsonObject1.getString("gamer_username");
                            String game_server = jsonObject1.getString("gamer_server");
                            String game_status = jsonObject1.getString("onay");

                            if(game_status.equals("1")){
                                textViewOnay.setText("Onaylı");
                            }else if(game_status.equals("0")){
                                textViewOnay.setText("Onay Bekliyor");
                            }else{
                                textViewOnay.setText("Kayıt Bekliyor");
                            }

                            switch (game_server) {
                                case "tr1": spinnerServer.setSelection(1); break;
                                case "eun1": spinnerServer.setSelection(2); break;
                                case "jp1": spinnerServer.setSelection(3); break;
                                case "kr": spinnerServer.setSelection(4); break;
                                case "la1": spinnerServer.setSelection(5); break;
                                case "la2": spinnerServer.setSelection(6); break;
                                case "na1": spinnerServer.setSelection(7); break;
                                case "oc1": spinnerServer.setSelection(8); break;
                                case "ru": spinnerServer.setSelection(9); break;
                                case "pbe1": spinnerServer.setSelection(10);break;
                                default: break;
                            }
                            textInputLayoutUserName.getEditText().setText(game_username);


                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> param = new HashMap<>();
                param.put("user_email", getEmail);
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void gamerUpdate() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Json Update Cevap", response);
                try {
                    JSONObject jsonObject  = new JSONObject(response);
                    int success = jsonObject.getInt("success");

                    if(success == 1){
                        textViewOnay.setText("Onay Bekliyor");
                        Toasty.success(getApplicationContext(), "Güncellendi", Toast.LENGTH_LONG, true).show();
                        startActivity(new Intent(GameSettings_Activity.this, Home_Activity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final String saveUsername = textInputLayoutUserName.getEditText().getText().toString().trim();
                String tempStatus = "0";

                HashMap<String, String> param = new HashMap<>();
                param.put("user_email", getEmail);
                param.put("gamer_username", saveUsername);
                param.put("gamer_server", serverItem);
                param.put("onay", tempStatus);

                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void gamerInsert() {
        final String saveUsername = textInputLayoutUserName.getEditText().getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject  = new JSONObject(response);
                    int success = jsonObject.getInt("success");

                    if(success == 1){
                        Toasty.success(getApplicationContext(), "Güncellendi", Toast.LENGTH_LONG, true).show();
                        textViewOnay.setText("Onay Bekliyor");
                        startActivity(new Intent(GameSettings_Activity.this, Home_Activity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String status = "0";
                Map<String, String> param = new HashMap<>();
                param.put("user_email", getEmail);
                param.put("gamer_username", saveUsername);
                param.put("gamer_server", serverItem);
                param.put("onay", status);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private boolean userNameCheck() {
        String tempUser = textInputLayoutUserName.getEditText().getText().toString().trim();

        if(tempUser.isEmpty()){
            Toasty.warning(this, "Lütfen Kullanıcı Adı Giriniz", Toast.LENGTH_LONG, true).show();
            return false;
        }else if(tempUser.length()<3){
            Toasty.warning(this, "Lütfen Geçerli Kullanıcı Adı Giriniz", Toast.LENGTH_LONG, true).show();
            return false;
        }
        return true;
    }

    private boolean serverCheck() {

        serverItem = spinnerServer.getSelectedItem().toString();

        if(serverItem.equals("Server")){
            Toasty.warning(this, "Lütfen Server Seçiniz", Toast.LENGTH_LONG, true).show();
            return false;
        }else {
            switch (serverItem) {
                case "TR": serverItem = "tr1";break;
                case "EUNE": serverItem = "eun1";break;
                case "JP": serverItem = "jp1";break;
                case "KR": serverItem = "kr";break;
                case "LAN": serverItem = "la1";break;
                case "LAS": serverItem = "la2";break;
                case "NA": serverItem = "na1";break;
                case "OCE": serverItem = "oc1";break;
                case "RU": serverItem = "ru";break;
                case "PBE": serverItem = "pbe1";break;
                default: break;
            }
        }

        Log.e("String Server", serverItem);
        return true;
    }

    private void setSessionManagement() {
        sessionManagement = new SessionManagement(this);
        sessionManagement.getUserDetail();

        HashMap<String, String> user  = sessionManagement.getUserDetail();
        getEmail = user.get(sessionManagement.EMAIL);
    }

    private void alertDialog() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

        if(status.equals("0")){

            builder1.setTitle("Uyarı");
            builder1.setMessage("Ekleyeceğiniz oyuncu ile Duo bulma işlemleri gibi birçok işlem yapacaksınız." +
                    "Bu yüzden oyuncu nickini doğru yazmayan oyuncular kalıcı olarak sistemden uzaklaştırılacaktır. \nKabul Ediyor Musunuz?");
            builder1.setCancelable(false);

            builder1.setPositiveButton(
                    "Evet",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    "Hayır",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    private void setReferences() {
        server = new String[] {"Server", "TR", "EUNE", "JP", "KR", "LAN", "LAS", "NA", "OCE", "RU", "PBE"};
        textInputLayoutUserName = findViewById(R.id.activityGameSettingsTextInputLayoutUsername);
        spinnerServer = findViewById(R.id.activityGameSettingsSpinnerLocation);
        buttonKaydet = findViewById(R.id.activityGameSettingsButtonKaydet);
        textViewOnay = findViewById(R.id.onayText1);
    }

    private void spinnerLoad() {

        final List<String> plantsList = new ArrayList<>(Arrays.asList(server));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,plantsList){

            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);

                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };


        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerServer.setAdapter(spinnerArrayAdapter);
    }
}
