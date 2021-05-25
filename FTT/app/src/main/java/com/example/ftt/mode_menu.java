package com.example.ftt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class mode_menu extends AppCompatActivity {

    private ImageView buttonmanga, buttonfilm, buttonserie, buttoncartoon, buttonmix;
    private Button buttonback;
    private MediaPlayer playmusic;
    private int nbTeams, randCards, max;
    private long teamTurn;
    private ImageView buttonmute;
    public long timeLeft;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_menu);

        this.buttonmanga = (ImageView) findViewById(R.id.buttonmangas);
        this.buttonfilm = (ImageView) findViewById(R.id.buttonfilms);
        this.buttonserie = (ImageView) findViewById(R.id.buttonseries);
        this.buttoncartoon = (ImageView) findViewById(R.id.buttoncartoons);
        this.buttonmix = (ImageView) findViewById(R.id.buttonmix);
        this.buttonback = (Button) findViewById(R.id.buttonback);
        this.buttonmute = (ImageView) findViewById(R.id.buttonmute);

        // TODO PUT THIS ON THE MAIN MENU
        /*// Background music settings
        playmusic = MediaPlayer.create(getApplicationContext(),R.raw.adagio);
        mute.setImageResource(R.drawable.soundconpng);
        playmusic.start();*/

        ((globalTurn)this.getApplication()).setTurn(0);
        ((globalTurn)this.getApplication()).initPoints(nbTeams);
        ((globalTurn)this.getApplication()).initRead();
        ((globalTurn)this.getApplication()).setEnd();

        // Start a manga party
        buttonmanga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(getApplicationContext(), nextTurn.class);
                randCards = randomCards.randomC(18);
                next.putExtra("random", randCards);
                ((globalTurn)getApplication()).setType(3);
                startActivity(next);
                finish();
            }
        });

        // Start a film party
        buttonfilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(getApplicationContext(), nextTurn.class);
                randCards = randomCards.randomC(18);
                next.putExtra("random", randCards);
                ((globalTurn)getApplication()).setType(1);
                startActivity(next);
                finish();
            }
        });

        // Start a serie party
        buttonserie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(getApplicationContext(), nextTurn.class);
                randCards = randomCards.randomC(18);
                next.putExtra("random", randCards);
                ((globalTurn)getApplication()).setType(2);
                startActivity(next);
                finish();
            }
        });

        // Start a cartoon party
        buttoncartoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getApplicationContext(), nextTurn.class);
                randCards = randomCards.randomC(18);
                next.putExtra("random", randCards);
                ((globalTurn)getApplication()).setType(4);
                startActivity(next);
                finish();
            }
        });

        // Start a mix party
        buttonmix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getApplicationContext(), nextTurn.class);
                randCards = randomCards.randomC(18);
                next.putExtra("random", randCards);
                ((globalTurn)getApplication()).setType(5);
                startActivity(next);
                finish();
            }
        });

        /*// Background music handler
        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playmusic.isPlaying()){
                    playmusic.pause();
                    mute.setImageResource(R.drawable.muteicon);
                }
                else{
                    playmusic.start();
                    mute.setImageResource(R.drawable.soundconpng);
                }
            }
        });*/

        // Go back to number teams selection
        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backP = new Intent(getApplicationContext(), nb_players.class);
                startActivity(backP);
                finish();
            }
        });
    }
}