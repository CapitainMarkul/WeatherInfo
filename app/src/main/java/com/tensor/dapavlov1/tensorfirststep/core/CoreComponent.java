package com.tensor.dapavlov1.tensorfirststep.core;

import android.content.Context;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoSession;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * Created by da.pavlov1 on 21.09.2017.
 */

@Singleton
@Component(modules = CoreModule.class)
public interface CoreComponent {
    DaoSession getDaoSession();

    Context getContext();

    OkHttpClient getHttpClient();
}
