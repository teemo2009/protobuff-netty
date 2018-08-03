package com.ga.wyc.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MController(value = "/account")
public class AccountController {

    Logger logger= LoggerFactory.getLogger(AccountController.class);

    @MRequestMapping(value = "/login")
    public void login(@MParam(name = "username") String username,
                      @MParam(name = "password")  String password){
       logger.info("username:{},password:{}",username,password);
    }



}
