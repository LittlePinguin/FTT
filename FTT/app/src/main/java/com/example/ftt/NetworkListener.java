package com.example.ftt;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class NetworkListener extends BroadcastReceiver {

    private MediaPlayer clickPlayer;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!CheckConnection.isConnectec(context)){  // No internet
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View layoutDialog = LayoutInflater.from(context).inflate(R.layout.check_internet_dialog, null);
            builder.setView(layoutDialog);

            Button btnRetry = layoutDialog.findViewById(R.id.buttonRetry);
            clickPlayer = MediaPlayer.create(context, R.raw.button_click);

            // Show dialog
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);

            dialog.getWindow().setGravity(Gravity.CENTER);

            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickPlayer.start();
                    dialog.dismiss();
                    //Intent menu = new Intent(context.getApplicationContext(), MainActivity.class);
                    //context.startActivity(menu);
                    onReceive(context, intent);
                }
            });
        }
    }
}
