package com.oberasoftware.robo.api.commands;

/**
 * @author renarj
 */
public interface ItemCommand extends Command {
    String getControllerId();

    String getItemId();
}
