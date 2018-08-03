package com.ga.wyc.controller;

import java.lang.reflect.Method;

public class MappExecutor {
    private Object handler;
    private Method method;
    private Class<?>[] params;

    public static MappExecutor valueOf(Object handler, Method method, Class<?>[] params) {
        MappExecutor executor = new MappExecutor();
        executor.method = method;
        executor.params = params;
        executor.handler = handler;
        return executor;
    }

    public Object getHandler() {
        return handler;
    }

    public void setHandler(Object handler) {
        this.handler = handler;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?>[] getParams() {
        return params;
    }

    public void setParams(Class<?>[] params) {
        this.params = params;
    }
}
