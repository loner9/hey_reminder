package info.project.hey.Class;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import info.project.hey.R;

public class Alert extends AppCompatActivity {
    MediaPlayer mp;
    int reso=R.raw.rooster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        mp = MediaPlayer.create(getApplicationContext(),reso);
        mp.start();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String msg = "Alert: " + getIntent().getExtras().getString(getString(R.string.title));
        builder.setMessage(msg).setCancelable(false).setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Alert.this.finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.release();
    }
}
