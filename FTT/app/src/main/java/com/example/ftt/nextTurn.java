package com.example.ftt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.TextView;


public class nextTurn extends AppCompatActivity {

    private int randCards, turn, nextTurn, maxTurn;
    private TextView counter, turnTxt;
    private long timeLeft2;
    private String txt;
    private MediaPlayer mediaPlayer, playerEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_next_turn);

        this.turnTxt = findViewById(R.id.turn);
        this.counter = findViewById(R.id.counter);


        // Get turn and increment
        turn = ((globalTurn)this.getApplication()).getTurn();
        nextTurn = turn+1;
        maxTurn = ((globalTurn)this.getApplication()).getNbTeams();
        if (nextTurn > maxTurn){
            nextTurn = 1;
        }
        ((globalTurn)this.getApplication()).setTurn(nextTurn);

        mediaPlayer = MediaPlayer.create(nextTurn.this, R.raw.tick_tock_turn);
        mediaPlayer.setLooping(true);
        playerEnd = MediaPlayer.create(nextTurn.this, R.raw.end_tick_tock);

        new CountDownTimer(4000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                mediaPlayer.start();
                timeLeft2 = millisUntilFinished;
                int sec = (int)(timeLeft2/1000)%60;
                txt = String.valueOf(sec);
                counter.setText(txt);
                txt = "Team"+nextTurn+" get ready!";
                turnTxt.setText(txt);
            }
            @Override
            public void onFinish() {
                mediaPlayer.stop();
                playerEnd.start();
                Intent next = new Intent(getApplicationContext(), game.class);
                randCards = randomCards.randomC(18);
                next.putExtra("random", randCards);
                ((globalTurn)getApplication()).setTimer();
                startActivity(next);
                finish();
            }
        }.start();
    }
}