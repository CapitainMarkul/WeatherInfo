package com.tensor.dapavlov1.tensorfirststep;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoMaster;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoSession;
import com.tensor.dapavlov1.tensorfirststep.domain.services.syncChangeOtherActivity.service.UpdateWeatherService;

import org.greenrobot.greendao.database.Database;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by da.pavlov1 on 04.08.2017.
 */
//по умолчанию  в одном экземпляре
public class App extends Application {
    private static DaoSession daoSession;
    static ExecutorService executorService = Executors.newFixedThreadPool(4);

    public static ExecutorService getExecutorService() {
        return executorService;
    }

    private static App instance;

    public App() {
        instance = this;
    }

    public static App getContext() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        setupLeakCanary();
        initSession();
        startService(new Intent(this, UpdateWeatherService.class));

//        createAlarmManager();
    }

//    private void createAlarmManager() {
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
////
//        alarmManager.setRepeating(
//                AlarmManager.RTC_WAKEUP,
//                System.currentTimeMillis(),
//                System.currentTimeMillis() + 60 * 60 * 1000, //
//                PendingIntent.getService(this, 0,
//                        new Intent(this, UpdateWeatherService.class), 0));
//    }

    protected RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    private void initSession() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "weathers");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }
}