package com.xu.blog.service;

import com.xu.blog.Dao.pojo.SysUser;
import com.xu.blog.vo.Result;
import com.xu.blog.vo.UserVo;

public interface SysUserService {
    UserVo findUserVoById(Long id);

    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);
    /*根据token查询用户信息*/
    Result findUserByToken(String token);
    /**
     * 根据账户名查询用户
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);

    void save(SysUser sysUser);
}
