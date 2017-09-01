package com.tensor.dapavlov1.tensorfirststep.provider.callbacks;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by da.pavlov1 on 31.08.2017.
 */

public interface CallbackCitiesRx<T> {
    Maybe<T> onUpdate(T result);

    Maybe<T> onOldFromDb(T result);

    public void isEmpty();
}
