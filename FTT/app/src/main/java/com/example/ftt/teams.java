package com.example.ftt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
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
    private MediaPlayer clickPlayer;

    NetworkListener networkListener = new NetworkListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_teams);

        this.next = (Button)findViewById(R.id.buttonnext);
        this.back = (Button)findViewById(R.id.buttonback);

        clickPlayer = MediaPlayer.create(teams.this, R.raw.button_click);

        // Set variables
        ((globalTurn)this.getApplication()).setTurn(0);
        ((globalTurn)this.getApplication()).initRead();
        ((globalTurn)this.getApplication()).setEnd();
        ((globalTurn)this.getApplication()).setTimer();

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
                clickPlayer.start();
                // Store nb teams
                ((globalTurn)getApplication()).setNbTeams(nbTeams);
                ((globalTurn)getApplication()).initPoints(nbTeams);
                startActivity(nextP);
                overridePendingTransition(R.anim.transition_zoom_in, R.anim.transition_static_anim);
                finish();
            }
        });

        // Go back to main menu
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent previousP = new Intent(getApplicationContext(), MainActivity.class);
                clickPlayer.start();
                startActivity(previousP);
                overridePendingTransition(R.anim.transition_static_anim, R.anim.transition_zoom_out);
                finish();
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