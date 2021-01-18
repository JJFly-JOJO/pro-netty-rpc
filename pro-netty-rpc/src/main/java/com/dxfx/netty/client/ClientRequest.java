package com.dxfx.netty.client;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/12 21:44
 * @description
 */
public class ClientRequest {

    /**
     * 并发场景下 保证自增id的唯一
     * 注意：static对应的属性值是不参与序列化
     */
    private final static AtomicLong aid = new AtomicLong(1);
    private final long id;
    private Object content;
    private String command;

    public ClientRequest() {
        id = aid.incrementAndGet();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public long getId() {
        return id;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
