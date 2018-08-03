package com.ga.wyc.controller;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MController {
    //value代表顶层路由
    String value() default "";
}
