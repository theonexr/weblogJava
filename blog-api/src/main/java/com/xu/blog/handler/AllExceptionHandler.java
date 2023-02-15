package com.xu.blog.handler;


import com.xu.blog.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
//对加了@ControllerAdvice的方法进行拦截处理 AOP实现
public class AllExceptionHandler {

    @ExceptionHandler(Exception.class)   //进行异常处理,处理Exception.class异常
    @ResponseBody                        //返回Json数据
    public Result doExceptionHandler(Exception e){
        e.printStackTrace();
        return Result.fail(-999,"系统异常");
    }
}

