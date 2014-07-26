package com.bignerdranch.android.photogallery;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

public class PollService extends IntentService {

    private static final String TAG = "PollService";
    private static final int POLL_INTERVAL = 1000 * 15; // 15 seconds
    private static final int POLL_SERVICE_REQUEST = 0;

    public PollService() {
        super(TAG);
    }

    /**
     * static method to start an instance of PollService
     *
     * @param context
     * @param setOn
     */
    public static void setServiceAlarm(Context context, boolean setOn) {
        Intent intent = new Intent(context, PollService.class);

        int flags = 0;
        PendingIntent pendingIntent = PendingIntent.getService(context, POLL_SERVICE_REQUEST, intent, flags);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        if (setOn) {
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), POLL_INTERVAL, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);     // since AlarmManager can only ever have one copy of this P.I, ...
            // ... AlarmManager will know to cancel it when we send it an identical copy.
            pendingIntent.cancel();
        }
    }

    /**
     * static method to check if pending intent is already scheduled
     * @param context
     * @return
     */
    public static boolean isServiceAlarmOn(Context context) {
        Intent intent = new Intent(context, PollService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, POLL_SERVICE_REQUEST, intent, PendingIntent.FLAG_NO_CREATE);
        return pendingIntent != null;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        @SuppressWarnings("deprication")
        boolean isNetworkAvailable = connectivityManager.getBackgroundDataSetting() &&
                connectivityManager.getActiveNetworkInfo() != null;
        if (!isNetworkAvailable) {
            return;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String query = prefs.getString(FlickrFetchr.PREF_SEARCH_QUERY, null);
        String lastResultId = prefs.getString(FlickrFetchr.PREF_LAST_RESULT_ID, null);

        ArrayList<GalleryItem> items;
        if (query != null) {
            items = new FlickrFetchr().search(query);
        } else {
            items = new FlickrFetchr().getItems();
        }

        if (items.isEmpty()) {
            return;
        }

        String resultId = items.get(0).getId();

        if (!resultId.equals(lastResultId)) {
            Log.i(TAG, "Got a new result: " + resultId);
        } else {
            Log.i(TAG, "Got an old result: " + resultId);
        }

        prefs.edit()
                .putString(FlickrFetchr.PREF_LAST_RESULT_ID, resultId)
                .commit();
    }


}
