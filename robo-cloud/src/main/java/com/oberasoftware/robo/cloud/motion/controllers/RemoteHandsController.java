package com.oberasoftware.robo.cloud.motion.controllers;

import com.oberasoftware.robo.api.commands.BasicCommand;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.motion.controller.HandsController;
import org.springframework.stereotype.Component;

import static com.oberasoftware.robo.core.model.BasicCommandBuilder.create;

/**
 * @author Renze de Vries
 */
@Component
public class RemoteHandsController implements HandsController, RemoteController {

    private Robot robot;

    @Override
    public void openHands() {
        BasicCommand command = create(robot.getName())
                .item("motion")
                .label("hands")
                .property("position", "open")
                .build();

        robot.getRemoteDriver().publish(command);
    }

    @Override
    public void openHand(HAND_ID hand) {
        BasicCommand command = create(robot.getName())
                .item("motion")
                .label("hands")
                .property("position", "open")
                .property("hand", hand.name())
                .build();

        robot.getRemoteDriver().publish(command);

    }

    @Override
    public void closeHand(HAND_ID hand) {
        BasicCommand command = create(robot.getName())
                .item("motion")
                .label("hands")
                .property("position", "closed")
                .property("hand", hand.name())
                .build();

        robot.getRemoteDriver().publish(command);
    }

    @Override
    public void closeHands() {
        BasicCommand command = create(robot.getName())
                .item("motion")
                .label("hands")
                .property("position", "closed")
                .build();

        robot.getRemoteDriver().publish(command);
    }

    @Override
    public void activate(Robot robot) {
        this.robot = robot;
    }
}
