package com.hoppinzq.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.aop.annotation.ApiServiceMapping;
import com.hoppinzq.service.aop.annotation.ServiceRegister;
import com.hoppinzq.service.bean.User;
import com.hoppinzq.service.dao.UserDao;
import com.hoppinzq.service.exception.ResultReturnException;
import com.hoppinzq.service.interfaceService.LoginService;
import com.hoppinzq.service.util.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

@ServiceRegister
@ApiServiceMapping(title = "登录认证服务",description = "这是登录认证服务",roleType = ApiServiceMapping.RoleType.RIGHT)
public class LoginServiceImpl implements LoginService,Serializable {

    private static final long serialVersionUID = 2783377098145240357L;

    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    private static final String user2RedisPrefix ="USER:";

    @Override
    @ApiMapping(value = "test",roleType = ApiMapping.RoleType.LOGIN)
    public int test() {
        return 1;
    }

    @Override
    @ApiMapping(value = "register", title = "注册用户", description = "注册用户")
    public void register(User user) {
        if(user.getId()==null){
            user.setId(UUIDUtil.getUUID());
        }
        user.MD5encode();
        userDao.createUser(user);
    }

    @Override
    @ApiMapping(value = "login")
    public void login(User user) {
        String password=user.getPassword();
        user.setPassword(null);
        List<User> userList= userDao.queryUser(user);
        if(userList.size()==0){
            throw new RuntimeException("用户不存在!");
        }
        User reallyUser=userList.get(0);
        if(!EncryptUtil.MD5(password).equals(reallyUser.getPassword())){
            throw new RuntimeException("用户名密码错误!");
        }
        //先判断现有的token是否存在或者过期
        String token = CookieUtils.getCookieValue(request,"ZQ_TOKEN");
        //如果有，删掉
        if(token==null||redisUtils.get(user2RedisPrefix+token)==null){
            token = UUIDUtil.getUUID();
            redisUtils.del(user2RedisPrefix+token);
            Cookie cookie = new Cookie("ZQ_TOKEN", "");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        //生成token
        user.setPassword(null);
        JSONObject userJson=JSONObject.parseObject(JSONObject.toJSONString(user));
        //把用户信息写入redis,设置其时间
        redisUtils.set(user2RedisPrefix+token,userJson,60);//暂时一分钟;
        //设置cookie时间，不设置的话cookie的有效期是关闭浏览器就失效。
        //CookieUtils.setCookie(request, response, "ZQ_TOKEN", token,1000*60);
        Cookie cookie = new Cookie("ZQ_TOKEN", token);
        cookie.setMaxAge(60);
        response.addCookie(cookie);
    }

    @Override
    public User getUserByToken(String token) {
        JSONObject json = (JSONObject) redisUtils.get(user2RedisPrefix +token);
        if (json==null) {
            return null;
           //throw new ResultReturnException("此session已经过期，请重新登录",403);
        }
        //更新过期时间
        redisUtils.expire(user2RedisPrefix+token, 60);
        return JSONObject.parseObject(JSONObject.toJSONString(json),User.class);
    }

    @Override
    @ApiMapping(value = "logout")
    public void logout() {
        String token = CookieUtils.getCookie(request,"ZQ_TOKEN");
        redisUtils.del(user2RedisPrefix+token);
        Cookie cookie = new Cookie("ZQ_TOKEN", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
//        String token=CookieUtils.getCookieValue(request,"ZQ_TOKEN");
//        CookieUtils.setCookie(request, response, "ZQ_TOKEN", token,0);
    }
}
