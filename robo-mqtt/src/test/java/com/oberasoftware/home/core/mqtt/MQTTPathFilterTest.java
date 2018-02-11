package com.oberasoftware.home.core.mqtt;

import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.base.event.HandlerEntry;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Renze de Vries
 */
public class MQTTPathFilterTest {
    @Test
    public void testNoPathDefined() throws NoSuchMethodException {
        HandlerEntry entry = mock(HandlerEntry.class);
        Method method = getMethod("receiveUnfiltered");
        when(entry.getEventMethod()).thenReturn(method);

        assertThat(new MQTTPathFilter().isFiltered(new MQTTMessageImpl("/test", "content"), entry), is(false));
    }

    @Test
    public void testSpecificControllerDefined() throws NoSuchMethodException {
        HandlerEntry entry = mock(HandlerEntry.class);
        Method method = getMethod("receiveSpecificController");
        when(entry.getEventMethod()).thenReturn(method);

        assertThat(new MQTTPathFilter().isFiltered(new MQTTMessageImpl("/commands/robot/sonar/distance", "content"), entry), is(true));
        assertThat(new MQTTPathFilter().isFiltered(new MQTTMessageImpl("/states/robot/sonar/distance", "content"), entry), is(false));

    }

    @Test
    public void testInvalidPath() throws NoSuchMethodException {
        HandlerEntry entry = mock(HandlerEntry.class);
        Method method = getMethod("receiveSpecificController");
        when(entry.getEventMethod()).thenReturn(method);

        assertThat(new MQTTPathFilter().isFiltered(new MQTTMessageImpl("/test", "content"), entry), is(true));
    }

    @Test
    public void testUnfilteredTypes() throws NoSuchMethodException {
        HandlerEntry entry = mock(HandlerEntry.class);
        Method method = getMethod("receiveSpecificController");
        when(entry.getEventMethod()).thenReturn(method);

        assertThat(new MQTTPathFilter().isFiltered("OtherObject", entry), is(false));

    }

    private Method getMethod(String methodName) throws NoSuchMethodException {
        return TestEventHandler.class.getMethod(methodName, MQTTMessage.class);
    }

    private class TestEventHandler {
        @EventSubscribe
        public void receiveUnfiltered(MQTTMessage message) {

        }

        @EventSubscribe
        @MQTTPath(group = MessageGroup.STATES, controller = "robot")
        public  void receiveSpecificController(MQTTMessage message) {

        }
    }
}
