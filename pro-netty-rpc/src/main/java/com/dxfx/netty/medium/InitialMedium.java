package com.dxfx.netty.medium;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/18 20:42
 * @description ---------将controller注册到中介者中 这里使用Spring的BeanPostProcessor后置处理器-----
 */
@Component
public class InitialMedium implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String s) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String s) throws BeansException {
        //注册Controller
        if (bean.getClass().isAnnotationPresent(Controller.class)) {
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method m : methods) {
                String key = bean.getClass().getName() + "." + m.getName();
                Map<String, BeanMethod> beanMap = Mediu.beanMap;
                BeanMethod beanMethod = new BeanMethod();
                //封装controller与method
                beanMethod.setBean(bean);
                beanMethod.setMethod(m);
                beanMap.put(key, beanMethod);
            }
        }
        return bean;
    }
}
