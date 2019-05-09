package info.project.hey.Class;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDBHandler extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
