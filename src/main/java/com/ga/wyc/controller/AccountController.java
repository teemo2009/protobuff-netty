package com.ga.wyc.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountController {

    Logger logger= LoggerFactory.getLogger(AccountController.class);

    public void login(String username,String password){
       logger.info("username:{},password:{}",username,password);
    }



}
