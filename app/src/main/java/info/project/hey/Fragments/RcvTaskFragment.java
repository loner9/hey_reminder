package info.project.hey.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import info.project.hey.AddReminder;
import info.project.hey.Class.Model.Task;
import info.project.hey.R;

public class RcvTaskFragment extends Fragment {
    private View taskView;
    private RecyclerView taskList;
    private FirebaseAuth mAuth;
    private DatabaseReference tasksRef, usersRef;
    private String currentUserId;


    public RcvTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        taskView = inflater.inflate(R.layout.fragment_rcv_task, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserId);
        tasksRef = FirebaseDatabase.getInstance().getReference().child("Task");
        taskList = taskView.findViewById(R.id.task_list);
        taskList.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.ItemAnimator animator = taskList.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        return taskView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Task> options = new FirebaseRecyclerOptions.Builder<Task>()
                .setQuery(usersRef,Task.class).build();

        FirebaseRecyclerAdapter<Task,TaskViewHolder> adapter =
                new FirebaseRecyclerAdapter<Task, TaskViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull TaskViewHolder holder, int position, @NonNull Task model) {
                        final String userIds = getRef(position).getKey();
                        tasksRef.child(userIds).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String judul = dataSnapshot.child("judul").getValue().toString();
                                final String arahan = dataSnapshot.child("arahan").getValue().toString();
                                holder.judul.setText(judul);
                                holder.arahan.setText(arahan);

                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent taskIntent = new Intent(getContext(), AddReminder.class);
                                        taskIntent.putExtra("mJudul",judul);
                                        startActivity(taskIntent);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_layout, viewGroup, false);
                        return new TaskViewHolder(view);
                    }
                };
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        TextView judul, arahan;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judul);
            arahan = itemView.findViewById(R.id.arahan);
        }
    }
}
