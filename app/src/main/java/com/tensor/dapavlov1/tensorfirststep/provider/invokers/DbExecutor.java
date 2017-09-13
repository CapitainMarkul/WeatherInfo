package com.tensor.dapavlov1.tensorfirststep.provider.invokers;

import com.tensor.dapavlov1.tensorfirststep.provider.command.db.DbCommand;

/**
 * Created by da.pavlov1 on 16.08.2017.
 */

public class DbExecutor {
    private DbCommand command;

    public void setCommand(DbCommand command) {
        this.command = command;
    }

    public void execute() {
        if (command != null) {
            command.execute();
        }
    }

    public void undo() {
        if (command != null) {
            command.undo();
        }
    }
}
