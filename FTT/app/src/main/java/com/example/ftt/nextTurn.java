package com.example.ftt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Locale;

public class nextTurn extends AppCompatActivity {

    private int randCards, minutes, sec, turn, nextTurn, maxTurn, end;
    private TextView counter, turnTxt;
    private long timeLeft, timeLeft2;
    private String timeFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_turn);

        this.turnTxt = (TextView)findViewById(R.id.turn);
        this.counter = (TextView)findViewById(R.id.counter);


        // Get turn and increment
        turn = ((globalTurn)this.getApplication()).getTurn();
        nextTurn = turn+1;
        maxTurn = ((globalTurn)this.getApplication()).getNbTeams();
        if (nextTurn > maxTurn){
            nextTurn = 1;
        }
        ((globalTurn)this.getApplication()).setTurn(nextTurn);

        new CountDownTimer(4000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft2 = millisUntilFinished;
                int sec = (int)(timeLeft2/1000)%60;
                counter.setText(""+ sec);
                turnTxt.setText("Team"+nextTurn+" get ready !");
            }
            @Override
            public void onFinish() {
                Intent next = new Intent(getApplicationContext(), game.class);
                randCards = randomCards.randomC(18);
                next.putExtra("random", randCards);
                timer();
                //System.out.println("####################################### TIME NEXT TURN = "+timeFormat);
                next.putExtra("time", timeFormat);
                startActivity(next);
                finish();
            }
        }.start();
    }
    public void timer (){
        // Timer
        CountDownTimer ct = new CountDownTimer(20000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                minutes = (int)(timeLeft/1000)/60;
                sec = (int)(timeLeft/1000)%60;
                timeFormat = String.format(Locale.getDefault(), "\n\n%02d:%02d", minutes, sec);
                //System.out.println("####################################### TIME ON TICK = "+timeFormat);
            }
            @Override
            public void onFinish() {
                Intent timesup = new Intent(getApplicationContext(), nextTurn.class);
                startActivity(timesup);
                finish();
            }
        };

        end = ((globalTurn)this.getApplication()).getEnd();
        if (end < 10){
            ct.start();
            System.out.println("####################################### COUNTDOWN START = "+end);
        }
        else{
            ct.cancel();
            System.out.println("####################################### COUNTDOWN CANCELED = "+end);
        }
    }
}