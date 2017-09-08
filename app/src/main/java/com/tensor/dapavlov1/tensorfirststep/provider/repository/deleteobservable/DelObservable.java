package com.tensor.dapavlov1.tensorfirststep.provider.repository.deleteobservable;

/**
 * Created by da.pavlov1 on 08.09.2017.
 */

public interface DelObservable {
    void subscribe(DelObserver observer);

    void unsubscribe(DelObserver observer);

    void notifyAllObservers(boolean isSuccess);
}
