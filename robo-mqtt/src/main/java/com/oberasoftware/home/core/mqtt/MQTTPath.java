package com.oberasoftware.home.core.mqtt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Renze de Vries
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MQTTPath {
    MessageGroup group() default MessageGroup.ALL;

    String controller() default "*";

    String device() default "*";

    String label() default "*";
}
