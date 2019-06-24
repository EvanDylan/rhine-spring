package org.rhine.framework.web;

import org.rhine.framework.core.MethodParameter;

import java.lang.reflect.Method;

/**
 * @author qs.zhou
 * @date 2019/06/24
 */
public class HandlerMethod {

    private Method method;

    private Object bean;

    private Class<?> beanType;

    private MethodParameter[] parameters;

    public HandlerMethod(Method method, Object bean) {
        this.method = method;
        this.bean = bean;
        this.beanType = bean.getClass();
        this.parameters = initParameters();
    }

    private MethodParameter[] initParameters() {

        return null;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public void setBeanType(Class<?> beanType) {
        this.beanType = beanType;
    }

    public MethodParameter[] getParameters() {
        return parameters;
    }

    public void setParameters(MethodParameter[] parameters) {
        this.parameters = parameters;
    }
}
