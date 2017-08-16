package com.tensor.dapavlov1.tensorfirststep.provider.commands;

import com.tensor.dapavlov1.tensorfirststep.interfaces.Command;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DaoClient;

/**
 * Created by da.pavlov1 on 16.08.2017.
 */

public class DelCityByIndexCommand implements Command {
    private DaoClient daoClient;
    private int index;

    public DelCityByIndexCommand(DaoClient daoClient, int index) {
        this.daoClient = daoClient;
        this.index = index;
    }

    @Override
    public void execute() {
        daoClient.deleteCity(index);
    }

    @Override
    public void undo() {

    }
}
