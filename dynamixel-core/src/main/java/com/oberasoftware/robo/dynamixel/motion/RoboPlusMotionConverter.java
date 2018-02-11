package com.oberasoftware.robo.dynamixel.motion;

import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.oberasoftware.robo.api.motion.KeyFrame;
import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.api.motion.MotionConverter;
import com.oberasoftware.robo.core.ConverterUtil;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.oberasoftware.robo.core.ConverterUtil.toSafeInt;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
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
    public static final int SERVO_ENABLED = 1;

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
        String enabledServos = advanceToLine(reader, "enable");
        String[] servoStringIndexes = enabledServos != null ? enabledServos.split(" ") : new String[] {};
        List<Integer> servoIndexes = asList(servoStringIndexes).stream()
                .map(ConverterUtil::toSafeInt).collect(Collectors.toList());

        int counter = 1;
        while(advanceToNextPage(reader)) {
            Multimap<String, String> attributes = loadAttributes(reader);

            String motionName = Iterables.getFirst(attributes.get(NAME), null);
            motionName = StringUtils.isEmpty(motionName) ? Integer.toString(counter) : motionName;

            String playParams = Iterables.getFirst(attributes.get("play_param"), null);
            String[] params = playParams != null ? playParams.split(" ") : new String[0];
            String nextMotion = params.length > 1 ? params[0] : null;
            String exitMotion = params.length > 1 ? params[1] : null;
            double speedRate = Double.parseDouble(params.length > 1 ? params[3] : "1");

            LOG.debug("Found a motion[{}]: {} linkedMotion: {}", counter, motionName, nextMotion);

            List<KeyFrame> keyFrames = loadSteps(attributes.get(STEP), servoIndexes, speedRate);
            motions.add(new MotionImpl(Integer.toString(counter), motionName, nextMotion, exitMotion, keyFrames));
            counter++;
        }

        return motions;
    }

    private List<KeyFrame> loadSteps(Collection<String> steps, List<Integer> servoIndexes, double speedRate) {
        final AtomicInteger stepCounter = new AtomicInteger(0);

        return steps.stream().map(s -> loadStep(stepCounter.incrementAndGet(), s, servoIndexes, speedRate))
                .collect(Collectors.toList());
    }

    private KeyFrame loadStep(int frameId, String step, List<Integer> servoIndexes, double speedRate) {
        String[] stepData = step.split(" ");
        double time = Double.parseDouble(stepData[stepData.length - 1]);
        double msTime = (time * 1000) / speedRate;

        StepBuilder stepBuilder = StepBuilder.create(Integer.toString(frameId), (long)msTime);
        for(int i=0; i<(servoIndexes.size()); i++) {
            if(servoIndexes.get(i) == SERVO_ENABLED) {
                int value = toSafeInt(stepData[i]);
                stepBuilder.servo(valueOf(i), value, 50);
            } else {
                LOG.debug("Servo: {} is disabled", i);
            }
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
        return advanceToLine(lineNumberReader, PAGE_BEGIN) != null;
    }

    private String advanceToLine(LineNumberReader lineNumberReader, String lineBegin) throws IOException {
        String line;
        while ((line = lineNumberReader.readLine()) != null) {
            if(line.startsWith(lineBegin)) {
                return line;
            }
        }

        return null;
    }
}
