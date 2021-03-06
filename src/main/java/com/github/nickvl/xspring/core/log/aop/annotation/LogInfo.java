/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop.annotation;

import java.lang.annotation.*;

/**
 * Annotation indicating that a method (or all methods on a class) should be logged.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Logging
public @interface LogInfo {

    /**
     * A part of the method to be logged. {@link LogPoint#BOTH} by default.
     */
    LogPoint value() default LogPoint.BOTH;

}