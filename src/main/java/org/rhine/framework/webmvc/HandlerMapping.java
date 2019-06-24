package org.rhine.framework.webmvc;

import org.rhine.framework.web.HandlerMethod;

public class HandlerMapping {

    private String path;

    private HandlerMethod handlerMethod;

    public HandlerMapping(String path, HandlerMethod handlerMethod) {
        this.path = path;
        this.handlerMethod = handlerMethod;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HandlerMethod getHandlerMethod() {
        return handlerMethod;
    }

    public void setHandlerMethod(HandlerMethod handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

}
