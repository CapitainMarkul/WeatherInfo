package com.tensor.dapavlov1.tensorfirststep.domain.services.syncWeather;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by da.pavlov1 on 15.09.2017.
 */

public class WeatherSyncService extends IntentService {
    public WeatherSyncService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
