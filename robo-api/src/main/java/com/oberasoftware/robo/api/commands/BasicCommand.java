package com.oberasoftware.robo.api.commands;

import java.util.Map;

/**
 * @author renarj
 */
public interface BasicCommand extends ItemCommand {
    String getCommandType();

    Map<String, String> getProperties();

    String getProperty(String property);
}
