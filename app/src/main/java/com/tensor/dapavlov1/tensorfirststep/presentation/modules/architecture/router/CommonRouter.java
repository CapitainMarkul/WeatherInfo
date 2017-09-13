package com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.router;

/**
 * Created by da.pavlov1 on 13.09.2017.
 */

public abstract class CommonRouter<Listener extends MvpRouter.Listener> implements MvpRouter<Listener> {
    private Listener listener;

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    protected Listener getListener() {
        return listener;
    }
}
