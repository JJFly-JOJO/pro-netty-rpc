package com.dxfx.netty.medium;

import java.lang.reflect.Method;

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/18 20:49
 * @description -----------维护controller对象以及对应的要调用的method
 */
public class BeanMethod {

    private Object bean;
    private Method method;

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

}
