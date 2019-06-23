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
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import info.project.hey.ChatActivity;
import info.project.hey.Class.Model.Contacts;
import info.project.hey.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private View privateChatsView;
    private RecyclerView chatsList;
    private FirebaseAuth mAuth;
    private DatabaseReference chatsRef, usersRef;
    private String currentUserId;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        privateChatsView =  inflater.inflate(R.layout.fragment_chat, container, false);

        chatsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserId);
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        chatsList = privateChatsView.findViewById(R.id.chats_list);
        chatsList.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.ItemAnimator animator = chatsList.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        return privateChatsView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(chatsRef, Contacts.class)
                .build();


        FirebaseRecyclerAdapter<Contacts, ChatsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, ChatsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChatsViewHolder holder, int position, @NonNull Contacts model) {
                        final String userIds = getRef(position).getKey();
                        final String [] profileImage = {"default"};
                        usersRef.child(userIds).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild("image")){
                                    profileImage[0] = dataSnapshot.child("image").getValue().toString();
                                    Picasso.get().load(profileImage[0]).placeholder(R.mipmap.ic_launcher_round).into(holder.profileImage);
                                }

                                final String userName = dataSnapshot.child("name").getValue().toString();
                                final String userLevel = dataSnapshot.child("level").getValue().toString();
                                holder.userName.setText(userName);
                                holder.userStatus.setText(userLevel);

                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                        chatIntent.putExtra("visitUserId", userIds);
                                        chatIntent.putExtra("visitUserName", userName);
                                        chatIntent.putExtra("visitUserImage", profileImage[0]);
                                        startActivity(chatIntent);
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
                    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                        return new ChatsViewHolder(view);
                    }
                };
        chatsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profileImage;
        TextView userStatus, userName;
        ImageView userOnlineStatus;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.users_profile_image);
            userStatus = itemView.findViewById(R.id.users_status);
            userName = itemView.findViewById(R.id.users_profile_name);
            userOnlineStatus = itemView.findViewById(R.id.user_online_status);
        }
    }
}