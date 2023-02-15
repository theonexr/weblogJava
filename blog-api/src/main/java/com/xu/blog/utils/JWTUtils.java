package com.xu.blog.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    private static final String jwtToken = "12345Lum!@#$%";			//密钥

    public static String createToken(Long userId){
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId",userId);
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,jwtToken)  //签发算法,密钥为jwtToken
                .setClaims(claims)//body数据,唯一,自行设置
                .setIssuedAt(new Date())  //设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60  *1000));  //一天都有效时间
        return jwtBuilder.compact();
    }

    public static Map<String,Object> checkToken(String token){
        try {
            Jwt parser = Jwts.parser().setSigningKey(jwtToken).parse(token);  //解析jwtToken
            return (Map<String, Object>) parser.getBody();
        }catch ( Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /*测试jwt*/
    public static void main(String[] args) {
        String token = JWTUtils.createToken(100L);
        System.out.println(token);
        Map<String, Object> map = JWTUtils.checkToken(token);
        System.out.println(map.get("userId"));
    }
}


