package com.dxfx.netty.util;

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/12 22:26
 * @description
 */
public class MyResponse {

    /**
     * id为request的id
     */
    private Long id;

    private Object result;

    /**
     * 00000表示成功，其他表示失败！
     */
    private String code = "00000";

    /**
     * 失败的原因
     */
    private String msg;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
