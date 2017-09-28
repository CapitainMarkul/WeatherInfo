package com.tensor.dapavlov1.tensorfirststep.domain.services.syncChangeOtherActivity.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.core.utils.NetworkHelper;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.facade.FacadeMap;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.DbClient;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.service.WeatherService;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.presenter.FavoritePresenter;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by da.pavlov1 on 15.09.2017.
 */

public class UpdateWeatherService extends IntentService {
    private static final String TAG = UpdateWeatherService.class.getSimpleName() + "_SERVICE";
    private final WeatherService weatherService;
    private static boolean isFirstLaunch = true;

    public UpdateWeatherService() {
        super(TAG);
        weatherService = App.get().businessComponent().getWeatherService();
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
        if (NetworkHelper.isOnline()) {
            //Проблемы с интернетом будут обработаны в последующих вызовах, чтобы не переписывать много логики
            if (isFirstLaunch) {
                sendBroadcast(new Intent(FavoritePresenter.WEATHER_SYNC_ACTION));
                return;
            }

            List<String> cities = weatherService.getDbService().getCitiesName();
            if (cities != null && !cities.isEmpty()) {
                weatherService.getWeatherService().getWeatherByCitiesRx(cities)
                        .map(json -> {

                            CityView city = FacadeMap.jsonToVM(json);
//                    set Update weather info in DB
                            App.getExecutorService().execute(() ->
                                    weatherService.getDbService().updateCity(city.getName(), city.getWeatherViews()));
                            return city;
                        }).doOnComplete(() -> {
                    Log.e("Update", "Я обновил БД. Проверь");

                    sendBroadcast(new Intent(FavoritePresenter.WEATHER_SYNC_ACTION).putExtra(FavoritePresenter.DB_IS_UPDATE, true));
                }).subscribe();
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
