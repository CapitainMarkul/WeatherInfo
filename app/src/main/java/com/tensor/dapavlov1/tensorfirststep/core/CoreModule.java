package com.tensor.dapavlov1.tensorfirststep.core;

import android.content.Context;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoMaster;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoSession;

import org.greenrobot.greendao.database.Database;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by da.pavlov1 on 21.09.2017.
 */

@Module
public class CoreModule {
    private final Context context;

    public CoreModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context getContext() {
        return context;
    }

    @Provides
    @Singleton
    DaoSession getDaoSession() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context.getApplicationContext(), "weathers");
        Database db = helper.getWritableDb();
        return new DaoMaster(db).newSession();
    }

    @Provides
    @Singleton
    OkHttpClient getHttpClient() {
        return new OkHttpClient();
    }
}
