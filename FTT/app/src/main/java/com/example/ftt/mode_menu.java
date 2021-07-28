package com.example.ftt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class mode_menu extends AppCompatActivity {

    private ImageView buttonmanga, buttonfilm, buttonserie, buttoncartoon, buttonmix;
    private Button buttonback;
    private int randCards;
    private ImageView buttonmute;
    private MediaPlayer clickPlayer;

    NetworkListener networkListener = new NetworkListener();

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mode_menu);

        this.buttonmanga = (ImageView) findViewById(R.id.buttonmangas);
        this.buttonfilm = (ImageView) findViewById(R.id.buttonfilms);
        this.buttonserie = (ImageView) findViewById(R.id.buttonseries);
        this.buttoncartoon = (ImageView) findViewById(R.id.buttoncartoons);
        this.buttonmix = (ImageView) findViewById(R.id.buttonmix);
        this.buttonback = (Button) findViewById(R.id.buttonback);
        this.buttonmute = (ImageView) findViewById(R.id.buttonmute);

        clickPlayer = MediaPlayer.create(mode_menu.this, R.raw.button_click);

        // Start a manga party
        buttonmanga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickPlayer.start();
                Intent next = new Intent(getApplicationContext(), nextTurn.class);
                randCards = randomCards.randomC(18);
                next.putExtra("random", randCards);
                ((globalTurn)getApplication()).setType(3);
                ((globalTurn)getApplication()).setTimer();
                startActivity(next);
                overridePendingTransition(R.anim.transition_zoom_in, R.anim.transition_static_anim);
                finish();
            }
        });

        // Start a film party
        buttonfilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickPlayer.start();
                Intent next = new Intent(getApplicationContext(), nextTurn.class);
                randCards = randomCards.randomC(18);
                next.putExtra("random", randCards);
                ((globalTurn)getApplication()).setType(1);
                ((globalTurn)getApplication()).setTimer();
                startActivity(next);
                overridePendingTransition(R.anim.transition_zoom_in, R.anim.transition_static_anim);
                finish();
            }
        });

        // Start a serie party
        buttonserie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickPlayer.start();
                Intent next = new Intent(getApplicationContext(), nextTurn.class);
                randCards = randomCards.randomC(18);
                next.putExtra("random", randCards);
                ((globalTurn)getApplication()).setType(2);
                ((globalTurn)getApplication()).setTimer();
                startActivity(next);
                overridePendingTransition(R.anim.transition_zoom_in, R.anim.transition_static_anim);
                finish();
            }
        });

        // Start a cartoon party
        buttoncartoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPlayer.start();
                Intent next = new Intent(getApplicationContext(), nextTurn.class);
                randCards = randomCards.randomC(18);
                next.putExtra("random", randCards);
                ((globalTurn)getApplication()).setType(4);
                ((globalTurn)getApplication()).setTimer();
                startActivity(next);
                overridePendingTransition(R.anim.transition_zoom_in, R.anim.transition_static_anim);
                finish();
            }
        });

        // Start a mix party
        buttonmix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPlayer.start();
                Intent next = new Intent(getApplicationContext(), nextTurn.class);
                randCards = randomCards.randomC(18);
                next.putExtra("random", randCards);
                ((globalTurn)getApplication()).setType(5);
                startActivity(next);
                overridePendingTransition(R.anim.transition_zoom_in, R.anim.transition_static_anim);
                finish();
            }
        });

        // Go back to number teams selection
        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPlayer.start();
                Intent backP = new Intent(getApplicationContext(), teams.class);
                startActivity(backP);
                overridePendingTransition(R.anim.transition_static_anim, R.anim.transition_zoom_out);
                finish();
            }
        });

        clickPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                System.out.println("************************************** MUSIC CLICK RELEASE");
                mp.reset();
                mp.release();
            }
        });
    }

    // Disable phone's back button
    public void onBackPressed(){

    }

    // Internet connection
    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkListener);
        super.onStop();
    }
}