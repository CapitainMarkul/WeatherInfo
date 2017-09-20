package com.tensor.dapavlov1.tensorfirststep.presentation.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tensor.dapavlov1.tensorfirststep.DisposableManager;
import com.tensor.dapavlov1.tensorfirststep.interfaces.ShowMessage;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.presenter.MvpPresenter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.viewmodel.AbstractViewModel;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.helper.PresenterStorage;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.viewmodel.MvpViewModel;

import org.parceler.Parcels;

/**
 * Created by da.pavlov1 on 14.09.2017.
 */

public abstract class BaseActivity<ViewModel extends MvpViewModel, Presenter extends MvpPresenter<ViewModel>>
        extends AppCompatActivity implements ActivityComponents, ShowMessage {

    private static final String VM_KEY = BaseActivity.class.getSimpleName() + "_VM";
    private static final String PRESENTER_KEY = BaseActivity.class.getSimpleName() + "_PRESENTER";
    public static final String DISPOSABLE_POOL_KEY = BaseActivity.class.getSimpleName() + "_DISPOSABLE";

    private DisposableManager disposableManager = DisposableManager.getInstance();

    private ViewModel viewModel;
    private Presenter presenter;

    protected abstract void inject();

    protected abstract Presenter createPresenter();

    protected abstract ViewModel createViewModel();

    protected Presenter getPresenter() {
        return presenter;
    }

    protected ViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inject();

        if (savedInstanceState == null) {
            viewModel = createViewModel();
            presenter = createPresenter();
        } else {
            //// FIXME: 14.09.2017 Для чего вытаскиваем ViewModel ? Попробовать убрать
            //Ведь если здесь Null, то какой смысл ? (Существование ViewModel без Presenter'a)
            viewModel = Parcels.unwrap(savedInstanceState.getParcelable(VM_KEY));
            presenter = PresenterStorage.getInstance().restorePresenter(PRESENTER_KEY);

            if (presenter == null) {
                presenter = createPresenter();
            } else {
                ViewModel presenterVm = presenter.getViewModel();
                if (presenterVm != null) {
                    viewModel = presenterVm;
                }
            }
        }

        presenter.attachView(getViewModel(), this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(getViewModel(), this);
    }

    protected DisposableManager getDisposableManager() {
        return disposableManager;
    }

    @Override
    protected void onStop() {
        presenter.detachView();

        //Определяем, нужно ли уничтожать Presenter
        if (this.isChangingConfigurations()) {
            //Нужно сохранить состояние Presenter'a
            PresenterStorage.getInstance().savePresenter(PRESENTER_KEY, presenter);
        } else {
            presenter.destroy();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            getDisposableManager().disposeAll(DISPOSABLE_POOL_KEY);
            Log.e("SIze:", String.valueOf(getDisposableManager().testSize(DISPOSABLE_POOL_KEY)));
        }
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // сохрарняем ViewModel
        outState.putParcelable(VM_KEY, Parcels.wrap(getViewModel()));
        super.onSaveInstanceState(outState);
    }
}
