package com.tensor.dapavlov1.tensorfirststep;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by da.pavlov1 on 04.09.2017.
 */

public class DisposableManager {
    public static DisposableManager getInstance() {
        return DisposableManager.DisposableManagerCurrentActivityLoader.INSTANCE;
    }

    private static final class DisposableManagerCurrentActivityLoader {
        private static final DisposableManager INSTANCE = new DisposableManager();
    }

    private Map<String, CompositeDisposable> mapCompositeDisposable;

    private DisposableManager() {
        mapCompositeDisposable = new HashMap<>();
    }

    public void addDisposable(String poolCompositeDisposableKey, Disposable disposable) {
        if (mapCompositeDisposable.get(poolCompositeDisposableKey) == null) {
            CompositeDisposable compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(disposable);

            mapCompositeDisposable.put(poolCompositeDisposableKey, compositeDisposable);
        } else {
            mapCompositeDisposable.get(poolCompositeDisposableKey).add(disposable);
        }
    }

    public void disposeAll(String poolCompositeDisposableKey) {
        if (mapCompositeDisposable.get(poolCompositeDisposableKey) != null) {
            mapCompositeDisposable.get(poolCompositeDisposableKey).dispose();
            //FixMe: Имеет ли смысл заносить Null ?
            mapCompositeDisposable.put(poolCompositeDisposableKey, null);
        }
    }

    //debug
    public int testSize(String idManager) {
        if (mapCompositeDisposable.get(idManager) != null) {
            return mapCompositeDisposable.get(idManager).size();
        }
        return 0;
    }
}
