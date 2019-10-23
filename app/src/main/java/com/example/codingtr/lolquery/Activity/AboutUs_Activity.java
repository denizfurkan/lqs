package com.example.codingtr.lolquery.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.codingtr.lolquery.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUs_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Element versionElement = new Element();
        versionElement.setTitle("Versiyon 1.0");

        Element adversiteEleement = new Element();
        adversiteEleement.setTitle("Geliştiriciler : Furkan Bodur, Alp Metehan Bozdağ ");


        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .addItem(versionElement)
                .addItem(adversiteEleement)
                .addGroup("İletişime Geç")
                .addEmail("denizf.b@outlook.com.tr", "İletişim")
                .addWebsite("http://codingtr.com", "Website")
                .addGitHub("denizfurkan", "GitHub")
                .addInstagram("bunderschriftsteller", "Instagram'dan Takip Et")
                .setDescription("Kardeşliğin Gücü Adına !")
                .create();

        setContentView(aboutPage);

    }

}
