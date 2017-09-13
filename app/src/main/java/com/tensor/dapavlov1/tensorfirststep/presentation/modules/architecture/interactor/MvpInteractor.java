package com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor;

/**
 * Created by da.pavlov1 on 12.09.2017.
 */

public interface MvpInteractor<Listener extends MvpInteractor.LPresenter> {

    void setListener(Listener listener);

    interface LPresenter {
    }
}
