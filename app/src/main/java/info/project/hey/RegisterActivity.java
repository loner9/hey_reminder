package info.project.hey;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog progressDialog;

    Button gotoLogin,signup;
    EditText email,password;
    Spinner level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);

        gotoLogin = findViewById(R.id.btn_to_login);
        signup = findViewById(R.id.btn_signup);
        level = findViewById(R.id.level);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_level = level.getSelectedItem().toString();

                if (TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_email)){
                    Toast.makeText(getApplicationContext(),"Harap isi semua kolom",Toast.LENGTH_SHORT).show();
                } else if(txt_password.length()<6){
                    Toast.makeText(getApplicationContext(),"Password setidaknya terdiri dari 6 karakter",Toast.LENGTH_SHORT).show();
                } else if(txt_level.equals("Pilih Level")){
                    Toast.makeText(getApplicationContext(),"Harap pilih kelas anda",Toast.LENGTH_SHORT).show();
                } else {
                    register(txt_email,txt_password,txt_level);
                }

            }
        });

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });

    }

    private void register(final String email,String password,String level){
        progressDialog.setMessage("Mendaftarkan User...");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("level",level);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        if (level.equals("Murid")){
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(RegisterActivity.this, StartAct.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else if (level.equals("Guru")){
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(RegisterActivity.this, TeaActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }
                            });

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Terjadi error. Harap Datfar kembali", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
