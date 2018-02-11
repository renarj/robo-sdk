package com.oberasoftware.home.core.mqtt;

import com.oberasoftware.base.event.EventFilter;
import com.oberasoftware.base.event.HandlerEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author Renze de Vries
 */
public class MQTTPathFilter implements EventFilter {
    private static final Logger LOG = LoggerFactory.getLogger(MQTTPathFilter.class);

    private static final String WILD_CARD = "*";

    @Override
    public boolean isFiltered(Object o, HandlerEntry handlerEntry) {
        if(o instanceof MQTTMessageImpl) {
            LOG.debug("Checking filter on message: {}", o);
            MQTTMessageImpl mqttMessage = (MQTTMessageImpl) o;
            Method eventMethod = handlerEntry.getEventMethod();
            MQTTPath mqttPath = eventMethod.getAnnotation(MQTTPath.class);
            if(mqttPath != null) {
                String actualPath = mqttMessage.getTopic();

                return !isPathSupported(mqttPath, actualPath);
            }
        }
        return false;
    }

    /**
     * actualpath could be /states/robomax/sonar/distance -> value {"value":10.5}
     * supportedPath could be /states/{controllerId}/{device}/{label}
     *
     * actualpath could be /commands/robomax/motion/stand
     *
     * @param supportedPath
     * @param actualPath
     * @return
     */
    private boolean isPathSupported(MQTTPath supportedPath, String actualPath) {
        ParsedPath parsedPath = MQTTPathParser.parsePath(actualPath);
        if(parsedPath != null) {
            boolean controllerMatched = isMatched(parsedPath.getControllerId(), supportedPath.controller());
            boolean deviceMatched = isMatched(parsedPath.getDeviceId(), supportedPath.device());
            boolean labelMatched = isMatched(parsedPath.getLabel(), supportedPath.label());
            boolean groupMatched = supportedPath.group() == MessageGroup.ALL ||
                    parsedPath.getMessageGroup() == supportedPath.group();
            return controllerMatched && deviceMatched && labelMatched && groupMatched;
        } else {
            return false;
        }
    }

    private boolean isMatched(String path, String supportedPath) {
        return supportedPath.equalsIgnoreCase(WILD_CARD)
                || supportedPath.equalsIgnoreCase(path);

    }
}
