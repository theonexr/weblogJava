package com.xu.blog.handler;

import com.alibaba.fastjson.JSON;
import com.xu.blog.Dao.pojo.SysUser;
import com.xu.blog.service.LoginService;
import com.xu.blog.utils.UserThreadLocal;
import com.xu.blog.vo.ErrorCode;
import com.xu.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        //在执行Controller方法(Handle)前进行执行
        // pre代表什么什么之前的
        /**
         * 1.需要判断,请求的接口路径是否为HandleMethod(controller方法)
         * 2.判断token是否为空,为空未登录
         * 3.不为空,登录验证 loginService  checkToken
         * 4.如果认证成功放行
         */
        if(!(handler instanceof HandlerMethod)){
            //说简单的就是Handler是controller里面的某一个方法
            //handle 可能是  RequestResourceHandle(访问资源handle)  springboot程序访问静态资源  默认去classpath下的static目录查询
            return true;
        }
//        得去拿Token，为什么这样呢，因为前端传东西过来的时候是，我们用@RequestHeader("Authorization") 传过来的
        String token = request.getHeader("Authorization");
        //日志问题,需要导入lombok下的@slf4
        log.info("=============request start=================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}",token);
        log.info("=============request end===================");
        /*token为空,拦截*/
        if(StringUtils.isBlank(token)){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(),ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf8");
            response.getWriter().print(JSON.toJSONString(result)); //返回json信息(fastjson进行转化)
            return false;
        }
        /*用户为空,拦截*/
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            //以下是错误返回的信息
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            //要告诉浏览器我们要返回的是这种类型
            response.setContentType("application/json;charset=utf8");
            //返回的东西是result类型，要转换为JSON类型才行
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //登录验证成功,放行
//        在controller直接获取用户信息
        UserThreadLocal.put(sysUser);
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //如果不删除ThreadLocal中的信息,会有内训泄露的风险
        UserThreadLocal.remove();
    }
}
