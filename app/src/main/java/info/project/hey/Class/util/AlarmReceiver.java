package info.project.hey.Class.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

import info.project.hey.Class.RemindDaBase;
import info.project.hey.Class.Reminder;
import info.project.hey.EditReminder;
import info.project.hey.R;

public class AlarmReceiver extends BroadcastReceiver {
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    @Override
    public void onReceive(Context context, Intent intent) {
        int mReceivedID = Integer.parseInt(intent.getStringExtra(EditReminder.EXTRA_REMINDER_ID));

        // mendapatkan title notif dari database
        RemindDaBase rb = new RemindDaBase(context);
        Reminder reminder = rb.getReminder(mReceivedID);
        String mTitle = reminder.getTitle();

        // membuat intent untuk membuka edit activity
        Intent intent1 = new Intent(context, EditReminder.class);
        intent1.putExtra(EditReminder.EXTRA_REMINDER_ID, Integer.toString(mReceivedID));
        PendingIntent click = PendingIntent.getActivity(context, mReceivedID, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        // membuat notifikasi
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_notifications_active_white_24dp)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setTicker(mTitle)
                .setContentText(mTitle)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(click)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);

        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(mReceivedID, builder.build());
    }
    public void cancelAlarm(Context context, int ID) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // membatalkan alarm dengan Reminder ID
        pendingIntent = PendingIntent.getBroadcast(context, ID, new Intent(context, AlarmReceiver.class), 0);
        alarmManager.cancel(pendingIntent);

        // Disable alarm
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void setAlarm(Context context, Calendar calendar, int ID) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // menaruh id ke intent
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EditReminder.EXTRA_REMINDER_ID, Integer.toString(ID));
        pendingIntent = PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // kalkulasi waktu notifikasi
        Calendar c = Calendar.getInstance();
        long currentTime = c.getTimeInMillis();
        long diffTime = calendar.getTimeInMillis() - currentTime;

        // memulai alarm dengan waktu dari notif
        alarmManager.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + diffTime,
                pendingIntent);

        // Restart alarm jika perangkat reboot
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void setRepeatAlarm(Context context, Calendar calendar, int ID, long RepeatTime) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // menaruh id ke Intent Extra
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EditReminder.EXTRA_REMINDER_ID, Integer.toString(ID));
        pendingIntent = PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // kalkulasi notifikasi masuk
        Calendar c = Calendar.getInstance();
        long currentTime = c.getTimeInMillis();
        long diffTime = calendar.getTimeInMillis() - currentTime;

        // memulai alarm dengan waktu dari notifikasi dan repeat time
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + diffTime,
                RepeatTime , pendingIntent);

        // Restart alarm jika perangkat di reboot
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
}
