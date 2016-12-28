/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

import com.github.nickvl.xspring.core.log.aop.annotation.*;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Method descriptor.
 */
final class InvocationDescriptor {
    private final Severity beforeSeverity;
    private final Severity afterSeverity;
    private final LogException exceptionAnnotation;

    private InvocationDescriptor(Severity beforeSeverity, Severity afterSeverity, LogException exceptionAnnotation) {
        this.beforeSeverity = beforeSeverity;
        this.afterSeverity = afterSeverity;
        this.exceptionAnnotation = exceptionAnnotation;
    }

    public Severity getBeforeSeverity() {
        return beforeSeverity;
    }

    public Severity getAfterSeverity() {
        return afterSeverity;
    }

    public LogException getExceptionAnnotation() {
        return exceptionAnnotation;
    }

    /**
     * Builder.
     */
    public static final class Builder {
        private final Method method;
        private Severity beforeSeverity;
        private Severity afterSeverity;
        private Severity defaultSeverity;
        private Severity classBeforeSeverity;
        private Severity classAfterSeverity;
        private Severity classDefaultSeverity;

        public Builder(Method method) {
            this.method = method;
        }

        public InvocationDescriptor build() {
            LogException logMethodExceptionAnnotation = parseAnnotations(findMethodAnnotations(method), true);
            LogException logClassExceptionAnnotation = parseAnnotations(findDeclaringClassAnnotations(method.getDeclaringClass()), false);

            if (Utils.hasNotNull(beforeSeverity, defaultSeverity, afterSeverity)) {
                return new InvocationDescriptor(
                        Utils.coalesce(beforeSeverity, defaultSeverity),
                        Utils.coalesce(afterSeverity, defaultSeverity),
                        Utils.coalesce(logMethodExceptionAnnotation, logClassExceptionAnnotation));
            }

            return new InvocationDescriptor(
                    Utils.coalesce(classBeforeSeverity, classDefaultSeverity),
                    Utils.coalesce(classAfterSeverity, classDefaultSeverity),
                    Utils.coalesce(logMethodExceptionAnnotation, logClassExceptionAnnotation));

        }

        private LogException parseAnnotations(Annotation[] annotations, boolean fromMethod) {
            LogException logExceptionAnnotation = null;
            for (Annotation currAnnotation : annotations) {
                if (currAnnotation.annotationType().equals(LogException.class)) {
                    logExceptionAnnotation = (LogException) currAnnotation;
                } else if (currAnnotation.annotationType().equals(LogFatal.class)) {
                    LogFatal logAnnotation = (LogFatal) currAnnotation;
                    setSeverity(logAnnotation.value(), Severity.FATAL, fromMethod);
                } else if (currAnnotation.annotationType().equals(LogError.class)) {
                    LogError logAnnotation = (LogError) currAnnotation;
                    setSeverity(logAnnotation.value(), Severity.ERROR, fromMethod);
                } else if (currAnnotation.annotationType().equals(LogWarn.class)) {
                    LogWarn logAnnotation = (LogWarn) currAnnotation;
                    setSeverity(logAnnotation.value(), Severity.WARN, fromMethod);
                } else if (currAnnotation.annotationType().equals(LogInfo.class)) {
                    LogInfo logAnnotation = (LogInfo) currAnnotation;
                    setSeverity(logAnnotation.value(), Severity.INFO, fromMethod);
                } else if (currAnnotation.annotationType().equals(LogDebug.class)) {
                    LogDebug logAnnotation = (LogDebug) currAnnotation;
                    setSeverity(logAnnotation.value(), Severity.DEBUG, fromMethod);
                } else if (currAnnotation.annotationType().equals(LogTrace.class)) {
                    LogTrace logAnnotation = (LogTrace) currAnnotation;
                    setSeverity(logAnnotation.value(), Severity.TRACE, fromMethod);
                }
            }
            return logExceptionAnnotation;
        }

        private void setSeverity(LogPoint logPoint, Severity targetSeverity, boolean fromMethod) {
            if (fromMethod) {
                if (logPoint == LogPoint.IN) {
                    beforeSeverity = Utils.max(targetSeverity, beforeSeverity);
                } else if (logPoint == LogPoint.OUT) {
                    afterSeverity = Utils.max(targetSeverity, afterSeverity);
                } else if (logPoint == LogPoint.BOTH) {
                    defaultSeverity = Utils.max(targetSeverity, defaultSeverity);
                }
            } else {
                if (logPoint == LogPoint.IN) {
                    classBeforeSeverity = Utils.max(targetSeverity, classBeforeSeverity);
                } else if (logPoint == LogPoint.OUT) {
                    classAfterSeverity = Utils.max(targetSeverity, classAfterSeverity);
                } else if (logPoint == LogPoint.BOTH) {
                    classDefaultSeverity = Utils.max(targetSeverity, classDefaultSeverity);
                }
            }
        }

        private Annotation[] findMethodAnnotations(Method method) {
            Class<?> clazz = method.getDeclaringClass();
            Annotation[] annotations = method.getAnnotations();
            if (clazz.getName().startsWith("com.alibaba.dubbo.common.bytecode")) {
                Set<Method> methods = MethodUtils.getOverrideHierarchy(method, ClassUtils.Interfaces.INCLUDE);
                annotations = methods.stream().
                        map(Method::getDeclaredAnnotations).
                        flatMap(Arrays::stream).
                        distinct().
                        toArray(size -> new Annotation[size]);
            }
            return annotations;
        }

        private Annotation[] findDeclaringClassAnnotations(Class<?> clazz) {
            Annotation[] annotations = clazz.getAnnotations();
            if (clazz.getName().startsWith("com.alibaba.dubbo.common.bytecode")) {
                List<Class<?>> superClasses = ClassUtils.getAllSuperclasses(clazz);
                List<Class<?>> interfaces = ClassUtils.getAllInterfaces(clazz);

                annotations = Stream.concat(superClasses.stream(), interfaces.stream()).
                        map(Class::getAnnotations).
                        flatMap(Arrays::stream).
                        distinct().
                        toArray(size -> new Annotation[size]);
            }
            return annotations;

        }

    }
}
