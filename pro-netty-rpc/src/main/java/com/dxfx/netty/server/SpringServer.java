package com.dxfx.netty.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/18 19:57
 * @description ---------Spring整合netty---------
 */
@Configuration
@ComponentScan("com.dxfx")
public class SpringServer {

    public static void main(String[] args) throws InterruptedException{
        ApplicationContext context=new AnnotationConfigApplicationContext(SpringServer.class);
    }

}
