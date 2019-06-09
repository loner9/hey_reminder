package info.project.hey.Class.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.List;

import info.project.hey.Class.RemindDaBase;
import info.project.hey.Class.Reminder;

public class BootReceiver extends BroadcastReceiver {
    public String mTitle,mTime,mDate, RepNo, RepType,mActive, Rep;
    private int Year, Month, Hour, Minute, Day, ReceiveID;
    private String[] DateSplit;
    private String[] TimeSplit;

    private Calendar cal;
    private AlarmReceiver alarmReceiver;
    private long RepTime;

    // Constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            RemindDaBase rb = new RemindDaBase(context);
            cal = Calendar.getInstance();
            alarmReceiver = new AlarmReceiver();

            List<Reminder> reminders = rb.getAllReminders();

            for (Reminder rm : reminders) {
                ReceiveID = rm.getID();
                Rep = rm.getRepeat();
                RepNo = rm.getRepeatNo();
                RepType = rm.getRepeatType();
                mActive = rm.getActive();
                mDate = rm.getDate();
                mTime = rm.getTime();

                DateSplit = mDate.split("/");
                TimeSplit = mTime.split(":");

                Day = Integer.parseInt(DateSplit[0]);
                Month = Integer.parseInt(DateSplit[1]);
                Year = Integer.parseInt(DateSplit[2]);
                Hour = Integer.parseInt(TimeSplit[0]);
                Minute = Integer.parseInt(TimeSplit[1]);

                cal.set(Calendar.MONTH, --Month);
                cal.set(Calendar.YEAR, Year);
                cal.set(Calendar.DAY_OF_MONTH, Day);
                cal.set(Calendar.HOUR_OF_DAY, Hour);
                cal.set(Calendar.MINUTE, Minute);
                cal.set(Calendar.SECOND, 0);

                if (RepType.equals("Minute")) {
                    RepTime = Integer.parseInt(RepNo) * milMinute;
                } else if (RepType.equals("Hour")) {
                    RepTime = Integer.parseInt(RepNo) * milHour;
                } else if (RepType.equals("Day")) {
                    RepTime = Integer.parseInt(RepNo) * milDay;
                } else if (RepType.equals("Week")) {
                    RepTime = Integer.parseInt(RepNo) * milWeek;
                } else if (RepType.equals("Month")) {
                    RepTime = Integer.parseInt(RepNo) * milMonth;
                }

                // Create a new notification
                if (mActive.equals("true")) {
                    if (Rep.equals("true")) {
                        alarmReceiver.setRepeatAlarm(context, cal, ReceiveID, RepTime);
                    } else if (Rep.equals("false")) {
                        alarmReceiver.setAlarm(context, cal, ReceiveID);
                    }
                }
            }
        }
    }
}
