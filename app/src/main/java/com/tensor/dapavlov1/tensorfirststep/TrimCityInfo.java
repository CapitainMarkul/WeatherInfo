package com.tensor.dapavlov1.tensorfirststep;

/**
 * Created by da.pavlov1 on 10.08.2017.
 */

public class TrimCityInfo {
    private static TrimCityInfo instance;

    private TrimCityInfo() {

    }

    public static TrimCityInfo getInstance() {
        if (instance == null) {
            instance = new TrimCityInfo();
        }
        return instance;
    }
    
    public String trimCityName(String fullCityName) {
        if (fullCityName.indexOf(',') != -1) {
            return fullCityName.substring(0, fullCityName.indexOf(','));
        } else {
            return fullCityName;
        }
    }
}
