package com.dxfx.netty.medium;

import com.alibaba.fastjson.JSONObject;
import com.dxfx.netty.handler.param.ServerRequest;
import com.dxfx.netty.util.MyResponse;
import com.dxfx.netty.util.ResponseUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/18 20:46
 * @description --------------中介者模式---------
 * 不论是客户端还是服务端的Handler，我们都不应将业务逻辑写在其中，而是将服务端或客户端收到的request集中交给
 * Medium中介者，由中介者分发给Controller
 */
public class Mediu {

    public static Map<String, BeanMethod> beanMap;
    /**
     * 这里采用延迟加载（懒汉式）且线程安全的单例模式
     */
    private volatile static Mediu m = null;

    static {
        beanMap = new HashMap<>();
    }

    /**
     * 双层检测的线程安全模式
     * 这里区别在newInstance方法前加synchronized的方法---锁粒度太大 每次调用该方法都要加锁
     *
     * @return
     */
    public static Mediu newInstance() {
        if (m == null) {
            synchronized (Mediu.class) {
                if (m == null) {
                    m = new Mediu();
                }
            }
        }
        return m;
    }

    /**
     * 根据request 调用对应的controller,处理之后返回response
     *
     * @param request
     * @return
     */
    public MyResponse process(ServerRequest request) {
        MyResponse response = null;
        try {
            String command = request.getCommand();
            BeanMethod bm = beanMap.get(command);
            if (bm == null) {
                response = ResponseUtil.createFailResult("1111", "value is null...");
                response.setId(request.getId());
                return response;
            }
            Object bean = bm.getBean();
            Method method = bm.getMethod();
            Class<?> paramType = method.getParameterTypes()[0];
            Object content = request.getContent();
            Object args = JSONObject.parseObject(JSONObject.toJSONString(content), paramType);
            //反射调用controller controller又会调用其中维护的service对象
            response = (MyResponse) method.invoke(bean, args);
            response.setId(request.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
