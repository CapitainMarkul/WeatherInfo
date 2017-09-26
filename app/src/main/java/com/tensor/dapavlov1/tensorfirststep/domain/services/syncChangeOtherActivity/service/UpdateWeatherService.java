package com.tensor.dapavlov1.tensorfirststep.domain.services.syncChangeOtherActivity.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.presenter.FavoritePresenter;

import java.util.Calendar;

/**
 * Created by da.pavlov1 on 15.09.2017.
 */

public class UpdateWeatherService extends IntentService {
    private static final String TAG = UpdateWeatherService.class.getSimpleName() + "_SERVICE";

    public UpdateWeatherService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        runAlarm();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        sendBroadcast(new Intent(FavoritePresenter.WEATHER_SYNC_ACTION));
    }

    private void runAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 60 * 60 * 1000,    // 1 hour
                PendingIntent.getService(this, 0,
                        new Intent(this, UpdateWeatherService.class), 0));
    }
}
