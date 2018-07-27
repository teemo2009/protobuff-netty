package com.ga.wyc.controller;


import com.ga.wyc.domain.proto.DataBase;

import java.lang.reflect.Method;

public class MappScan {

    public static void main(String[] args) {
        try {
            DataBase.MString username=DataBase.MString.newBuilder().setValue("luqi").build();
            DataBase.MString password=DataBase.MString.newBuilder().setValue("123456").build();
           Class<?> mClass= Class.forName("com.ga.wyc.controller.AccountController");
           Method method=mClass.getMethod("login",username.getValue().getClass(),password.getValue().getClass());
           method.invoke(mClass.newInstance(),username.getValue(),password.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
