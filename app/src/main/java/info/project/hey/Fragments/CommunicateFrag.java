package info.project.hey.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import info.project.hey.Class.Adapter.TabsAccessorAdapter;
import info.project.hey.CommunicateActivity;
import info.project.hey.FindFriendActivity;
import info.project.hey.R;
import info.project.hey.SettingsActivity;

public class CommunicateFrag extends Fragment {
    private Toolbar mToolbar;
    private ViewPager mViewPaper;
    private TabLayout mTabLayout;
    private TabsAccessorAdapter mTabsAccessorAdapter;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private String currentUserId;

    public CommunicateFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_communicate, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();

        setHasOptionsMenu(true);
        mToolbar = rootview.findViewById(R.id.main_page_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Perpesanan");

        mViewPaper = rootview.findViewById(R.id.main_tabs_paper);
        mTabsAccessorAdapter = new TabsAccessorAdapter(getChildFragmentManager());
        mViewPaper.setAdapter(mTabsAccessorAdapter);

        mTabLayout = rootview.findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPaper);

//        updateUserStatus("online");

        String currentUserID = mAuth.getCurrentUser().getUid();
        rootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("deviceToken").exists())){
                    userToken();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootview;
    }

    @Override
    public void onStart(){
        super.onStart();

//        VerifyUserExistance();
//        updateUserStatus("online");
    }

    private void userToken(){
        Task<InstanceIdResult> token = FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(getActivity(),new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();

            }
        });


        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        assert fuser != null;
        String userid = fuser.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid).child("deviceToken");
        reference.setValue(token.getResult().getToken());
    }

    private void VerifyUserExistance() {
        String currentUserID = mAuth.getCurrentUser().getUid();
        rootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("name").exists())){
                    SendUserToSettingsActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToFindFriendsActivity() {
        Intent findFriendsIntent = new Intent(getActivity(), FindFriendActivity.class);
        startActivity(findFriendsIntent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void SendUserToSettingsActivity() {
        Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(settingsIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.main_settings_option){
            SendUserToSettingsActivity();
        }

        if(item.getItemId() == R.id.main_create_group_option){
            RequestNewGroup();
        }

        if(item.getItemId() == R.id. main_find_friends_option){
            SendUserToFindFriendsActivity();
        }
        return true;
    }

    private void RequestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
        builder.setTitle("Masukkan Nama Grup");

        final EditText groupNameField = new EditText(getActivity());
        groupNameField.setHint("e.g Oh My Chat BKU");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String groupName = groupNameField.getText().toString();
                if(TextUtils.isEmpty(groupName)){
                    Toast.makeText(getActivity(), "Please write Group Name..", Toast.LENGTH_SHORT).show();
                }
                else{
                    CreateNewGroup(groupName);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void CreateNewGroup(final String groupName) {
        rootRef.child("Groups").child(groupName).setValue("").
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), groupName + " group is Create Successfully...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUserStatus(String state){
        Calendar calendar = Calendar.getInstance();
        String saveCurrentUserTime, saveCurrentUserDate;
        SimpleDateFormat currentDate  = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentUserDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime  = new SimpleDateFormat("hh:mm ss");
        saveCurrentUserTime = currentTime.format(calendar.getTime());

        HashMap<String, Object> onlineStateMap = new HashMap<>();

        onlineStateMap.put("time", saveCurrentUserTime);
        onlineStateMap.put("date", saveCurrentUserDate);
        onlineStateMap.put("state", state);

        currentUserId = currentUser.getUid();
        rootRef.child("Users").child(currentUserId).child("userState")
                .updateChildren(onlineStateMap);
    }

}
