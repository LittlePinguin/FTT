package com.example.ftt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class scores extends AppCompatActivity {

    private static int TIME_OUT = 7000;
    private TextView t1, s1, t2, s2, t3, s3, t4, s4, textViewWinner;
    private int nbTeams, score, winner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        this.t1 = (TextView)findViewById(R.id.textViewT1);
        this.s1 = (TextView)findViewById(R.id.textViewS1);
        this.t2 = (TextView)findViewById(R.id.textViewT2);
        this.s2 = (TextView)findViewById(R.id.textViewS2);
        this.t3 = (TextView)findViewById(R.id.textViewT3);
        this.s3 = (TextView)findViewById(R.id.textViewS3);
        this.t4 = (TextView)findViewById(R.id.textViewT4);
        this.s4 = (TextView)findViewById(R.id.textViewS4);
        this.textViewWinner = (TextView)findViewById(R.id.textViewWinner);

        nbTeams = ((globalTurn)this.getApplication()).getNbTeams();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(scores.this, mode_menu.class);
                startActivity(intent);
                finish();
            }
        },TIME_OUT);

        winner = ((globalTurn)this.getApplication()).getWinner();
        textViewWinner.setText("Team"+winner+" won !");

        for (int i=1; i<=nbTeams; i++){
            score = ((globalTurn)this.getApplication()).getPoints(i);
            //System.out.println("############################# SCORE = "+score);
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
    }
}