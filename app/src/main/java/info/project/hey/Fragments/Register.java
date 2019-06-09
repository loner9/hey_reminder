package info.project.hey.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import info.project.hey.MainActivity;
import info.project.hey.R;
import info.project.hey.StuActivity;
import info.project.hey.TeaActivity;


public class Register extends Fragment {
    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog progressDialog;

    Button gotoLogin,signup;
    EditText email,password,username;
    Spinner level;

    public Register() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        username = view.findViewById(R.id.username);
        gotoLogin = view.findViewById(R.id.btn_to_login);
        signup = view.findViewById(R.id.btn_signup);
        level = view.findViewById(R.id.level);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);

        progressDialog=new ProgressDialog(getActivity());
        auth = FirebaseAuth.getInstance();

        gotoLogin.setOnClickListener(v ->
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new Login())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit());

        signup.setOnClickListener(v -> {
            String txt_username = username.getText().toString();
            String txt_email = email.getText().toString();
            String txt_password = password.getText().toString();
            String txt_level = level.getSelectedItem().toString();

            if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_email)){
                Toast.makeText(getActivity(),"Harap isi semua kolom",Toast.LENGTH_SHORT).show();
            } else if(txt_password.length()<6){
                Toast.makeText(getActivity(),"Password setidaknya terdiri dari 6 karakter",Toast.LENGTH_SHORT).show();
            } else if(txt_level.equals("Pilih Level")){
                Toast.makeText(getActivity(),"Harap pilih kelas anda",Toast.LENGTH_SHORT).show();
            } else {
                register(txt_username,txt_email,txt_password,txt_level);
            }
        });

        return view;
    }
    private void register(final String username,String email,String password,String level){

        progressDialog.setMessage("Mendaftarkan User...");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        assert firebaseUser != null;
                        String userid = firebaseUser.getUid();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id",userid);
                        hashMap.put("username",username);
                        hashMap.put("imageURL", "default");
                        hashMap.put("status", "offline");
                        hashMap.put("search", username.toLowerCase());
                        hashMap.put("level",level);

                        reference.setValue(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()){
                                if (level.equals("Murid")){
                                    Intent intent = new Intent(getActivity(), StuActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                } else if (level.equals("Guru")){
                                    Intent intent = new Intent(getActivity(), TeaActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            }
                        });

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),"Terjadi error. Harap Datfar kembali",Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
