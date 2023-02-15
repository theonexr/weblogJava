package com.xu.blog.controller;

import com.xu.blog.service.LoginService;
import com.xu.blog.vo.Result;
import com.xu.blog.vo.params.LoginParam;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/login")
public class LoginController {

//    @Autowired
//    private SysUserService sysUserService;
//    不建议,每个Service都有单独的业务

    @Resource
    private LoginService loginService;

    @PostMapping
    public Result login(@RequestBody LoginParam loginParam){
        //登陆验证用户 访问用户表
        return loginService.login(loginParam);

    }

}


