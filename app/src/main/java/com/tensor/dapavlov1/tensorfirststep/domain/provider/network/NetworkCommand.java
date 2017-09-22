package com.tensor.dapavlov1.tensorfirststep.domain.provider.network;

/**
 * Created by da.pavlov1 on 13.09.2017.
 */

public interface NetworkCommand<T> {
    T execute();
}
