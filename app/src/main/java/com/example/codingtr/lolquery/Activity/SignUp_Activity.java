package com.example.codingtr.lolquery.Activity;

import android.content.Intent;
import android.media.Image;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.codingtr.lolquery.Home_Activity;
import com.example.codingtr.lolquery.Management.SessionManagement;
import com.example.codingtr.lolquery.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class SignUp_Activity extends AppCompatActivity {

    private EditText inputLayoutEmail, inputLayoutPassword, inputLayoutFullName;
    private Button buttonRegister;
    private ImageView activitySignUpBackArrow;

    private String tempEmail = null;
    private String tempPassword = null;
    private String tempFullName = null;

    private SessionManagement sessionManagement;

    private final static String URL_SIGNUP = "http://lolquery.codingtr.com/webservice/signup.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sessionManagement = new SessionManagement(this);

        if (sessionManagement.isLoggin() == true){
            startActivity(new Intent(SignUp_Activity.this, Home_Activity.class));
        }

        setReferences();
        activitySignUpBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonRegister
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailCheck() && passwordCheck() && fullNameCheck()) {
                    signUp();
                }
            }
        });
    }

    private void signUp() {

        StringRequest request = new StringRequest(Request.Method.POST, URL_SIGNUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");

                    if(success == 0){
                        Toasty.error(getApplicationContext(),
                                "Kayıt Yapılamadı, Lütfen Bilgilerinizi Kontrol Edin.",
                                Toast.LENGTH_LONG, true).show();
                    }else{
                        Toasty.success(getApplicationContext(),
                                "Kayıt Başarılı, Lütfen Giriş Yapın.",
                                Toast.LENGTH_LONG, true).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(), Login_Activity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Cevap", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("user_email", tempEmail);
                params.put("user_password", tempPassword);
                params.put("user_fullname", tempFullName);

                return params;
            }
        };

        Volley.newRequestQueue(SignUp_Activity.this).add(request);
    }

    private boolean fullNameCheck() {
        tempFullName = inputLayoutFullName.getText().toString().trim();

        if(TextUtils.isEmpty(tempFullName)){
            inputLayoutFullName.setError("İsim Boş Geçilmez");
            return false;
        }else {
            return true;
        }
    }

    private boolean passwordCheck() {
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

    private boolean emailCheck() {

        tempEmail = inputLayoutEmail.getText().toString().trim();

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

    private void setReferences() {

        inputLayoutEmail = findViewById(R.id.signUpActivityTextInputLayoutEmail);
        inputLayoutPassword = findViewById(R.id.signUpActivityTextInputLayoutPassword);
        inputLayoutFullName = findViewById(R.id.signUpActivityTextInputLayoutFullName);
        activitySignUpBackArrow = findViewById(R.id.signUpActivityBackArrow);

        buttonRegister = findViewById(R.id.signUpActivityButtonSave);
    }
}
