package info.project.hey;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;


import java.util.Calendar;
import java.util.Random;

import info.project.hey.Class.AlarmScheduler;


public class NewTaskAct extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener{

    EditText judulevnt, ketevnt,wktevnt, tglevnt;
    Button btnSaveTask, btnCancel;
    Switch alrmActiv;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    DatabaseReference reference;
    FirebaseDatabase mDatabase;
    FirebaseUser user;
    String tanggal,waktu,mActive;
    Uri mCurrentReminderUri;

    Calendar mCalendar;
    private int mYear, mMonth, mDay, mHour, mMinute;
    long tanggalDipilih;
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;


    Integer doesNum = new Random().nextInt();
    String keyevnt = Integer.toString(doesNum);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        Intent intent = getIntent();
        mCurrentReminderUri = intent.getData();

        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);

        tanggal = mDay + "/" + mMonth + "/" + mYear;
        waktu = mHour + ":" + mMinute;

        alrmActiv = findViewById(R.id.active);
        judulevnt = findViewById(R.id.titledoes);
        ketevnt = findViewById(R.id.descdoes);
        wktevnt = findViewById(R.id.time);
        tglevnt = findViewById(R.id.date);
        mActive = "false";

        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnCancel = findViewById(R.id.btnCancel);

        alrmActiv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mActive = "true";
                } else {
                    mActive = "false";
                }
            }
        });

        tglevnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        NewTaskAct.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        wktevnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        NewTaskAct.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.setThemeDark(false);
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(NewTaskAct.this,StuActivity.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(a);
            }
        });

        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                mDatabase = FirebaseDatabase.getInstance();
                reference = mDatabase.getReference();
                String uid = user.getUid();

                reference = FirebaseDatabase.getInstance().getReference("Tasks").child(uid).child("Task"+keyevnt);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("judulevnt").setValue(judulevnt.getText().toString());
                        dataSnapshot.getRef().child("ketevnt").setValue(ketevnt.getText().toString());
                        dataSnapshot.getRef().child("wktevnt").setValue(waktu);
                        dataSnapshot.getRef().child("tglevnt").setValue(tanggal);
                        dataSnapshot.getRef().child("idTask").setValue(mCurrentReminderUri);
                        dataSnapshot.getRef().child("keyevnt").setValue(keyevnt);

                        Intent a = new Intent(NewTaskAct.this,StuActivity.class);
                        a.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        aturWaktu();
                        startActivity(a);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                finish();
            }
        });

    }
    public void onBackPressed() {
        finish();
    }

    public void aturWaktu(){
        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);

        tanggalDipilih = mCalendar.getTimeInMillis();

        if (mActive.equals("true")){
            new AlarmScheduler().setAlarm(getApplicationContext(), tanggalDipilih, mCurrentReminderUri);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear ++;
        mDay = dayOfMonth;
        mMonth = monthOfYear;
        mYear = year;
        tanggal = mDay + "/" + mMonth + "/" + mYear;
        tglevnt.setText(tanggal);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        mHour = hourOfDay;
        mMinute = minute;
        if (minute < 10){
            waktu = hourOfDay + ":" + "0" + minute;
        } else {
            waktu = hourOfDay + ":" +minute;
        }
        wktevnt.setText(waktu);
    }
}

