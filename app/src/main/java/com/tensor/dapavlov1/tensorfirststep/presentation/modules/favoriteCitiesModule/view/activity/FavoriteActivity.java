package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.tensor.dapavlov1.tensorfirststep.CheckUpdateInOtherActivity;
import com.tensor.dapavlov1.tensorfirststep.DisposableManager;
import com.tensor.dapavlov1.tensorfirststep.BaseLoader;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.databinding.ActivityFavoriteBinding;
import com.tensor.dapavlov1.tensorfirststep.interfaces.EmptyListener;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteCityRouterPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.view.adapter.FavoriteAdapter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.presenter.FavoritePresenter;
import com.tensor.dapavlov1.tensorfirststep.interfaces.DelItemListener;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.router.FavoriteToAddCityRouter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class FavoriteActivity extends AppCompatActivity
        implements com.tensor.dapavlov1.tensorfirststep.interfaces.FavoriteActivity,
        DelItemListener, EmptyListener,
        LoaderManager.LoaderCallbacks<Map<String, Object>> {
    private DisposableManager disposableManager;
    private FavoritePresenter mPresenter;
    private FavoriteAdapter favoriteAdapter;

    private CheckUpdateInOtherActivity checkUpdateInOtherActivity = CheckUpdateInOtherActivity.getInstance();

    private ActivityFavoriteBinding binding;

    private final static String FAVORITE_PRESENTER = "favorite_presenter";
    private final static String FAVORITE_ADAPTER = "favorite_adapter";
    public final static int ID_POOL_COMPOSITE_DISPOSABLE = 1;
    private final static int LOADER_FAVORITE_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite);

        initDisposableManager();
        setupLoaders();
        setupListeners();
    }

    private void initDisposableManager() {
        disposableManager = DisposableManager.getInstance();
    }

    private void checkUpdateInOtherActivity() {
        if (checkUpdateInOtherActivity.isUpdate()) {
            startUpdateWeatherInfo();
            checkUpdateInOtherActivity.setUpdate(false);
        }
    }

    public DisposableManager getDisposableManager() {
        return disposableManager;
    }


//    private void setupRouter() {
//        favoriteToAddCityRouter = new FavoriteToAddCityRouter();
//        mPresenter.setRouter(favoriteToAddCityRouter);
//    }

    private void setupPresenter() {
        mPresenter = new FavoritePresenter();
        mPresenter.attachActivity(this);
//        setupRouter();
    }

    private void startUpdateWeatherInfo() {
        favoriteAdapter.setDefaultSetting();
        mPresenter.updateWeathers();
    }

    private void setupListeners() {
        binding.srRefresh.setOnRefreshListener(() -> startUpdateWeatherInfo());
    }

    @Override
    public void setItems(final List<CityView> weathers) {
        favoriteAdapter.setItems(weathers);
    }

    public void setItemInAdapter(CityView cityViewWeather) {
        favoriteAdapter.setItem(cityViewWeather);
        //для работы анимации на 1 элементе
        binding.recyclerViewFavorite.scrollToPosition(favoriteAdapter.getItemCount());
    }

    @Override
    public void showMessage(@StringRes final int message) {
        Snackbar.make(binding.rootContainer, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showErrorMessage(@StringRes final int message) {
        Snackbar.make(binding.rootContainer, message, Snackbar.LENGTH_LONG).show();
    }

    private Map<String, Object> createConfigMap() {
        Map<String, Object> values = new HashMap<>();
        values.put(FAVORITE_PRESENTER, mPresenter);
        values.put(FAVORITE_ADAPTER, favoriteAdapter);
        return values;
    }

    //OTHER
    @Override
    protected void onStart() {
        super.onStart();
        checkUpdateInOtherActivity();
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            disposableManager.disposeAll(ID_POOL_COMPOSITE_DISPOSABLE);
        }
        super.onDestroy();
        mPresenter.detachActivity();
    }

    @Override
    public void onItemClick(CityView city) {
        mPresenter.deleteCity(city);
    }

    public void delCityFromAdapter(CityView deletedCity){
        int deletedPosition = favoriteAdapter.getItems().indexOf(deletedCity);
        favoriteAdapter.getItems().remove(deletedPosition);
        favoriteAdapter.notifyItemRemoved(deletedPosition);
        // TODO: 13.09.2017 Подумать насчет анимации
//        favoriteAdapter.notifyItemRangeChanged(deletedPosition, favoriteAdapter.getItemCount());
//        favoriteAdapter.setAnimation(binding.cardFullInfo, position, R.anim.recyclerdel);
        if(favoriteAdapter.getItems().isEmpty()){
            onEmpty();
        }
    }

//    private void removeCard(int position) {
//            cityViewWeathers.remove(position);  //удаляем из листаАдаптера
//            notifyItemRemoved(position);    //тправляем запрос на обновление списка
//            notifyItemRangeChanged(getAdapterPosition(), cityViewWeathers.size());  //склеиваем новый список
//            setAnimation(binding.cardFullInfo, position, R.anim.recyclerdel);
//            if (emptyListener != null && cityViewWeathers.isEmpty()) {
//                emptyListener.onEmpty();
//            }
//        }




    //Loaders
    private void setupLoaders() {
        getSupportLoaderManager().initLoader(LOADER_FAVORITE_ID, null, this);
    }

    private void setupRecyclerAdapter() {
        favoriteAdapter = new FavoriteAdapter();
        favoriteAdapter.setListener(FavoriteActivity.this);
//        favoriteAdapter.setEmptyListener(FavoriteActivity.this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        setupPresenter();
        setupRecyclerAdapter();
        setupRecyclerView();

        //Ситуация, на другом экране делаем Terminate, потом добавляем город в избранное.
        // Возвращаемся и получаем, что лоадер создается новый и вызывает этот метод
        // + были изменения на другом экране, следовательно метод checkUpdateInOtherActivity() тоже начнет обновление списка.
        // В итоге мы получаем дублирующиеся результаты.
        // Решение: если был Terminate, не обновляем информацию при создании Лоадера
        if (!checkUpdateInOtherActivity.isUpdate()) {
            startUpdateWeatherInfo();
        }
        return new BaseLoader(getBaseContext(), createConfigMap());
    }

    private void setupRecyclerView() {
        binding.recyclerViewFavorite.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recyclerViewFavorite.setAdapter(favoriteAdapter);
        binding.recyclerViewFavorite.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadFinished(Loader<Map<String, Object>> loader, Map<String, Object> dataMap) {
        mPresenter = (FavoritePresenter) dataMap.get(FAVORITE_PRESENTER);

        binding.setMPresenter(mPresenter);

        //восстаансливаем прошлый адаптер
        favoriteAdapter = (FavoriteAdapter) dataMap.get(FAVORITE_ADAPTER);
        setupRecyclerView();

        mPresenter.attachActivity(this);

        //если ответ от сервера уже пришел, то показываем результат
        if (mPresenter.isLoadingComplete()) {
            favoriteAdapter.setAnimate(isChangingConfigurations());
            mPresenter.showCachedCities();
        } else {
            if (binding.getCities() != null && binding.getCityView() != null) {
                binding.setIsLoading(true);
            } else {
                if (binding.cardWeatherDefault.cvDefault.getVisibility() == View.VISIBLE) {
                    binding.setIsLoading(true);
                }
            }
        }

        //debug
//        binding.re.recyclerViewFavorite.setVisibility(View.INVISIBLE);
//        //test
//        if (binding.cardWeatherDefault.cvDefault.getVisibility() == View.VISIBLE ) {
//            if (binding.recyclerViewFavorite.getVisibility() == View.VISIBLE ) {
//
//                Log.e("First", " yes");
//            }
//        }
//        if (binding.cardWeatherDefault.cvDefault.isShown()) {
//            if (binding.recyclerViewFavorite.isShown()) {
//
//                Log.e("Second", " yes");
//            }
//        }
    }

    public ActivityFavoriteBinding getBinding() {
        return binding;
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onEmpty() {
        //чистим последний cachedCity, чтобы он не показывался при повороте экрана
//        mPresenter.clearCacheCities();

        Log.e("Visible", String.valueOf(binding.cardWeatherDefault.cvDefault.getVisibility()));

        binding.setIsLoading(false);
        binding.setCities(null);
        binding.setCityView(null);

        Log.e("isLoading", String.valueOf(binding.getIsLoading()));
        Log.e("City", String.valueOf(binding.getCityView()));
        Log.e("Cities", String.valueOf(binding.getCities()));
        Log.e("VisibleTotal", String.valueOf(binding.cardWeatherDefault.cvDefault.getVisibility()));


//// FIXME: 05.09.2017 Когда RecyclerView становится пуст приветственная Карточка не появляется, хотя выражение в Биндинге вроде как верное и в логах
        //Update: если не переворачивать экран, то все появится, но если во время обновления повернуть, а потом удалить, то нет
//        binding.recyclerViewFavorite.setVisibility(View.INVISIBLE);
//        binding.recyclerViewFavorite.setVisibility(View.INVISIBLE);
//        binding.cardWeatherDefault.cvDefault.setVisibility(View.VISIBLE);
//        //        binding.setIsLoading(true);
//        Log.e("CityView:", String.valueOf(getBinding().getCities()));
//        Log.e("Cities:", String.valueOf(getBinding().getCityView()));
//        Log.e("Loading:", String.valueOf(getBinding().getIsLoading()));
    }
}
