package com.oberasoftware.robo.api.managers;

import com.oberasoftware.robo.api.model.State;
import com.oberasoftware.robo.api.model.Value;

import java.util.List;

/**
 * @author renarj
 */
public interface StateManager {
    State setState(String controllerId, String itemId, String label, Value value);

    State getState(String controllerId, String itemId);

    List<State> getStates(String controllerId);
}
