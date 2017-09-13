package com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.router;

public interface MvpRouter<L extends MvpRouter.Listener> {
    interface Listener {}

    void setListener(L listener);
}