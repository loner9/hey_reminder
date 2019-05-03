package info.project.hey.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import info.project.hey.Class.MyEvent;
import info.project.hey.Class.reclerAdapter;
import info.project.hey.NewTaskAct;
import info.project.hey.R;

public class PlannerFragment extends Fragment {
    TextView judul,subjudul,endlaman;
    Button tmbhBaru;
    FirebaseDatabase mDatabase;
    FirebaseUser user;

    DatabaseReference reference;
    RecyclerView recyclerView;
    reclerAdapter adapter;
    ArrayList<MyEvent> list;

    public PlannerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_planner, container, false);
        judul = RootView.findViewById(R.id.judul);
        subjudul = RootView.findViewById(R.id.subjudul);
        endlaman = RootView.findViewById(R.id.endlaman);
        tmbhBaru = RootView.findViewById(R.id.tmbhBru);

        //recyclerview reference
        recyclerView = RootView.findViewById(R.id.eventkita);
        recyclerView.setHasFixedSize(true);
        //set layoutmanager
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        list = new ArrayList<MyEvent>();

        tmbhBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(getActivity(), NewTaskAct.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(a);
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference();

        String uid = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Tasks").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    MyEvent p = dataSnapshot1.getValue(MyEvent.class);
                    list.add(p);
                }
                adapter = new reclerAdapter(getContext(), list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Tidak Ada Data", Toast.LENGTH_SHORT).show();
            }
        });

        return RootView;
    }

}


