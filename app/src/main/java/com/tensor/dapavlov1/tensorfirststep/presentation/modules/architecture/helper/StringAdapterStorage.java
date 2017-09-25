package com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.helper;

import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by da.pavlov1 on 14.09.2017.
 */

public class StringAdapterStorage {
    public static StringAdapterStorage getInstance() {
        return AdapterStorageLoader.INSTANCE;
    }

    private static final class AdapterStorageLoader {
        private static final StringAdapterStorage INSTANCE = new StringAdapterStorage();
    }

    private StringAdapterStorage() {
        storage = new HashMap<>();
    }

    private Map<String, ? super ArrayAdapter<String>> storage;

    public void saveAdapter(String adapterId, ArrayAdapter<String> adapter) {
        storage.put(adapterId, adapter);
    }

    public <T extends ArrayAdapter<String>> T restoreAdapter(String adapterId) {
        return (T) storage.remove(adapterId);
    }
}
