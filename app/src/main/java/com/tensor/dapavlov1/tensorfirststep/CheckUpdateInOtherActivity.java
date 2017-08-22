package com.tensor.dapavlov1.tensorfirststep;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public class CheckUpdateInOtherActivity {
    private static final CheckUpdateInOtherActivity ourInstance = new CheckUpdateInOtherActivity();

    public static CheckUpdateInOtherActivity getInstance() {
        return ourInstance;
    }

    private CheckUpdateInOtherActivity() {
    }

    private boolean isUpdate = false;

    @Nullable
    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(@NonNull boolean update) {
        isUpdate = update;
    }
}
