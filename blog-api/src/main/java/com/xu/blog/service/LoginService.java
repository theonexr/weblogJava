package com.xu.blog.service;

import com.xu.blog.Dao.pojo.SysUser;
import com.xu.blog.vo.Result;
import com.xu.blog.vo.params.LoginParam;

public interface LoginService {
    Result login(LoginParam loginParam);

    SysUser checkToken(String token);

    Result logout(String token);

    Result register(LoginParam loginParam);
}
