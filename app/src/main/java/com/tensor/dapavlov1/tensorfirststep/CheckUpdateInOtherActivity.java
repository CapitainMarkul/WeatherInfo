package com.tensor.dapavlov1.tensorfirststep;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public class CheckUpdateInOtherActivity {
    public static CheckUpdateInOtherActivity getInstance() {
        return CheckUpdateInOtherActivityLoader.INSTANCE;
    }

    private static final class CheckUpdateInOtherActivityLoader {
        private static final CheckUpdateInOtherActivity INSTANCE = new CheckUpdateInOtherActivity();
    }

    private CheckUpdateInOtherActivity() {
    }

    private boolean isUpdate = false;

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }
}
