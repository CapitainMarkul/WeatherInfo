package com.tensor.dapavlov1.tensorfirststep.domain.services.syncChangeOtherActivity.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by da.pavlov1 on 15.09.2017.
 */

public class UpdateActivityInfoService extends IntentService {
    private static final String TAG = UpdateActivityInfoService.class.getSimpleName() + "_SERVICE";

    public UpdateActivityInfoService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
