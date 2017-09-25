package com.tensor.dapavlov1.tensorfirststep.core.utils;

import android.support.annotation.NonNull;

import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 22.09.2017.
 */

public class RepositoryLogic {
    public static <Type> Observable<Type> loadNetworkPriority(final Observable<Type> disk, @NonNull final Observable<Type> network) {
        if (NetworkHelper.isOnline()) {
            return network;
        } else {
            if (disk != null) {
                return disk;
            } else {
                return Observable.empty();
            }
        }
    }
}
