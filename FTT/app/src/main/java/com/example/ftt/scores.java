package com.example.ftt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;


public class scores extends AppCompatActivity {

    private static int TIME_OUT = 4000;
    private TextView t1, s1, t2, s2, t3, s3, t4, s4, textViewWinner;
    private int nbTeams, score;
    private int [] winner = new int[5];
    private  String txt;
    private MediaPlayer mediaPlayer;

    NetworkListener networkListener = new NetworkListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scores);

        this.t1 = findViewById(R.id.textViewT1);
        this.s1 = findViewById(R.id.textViewS1);
        this.t2 = findViewById(R.id.textViewT2);
        this.s2 = findViewById(R.id.textViewS2);
        this.t3 = findViewById(R.id.textViewT3);
        this.s3 = findViewById(R.id.textViewS3);
        this.t4 = findViewById(R.id.textViewT4);
        this.s4 = findViewById(R.id.textViewS4);
        this.textViewWinner = findViewById(R.id.textViewWinner);

        nbTeams = ((globalTurn)this.getApplication()).getNbTeams();

        mediaPlayer = MediaPlayer.create(scores.this, R.raw.fanfare);
        mediaPlayer.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(scores.this, teams.class);
                startActivity(intent);
                overridePendingTransition(R.anim.transition_zoom_in, R.anim.transition_static_anim);
                finish();
            }
        },TIME_OUT);

        // TODO : handle exequo
        winner = ((globalTurn)this.getApplication()).getWinner();
        if (winner.length == 2){
            txt = "Team"+winner[0]+" win !";
        }
        else {
            for (int i=0; i<winner.length; i++){
              if(i<winner.length-1){
                  txt += "Team"+winner[i]+ " and ";
              }
              else {
                  txt += "Team"+winner[i];
              }
            }
            txt+= " win !";
        }
        textViewWinner.setText(txt);

        for (int i=1; i<=nbTeams; i++){
            score = ((globalTurn)this.getApplication()).getPoints(i);
            switch (i){
                case 1:
                    t1.setText("Team1");
                    s1.setText(String.valueOf(score));
                    break;
                case 2:
                    t2.setText("Team2");
                    s2.setText(String.valueOf(score));
                    break;
                case 3:
                    t3.setText("Team3");
                    s3.setText(String.valueOf(score));
                    break;
                case 4:
                    t4.setText("Team4");
                    s4.setText(String.valueOf(score));
                    break;
            }
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                System.out.println("************************************** MUSIC WINNER RELEASE");
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