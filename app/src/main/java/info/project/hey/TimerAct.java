package info.project.hey;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class TimerAct extends AppCompatActivity implements View.OnClickListener{
    private long timeCountInMilliSeconds = 60000;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;

    private ProgressBar progressBarCircle;
    private EditText editTextMinute;
    private TextView textViewTime;
    private ImageView imageViewReset;
    private ImageView imageViewStartStop;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_timer);
        super.onCreate(savedInstanceState);
        progressBarCircle = findViewById(R.id.ProgresView);
        editTextMinute = findViewById(R.id.editMinute);
        textViewTime = findViewById(R.id.textViewTime);
        imageViewReset = findViewById(R.id.imageViewReset);
        imageViewStartStop = findViewById(R.id.imageViewStartStop);

        imageViewReset.setOnClickListener(this);
        imageViewStartStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageViewReset:
                reset();
                break;
            case R.id.imageViewStartStop:
                startStop();
                break;
        }
    }

    private void reset(){
        stopCountDownTimer();
        startCountDownTimer();
    }

    public void startStop(){
        if (timerStatus == TimerStatus.STOPPED){
            //init progress value
            setTimerValue();
            //init progress var values
            setProgressBarValues();
            //nampilne reset icon
            imageViewReset.setVisibility(View.VISIBLE);
            //ganti icon dari play ke stop
            imageViewStartStop.setImageResource(R.drawable.ic_stop_24dp);
            //dissable textedit
            editTextMinute.setEnabled(false);
            //mengganti status ketika dimulai
            timerStatus = TimerStatus.STARTED;
            //memulai timer
            startCountDownTimer();
        } else {
            //hiding reset button
            imageViewReset.setVisibility(View.GONE);
            //mengganti stop icon ke start
            imageViewStartStop.setImageResource(R.drawable.ic_play_circle_outline_24dp);
            //enable textedit
            editTextMinute.setEnabled(true);
            //mengganti timer status ke stop
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();
        }
    }

    private void setTimerValue(){
        int time = 0;
        if (!editTextMinute.getText().toString().isEmpty()){
            time = Integer.parseInt(editTextMinute.getText().toString().trim());
        } else {
            Toast.makeText(getApplicationContext(), "Tolong masukkan nilai(menit)", Toast.LENGTH_SHORT).show();
        }
        timeCountInMilliSeconds = time * 60 * 1000;
    }

    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));

                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {

                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                setProgressBarValues();
                imageViewReset.setVisibility(View.GONE);
                imageViewStartStop.setImageResource(R.drawable.ic_play_circle_outline_24dp);
                editTextMinute.setEnabled(true);
                timerStatus = TimerStatus.STOPPED;
            }

        }.start();
        countDownTimer.start();
    }

    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }

    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }

    private String hmsTimeFormatter(long millisUntilFinished) {
        String hms = String.format("%2d:%2d:%2d",
                TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
        return hms;
    }
}