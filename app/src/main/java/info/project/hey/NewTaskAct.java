package info.project.hey;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import info.project.hey.R;

public class NewTaskAct extends AppCompatActivity{
    EditText judulevnt, ketevnt,wktevnt, tglevnt;
    Switch alrmActiv;
    Button btnDate,btnTime,btnSaveTask, btnCancel;
    DatabaseReference reference;
    FirebaseDatabase mDatabase;
    FirebaseUser user;

    private int mYear, mMonth, mDay, mHour, mMinute;
    Integer doesNum = new Random().nextInt();
    String keyevnt = Integer.toString(doesNum);
//    Calendar cal = Calendar.getInstance();
//    SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");
//    String wkt = mdformat.format(cal.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        alrmActiv = findViewById(R.id.active);
        judulevnt = findViewById(R.id.titledoes);
        ketevnt = findViewById(R.id.descdoes);
        wktevnt = findViewById(R.id.time);
        tglevnt = findViewById(R.id.date);

        btnTime = findViewById(R.id.btn_time);
        btnDate = findViewById(R.id.btn_date);
        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnCancel = findViewById(R.id.btnCancel);

        alrmActiv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                } else {

                }
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
                        dataSnapshot.getRef().child("tglevnt").setValue(tglevnt.getText().toString());
                        dataSnapshot.getRef().child("keyevnt").setValue(keyevnt);

                        Intent a = new Intent(NewTaskAct.this,StuActivity.class);
                        a.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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
}

