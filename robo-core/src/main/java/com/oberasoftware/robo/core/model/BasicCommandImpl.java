package com.oberasoftware.robo.core.model;

import com.oberasoftware.robo.api.commands.BasicCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * @author renarj
 */
public class BasicCommandImpl implements BasicCommand {
    private String controllerId;
    private String itemId;
    private String commandType;

    private Map<String, String> properties = new HashMap<>();

    public String getControllerId() {
        return controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public String getProperty(String property) {
        return properties.get(property);
    }

    @Override
    public String toString() {
        return "BasicCommandImpl{" +
                "controllerId='" + controllerId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", commandType='" + commandType + '\'' +
                ", properties=" + properties +
                '}';
    }
}
