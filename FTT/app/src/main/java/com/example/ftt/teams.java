package com.example.ftt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class teams extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button next, back;
    private int nbTeams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_teams);

        this.next = (Button)findViewById(R.id.buttonnext);
        this.back = (Button)findViewById(R.id.buttonback);

        // Set variables
        ((globalTurn)this.getApplication()).setTurn(0);
        ((globalTurn)this.getApplication()).initRead();
        ((globalTurn)this.getApplication()).setEnd();
        ((globalTurn)getApplication()).setTimer();

        // Spinner settings
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Go to mode selection
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextP = new Intent(getApplicationContext(), mode_menu.class);
                // Store nb teams
                ((globalTurn)getApplication()).setNbTeams(nbTeams);
                ((globalTurn)getApplication()).initPoints(nbTeams);
                startActivity(nextP);
                finish();
            }
        });

        // Go back to main menu
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent previousP = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(previousP);
                finish();
            }
        });
    }

    // Get number of teams
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        nbTeams = Integer.parseInt(parent.getItemAtPosition(position).toString());
    }

    // Error message if no teams selected
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast toast = Toast.makeText(getApplicationContext(), " Please select a number of teams.", Toast.LENGTH_SHORT);
        toast.show();
    }
}