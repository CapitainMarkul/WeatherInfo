package com.tensor.dapavlov1.tensorfirststep;

import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;

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

    private Map<Integer, CompositeDisposable> mapCompositeDisposable;

    private DisposableManager() {
        mapCompositeDisposable = new HashMap<>();
    }

    public void addDisposable(int idPoolCompositeDisposable, Disposable disposable) {
        if (mapCompositeDisposable.get(idPoolCompositeDisposable) == null) {
            CompositeDisposable compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(disposable);
            mapCompositeDisposable.put(idPoolCompositeDisposable, compositeDisposable);
        } else {
            mapCompositeDisposable.get(idPoolCompositeDisposable).add(disposable);
        }
    }

    public void disposeAll(int idPoolCompositeDisposable) {
        if (mapCompositeDisposable.get(idPoolCompositeDisposable) != null) {
            mapCompositeDisposable.get(idPoolCompositeDisposable).dispose();
            //FixMe: Имеет ли смысл заносить Null ?
            mapCompositeDisposable.put(idPoolCompositeDisposable, null);
        }
    }

    //debug
//    public static int testSize(int idManager) {
//        if (mapCompositeDisposable.get(idManager) != null) {
//            return mapCompositeDisposable.get(idManager).size();
//        }
//        return 0;
//    }
}
