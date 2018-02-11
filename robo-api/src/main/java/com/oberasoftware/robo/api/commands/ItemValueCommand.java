package com.oberasoftware.robo.api.commands;

import com.oberasoftware.robo.api.model.Value;

import java.util.Map;

/**
 * @author renarj
 */
public interface ItemValueCommand extends ItemCommand {

    Value getValue(String property);

    Map<String, Value> getValues();
}
