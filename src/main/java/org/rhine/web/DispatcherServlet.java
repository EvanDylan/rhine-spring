package org.rhine.web;

import javax.servlet.*;

/**
 * @author qs.zhou
 * @date 2019/06/20
 */
public class DispatcherServlet implements Servlet {

    private final String INIT_PARAMETER_PATH = "contextPath";

    @Override
    public void init(ServletConfig config) {
        String configFilePath = config.getInitParameter(INIT_PARAMETER_PATH);
        doInit(configFilePath);
    }

    private void doInit(String configFilePath) {

        // parse()
        // scan
        // init

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) {
        doDispatch(req, res);
    }

    private void doDispatch(ServletRequest req, ServletResponse res) {
        return;
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
