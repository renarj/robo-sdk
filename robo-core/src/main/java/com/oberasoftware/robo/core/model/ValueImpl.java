package com.oberasoftware.robo.core.model;

/**
 * @author renarj
 */
public class ValueImpl implements com.oberasoftware.robo.api.model.Value {
    
    private Object value;
    private com.oberasoftware.robo.api.model.VALUE_TYPE type;

    public ValueImpl(com.oberasoftware.robo.api.model.VALUE_TYPE type, Object value) {
        this.type = type;
        this.value = value;
    }

    public ValueImpl() {
    }

    @Override
    public com.oberasoftware.robo.api.model.VALUE_TYPE getType() {
        return type;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }

    @Override
    public String asString() {
        return value.toString();
    }

    @Override
    public String toString() {
        return "ValueImpl{" +
                "value=" + value +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValueImpl value1 = (ValueImpl) o;

        if (!value.equals(value1.value)) return false;
        return type == value1.type;

    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
