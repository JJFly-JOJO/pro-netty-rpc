package com.dxfx.user.controller;

import com.dxfx.netty.util.MyResponse;
import com.dxfx.netty.util.ResponseUtil;
import com.dxfx.user.bean.User;
import com.dxfx.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/18 21:23
 * @description
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 客户端远程调用对应的controller方法 然后controller调用service层执行业务逻辑 将执行后的结果放入response中
     *
     * @param user
     * @return
     */
    public MyResponse saveUser(User user) {
        userService.save(user);
        return ResponseUtil.createSuccessResult(user);
    }

    public MyResponse saveUsers(List<User> users) {
        userService.saveList(users);
        return ResponseUtil.createSuccessResult(users);
    }

}
