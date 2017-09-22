package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.interactor;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.core.utils.RepositoryLogic;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.DbToViewMap;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.GsonToDbMap;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.GsonFactory;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.mythrows.NetworkConnectException;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.service.WeatherService;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.CommonInteractor;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.Wrapper.ResultWrapper;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteInteractorPresenterContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 12.09.2017.
 */

public class FavoriteCityInteractor extends CommonInteractor<FavoriteInteractorPresenterContract.Presenter, ResultWrapper<Flowable<CityView>>>
        implements FavoriteInteractorPresenterContract.Interactor {

    private final WeatherService weatherService;
//    private final CitiesDataRepository citiesDataRepository;

    @Inject
    public FavoriteCityInteractor(WeatherService weatherService) {
        this.weatherService = weatherService;
//        this.citiesDataRepository = citiesDataRepository;
    }

    @Override
    public void obtainCitiesWeather() {
        List<String> cityNames = weatherService.getDbService().getCitiesName();
// TODO: 22.09.2017 Разобраться с Flowable (to Observable ?)
        Observable<CityView> network = weatherService.getWeatherService().getWeatherByCitiesRx(cityNames)
                .map(string -> GsonFactory.getInstance().createGsonCityModel(string))
                .map(gsonCity -> GsonToDbMap.getInstance().convertGsonModelToDaoModel(gsonCity))
                .map(cityWeatherWrapper -> {
//                    set Update weather info in DB
                    App.getExecutorService().execute(() -> weatherService.getDbService().updateCity(cityWeatherWrapper));
                    return DbToViewMap.getInstance().convertDbModelToViewModel(
                            cityWeatherWrapper.getCityDb(), cityWeatherWrapper.getWeathers(), true);
                });

        Observable<CityView> disk = weatherService.getDbService().loadAllCitiesViewRx().toObservable();

        Observable<CityView> observable = RepositoryLogic.loadNetworkPriority(disk, network);

        ResultWrapper<Flowable<CityView>> result;

        if (observable == disk) {
            result = new ResultWrapper<>(observable.toFlowable(BackpressureStrategy.BUFFER), new NetworkConnectException());
        } else {
            result = new ResultWrapper<>(observable.toFlowable(BackpressureStrategy.BUFFER), null);
        }

        getListener().onObtainCitiesWeather(result);
    }

    /**
     * Network
     * */

//    public Flowable<CityView> getCitiesRx(List<String> cityNames) {
//        return weatherApiClient.getWeatherByCitiesRx(cityNames)
//                .map(string -> gsonFactory.createGsonCityModel(string))
//                .map(gsonCity -> gsonToDbMap.convertGsonModelToDaoModel(gsonCity))
//                .toFlowable(BackpressureStrategy.BUFFER)
//                .switchMap(cityWeatherWrapper -> {
//                    //set Update weather info in DB
//                    App.getExecutorService().execute(() -> dbClient.updateCity(cityWeatherWrapper));
//                    return Flowable.just(dbToViewMap.convertDbModelToViewModel(cityWeatherWrapper.getCityDb(), cityWeatherWrapper.getWeathers(), true));
//                });
//    }

    /**
     * Disk
     */
//    @Override
//    public Flowable<CityView> getCitiesRx() {
//        return Flowable.create(e ->
//                dbClient.loadAllCitiesDbRx()
//                        .subscribeOn(Schedulers.from(App.getExecutorService()))
//                        .flatMap(new Function<List<CityDb>, Publisher<CityDb>>() {
//                            @Override
//                            public Publisher<CityDb> apply(@NonNull List<CityDb> cityDbs) throws Exception {
//                                return Flowable.fromIterable(cityDbs);
//                            }
//                        })
//                        .map(city ->
//                                DbToViewMap.getInstance().convertDbModelToViewModel(city, city.getWeathers(), true))
//                        .subscribe(
//                                result -> {
//                                    e.onNext(result);
//                                    // TODO: 31.08.2017 Здесь можно установить задержку, для плавности анимации, при загрузке из хранилища
//                                    Thread.sleep(100);
//                                },
//                                e::onError,
//                                e::onComplete), BackpressureStrategy.BUFFER);
//    }

//    public List<String> getCitiesName() {
//        try {
//            return dbClient.getCitiesName();
//        } catch (EmptyDbException e) {
//            return new ArrayList<>();
//        }
//    }
    @Override
    public void delCityFromDb(CityView city) {
        weatherService.getDbService().deleteCity(city);
//        executeVoidCommand(new DelCityFromDbCommand(city));
    }
}
