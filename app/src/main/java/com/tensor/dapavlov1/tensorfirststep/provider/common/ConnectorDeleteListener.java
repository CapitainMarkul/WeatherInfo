package com.tensor.dapavlov1.tensorfirststep.provider.common;

import com.tensor.dapavlov1.tensorfirststep.provider.callbacks.DelCityCallBack;

/**
 * Created by da.pavlov1 on 06.09.2017.
 */

public class ConnectorDeleteListener {
    public static ConnectorDeleteListener getInstance() {
        return ConnectorDeleteListener.TestSinglLoader.INSTANCE;
    }

    private static final class TestSinglLoader {
        private static final ConnectorDeleteListener INSTANCE = new ConnectorDeleteListener();
    }

    private DelCityCallBack delCityCallBack;

    public void setCallBack(DelCityCallBack delCityCallBack) {
        this.delCityCallBack = delCityCallBack;
    }

    public DelCityCallBack getDelCityCallBack() {
        return delCityCallBack;
    }
}
