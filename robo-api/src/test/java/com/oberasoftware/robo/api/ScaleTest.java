package com.oberasoftware.robo.api;

import com.oberasoftware.robo.api.commands.Scale;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ScaleTest {
    @Test
    public void testConvertScales() {
        Scale webScale = new Scale(-100, 100);

        Scale dynamixelScale = new Scale(-400, 400);

        assertThat(webScale.convertToScale(-100, dynamixelScale), is(-400));
        assertThat(webScale.convertToScale(100, dynamixelScale), is(400));
        assertThat(webScale.convertToScale(0, dynamixelScale), is(0));
        assertThat(webScale.convertToScale(-50, dynamixelScale), is(-200));
        assertThat(webScale.convertToScale(50, dynamixelScale), is(200));


        assertThat(dynamixelScale.convertToScale(-400, webScale), is(-100));
        assertThat(dynamixelScale.convertToScale(400, webScale), is(100));
        assertThat(dynamixelScale.convertToScale(0, webScale), is(0));
        assertThat(dynamixelScale.convertToScale(-200, webScale), is(-50));
        assertThat(dynamixelScale.convertToScale(200, webScale), is(50));
    }

    @Test
    public void testPositionScale() {
        Scale webScale = new Scale(0, 2000);

        Scale dynamixelScale = new Scale(0, 4095);

        assertThat(webScale.convertToScale(0, dynamixelScale), is(0));
        assertThat(webScale.convertToScale(1000, dynamixelScale), is(2047));
        assertThat(webScale.convertToScale(2000, dynamixelScale), is(4094));
    }

    @Test
    public void testConvertRadialScale() {
        Scale dynamixelScale = new Scale(0, 4000);

        Scale radialScale = new Scale(-180, 180);

        assertThat(dynamixelScale.convertToScale(2000, radialScale), is(0));

        assertThat(dynamixelScale.convertToScale(4000, radialScale), is(180));
        assertThat(dynamixelScale.convertToScale(0, radialScale), is(-180));
        assertThat(dynamixelScale.convertToScale(1000, radialScale), is(-90));


        assertThat(radialScale.convertToScale(180, dynamixelScale), is(4000));
        assertThat(radialScale.convertToScale(0, dynamixelScale), is(2000));
        assertThat(radialScale.convertToScale(-180, dynamixelScale), is(0));
        assertThat(radialScale.convertToScale(-90, dynamixelScale), is(1000));

    }
}
