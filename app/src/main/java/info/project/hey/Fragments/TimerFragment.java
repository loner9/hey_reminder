package info.project.hey.Fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import info.project.hey.R;


public class TimerFragment extends Fragment implements View.OnClickListener{

    private long timeCountInMilliSec = 1 * 60000;
    private enum TimerStatus{
        STARTED,
        STOPPED
    }

    public TimerFragment() {
        // Required empty public constructor
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;

    private ProgressBar progressBar;
    private EditText editText;
    private TextView textViewTime;
    private ImageView imageViewReset;
    private ImageView imageViewStartStop;
    private CountDownTimer countDownTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initListeners(){
        imageViewReset.setOnClickListener(this);
        imageViewStartStop.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_dua, container, false);

        progressBar =  RootView.findViewById(R.id.progressBar);
        editText =  RootView.findViewById(R.id.editTextMinute);
        textViewTime =  RootView.findViewById(R.id.textViewTime);
        imageViewReset =   RootView.findViewById(R.id.imageviewReset);
        imageViewStartStop =  RootView.findViewById(R.id.imageviewStartStop);
        initListeners();
        return RootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageviewReset:
                reset();
                break;
            case R.id.imageviewStartStop:
                startStop();
                break;
        }
    }

    public void reset(){
        stopCountDownTimer();
        startCountDownTimer();
    }

    public void startStop(){
        if (timerStatus == TimerStatus.STOPPED){
            //init progress value
            setTimerValues();
            //init progress var values
            setProgressBarValues();
            //nampilne reset icon
            imageViewReset.setVisibility(View.VISIBLE);
            //ganti icon dari play ke stop
            imageViewStartStop.setImageResource(R.drawable.icon_stop);
            //dissable textedit
            editText.setEnabled(false);
            //mengganti status ketika dimulai
            timerStatus = TimerStatus.STARTED;
            //memulai timer
            startCountDownTimer();
        } else {
            //hiding reset button
            imageViewReset.setVisibility(View.GONE);
            //mengganti stop icon ke start
            imageViewStartStop.setImageResource(R.drawable.icon_start);
            //enable textedit
            editText.setEnabled(true);
            //mengganti timer status ke stop
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();
        }
    }

    private void setTimerValues(){
        int time = 0;
        if (!editText.getText().toString().isEmpty()){
            //fetch data dari edittext dan mengubah ke int
            time = Integer.parseInt(editText.getText().toString().trim());
        } else {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.message_minutes), Toast.LENGTH_LONG).show();
        }
        timeCountInMilliSec = time * 60 * 1000;
    }

    private void startCountDownTimer(){
        countDownTimer = new CountDownTimer(timeCountInMilliSec, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                progressBar.setProgress((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSec));
                setProgressBarValues();
                imageViewReset.setVisibility(View.GONE);
                imageViewStartStop.setImageResource(R.drawable.icon_start);
                editText.setEnabled(true);
                timerStatus = TimerStatus.STOPPED;
            }
        }.start();
        countDownTimer.start();
    }

    private void stopCountDownTimer(){
        countDownTimer.cancel();
    }

    public void setProgressBarValues(){
        progressBar.setMax((int) timeCountInMilliSec/1000);
        progressBar.setProgress((int)timeCountInMilliSec/1000);
    }


    private String hmsTimeFormatter(long millisUntilFinished) {
        String hms = String.format("%2d:%2d:%2d",
                TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
        return hms;
    }

}
