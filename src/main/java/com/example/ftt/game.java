package com.example.ftt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class game extends AppCompatActivity {

    private int randCards;
    private TextView synopsis;

    /*FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference root = db.getReference();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.synopsis = (TextView) findViewById(R.id.textViewSynospsis);

        if (getIntent().hasExtra("ID")) {
            randCards = getIntent().getIntExtra("ID", 0);
        }
        synopsis.setText(randCards);

    }
}