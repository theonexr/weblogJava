package com.xu.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xu.blog.Dao.pojo.SysUser;
import com.xu.blog.service.LoginService;
import com.xu.blog.service.SysUserService;
import com.xu.blog.utils.JWTUtils;
import com.xu.blog.vo.ErrorCode;
import com.xu.blog.vo.Result;
import com.xu.blog.vo.params.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private SysUserService sysUserService;//需要用到用户表

    @Resource
    private RedisTemplate<String,String> redisTemplate;
    private static final String salt="lum!@#";

    @Override
    public Result login(LoginParam loginParam){
        /**
         * 1.检查参数是否合法
         * 2.根据用户名和密码区user表中查询是否存在
         * 3.如果不存在 登陆失败
         * 4.如果存在，使用jwt 生成 token 返回给前端
         * 5.token放入redis中，redis映射token和user信息，设置过期时间，先认证token是否合法，再认证redis中是否存在
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        if (StringUtils.isBlank(account)||StringUtils.isBlank(password)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        password = DigestUtils.md5Hex(password + salt); //密码加盐
        SysUser sysUser = sysUserService.findUser(account,password);
        if (sysUser == null) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS); //过期时间
        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
        if (StringUtils.isBlank(token)){
            return null;
        }
        Map<String, Object> checkToken = JWTUtils.checkToken(token);
        if (checkToken == null) {
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if(StringUtils.isBlank(userJson)){
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }

    @Override
    public Result register(LoginParam loginParam) {
        /**
         * 1.判断参数是否合法
         * 2.判断账户是否存在
         * 3.账户不存在注册用户
         * 4.生成token
         * 5.存入redis并返回
         * 6.注意  加上事务,一旦中间出现任何问题,注册用户需要回滚
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickname = loginParam.getNickname();
        if (StringUtils.isBlank(account)
                ||StringUtils.isBlank(nickname)
                ||StringUtils.isBlank(password)
        ){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }

        SysUser sysUser = sysUserService.findUserByAccount(account);
        if (sysUser != null) {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), "账号已经被用了欧");
        }
        sysUser=new SysUser();
        sysUser.setAccount(account);                                   //账户名
        sysUser.setNickname(nickname);                                  //昵称
        sysUser.setPassword(DigestUtils.md5Hex(password+salt));  //密码加盐md5
        sysUser.setCreateDate(System.currentTimeMillis());              //创建时间
        sysUser.setLastLogin(System.currentTimeMillis());               //最后登录时间
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");              //头像
        sysUser.setAdmin(1);                                             //管理员权限
        sysUser.setDeleted(0);                                             //假删除
        sysUser.setSalt("");                                                //盐
        sysUser.setStatus("");                                              //状态
        sysUser.setEmail("");                                               //邮箱
        this.sysUserService.save(sysUser);

//传token
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);

    }
}
