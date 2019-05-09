package info.project.hey.Class;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import info.project.hey.R;
import info.project.hey.StuActivity;

public class ReminderAlarmService extends IntentService {
    private static final String TAG = ReminderAlarmService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 42;

    //This is a deep link intent, and needs the task stack
    public static PendingIntent getReminderPendingIntent(Context context, Uri uri) {
        Intent action = new Intent(context, ReminderAlarmService.class);
        action.setData(uri);
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public ReminderAlarmService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Uri uri = intent.getData();

        Intent action = new Intent(this, StuActivity.class);
        action.setData(uri);
        PendingIntent operation = TaskStackBuilder.create(this).
                addNextIntentWithParentStack(action).
                getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification note = new NotificationCompat.Builder(this)
                .setContentTitle("Coba")
                .setContentText("Jajal")
                .setSound(defaultSoundUri)
                .setSmallIcon(R.drawable.ic_notifications_active_white_24dp)
                .setContentIntent(operation)
                .setAutoCancel(true)
                .build();

        manager.notify(NOTIFICATION_ID,note);
    }
}
