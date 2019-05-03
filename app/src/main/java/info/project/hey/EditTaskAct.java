package info.project.hey;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditTaskAct extends AppCompatActivity {

    EditText judulevnt, ketevnt;
    TextView wktevnt, tglevnt;
    Switch alrmActiv;
    Button btnSave,btnDelete;
    DatabaseReference reference;
    FirebaseDatabase mDatabase;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        alrmActiv = findViewById(R.id.active);
        judulevnt = findViewById(R.id.titledoes);
        ketevnt = findViewById(R.id.descdoes);
        wktevnt = findViewById(R.id.time);
        tglevnt = findViewById(R.id.date);

        btnSave = findViewById(R.id.btnUpdtTask);
        btnDelete = findViewById(R.id.btnDelete);

        judulevnt.setText(getIntent().getStringExtra("judulevnt"));
        ketevnt.setText(getIntent().getStringExtra("ketevnt"));
        tglevnt.setText(getIntent().getStringExtra("tglevnt"));

        final String keyevnt = getIntent().getStringExtra("keyevnt");

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        String uid = user.getUid();
        reference = mDatabase.getReference("Tasks").child(uid).child("Task"+keyevnt);
        save();
        delete();
    }
    public void save(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("judulevnt").setValue(judulevnt.getText().toString());
                        dataSnapshot.getRef().child("ketevnt").setValue(ketevnt.getText().toString());
                        dataSnapshot.getRef().child("tglevnt").setValue(tglevnt.getText().toString());
                        Intent a = new Intent(EditTaskAct.this, StuActivity.class);
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

    public void delete(){
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            Intent a = new Intent(EditTaskAct.this,StuActivity.class);
                            a.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(a);
                        } else {
                            Toast.makeText(getApplicationContext(),"Gagal", Toast.LENGTH_SHORT).show();
                        }
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
