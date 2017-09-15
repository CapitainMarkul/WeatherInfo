package com.tensor.dapavlov1.tensorfirststep.domain.provider.command.cloud;

/**
 * Created by da.pavlov1 on 13.09.2017.
 */

public interface CloudCommand<T> {
    T execute();
}
