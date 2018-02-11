package com.oberasoftware.home.core.mqtt;

/**
 * @author Renze de Vries
 */
public class ParsedPath {
    private final MessageGroup messageGroup;
    private final String controllerId;
    private final String deviceId;
    private final String label;

    public ParsedPath(MessageGroup messageGroup, String controllerId, String deviceId, String label) {
        this.messageGroup = messageGroup;
        this.controllerId = controllerId;
        this.deviceId = deviceId;
        this.label = label;
    }

    public MessageGroup getMessageGroup() {
        return messageGroup;
    }

    public String getControllerId() {
        return controllerId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getLabel() {
        return label;
    }


    @Override
    public String toString() {
        return "ParsedPath{" +
                "messageGroup=" + messageGroup +
                ", controllerId='" + controllerId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
