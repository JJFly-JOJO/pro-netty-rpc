package com.dxfx.netty.util;

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/18 21:17
 * @description -------------封装对response赋值的操作-------------
 */
public class ResponseUtil {

    public static MyResponse createSuccessResult() {
        return new MyResponse();
    }

    public static MyResponse createFailResult(String code, String msg) {
        MyResponse response = new MyResponse();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }

    public static MyResponse createSuccessResult(Object content) {
        MyResponse response = new MyResponse();
        response.setResult(content);
        return response;
    }

}
