package com.example.codingtr.lolquery.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.codingtr.lolquery.Home_Activity;
import com.example.codingtr.lolquery.Management.SessionManagement;
import com.example.codingtr.lolquery.R;

public class Welcome_Activity extends AppCompatActivity {

    private SessionManagement sessionManagement;
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        button = findViewById(R.id.activityWelcomeButton);
        textView = findViewById(R.id.activityWelcomeTextView);

        sessionManagement = new SessionManagement(this);

        if (sessionManagement.isLoggin() == true){
            startActivity(new Intent(this, Home_Activity.class));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Welcome_Activity.this, Login_Activity.class));
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Welcome_Activity.this, SignUp_Activity.class));
            }
        });


    }
}
