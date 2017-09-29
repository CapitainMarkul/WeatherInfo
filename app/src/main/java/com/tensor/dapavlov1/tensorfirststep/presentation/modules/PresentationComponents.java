package com.tensor.dapavlov1.tensorfirststep.presentation.modules;

import com.tensor.dapavlov1.tensorfirststep.domain.assembly.BusinessComponent;
import com.tensor.dapavlov1.tensorfirststep.domain.services.UpdateWeatherService;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.assembly.AddCityComponent;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.assembly.AddCityDaggerModule;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.assembly.FavoriteComponent;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.assembly.FavoriteDaggerModule;

import dagger.Component;

/**
 * Created by da.pavlov1 on 20.09.2017.
 */

@PerPresentationScope
@Component(dependencies = BusinessComponent.class)
public interface PresentationComponents {
    void inject(UpdateWeatherService service);

    AddCityComponent addCityComponent(AddCityDaggerModule addCityDaggerModule);
    FavoriteComponent favoriteComponent(FavoriteDaggerModule favoriteDaggerModule);
}
