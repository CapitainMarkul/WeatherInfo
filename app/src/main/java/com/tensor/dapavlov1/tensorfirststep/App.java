package com.tensor.dapavlov1.tensorfirststep;

import android.app.Application;
import android.content.Context;

import com.tensor.dapavlov1.tensorfirststep.data.DaoModels.DaoMaster;
import com.tensor.dapavlov1.tensorfirststep.data.DaoModels.DaoSession;

import org.greenrobot.greendao.database.Database;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by da.pavlov1 on 04.08.2017.
 */
//по умолчанию  в одном экземпляре
public class App extends Application {
    private static DaoSession daoSession;
    private static Context context;

    static ExecutorService executorService = Executors.newFixedThreadPool(1);

    public static ExecutorService getExecutorService(){
        return executorService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        initSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static Context getContext() {
        return context;
    }

    private void initSession(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "weathers");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }
}