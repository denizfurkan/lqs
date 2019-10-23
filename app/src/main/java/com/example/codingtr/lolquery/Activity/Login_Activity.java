package com.example.codingtr.lolquery.Activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.HashMap;
import java.util.Map;

public class Login_Activity extends AppCompatActivity {

    private EditText inputLayoutEmail, inputLayoutPassword;
    private TextView textViewKayitOl;
    private Button buttonSignIn;
    private ImageView imageView;

    private String tempEmail = null;
    private String tempPassword = null;

    private SessionManagement sessionManagement;

    private final static String URL_LOGIN = "http://lolquery.codingtr.com/webservice/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManagement = new SessionManagement(this);

        if (sessionManagement.isLoggin() == true){
            startActivity(new Intent(this, Home_Activity.class));
        }

        setReferences();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textViewKayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this, SignUp_Activity.class));
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailCheck() && passwordCheck()){
                    signIn();
                }
            }

            private void signIn() {
                StringRequest request = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            int success = jsonObject.getInt("success");

                            if(success == 1) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String name = jsonObject1.getString("user_fullname").trim();
                                    String email = jsonObject1.getString("user_email").trim();
                                    String id = jsonObject1.getString("user_id").trim();

                                    sessionManagement.createSession(name, email, id);

                                    Intent intent = new Intent(Login_Activity.this, Home_Activity.class);
                                    intent.putExtra("user_fullname", name);
                                    intent.putExtra("user_email", email);
                                    intent.putExtra("user_id", id);
                                    startActivity(intent);

                                }
                            }else{
                                Log.e("Hata", "Login Parse");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Hata", "Hata");
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("user_email", tempEmail);
                        params.put("user_password", tempPassword);

                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(Login_Activity.this);
                requestQueue.add(request);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sessionManagement.isLoggin() == true){
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        }
    }

    private void setReferences() {
        inputLayoutEmail = findViewById(R.id.loginActivityTextInputLayoutEmail);
        inputLayoutPassword = findViewById(R.id.loginActivityTextInputLayoutPassword);
        buttonSignIn = findViewById(R.id.loginActivityButtonSignIn);
        textViewKayitOl = findViewById(R.id.loginActivityTextViewRegister);
        imageView = findViewById(R.id.activityLoginBackArrow);
    }

    private boolean emailCheck(){

        tempEmail = inputLayoutEmail.getText().toString().trim();
        Log.e("Email", tempEmail);

        if(tempEmail.isEmpty()){
            inputLayoutEmail.setError("E-Mail Boş Geçilemez");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(tempEmail).matches()){
            inputLayoutEmail.setError("Geçerli Bir E-Mail Adresi Girin");
            return false;
        }else{
            inputLayoutEmail.setError(null);
            return true;
        }

    }

    private boolean passwordCheck(){

        tempPassword = inputLayoutPassword.getText().toString().trim();

        if(tempPassword.isEmpty()){
            inputLayoutPassword.setError("Şifre Boş Geçilemez");
            return false;
        }else if(tempPassword.length()<6){
            inputLayoutPassword.setError("Şifre zayıf");
            return false;
        }else{
            inputLayoutPassword.setError(null);
            return true;
        }
    }
}
