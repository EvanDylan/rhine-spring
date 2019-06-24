package org.rhine.framework.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class MethodParameter {

    private Parameter parameter;

    private Class parameterType;

    private final int parameterIndex;

    private Annotation annotation;

    public MethodParameter(Parameter parameter, Class parameterType, int parameterIndex) {
        this.parameter = parameter;
        this.parameterType = parameterType;
        this.parameterIndex = parameterIndex;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public Class getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class parameterType) {
        this.parameterType = parameterType;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }
}
