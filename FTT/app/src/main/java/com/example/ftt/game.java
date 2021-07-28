package com.example.ftt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class game extends AppCompatActivity {

    private int randCards, end, type, read, randWtBtn, randWgBtn, turn, randBtn1, randBtn2, randBtn3, randBtn4, randMix, timer, minutes, sec;
    public TextView synopsis, chrono;
    private Button buttonback, buttonAnswer1, buttonAnswer2, buttonAnswer3, buttonAnswer4;
    private String story, name="", nameWrong, typeDB, timeFormat, timeLeft;
    private long time;
    private Boolean backClicked, btnClicked;
    private MediaPlayer playerEnd, clickPlayer, wrongPlayer, rightPlayer;

    //NetworkListener networkListener = new NetworkListener();

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference root = db.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        this.synopsis = findViewById(R.id.synopsis);
        this.chrono = findViewById(R.id.chrono);
        this.buttonback = findViewById(R.id.back);
        this.buttonAnswer1 = findViewById(R.id.buttonAnswer1);
        this.buttonAnswer2 = findViewById(R.id.buttonAnswer2);
        this.buttonAnswer3 = findViewById(R.id.buttonAnswer3);
        this.buttonAnswer4 = findViewById(R.id.buttonAnswer4);

        // Get turn
        turn = ((globalTurn)this.getApplication()).getTurn();

        // Get if timer already started or not
        timer = ((globalTurn)this.getApplication()).getTimer();

        // Set to false button back clicked
        ((globalTurn)this.getApplication()).setGameCanceled();

        ((globalTurn)this.getApplication()).setBtnClicked();

        // Get random card selected
        if (getIntent().hasExtra("random")) {
            randCards = getIntent().getIntExtra("random", 0);
        }

        if (getIntent().hasExtra("time")) {
            timeLeft = getIntent().getStringExtra("time");
        }

        playerEnd = MediaPlayer.create(game.this, R.raw.end_tick_tock);
        clickPlayer = MediaPlayer.create(game.this, R.raw.button_click);
        rightPlayer = MediaPlayer.create(game.this, R.raw.right_answer);
        wrongPlayer = MediaPlayer.create(game.this, R.raw.wrong_answer);

        // Timer settings
        CountDownTimer ct = new CountDownTimer(15000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                time = millisUntilFinished;
                minutes = (int)(time/1000)/60;
                sec = (int)(time/1000)%60;
                timeFormat = String.format(Locale.getDefault(), "\n\n%02d:%02d", minutes, sec);
                //System.out.println("####################################### TIME ON TICK = "+timeFormat);
                chrono.setText(timeFormat);
                chrono.setTextColor(Color.BLACK);
                if (time <= 10000){
                    chrono.setTextColor(Color.RED);
                }
                end = ((globalTurn)getApplication()).getEnd();
                backClicked = ((globalTurn)getApplication()).getGameCanceled();
                btnClicked = ((globalTurn)getApplication()).getBtnClicked();
                if (end == 10 || backClicked){
                    playerEnd.start();
                    System.out.println("************************************** CHRONO CANCELED END ON TICK");
                    System.out.println("************************************** BACK BUTTON CLICKED : "+backClicked);
                    this.cancel();
                }
                if (btnClicked){
                    Intent next = new Intent(getApplicationContext(), game.class);
                    randCards = randomCards.randomC(18);
                    next.putExtra("random", randCards);
                    next.putExtra("time", timeFormat);
                    startActivity(next);
                    overridePendingTransition(0,0);
                    finish();
                }
            }
            @Override
            public void onFinish() {
                playerEnd.start();
                end = ((globalTurn)getApplication()).getEnd();
                backClicked = ((globalTurn)getApplication()).getGameCanceled();
                if (end == 10){
                    Intent timesup = new Intent(getApplicationContext(), scores.class);
                    System.out.println("************************************** CHRONO CANCELED END");
                    this.cancel();
                    startActivity(timesup);
                    finish();
                }
                else{
                    if (backClicked){
                        Intent cancelGame = new Intent(getApplicationContext(), teams.class);
                        this.cancel();
                        startActivity(cancelGame);
                        finish();
                    }
                    else {
                        Intent timesup = new Intent(getApplicationContext(), nextTurn.class);
                        System.out.println("************************************** CHRONO CANCELED");
                        this.cancel();
                        startActivity(timesup);
                        finish();
                    }
                }
            }
        };

        // Start timer
        if (timer == 1){
            ct.start();
            ((globalTurn)this.getApplication()).startTimer();
        }

        // TODO : Display chrono
        chrono.setText(timeLeft);
        chrono.setTextColor(Color.BLACK);

        // Get type cards
        type = ((globalTurn)this.getApplication()).getType();
        switch (type){
            case 1:
                typeDB = "Films";
                break;
            case 2:
                typeDB = "Series";
                break;
            case 3:
                typeDB = "Mangas";
                break;
            case 4:
                typeDB = "Cartoons";
                break;
            case 5:
                typeDB = "Mix";
                randMix = randomCards.randomC(4);
                switch (randMix){
                    case 1:
                        typeDB = "Films";
                        break;
                    case 2:
                        typeDB = "Series";
                        break;
                    case 3:
                        typeDB = "Mangas";
                        break;
                    case 4:
                        typeDB = "Cartoons";
                        break;
                }
                break;
        }

        // Random button correct answer
        randWtBtn = randomCards.randomC(4);

        // Get a card not already picked
        read = ((globalTurn)getApplication()).checkRead(randCards);
        while (read!=0){
            randCards = randomCards.randomC(18);
            read = ((globalTurn)getApplication()).checkRead(randCards);
        }

        // Read card from DB
        Query query;
        query = root.child(String.valueOf(typeDB)).child(String.valueOf(randCards));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get synopsis
                    story = snapshot.child("Synopsis").getValue().toString();
                    // Get name of story
                    name = snapshot.child("Name").getValue().toString();
                    synopsis.setText(story);
                    // Set story to read
                    ((globalTurn)getApplication()).setRead(randCards);

                    // Set text buttons
                    switch (randWtBtn) {
                        case 1:
                            buttonAnswer1.setText(name);
                            // Random buttons wrong answers
                            randWgBtn = randomCards.randomC(18);
                            while (randWgBtn == randCards){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn2 = randWgBtn;
                            getName(2, randBtn2);
                            randWgBtn = randomCards.randomC(18);
                            while(randWgBtn == randCards || randWgBtn == randBtn2){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn3 = randWgBtn;
                            getName(3, randBtn3);
                            randWgBtn = randomCards.randomC(18);
                            while (randWgBtn == randCards || randWgBtn == randBtn2 || randWgBtn == randBtn3){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn4 = randWgBtn;
                            getName(4, randBtn4);
                            break;
                        case 2:
                            buttonAnswer2.setText(name);
                            randWgBtn = randomCards.randomC(18);
                            while (randWgBtn == randCards){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn1 = randWgBtn;
                            getName(1, randBtn1);
                            randWgBtn = randomCards.randomC(18);
                            while(randWgBtn == randCards || randWgBtn == randBtn1){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn3 = randWgBtn;
                            getName(3, randBtn3);
                            randWgBtn = randomCards.randomC(18);
                            while (randWgBtn == randCards || randWgBtn == randBtn1 || randWgBtn == randBtn3){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn4 = randWgBtn;
                            getName(4, randBtn4);
                            break;
                        case 3:
                            buttonAnswer3.setText(name);
                            randWgBtn = randomCards.randomC(18);
                            while (randWgBtn == randCards){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn1 = randWgBtn;
                            getName(1, randBtn1);
                            randWgBtn = randomCards.randomC(18);
                            while(randWgBtn == randCards || randWgBtn == randBtn1){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn2 = randWgBtn;
                            getName(2, randBtn2);
                            randWgBtn = randomCards.randomC(18);
                            while (randWgBtn == randCards || randWgBtn == randBtn1 || randWgBtn == randBtn2){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn4 = randWgBtn;
                            getName(4, randBtn4);
                            break;
                        case 4:
                            buttonAnswer4.setText(name);
                            randWgBtn = randomCards.randomC(18);
                            while (randWgBtn == randCards){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn1 = randWgBtn;
                            getName(1, randBtn1);
                            randWgBtn = randomCards.randomC(18);
                            while(randWgBtn == randCards || randWgBtn == randBtn1){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn2 = randWgBtn;
                            getName(2, randBtn2);
                            randWgBtn = randomCards.randomC(18);
                            while (randWgBtn == randCards || randWgBtn == randBtn1 || randWgBtn == randBtn2){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn3 = randWgBtn;
                            getName( 3, randBtn3);
                            break;
                    }
                }
                else{
                    synopsis.setText("NO DATA FOUND ! ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Handle answers
        buttonAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment points to team
                if (randWtBtn == 1){
                    rightPlayer.start();
                    synopsis.setTextColor(0xFF12DC0C);
                    ((globalTurn)getApplication()).addPoints(turn);
                }
                else {
                    wrongPlayer.start();
                    synopsis.setTextColor(Color.RED);
                }
                // Increment number of card read
                ((globalTurn)getApplication()).addEnd();
                // If players read 40 cards end of game
                end = ((globalTurn)getApplication()).getEnd();
                if (end == 10){
                    Intent endGame = new Intent(getApplicationContext(), scores.class);
                    ct.cancel();
                    startActivity(endGame);
                    overridePendingTransition(R.anim.transition_zoom_in, R.anim.transition_static_anim);
                    finish();
                }
                else {
                    /*Intent next = new Intent(getApplicationContext(), game.class);
                    randCards = randomCards.randomC(18);
                    next.putExtra("random", randCards);
                    overridePendingTransition(R.anim.transition_zoom_in, R.anim.transition_static_anim);
                    startActivity(next);
                    overridePendingTransition(0,0);
                    finish();*/
                    ((globalTurn)getApplication()).btnIsClicked();
                }
            }
        });
        buttonAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment points to team
                if (randWtBtn == 2){
                    rightPlayer.start();
                    synopsis.setTextColor(0xFF12DC0C);
                    ((globalTurn)getApplication()).addPoints(turn);
                }
                else {
                    wrongPlayer.start();
                    synopsis.setTextColor(Color.RED);
                }
                ((globalTurn)getApplication()).addEnd();
                // If players read 20 cards end of game
                end = ((globalTurn)getApplication()).getEnd();
                if (end == 10){
                    Intent endGame = new Intent(getApplicationContext(), scores.class);
                    ct.cancel();
                    startActivity(endGame);
                    overridePendingTransition(R.anim.transition_zoom_in, R.anim.transition_static_anim);
                    finish();
                }
                else {
                    Intent next = new Intent(getApplicationContext(), game.class);
                    randCards = randomCards.randomC(18);
                    next.putExtra("random", randCards);
                    startActivity(next);
                    overridePendingTransition(0,0);
                    finish();
                }
            }
        });
        buttonAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment points to team
                if (randWtBtn == 3){
                    rightPlayer.start();
                    synopsis.setTextColor(0xFF12DC0C);
                    ((globalTurn)getApplication()).addPoints(turn);
                }
                else {
                    wrongPlayer.start();
                    synopsis.setTextColor(Color.RED);
                }
                ((globalTurn)getApplication()).addEnd();
                // If players read 20 cards end of game
                end = ((globalTurn)getApplication()).getEnd();
                if (end == 10){
                    Intent endGame = new Intent(getApplicationContext(), scores.class);
                    ct.cancel();
                    startActivity(endGame);
                    overridePendingTransition(R.anim.transition_zoom_in, R.anim.transition_static_anim);
                    finish();
                }
                else {
                    Intent next = new Intent(getApplicationContext(), game.class);
                    randCards = randomCards.randomC(18);
                    next.putExtra("random", randCards);
                    startActivity(next);
                    overridePendingTransition(0,0);
                    finish();
                }
            }
        });
        buttonAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment points to team
                if (randWtBtn == 4){
                    rightPlayer.start();
                    synopsis.setTextColor(0xFF12DC0C);
                    ((globalTurn)getApplication()).addPoints(turn);
                }
                else {
                    wrongPlayer.start();
                    synopsis.setTextColor(Color.RED);
                }
                ((globalTurn)getApplication()).addEnd();
                // If players read 20 cards end of game
                end = ((globalTurn)getApplication()).getEnd();
                if (end == 10){
                    Intent endGame = new Intent(getApplicationContext(), scores.class);
                    ct.cancel();
                    startActivity(endGame);
                    overridePendingTransition(R.anim.transition_zoom_in, R.anim.transition_static_anim);
                    finish();
                }
                else {
                    Intent next = new Intent(getApplicationContext(), game.class);
                    randCards = randomCards.randomC(18);
                    next.putExtra("random", randCards);
                    startActivity(next);
                    overridePendingTransition(0,0);
                    finish();
                }
            }
        });

        // Go back to mode menu
        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPlayer.start();
                Intent backP = new Intent(getApplicationContext(), teams.class);
                ((globalTurn)getApplication()).gameIsCanceled();
                ct.cancel();
                startActivity(backP);
                overridePendingTransition(R.anim.transition_static_anim, R.anim.transition_zoom_out);
                finish();
            }
        });

        playerEnd.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                System.out.println("************************************** MUSIC END RELEASE");
                mp.reset();
                mp.release();
            }
        });
        rightPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                System.out.println("************************************** MUSIC RIGHT RELEASE");
                mp.reset();
                mp.release();
            }
        });
        wrongPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                System.out.println("************************************** MUSIC WRONG RELEASE");
                mp.reset();
                mp.release();
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

    // Get name story where num = randWgBtn
    public void getName(int btn, int rand){
        Query query;
        query = root.child(String.valueOf(typeDB)).child(String.valueOf(rand)).child("Name");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    nameWrong = snapshot.getValue().toString();
                    switch (btn){
                        case 1:
                            buttonAnswer1.setText(nameWrong);
                            break;
                        case 2:
                            buttonAnswer2.setText(nameWrong);
                            break;
                        case 3:
                            buttonAnswer3.setText(nameWrong);
                            break;
                        case 4:
                            buttonAnswer4.setText(nameWrong);
                            break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Disable phone's back button
    public void onBackPressed(){

    }

    // Internet connection
    /*@Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkListener);
        super.onStop();
    }*/
}