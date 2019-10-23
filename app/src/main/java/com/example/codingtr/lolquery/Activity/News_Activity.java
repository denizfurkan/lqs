package com.example.codingtr.lolquery.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.codingtr.lolquery.R;

public class News_Activity extends AppCompatActivity {
    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Bundle extras = getIntent().getExtras();
        link = extras.getString("send_url");

        Log.e("News_Activity", link);
    }
}
