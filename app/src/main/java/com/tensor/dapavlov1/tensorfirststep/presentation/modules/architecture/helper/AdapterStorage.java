package com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.helper;

import android.support.v7.widget.RecyclerView;

import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.presenter.MvpPresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by da.pavlov1 on 14.09.2017.
 */

public class AdapterStorage {
    public static AdapterStorage getInstance() {
        return AdapterStorageLoader.INSTANCE;
    }

    private static final class AdapterStorageLoader {
        private static final AdapterStorage INSTANCE = new AdapterStorage();
    }

    private AdapterStorage() {
        storage = new HashMap<>();
    }

    private Map<String, ? super RecyclerView.Adapter> storage;

    public void saveAdapter(String adapterId, RecyclerView.Adapter adapter) {
        storage.put(adapterId, adapter);
    }

    public <T extends RecyclerView.Adapter> T restoreAdapter(String adapterId) {
        return (T) storage.remove(adapterId);
    }
}
