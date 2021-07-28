package com.example.ftt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splash_screen extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT = 2000;
    Animation sideanim, topanim, bottomannim;
    ImageView fennec, under;
    TextView gloups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //getSupportActionBar().hide();

        this.under = (ImageView) findViewById(R.id.undergloups);
        this.fennec = (ImageView)findViewById(R.id.fennec);
        this.gloups = (TextView)findViewById(R.id.gloups);

        sideanim = AnimationUtils.loadAnimation(this, R.anim.undergloups);
        topanim = AnimationUtils.loadAnimation(this, R.anim.fennec);
        bottomannim = AnimationUtils.loadAnimation(this, R.anim.gloups);

        under.setAnimation(sideanim);
        gloups.setAnimation(bottomannim);
        fennec.setAnimation(topanim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash_screen.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.transition_zoom_in, R.anim.transition_static_anim);
                finish();
            }
        },SPLASH_SCREEN_TIME_OUT);
    }
}