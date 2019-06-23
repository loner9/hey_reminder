package info.project.hey;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SndTaskActivity extends AppCompatActivity {
    private EditText mJudul,mArahan;
    private TextView username;
    private String receiverUserId, senderUserId;
    private DatabaseReference userRef,taskRef;
    private Button mSend;
    private FirebaseAuth mAuth;
    String receiverName, senderId,receiverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snd_task);

        mJudul = findViewById(R.id.etTgas);
        mArahan = findViewById(R.id.etArhn);
        mSend = findViewById(R.id.sndTgas);
        username = findViewById(R.id.username);
        userRef = FirebaseDatabase.getInstance().getReference();
        taskRef = FirebaseDatabase.getInstance().getReference().child("Tasks");
        mAuth = FirebaseAuth.getInstance();

        senderId = mAuth.getCurrentUser().getUid();
        receiverName = getIntent().getExtras().get("visitUserName").toString();
        receiverId = getIntent().getExtras().get("visitUserId").toString();

        username.setText(receiverName);

        mSend.setOnClickListener(v -> sendTask());
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private void sendTask() {
        String judulText = mJudul.getText().toString();
        String arahanText = mArahan.getText().toString();
        if(TextUtils.isEmpty(judulText) || TextUtils.isEmpty(arahanText)){
            Toast.makeText(this, "Isi semua kolom yang tersedia...", Toast.LENGTH_LONG).show();
        } else {
//            HashMap<String,String> hashMap = new HashMap<>();
//            hashMap.put("judul",judulText);
//            hashMap.put("arahan",arahanText);

            String SenderRef = "Task/" + senderId + "/" + receiverId;
            String ReceiverRef = "Task/" + receiverId + "/" + senderId;

            DatabaseReference userTaskKeyRef = userRef.child("Task")
                    .child(senderId)
                    .child(receiverId)
                    .push();

            String taskPushID = userTaskKeyRef.getKey();

            Map taskTextBody = new HashMap();
            taskTextBody.put("judul",judulText);
            taskTextBody.put("arahan",arahanText);

            Map taskTextDetail = new HashMap();
            taskTextDetail.put(SenderRef+ "/" + taskPushID, taskTextBody);
            taskTextDetail.put(ReceiverRef + "/" + taskPushID, taskTextBody);

            userRef.updateChildren(taskTextDetail).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SndTaskActivity.this, "Tugas Terkirim dengan sukses..", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    else {
                        Toast.makeText(SndTaskActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }
}
