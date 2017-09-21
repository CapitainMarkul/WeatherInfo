package com.tensor.dapavlov1.tensorfirststep;

import android.app.Application;
import android.content.Intent;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tensor.dapavlov1.tensorfirststep.core.CoreComponent;
import com.tensor.dapavlov1.tensorfirststep.core.CoreModule;
import com.tensor.dapavlov1.tensorfirststep.core.DaggerCoreComponent;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoMaster;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoSession;
import com.tensor.dapavlov1.tensorfirststep.domain.assembly.BusinessComponent;
import com.tensor.dapavlov1.tensorfirststep.domain.assembly.BusinessModule;
import com.tensor.dapavlov1.tensorfirststep.domain.assembly.DaggerBusinessComponent;
import com.tensor.dapavlov1.tensorfirststep.domain.services.syncChangeOtherActivity.service.UpdateWeatherService;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.DaggerPresentationComponents;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.PresentationComponents;

import org.greenrobot.greendao.database.Database;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by da.pavlov1 on 04.08.2017.
 */
//по умолчанию  в одном экземпляре
public class App extends Application {
    //    private static DaoSession daoSession;
    static ExecutorService executorService = Executors.newFixedThreadPool(4);

    private PresentationComponents presentationComponents;
    private BusinessComponent businessComponent;
    private CoreComponent coreComponent;

    public static ExecutorService getExecutorService() {
        return executorService;
    }

    private static App instance;

    public App() {
        instance = this;
    }

    public static App get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setupLeakCanary();
//        initSession();
        startService(new Intent(this, UpdateWeatherService.class));

        injectDependencies();
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
    public PresentationComponents presentationComponents() {
        return presentationComponents;
    }

    public BusinessComponent businessComponent() {
        return businessComponent;
    }

    public CoreComponent coreComponent() {
        return coreComponent;
    }

    private void injectDependencies() {
        coreComponent = DaggerCoreComponent.builder()
                .coreModule(new CoreModule(this))
                .build();
        businessComponent = DaggerBusinessComponent.builder()
                .coreComponent(coreComponent)
                .businessModule(new BusinessModule())
                .build();

        presentationComponents = DaggerPresentationComponents.builder()
                .businessComponent(businessComponent)
                .build();
    }


    protected RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

//    public static DaoSession getDaoSession() {
//        return daoSession;
//    }

    private void initSession() {
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "weathers");
//        Database db = helper.getWritableDb();
//        daoSession = new DaoMaster(db).newSession();
    }
}