package com.tensor.dapavlov1.tensorfirststep.provider.commands;

import com.tensor.dapavlov1.tensorfirststep.interfaces.Command;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;

/**
 * Created by da.pavlov1 on 16.08.2017.
 */

public class DelCityByIndexCommand implements Command {
    private DbClient dbClient;
    private int index;

    public DelCityByIndexCommand(DbClient dbClient, int index) {
        this.dbClient = dbClient;
        this.index = index;
    }

    @Override
    public void execute() {
        dbClient.deleteCity(index);
    }

    @Override
    public void undo() {

    }
}
