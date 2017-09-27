package com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.helper;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by da.pavlov1 on 14.09.2017.
 */

public class RecyclerAdapterStorage {
    public static RecyclerAdapterStorage getInstance() {
        return AdapterStorageLoader.INSTANCE;
    }

    private static final class AdapterStorageLoader {
        private static final RecyclerAdapterStorage INSTANCE = new RecyclerAdapterStorage();
    }

    private RecyclerAdapterStorage() {
        storage = new HashMap<>();
    }

    private Map<String, ? super RecyclerView.Adapter> storage;

    public void saveAdapter(String adapterId, RecyclerView.Adapter adapter) {
        storage.put(adapterId, adapter);
    }

    public <T extends RecyclerView.Adapter> T restoreAdapter(String adapterId) {
        return (T) storage.remove(adapterId);
    }

    public void test(){
        Log.e("TAG", "tre");
    }
}
