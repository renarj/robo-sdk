package com.oberasoftware.robo.core.model;

import com.oberasoftware.robo.api.commands.BasicCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Renze de Vries
 */
public class BasicCommandBuilder {

    private final Map<String, String> properties = new HashMap<>();
    private final String controllerId;

    private String item;
    private String label;

    private BasicCommandBuilder(String controllerId) {
        this.controllerId = controllerId;
    }

    public static BasicCommandBuilder create(String controllerId) {
        return new BasicCommandBuilder(controllerId);
    }

    public BasicCommandBuilder item(String itemId) {
        this.item = itemId;
        return this;
    }

    public BasicCommandBuilder label(String label) {
        this.label = label;
        return this;
    }

    public BasicCommandBuilder property(String name, String value) {
        this.properties.put(name, value);
        return this;
    }

    public BasicCommand build() {
        BasicCommandImpl command = new BasicCommandImpl();
        command.setControllerId(controllerId);
        command.setCommandType(label);
        command.setItemId(item);
        command.setProperties(properties);

        return command;
    }
}
