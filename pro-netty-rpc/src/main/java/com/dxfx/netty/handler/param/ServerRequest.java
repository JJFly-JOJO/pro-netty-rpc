package com.dxfx.netty.handler.param;

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/18 17:01
 * @description
 */
public class ServerRequest {

    private Long id;
    /**
     * 这里content用object统一接收（客户端传来的request对象中得到content是调用方法的参数）
     * 解析时转换成string再转换成对应类型
     */
    private Object content;
    /**
     * 客户端请求method方法的string（代表某一个类的某一个方法）
     */
    private String command;


    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

}
