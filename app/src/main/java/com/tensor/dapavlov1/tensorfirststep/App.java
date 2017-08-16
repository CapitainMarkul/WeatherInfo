package com.tensor.dapavlov1.tensorfirststep;

import android.app.Application;
import android.app.FragmentManager;
import android.content.Context;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoMaster;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoSession;

import org.greenrobot.greendao.database.Database;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;


/**
 * Created by da.pavlov1 on 04.08.2017.
 */
//по умолчанию  в одном экземпляре
public class App extends Application {
    private static DaoSession daoSession;
    private static Context context;
//    private OkHttpClient okHttpClient;

    static ExecutorService executorService = Executors.newFixedThreadPool(4);

    public static ExecutorService getExecutorService(){
        return executorService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
//        okHttpClient = new OkHttpClient();
        initSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static Context getContext() {
        return context;
    }

//    public OkHttpClient getOkHttpClient(){
//        return okHttpClient;
//    }

    private void initSession(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "weathers");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }
}