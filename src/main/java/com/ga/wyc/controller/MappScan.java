package com.ga.wyc.controller;


import com.ga.wyc.domain.entity.RpcRequest;
import com.ga.wyc.domain.proto.DataBase;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MappScan {

    public static void main(String[] args) {
        try {
            packageScan();
          /*  DataBase.MString username=DataBase.MString.newBuilder().setValue("luqi").build();
            DataBase.MString password=DataBase.MString.newBuilder().setValue("123456").build();
           Class<?> mClass= Class.forName("com.ga.wyc.controller.AccountController");
           Method method=mClass.getMethod("login",username.getValue().getClass(),password.getValue().getClass());
           method.invoke(mClass.newInstance(),username.getValue(),password.getValue());*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void packageScan() throws InvalidProtocolBufferException, InvocationTargetException, IllegalAccessException {
        final Map<String, MappExecutor> mappHandlers = new HashMap<String, MappExecutor>();

        Set<Class<?>> controllers = ClassScanner.listClassesWithAnnotation("com.ga.wyc.controller", MController.class);
        for (Class<?> controller : controllers) {
            try {
                Object handler = controller.newInstance();
                Method[] methods = controller.getDeclaredMethods();
                String rootMapp = controller.getAnnotation(MController.class).value();
                for (Method method : methods) {
                    MRequestMapping mapperAnnotation = method.getAnnotation(MRequestMapping.class);
                    if (mapperAnnotation != null) {
                        String mapp = mapperAnnotation.value();
                        String mapping = rootMapp + mapp;
                        MappExecutor mappExecutor = MappExecutor.valueOf(handler, method, method.getParameterTypes());
                        if (mappExecutor == null) {
                            throw new RuntimeException(String.format("mapping[%d]不存在", mapping));
                        }
                        mappHandlers.put(mapping, mappExecutor);
                    }
                }
            } catch (Exception e) {
                System.out.println("启动扫描错误");
            }
        }

        MappExecutor mapp = mappHandlers.get("/account/login");
        Class<?> methodParams[] = mapp.getParams();
        Annotation[][] an = mapp.getMethod().getParameterAnnotations();
        Object[] result = new Object[methodParams == null ? 0 : methodParams.length];
        DataBase.MString username = DataBase.MString.newBuilder().setValue("luqi").build();
        DataBase.MString password = DataBase.MString.newBuilder().setValue("123456").build();
        Map<String, Any> paramMap = new HashMap<>();
        paramMap.put("username", Any.pack(username));
        paramMap.put("password", Any.pack(password));
        for (Map.Entry<String, Any> param : paramMap.entrySet()) {
            // System.out.println(param.getKey());
        }
        RpcRequest.MRequest mRequest = RpcRequest.MRequest.newBuilder().putAllParams(paramMap).build();
        Set<Object> finalR = new HashSet<>();
        for (int i = 0; i < result.length; i++) {
            Class<?> param = methodParams[i];
            String className = param.getName();
            Annotation[] tmp = an[i];
            MParam tParam = (MParam) tmp[0];
            if (className.equals(String.class.getName())) {
                String value = paramMap.get(tParam.name()).unpack(DataBase.MString.class).getValue();
                finalR.add(value);
            }
        }
        mapp.getMethod().invoke(mapp.getHandler(),finalR.toArray());
    }
}
