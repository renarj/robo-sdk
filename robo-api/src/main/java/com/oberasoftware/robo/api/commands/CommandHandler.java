package com.oberasoftware.robo.api.commands;

import com.oberasoftware.robo.api.model.Item;

/**
 * @author renarj
 */
public interface CommandHandler<T extends Item> {
    void receive(T item, Command command);
}
