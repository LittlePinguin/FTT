package com.example.ftt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private String timeLeft, story, name="", nameWrong, typeDB, timeFormat;
    private long time;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference root = db.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        this.synopsis = (TextView) findViewById(R.id.synopsis);
        this.chrono = (TextView)findViewById(R.id.chrono);
        this.buttonback = (Button)findViewById(R.id.back);
        this.buttonAnswer1 = (Button)findViewById(R.id.buttonAnswer1);
        this.buttonAnswer2 = (Button)findViewById(R.id.buttonAnswer2);
        this.buttonAnswer3 = (Button)findViewById(R.id.buttonAnswer3);
        this.buttonAnswer4 = (Button)findViewById(R.id.buttonAnswer4);

        // Get turn
        turn = ((globalTurn)this.getApplication()).getTurn();

        // Get timer
        timer = ((globalTurn)this.getApplication()).getTimer();

        if (getIntent().hasExtra("random")) {
            randCards = getIntent().getIntExtra("random", 0);
            //System.out.println("######################################### RANDOM = "+randCards);
        }
        //if (getIntent().hasExtra("time")) {
            /*timeLeft = getIntent().getStringExtra("time");
            System.out.println("######################################### TIME = "+timeLeft);*/
        //}

        CountDownTimer ct = new CountDownTimer(5000, 1000){
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
            }
            @Override
            public void onFinish() {
                Intent timesup = new Intent(getApplicationContext(), nextTurn.class);
                startActivity(timesup);
                finish();
            }
        };

        if (timer == 1){
            ct.start();
            ((globalTurn)this.getApplication()).startTimer();
        }

        // TODO : Display chrono


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
                randMix = randomCards.randomC(5);
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
        System.out.println("######################################### RANDOM CORRECT BUTTON = "+randWtBtn);

        read = ((globalTurn)getApplication()).checkRead(randCards);
        while (read!=0){
            randCards = randomCards.randomC(18);
            read = ((globalTurn)getApplication()).checkRead(randCards);
        }

        // Read cards from DB
        Query query;
        query = root.child(String.valueOf(typeDB)).child(String.valueOf(randCards));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    story = snapshot.child("Synopsis").getValue().toString();
                    name = snapshot.child("Name").getValue().toString();
                    synopsis.setText(story);
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
                            getName(name, 2, randBtn2);
                            randWgBtn = randomCards.randomC(18);
                            while(randWgBtn == randCards || randWgBtn == randBtn2){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn3 = randWgBtn;
                            getName(name, 3, randBtn3);
                            randWgBtn = randomCards.randomC(18);
                            while (randWgBtn == randCards || randWgBtn == randBtn2 || randWgBtn == randBtn3){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn4 = randWgBtn;
                            getName(name, 4, randBtn4);
                            break;
                        case 2:
                            buttonAnswer2.setText(name);
                            randWgBtn = randomCards.randomC(18);
                            while (randWgBtn == randCards){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn1 = randWgBtn;
                            getName(name, 1, randBtn1);
                            randWgBtn = randomCards.randomC(18);
                            while(randWgBtn == randCards || randWgBtn == randBtn1){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn3 = randWgBtn;
                            getName(name, 3, randBtn3);
                            randWgBtn = randomCards.randomC(18);
                            while (randWgBtn == randCards || randWgBtn == randBtn1 || randWgBtn == randBtn3){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn4 = randWgBtn;
                            getName(name, 4, randBtn4);
                            break;
                        case 3:
                            buttonAnswer3.setText(name);
                            randWgBtn = randomCards.randomC(18);
                            while (randWgBtn == randCards){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn1 = randWgBtn;
                            getName(name, 1, randBtn1);
                            randWgBtn = randomCards.randomC(18);
                            while(randWgBtn == randCards || randWgBtn == randBtn1){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn2 = randWgBtn;
                            getName(name, 2, randBtn2);
                            randWgBtn = randomCards.randomC(18);
                            while (randWgBtn == randCards || randWgBtn == randBtn1 || randWgBtn == randBtn2){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn4 = randWgBtn;
                            getName(name, 4, randBtn4);
                            break;
                        case 4:
                            buttonAnswer4.setText(name);
                            randWgBtn = randomCards.randomC(18);
                            while (randWgBtn == randCards){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn1 = randWgBtn;
                            getName(name, 1, randBtn1);
                            randWgBtn = randomCards.randomC(18);
                            while(randWgBtn == randCards || randWgBtn == randBtn1){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn2 = randWgBtn;
                            getName(name, 2, randBtn2);
                            randWgBtn = randomCards.randomC(18);
                            while (randWgBtn == randCards || randWgBtn == randBtn1 || randWgBtn == randBtn2){
                                randWgBtn = randomCards.randomC(18);
                            }
                            randBtn3 = randWgBtn;
                            getName(name, 3, randBtn3);
                            break;
                    }
                }
                else{
                    synopsis.setText("NO DATA FOUND ! : "+randCards);
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
                    ((globalTurn)getApplication()).addPoints(turn);
                    int pt = ((globalTurn)getApplication()).getPoints(turn);
                    //System.out.println("######################################### POINTS = "+pt+"  TEAM = "+turn);
                }
                ((globalTurn)getApplication()).addEnd();
                // If players read 40 cards end of game
                end = ((globalTurn)getApplication()).getEnd();
                System.out.println("######################################### END = "+end);
                if (end == 10){
                    System.out.println("######################################### END GAME");
                    Intent endGame = new Intent(getApplicationContext(), scores.class);
                    ct.cancel();
                    startActivity(endGame);
                    finish();
                }
                else {
                    Intent next = new Intent(getApplicationContext(), game.class);
                    randCards = randomCards.randomC(18);
                    next.putExtra("random", randCards);
                    //next.putExtra("time", timeLeft);
                    startActivity(next);
                    finish();
                }
            }
        });
        buttonAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment points to team
                if (randWtBtn == 2){
                    ((globalTurn)getApplication()).addPoints(turn);
                    int pt = ((globalTurn)getApplication()).getPoints(turn);
                    //System.out.println("######################################### POINTS = "+pt+"  TEAM = "+turn);
                }
                ((globalTurn)getApplication()).addEnd();
                // If players read 20 cards end of game
                end = ((globalTurn)getApplication()).getEnd();
                System.out.println("######################################### END = "+end);
                if (end == 10){
                    System.out.println("######################################### END GAME");
                    Intent endGame = new Intent(getApplicationContext(), scores.class);
                    ct.cancel();
                    startActivity(endGame);
                    finish();
                }
                else {
                    Intent next = new Intent(getApplicationContext(), game.class);
                    randCards = randomCards.randomC(18);
                    next.putExtra("random", randCards);
                    //next.putExtra("time", timeLeft);
                    startActivity(next);
                    finish();
                }
            }
        });
        buttonAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment points to team
                if (randWtBtn == 3){
                    ((globalTurn)getApplication()).addPoints(turn);
                    int pt = ((globalTurn)getApplication()).getPoints(turn);
                    //System.out.println("######################################### POINTS = "+pt+"  TEAM = "+turn);
                }
                ((globalTurn)getApplication()).addEnd();
                // If players read 20 cards end of game
                end = ((globalTurn)getApplication()).getEnd();
                System.out.println("######################################### END = "+end);
                if (end == 10){
                    System.out.println("######################################### END GAME");
                    Intent endGame = new Intent(getApplicationContext(), scores.class);
                    ct.cancel();
                    startActivity(endGame);
                    finish();
                }
                else {
                    Intent next = new Intent(getApplicationContext(), game.class);
                    randCards = randomCards.randomC(18);
                    next.putExtra("random", randCards);
                    //next.putExtra("time", timeLeft);
                    startActivity(next);
                    finish();
                }
            }
        });
        buttonAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment points to team
                if (randWtBtn == 4){
                    ((globalTurn)getApplication()).addPoints(turn);
                    int pt = ((globalTurn)getApplication()).getPoints(turn);
                    //System.out.println("######################################### POINTS = "+pt+"  TEAM = "+turn);
                }
                ((globalTurn)getApplication()).addEnd();
                // If players read 20 cards end of game
                end = ((globalTurn)getApplication()).getEnd();
                System.out.println("######################################### END = "+end);
                if (end == 10){
                    System.out.println("######################################### END GAME");
                    Intent endGame = new Intent(getApplicationContext(), scores.class);
                    ct.cancel();
                    startActivity(endGame);
                    finish();
                }
                else {
                    Intent next = new Intent(getApplicationContext(), game.class);
                    randCards = randomCards.randomC(18);
                    next.putExtra("random", randCards);
                    //next.putExtra("time", timeLeft);
                    startActivity(next);
                    finish();
                }
            }
        });

        // Go back to mode menu
        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backP = new Intent(getApplicationContext(), nb_players.class);
                ct.cancel();
                startActivity(backP);
                finish();
            }
        });
    }

    public void getName(String name, int btn, int rand){
        // Get name story where num = randWgBtn
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
}