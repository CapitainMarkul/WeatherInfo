package com.tensor.dapavlov1.tensorfirststep.domain.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.core.utils.NetworkHelper;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.facade.FacadeMap;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.service.WeatherService;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.presenter.FavoritePresenter;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by da.pavlov1 on 15.09.2017.
 */

public class UpdateWeatherService extends IntentService {
    private static final String TAG = UpdateWeatherService.class.getSimpleName() + "_SERVICE";
    //    private final WeatherService weatherService;
    private static boolean isFirstLaunch = true;

    @Inject
    Lazy<WeatherService> weatherService;

    public UpdateWeatherService() {
        super(TAG);
        App.get().presentationComponents().inject(this);
//        weatherService = App.get().businessComponent().getWeatherService();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        runAlarm();
        isFirstLaunch = false;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //Проблемы с интернетом будут обработаны в последующих вызовах, чтобы не переписывать много логики
        if (isFirstLaunch) {
            sendBroadcast(new Intent(FavoritePresenter.WEATHER_SYNC_ACTION));
            return;
        }
        if (NetworkHelper.isOnline()) {
            List<String> cities = weatherService.get().getDbService().getCitiesName();
            if (cities != null && !cities.isEmpty()) {
                weatherService.get().getWeatherService().getWeatherByCitiesRx(cities)
                        .map(FacadeMap::jsonToVM)
                        .doOnNext(city -> App.getExecutorService().execute(() ->
                                weatherService.get().getDbService().updateCity(city.getName(), city.getWeatherViews())))
                        .doOnComplete(() ->
                                sendBroadcast(new Intent(FavoritePresenter.WEATHER_SYNC_ACTION).putExtra(FavoritePresenter.DB_IS_UPDATE, true)))
                        .subscribe();
            }
        }
    }

    private void runAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 60 * 60 * 1000,    // 1 hour
//                System.currentTimeMillis() + 10 * 1000,    // 10 sec
                PendingIntent.getService(this, 0,
                        new Intent(this, UpdateWeatherService.class), 0));
    }
}
