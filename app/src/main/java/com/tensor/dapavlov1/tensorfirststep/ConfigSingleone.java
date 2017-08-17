package com.tensor.dapavlov1.tensorfirststep;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by da.pavlov1 on 17.08.2017.
 */

public class ConfigSingleone {
    private static final ConfigSingleone ourInstance = new ConfigSingleone();

    public static ConfigSingleone getInstance() {
        return ourInstance;
    }

    private ConfigSingleone() {
    }

    private Map<String, Object> rootMap = new HashMap<>();

    public void setData(String key, Object value){
        rootMap.put(key, value);
    }

    public Object getData(String key){
        return rootMap.get(key);
    }
}
