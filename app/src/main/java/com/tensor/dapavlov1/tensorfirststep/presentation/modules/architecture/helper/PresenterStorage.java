package com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.helper;

import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.presenter.MvpPresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by da.pavlov1 on 14.09.2017.
 */

public class PresenterStorage {
    public static PresenterStorage getInstance() {
        return PresenterStorageLoader.INSTANCE;
    }

    private static final class PresenterStorageLoader {
        private static final PresenterStorage INSTANCE = new PresenterStorage();
    }

    private PresenterStorage() {
        storage = new HashMap<>();
    }

    private Map<String, ? super MvpPresenter> storage;

    public void savePresenter(String presenterId, MvpPresenter presenter) {
        storage.put(presenterId, presenter);
    }

    // TODO: 14.09.2017 Разобраться до конца
    public <T extends MvpPresenter> T restorePresenter(String presenterId) {
        return (T) storage.remove(presenterId);
    }
}
