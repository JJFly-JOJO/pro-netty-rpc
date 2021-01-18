package com.dxfx;

import com.dxfx.netty.client.ClientRequest;
import com.dxfx.netty.client.TcpClient;
import com.dxfx.netty.util.MyResponse;
import com.dxfx.user.bean.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestTcp {
    @Test
    public void testGetResponse() {
        ClientRequest request = new ClientRequest();
        request.setContent("测试tcp长连接请求");
        MyResponse resp = TcpClient.send(request);
        System.out.println(resp.getResult());
    }

    @Test
    public void testSaveUser() {
        ClientRequest request = new ClientRequest();
        User u = new User();
        u.setId(1);
        u.setName("张三");
        request.setCommand("com.dxfx.user.controller.UserController.saveUser");
        request.setContent(u);
        MyResponse resp = TcpClient.send(request);
        System.out.println(resp.getResult());
    }


    @Test
    public void testSaveUsers() {
        ClientRequest request = new ClientRequest();
        List<User> users = new ArrayList<User>();
        User u = new User();
        u.setId(1);
        u.setName("张三");
        users.add(u);
        request.setCommand("com.dxfx.user.controller.UserController.saveUsers");
        request.setContent(users);
        MyResponse resp = TcpClient.send(request);
        System.out.println(resp.getResult());
    }
}
