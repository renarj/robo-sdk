package com.oberasoftware.robo.dynamixel.motion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.api.motion.MotionConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * @author Renze de Vries
 */
@Component
public class JsonMotionLoader implements MotionConverter {
    private static final Logger LOG = LoggerFactory.getLogger(JsonMotionLoader.class);

    @Override
    public List<Motion> loadMotions(String motionFile) {
        try {
            URL resource = this.getClass().getResource(motionFile);
            if(resource != null) {
                LOG.debug("Opening resource: {}", resource.toURI());
                Path path = Paths.get(resource.toURI());

                String json = new String(Files.readAllBytes(path), "utf-8");
                LOG.debug("Loaded json resource: {}", json);

                return new ObjectMapper().readValue(json, MotionList.class).getMotions();
            } else {
                LOG.error("Could not find motion file: {}", motionFile);
            }
        } catch (URISyntaxException | IOException e) {
            LOG.error("Could not read file", e);
        }

        return emptyList();
    }
}
