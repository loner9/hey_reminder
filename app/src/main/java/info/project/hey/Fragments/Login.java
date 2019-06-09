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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import info.project.hey.R;
import info.project.hey.StuActivity;
import info.project.hey.TeaActivity;


public class Login extends Fragment {
    Button gotoRegister,login;
    EditText email, password;
    FirebaseAuth auth;
    FirebaseDatabase mDatabase;
    DatabaseReference reference;
    FirebaseUser user;
    ProgressDialog progresDialog;

    public Login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        auth = FirebaseAuth.getInstance();
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        login = view.findViewById(R.id.btn_login);
        gotoRegister = view.findViewById(R.id.btn_to_signup);
        progresDialog=new ProgressDialog(getActivity());

        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference();

        gotoRegister.setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new Register())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit());

        login.setOnClickListener(v -> {
            String txt_email = email.getText().toString();
            String txt_password = password.getText().toString();

            if (TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_email)){
                Toast.makeText(getActivity(), "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            } else {
                progresDialog.setMessage("Sedang Login");
                progresDialog.show();

                auth.signInWithEmailAndPassword(txt_email,txt_password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                user = auth.getCurrentUser();
                                assert user != null;
                                String userid = user.getUid();
                                reference.child("Users").child(userid).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String userLevel = dataSnapshot.child("level").getValue(String.class);
                                        if ("Murid".equals(userLevel)) {
                                            Intent i=new Intent(getActivity(), StuActivity.class);
                                            getActivity().startActivity(i);
                                            getActivity().finish();
                                        } if ("Guru".equals(userLevel)) {
                                            Intent i=new Intent(getActivity(), TeaActivity.class);
                                            getActivity().startActivity(i);
                                            getActivity().finish();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });

                            } else {
                                progresDialog.dismiss();
                                Toast.makeText(getActivity(), "Autentikasi gagal", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return view;
    }

}
