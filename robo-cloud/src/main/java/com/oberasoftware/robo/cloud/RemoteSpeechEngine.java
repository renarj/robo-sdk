package com.oberasoftware.robo.cloud;

import com.oberasoftware.robo.api.commands.BasicCommand;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.SpeechEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.oberasoftware.robo.core.model.BasicCommandBuilder.create;

@Component
@Scope("prototype")
public class RemoteSpeechEngine implements SpeechEngine {
    private static final Logger LOG = LoggerFactory.getLogger(RemoteSpeechEngine.class);

    private Robot robot;

    @Override
    public void say(String text, String language) {
        BasicCommand command = create(robot.getName())
                .item("tts")
                .label("say")
                .property("text", text)
                .property("language", language)
                .build();

        LOG.info("Doing a remote TTS operation: {}", command);
        robot.getRemoteDriver().publish(command);
    }

    @Override
    public void activate(Robot robot, Map<String, String> properties) {
        LOG.info("Activating remote speech engine for robot: {}", robot.getName());
        this.robot = robot;
    }

    @Override
    public void shutdown() {

    }
}
