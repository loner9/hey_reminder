package info.project.hey.Class;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

public class AlarmScheduler {
    public void setAlarm(Context context, long alarmTime, Uri reminderTask){
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                ReminderAlarmService.getReminderPendingIntent(context,reminderTask);

        if (Build.VERSION.SDK_INT >= 23) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,alarmTime,operation);
        } else if (Build.VERSION.SDK_INT >= 21) {
            manager.setExact(AlarmManager.RTC_WAKEUP, alarmTime,operation);
        }
    }

    public void unsetAlarm(Context context,Uri reminderTask){
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                ReminderAlarmService.getReminderPendingIntent(context,reminderTask);

        manager.cancel(operation);
    }
}
