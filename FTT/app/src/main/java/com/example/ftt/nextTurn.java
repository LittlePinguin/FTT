package com.example.ftt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
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

    NetworkListener networkListener = new NetworkListener();

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
                mediaPlayer.reset();
                mediaPlayer.release();
                playerEnd.start();
                Intent next = new Intent(getApplicationContext(), game.class);
                randCards = randomCards.randomC(18);
                next.putExtra("random", randCards);
                ((globalTurn)getApplication()).setTimer();
                startActivity(next);
                overridePendingTransition(R.anim.transition_zoom_in, R.anim.transition_static_anim);
                finish();
            }
        }.start();
        playerEnd.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                System.out.println("************************************** MUSIC RING BELL RELEASE");
                mp.reset();
                mp.release();
            }
        });
    }

    // Disable phone's back button
    public void onBackPressed(){}

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