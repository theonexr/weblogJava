package com.xu.blog.utils;

import com.xu.blog.Dao.pojo.SysUser;

public class UserThreadLocal {
    //这句话的意思是声明为私有
    private UserThreadLocal(){
    }
    //实例化一个ThreadLocal的类，也就是启用
    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }

    public static SysUser get(){
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }

}
