package org.rhine.web;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.rhine.annotation.*;
import org.rhine.core.Scanner;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qs.zhou
 * @date 2019/06/20
 */
public class DispatcherServlet implements Servlet {

    private static final String INIT_PARAMETER_PATH = "contextPath";

    private static final String CONTEXT_NAMESPACE_URL = "http://www.springframework.org/schema/context";

    private static final Map<String, Class> CLASSNAME_CLASS = new ConcurrentHashMap<>();

    private static final Map<String, Method> PATH_METHOD = new ConcurrentHashMap<>();

    private static final Map<String, Object> BEAN_CONTAINER = new ConcurrentHashMap<>();

    @Override
    public void init(ServletConfig config) {
        String configFilePath = config.getInitParameter(INIT_PARAMETER_PATH);
        doInit(configFilePath);
    }

    private void doInit(String configFilePath) {
        resolve(configFilePath);
        initializationBean();
        initializationWire();
        initHandleMapping();
    }

    private void resolve(String configFilePath) {
        SAXReader reader = new SAXReader();
        Document document;
        try {
            document = reader.read(Objects.requireNonNull(
                    Thread.currentThread().getContextClassLoader().getResource(configFilePath)));
        } catch (DocumentException e) {
            throw new RuntimeException(e.getMessage());
        }
        Element root = document.getRootElement();
        List<Element> childs = root.elements();
        for (Element element : childs) {
            if (CONTEXT_NAMESPACE_URL.equalsIgnoreCase(element.getNamespaceURI())) {
                String value = element.attributeValue("base-package");
                Scanner scanner = new Scanner();
                scanner.doScan(value);
                break;
            }
        }
    }

    private void initializationBean() {
        String prefix = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        try {
            for (File file : Scanner.FILES) {
                String classPath = file.toURI().getPath().replace(prefix, "")
                        .replace("/", ".").replace(".class", "");
                Class clazz = Class.forName(classPath);
                CLASSNAME_CLASS.put(classPath, clazz);
                if (clazz.isAnnotation()) {
                    continue;
                }
                if (withAnyAnnotation(clazz, Component.class, Controller.class)) {
                    BEAN_CONTAINER.put(clazz.getName(), clazz.newInstance());
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void initializationWire() {
        for (Map.Entry<String, Object> entry : BEAN_CONTAINER.entrySet()) {
            Class clazz = CLASSNAME_CLASS.get(entry.getKey());
            Object targetObject = entry.getValue();
            for (Field field : clazz.getDeclaredFields()) {
                if (withAnnotation(Autowried.class, field)) {
                    field.setAccessible(true);
                    try {
                        field.set(targetObject, BEAN_CONTAINER.get(field.getType().getName()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void initHandleMapping() {
        for (Map.Entry<String, Object> entry : BEAN_CONTAINER.entrySet()) {
            Class clazz = CLASSNAME_CLASS.get(entry.getKey());
            if (!withAnnotation(Controller.class, clazz)) {
                continue;
            }
            Controller controller = (Controller) clazz.getAnnotation(Controller.class);
            String rootPath = controller.value();
            for (Method method : clazz.getDeclaredMethods()) {
                if (withAnnotation(RequestMapping.class, method)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    PATH_METHOD.put(rootPath + requestMapping.value(), method);
                }
            }
        }
    }

    private boolean withAnnotation(Class annotation, Parameter parameter) {
        return parameter.getAnnotation(annotation) != null;
    }

    private boolean withAnnotation(Class annotation, Method method) {
        return method.getAnnotation(annotation) != null;
    }

    private boolean withAnnotation(Class annotation, Field field) {
        return field.getAnnotation(annotation) != null;
    }

    private boolean withAnnotation(Class annotation, Class clazz) {
        return clazz.getAnnotation(annotation) != null;
    }

    private boolean withAnyAnnotation(Class clazz, Class... annotations) {
        for (Class annotation : annotations) {
            if (withAnnotation(annotation, clazz)) {
                return true;
            }
        }
        return false;
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
        HttpServletRequest httpServletRequest = (HttpServletRequest) req;
        String path = httpServletRequest.getRequestURI();
        Method method = PATH_METHOD.get(path);
        Object o = BEAN_CONTAINER.get(method.getDeclaringClass().getName());

        try {
            String parameterName = null;
            for (Parameter parameter : method.getParameters()) {
                if (!withAnnotation(RequestParam.class, parameter)) {
                    continue;
                }
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                parameterName = requestParam.value();
            }
            Object returnValue = method.invoke(o, req.getParameter(parameterName));
            res.getOutputStream().write(returnValue.toString().getBytes());
            res.getOutputStream().flush();
        } catch (IllegalAccessException | InvocationTargetException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
