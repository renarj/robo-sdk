package com.oberasoftware.robo.core.robomotion;

import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.oberasoftware.robo.api.MotionConverter;
import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.api.motion.Step;
import com.oberasoftware.robo.core.motion.MotionImpl;
import com.oberasoftware.robo.core.motion.StepBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.oberasoftware.robo.core.ConverterUtil.toSafeInt;
import static java.lang.String.valueOf;
import static java.util.Collections.emptyList;

/**
 * @author Renze de Vries
 */
@Component
public class RoboPlusMotionConverter implements MotionConverter {
    private static final Logger LOG = LoggerFactory.getLogger(RoboPlusMotionConverter.class);

    private static final String NAME = "name";
    private static final String STEP = "step";
    private static final String PAGE_END = "page_end";
    private static final String PAGE_BEGIN = "page_begin";

    @Override
    public List<Motion> loadMotions(String motionFile) {
        try {
            URL resource = this.getClass().getResource(motionFile);
            if(resource != null) {
                LOG.debug("Opening resource: {}", resource.toURI());
                Path path = Paths.get(resource.toURI());

                BufferedReader reader = Files.newBufferedReader(path);
                LineNumberReader lineReader = new LineNumberReader(reader);

                return loadMotions(lineReader);
            } else {
                LOG.error("Could not find motion file: {}", motionFile);
            }
        } catch (URISyntaxException | IOException e) {
            LOG.error("Could not read file", e);
        }

        return emptyList();
    }

    private List<Motion> loadMotions(LineNumberReader reader) throws IOException {
        List<Motion> motions = new ArrayList<>();
        while(advanceToNextPage(reader)) {
            Multimap<String, String> attributes = loadAttributes(reader);

            String motionName = Iterables.getFirst(attributes.get(NAME), null);
            if(!StringUtils.isEmpty(motionName)) {
                LOG.debug("Found a motion: {}", motionName);

                List<Step> steps = loadSteps(attributes.get(STEP));
                motions.add(new MotionImpl(motionName, 0, steps));
            }
        }

        return motions;
    }

    private List<Step> loadSteps(Collection<String> steps) {
        return steps.stream().map(this::loadStep).collect(Collectors.toList());
    }

    private Step loadStep(String step) {

        String[] stepData = step.split(" ");
        double time = Double.parseDouble(stepData[stepData.length - 1]);
        double msTime = time * 1000;

        StepBuilder stepBuilder = StepBuilder.create((long)msTime);
        for(int i=1; i<stepData.length; i++) {
            int value = toSafeInt(stepData[i]);
            if(value == 0) {
                break;
            }
            stepBuilder.servo(valueOf(i), value, 50);
        }

        return stepBuilder.build();
    }

    private Multimap<String, String> loadAttributes(LineNumberReader reader) throws IOException {
        Multimap<String, String> keyPairs = LinkedListMultimap.create();

        String line;
        while((line = reader.readLine()) != null && !line.equals(PAGE_END)) {
            String[] keyPair = line.split("=");
            if(keyPair.length == 2) {

                keyPairs.put(keyPair[0], keyPair[1]);
            }
        }

        return keyPairs;
    }

    private boolean advanceToNextPage(LineNumberReader lineNumberReader) throws IOException {
        String line;
        while ((line = lineNumberReader.readLine()) != null) {
            if(line.equals(PAGE_BEGIN)) {
                return true;
            }
        }

        LOG.debug("No more pages found");
        return false;
    }
}
