package com.tensor.dapavlov1.tensorfirststep.domain.assembly;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by da.pavlov1 on 21.09.2017.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerBusinessScope {
}
