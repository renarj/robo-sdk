package com.oberasoftware.home.core.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Renze de Vries
 */
public class MQTTPathParser {
    private static final Logger LOG = LoggerFactory.getLogger(MQTTPathParser.class);

    private static final String PATH_REGEX = "/(.*)/(.*)/(.*)/(.*)";
    private static final Pattern PATH_PATTERN = Pattern.compile(PATH_REGEX);
    private static final int GROUP_COUNT = 4;

    public static ParsedPath parsePath(String path) {
        Matcher matched = PATH_PATTERN.matcher(path);
        if(matched.find() && matched.groupCount() == GROUP_COUNT) {
            String group = matched.group(1);
            String controllerId = matched.group(2);
            String device = matched.group(3);
            String label = matched.group(4);

            return new ParsedPath(MessageGroup.fromGroup(group), controllerId, device, label);
        } else {
            LOG.debug("Invalid path: {}", path);
            return null;
        }
    }
}
