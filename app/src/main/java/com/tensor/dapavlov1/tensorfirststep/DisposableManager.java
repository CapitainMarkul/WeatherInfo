package com.tensor.dapavlov1.tensorfirststep;

import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by da.pavlov1 on 04.09.2017.
 */

public class DisposableManager<T extends String> {
    public static DisposableManager getInstance() {
        return DisposableManager.DisposableManagerLoader.INSTANCE;
    }

    private static final class DisposableManagerLoader {
        private static final DisposableManager INSTANCE = new DisposableManager();
    }

    private DisposableManager() {
    }

    private Map<T, CompositeDisposable> mapCompositeDisposable = new HashMap<>();

    private static CompositeDisposable compositeDisposable;

    public void addDisposable(T className, Disposable disposable) {
//        CompositeDisposable tempCompositeDisposable = mapCompositeDisposable.get(className);

        if (mapCompositeDisposable.get(className) == null) {
            mapCompositeDisposable.put(className, new CompositeDisposable());
        } else {
            mapCompositeDisposable.get(className).add(disposable);
        }

//        getCompositeDisposable().add(disposable);
    }

    public  void dispose(T className) {
        CompositeDisposable tempCompositeDisposable =  mapCompositeDisposable.get(className).add(disposable);
//        getCompositeDisposable().dispose();
    }

    public static int testSize() {
        return getCompositeDisposable().size();
    }

    private static CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        return compositeDisposable;
    }
}
