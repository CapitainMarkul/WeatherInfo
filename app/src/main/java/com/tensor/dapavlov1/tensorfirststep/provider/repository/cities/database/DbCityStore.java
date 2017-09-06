//package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.database;
//
//import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityWeatherWrapper;
//import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
//import com.tensor.dapavlov1.tensorfirststep.provider.CreatorDbClient;
//import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;
//import com.tensor.dapavlov1.tensorfirststep.provider.commands.AddCityInDbCmd;
//import com.tensor.dapavlov1.tensorfirststep.provider.commands.DelCityFromDbCmd;
//import com.tensor.dapavlov1.tensorfirststep.provider.invokers.DbExecutor;
//import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces.CityDataStore;
//import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyResponseException;
//
//import io.reactivex.Observable;
//
///**
// * Created by da.pavlov1 on 24.08.2017.
// */
//
//public class DbCityStore {
//    private DbClient dbClient = CreatorDbClient.getInstance().createNewDaoClient();
//    private DbExecutor dbExecutor;
//
//    public DbCityStore() {
//        dbExecutor = new DbExecutor();
//    }
//
//    public void add(CityWeatherWrapper city) {
//        dbExecutor.setCommand(
//                new AddCityInDbCmd(dbClient, city));
//        dbExecutor.execute();
//    }
//
//    public void delete(Object city) {
//        //удаление по индексу
//        if (city instanceof Integer) {
//            dbExecutor.setCommand(
//                    new DelCityFromDbCmd(dbClient, (Integer) city));
//            dbExecutor.execute();
//        }
//        //удаление по элементу
//        else if (city instanceof CityWeatherWrapper) {
//            dbExecutor.setCommand(
//                    new AddCityInDbCmd(dbClient, (CityWeatherWrapper) city));
//            dbExecutor.undo();
//        }
//    }
//}
