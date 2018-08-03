package com.ga.wyc.controller;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MRequestMapping {
    //路由
    String value() default "";
}
