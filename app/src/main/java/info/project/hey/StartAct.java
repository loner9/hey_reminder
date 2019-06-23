package info.project.hey;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import info.project.hey.Fragments.CommunicateFrag;
import info.project.hey.Fragments.InfoFragment;
import info.project.hey.Fragments.MenuFragment;
import info.project.hey.Fragments.RcvTaskFragment;
import info.project.hey.Fragments.ReminderFragment;
import info.project.hey.Fragments.TimerFragment;

public class StartAct extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        loadFragment(new ReminderFragment());
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationst);
        bottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        VerifyUserExistance();
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
                .updateChildren(onlineStateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(StartAct.this, "Selamat Datang", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void SendUserToSettingsActivity() {
        Intent settingsIntent = new Intent(StartAct.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case R.id.tugas:
                fragment = new ReminderFragment();
                break;
            case R.id.perpesanan:
                fragment = new CommunicateFrag();
                break;
            case R.id.tugase:
                fragment = new RcvTaskFragment();
                break;
            case R.id.pewaktu:
                fragment = new TimerFragment();
                break;
            case R.id.other:
                BottomSheetDialogFragment bottom = new MenuFragment();
                bottom.show(getSupportFragmentManager(),bottom.getTag());
                break;
        }
        return loadFragment(fragment);
    }
}
