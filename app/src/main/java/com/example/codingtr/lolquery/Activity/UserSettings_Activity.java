package com.example.codingtr.lolquery.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.bumptech.glide.Glide;
import com.example.codingtr.lolquery.Home_Activity;
import com.example.codingtr.lolquery.Management.SessionManagement;
import com.example.codingtr.lolquery.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class UserSettings_Activity extends AppCompatActivity {

    private SessionManagement sessionManagement;

    private Spinner spinnerGender;
    private CircleImageView circleImageView;
    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword, textInputLayoutFullName, textInputLayoutPhoneNumber;
    private Button buttonSave;

    private final static String URL_READ = "http://lolquery.codingtr.com/webservice/read_detail.php";
    private final static String URL_EDIT = "http://lolquery.codingtr.com/webservice/update.php";
    private final static String URL_UPLOAD = "http://lolquery.codingtr.com/webservice/upload.php";

    private Bitmap bitmap;

    private String getId;
    private String tempEmail, tempPassword, tempFullName, tempPhoneNumber;
    private String[] gender;
    private String genderSelectedItemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        sessionManagement = new SessionManagement(this);
        sessionManagement.getUserDetail();

        HashMap<String, String> user  = sessionManagement.getUserDetail();
        getId = user.get(sessionManagement.ID);

        setReference();
        spinnerLoad();
        getUserDetail();

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailCheck() && passwordCheck() && fullNameCheck() && phoneNumberCheck() && genderCheck() ) {
                    saveUserDetail();
                }
            }
        });

    }

    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Resim Seç"), 1);
    }

    private void uploadPicture(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Resim Cevap", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");

                    if(success == 1){
                        Log.e("Resim Yüklendi", String.valueOf(success));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Hata : Error Response", error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String tempProfil = getStringImage(bitmap);

                Map<String, String> params = new HashMap<>();
                params.put("user_id", getId);
                params.put("user_photo", tempProfil);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(UserSettings_Activity.this);
        requestQueue.add(stringRequest);

    }

    public String getStringImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        return encodedImage;
    }

    private void getUserDetail(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ, new Response.Listener<String>() {
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
                            String jsonPassword = jsonObject1.getString("user_password").trim();
                            String jsonPhone = jsonObject1.getString("user_phone").trim();
                            String jsonPhoto = jsonObject1.getString("user_photo").trim();
                            String jsonGender = jsonObject1.getString("user_gender").trim();

                            String photo = "http://lolquery.codingtr.com/img/"+jsonPhoto;

                            Picasso.get()
                                    .load(photo)
                                    .placeholder(R.drawable.ic_account_circle_black_24dp)
                                    .into(circleImageView);

                            textInputLayoutEmail.getEditText().setText(jsonEmail);
                            textInputLayoutPassword.getEditText().setText(jsonPassword);
                            textInputLayoutFullName.getEditText().setText(jsonFullName);
                            textInputLayoutPhoneNumber.getEditText().setText(jsonPhone);

                            if(jsonGender.equals("Erkek")){
                                spinnerGender.setSelection(1);
                            }else{
                                spinnerGender.setSelection(2);
                            }
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

    private void saveUserDetail(){
        final String saveEmail = textInputLayoutEmail.getEditText().getText().toString().toLowerCase().trim();
        final String savePassword = textInputLayoutPassword.getEditText().getText().toString().trim();
        final String saveName = textInputLayoutFullName.getEditText().getText().toString().trim();
        final String savePhoneNumber = textInputLayoutPhoneNumber.getEditText().getText().toString().trim();
        final String saveGender = spinnerGender.getSelectedItem().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");

                    if(success ==1){
                        Toasty.success(getApplicationContext(), "Güncellendi", Toast.LENGTH_LONG, true).show();
                        startActivity(new Intent(UserSettings_Activity.this, Home_Activity.class));
                    }else{
                        Toasty.error(getApplicationContext(), "Güncelleme Başarısız", Toast.LENGTH_LONG, true).show();
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
                Map<String, String> params = new HashMap<>();
                params.put("user_id", getId);
                params.put("user_email", saveEmail);
                params.put("user_password", savePassword);
                params.put("user_fullname", saveName);
                params.put("user_phone", savePhoneNumber);
                params.put("user_gender", saveGender);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private boolean genderCheck() {
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderSelectedItemText = (String) parent.getItemAtPosition(position);
                if(genderSelectedItemText.isEmpty()){
                    genderSelectedItemText = "Boş";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }});

        return true;
    }

    private boolean phoneNumberCheck() {
        tempPhoneNumber = textInputLayoutPhoneNumber.getEditText().getText().toString().trim();

        if(tempPhoneNumber.isEmpty()){
            return true;
        }else if(!Patterns.PHONE.matcher(tempPhoneNumber).matches()){
            textInputLayoutPhoneNumber.setError("Geçerli Telefon Numarası Girin");
            return false;
        }else{
            textInputLayoutPhoneNumber.setError(null);
            return true;
        }
    }

    private boolean fullNameCheck() {

        tempFullName = textInputLayoutFullName.getEditText().getText().toString().trim();

        if(TextUtils.isEmpty(tempFullName)){
            textInputLayoutFullName.setError("İsim Boş Geçilmez");
            return false;
        } else {
            return true;
        }
    }

    private boolean passwordCheck() {
        tempPassword = textInputLayoutPassword.getEditText().getText().toString().trim();

        if(tempPassword.isEmpty()){
            textInputLayoutPassword.setError("Şifre Boş Geçilemez");
            return false;
        }else if(tempPassword.length()<6){
            textInputLayoutPassword.setError("Şifre zayıf");
            return false;
        }else{
            textInputLayoutPassword.setError(null);
            return true;
        }
    }

    private boolean emailCheck() {
        tempEmail = textInputLayoutEmail.getEditText().getText().toString().trim();

        if(tempEmail.isEmpty()){
            textInputLayoutEmail.setError("E-Mail Boş Geçilemez");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(tempEmail).matches()){
            textInputLayoutEmail.setError("Geçerli Bir E-Mail Adresi Girin");
            return false;
        }else{
            textInputLayoutEmail.setError(null);
            return true;
        }
    }

    private void setReference() {
        gender = new String[]{"Cinsiyet", "Belirtmek İstemiyorum", "Erkek", "Kadın",};
        spinnerGender = findViewById(R.id.activityUserSettingsTextInputLayoutGender);
        circleImageView = findViewById(R.id.activityUserSettingsProfileImageView);
        textInputLayoutEmail = findViewById(R.id.activityUserSettingsTextInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.activityUserSettingsTextInputLayoutPassword);
        textInputLayoutFullName = findViewById(R.id.activityUserSettingsTextInputLayoutFullName);
        textInputLayoutPhoneNumber = findViewById(R.id.activityUserSettingsTextInputLayoutPhoneNumber);
        buttonSave = findViewById(R.id.activityUserSettingsButtonSave);
    }

    private void spinnerLoad() {

        final List<String> plantsList = new ArrayList<>(Arrays.asList(gender));

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
        spinnerGender.setAdapter(spinnerArrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null ){
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                circleImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            uploadPicture();

        }
    }
}
