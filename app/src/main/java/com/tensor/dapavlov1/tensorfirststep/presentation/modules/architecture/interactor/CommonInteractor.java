package com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor;

import android.support.annotation.NonNull;

import com.tensor.dapavlov1.tensorfirststep.domain.provider.network.NetworkCommand;

/**
 * Created by da.pavlov1 on 12.09.2017.
 */

public abstract class CommonInteractor<Listener extends MvpInteractor.LPresenter, Result>
        implements MvpInteractor<Listener> {

    private Listener listener;

    public Result executeCommand(NetworkCommand<Result> networkCommand) {
        return networkCommand.execute();
    }

//    public void executeVoidCommand(DbCommand cloudCommand) {
//        cloudCommand.execute();
//    }

    @Override
    public void setListener(@NonNull Listener listener) {
        this.listener = listener;
    }

    protected Listener getListener() {
        return listener;
    }
}
