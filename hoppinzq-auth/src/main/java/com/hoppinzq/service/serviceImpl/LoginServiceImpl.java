package com.hoppinzq.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.aop.annotation.ApiServiceMapping;
import com.hoppinzq.service.aop.annotation.ServiceRegister;
import com.hoppinzq.service.bean.User;
import com.hoppinzq.service.dao.UserDao;
import com.hoppinzq.service.common.UserException;
import com.hoppinzq.service.interfaceService.LoginService;
import com.hoppinzq.service.mail.SimpleMailSender;
import com.hoppinzq.service.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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
    private static final String cookiePrefix ="UCOOKIE:";

    private static final int userRegisterTimeout=60*5;

    @Value("${zqAuth.redisUserTimeout:60*60*24*7}")
    private int redisUserTimeout;

    @Value("${zqAuth.cookieUserTimeout:60*60*24*7}")
    private int cookieUserTimeout;

    @Override
    @ApiMapping(value = "test",roleType = ApiMapping.RoleType.LOGIN)
    public int test() {
        return 1;
    }


    @Override
    @ApiMapping(value = "register", title = "注册用户", description = "注册用户",type = ApiMapping.Type.POST)
    public void register(User user) throws UserException {
        if(user.getId()==null){
            user.setId(UUIDUtil.getUUID());
        }
        user.MD5encode();
        if(redisUtils.get(user2RedisPrefix+user.getUsername())!=null){
            throw new UserException("该用户名已存在！但是尚未激活，可以等待该用户过期或者重新注册！");
        }
        if(userDao.isUser(user.getUsername(),null,null)>0){
            throw new UserException("该用户名已存在！");
        }
        userDao.createUser(user);
    }

    @ApiMapping(value = "activeUser", title = "激活用户", description = "激活用户",type = ApiMapping.Type.GET)
    public void activeUser(User user) throws UserException {
        userDao.createUser(user);
    }

    @Override
    @ApiMapping(value = "registerEmail", title = "通过邮箱注册用户", description = "通过邮箱注册用户",type = ApiMapping.Type.POST)
    public void registerByEmail(User user) throws UserException {
        String userName=user.getUsername();
        if(user.getCode()>0){
            if(redisUtils.get(user2RedisPrefix+userName)==null){
                throw new UserException("该用户已过期,请重新注册！");
            }
            JSONObject userJSON=(JSONObject)redisUtils.get(user2RedisPrefix+userName);
            if(Integer.parseInt(userJSON.get("code").toString())!=user.getCode()){
                throw new UserException("验证码不正确！");
            }else{
                User user1=JSONObject.parseObject(userJSON.toJSONString(),User.class);
                this.activeUser(user1);
                redisUtils.del(user2RedisPrefix+userName);
            }
        }else{
            if(user.getId()==null){
                user.setId(UUIDUtil.getUUID());
            }
            user.MD5encode();
            //没有验证码，第一次注册
            if(redisUtils.get(user2RedisPrefix+userName)!=null){
                throw new UserException("该用户名已存在！但是尚未激活，可以等待该用户过期或者重新注册！");
            }
            if(userDao.isUser(userName,null,null)>0){
                throw new UserException("该用户名已存在！");
            }
            if(!Tools.checkEmail(user.getEmail())){
                throw new UserException("不正确的邮箱！");
            }
            if(userDao.isUser(null,user.getEmail(),null)>0){
                throw new UserException("该邮箱已被使用！");
            }
            int codeEmail=Tools.getRandomNum();
            String html=emailPage(codeEmail);
            user.setCode(codeEmail);
            redisUtils.set(user2RedisPrefix+userName,JSONObject.toJSON(user),userRegisterTimeout+10);//多存10s
            //发送到用户注册邮箱一个页面
            try{
                sendEmail(user.getEmail(),"亲爱的"+userName+":感谢您注册本网站",html,"2");
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    /**
     *
     * @param toEMAIL 对方邮箱
     * @param TITLE 标题
     * @param CONTENT 内容
     * @param TYPE 类型
     * @return
     */
    public static void sendEmail(String toEMAIL,String TITLE,String CONTENT,String TYPE){
        TYPE="2";
        String strEMAIL = "smtp.163.com,fh,25,fh,anmiezata@163.com,fh,XXPGRJMTYQVMTVQF";
        String strEM[] = strEMAIL.split(",fh,");
        toEMAIL = toEMAIL.replaceAll("；", ";");
        toEMAIL = toEMAIL.replaceAll(" ", "");
        String[] arrTITLE = toEMAIL.split(";");
        try {
            for(int i=0;i<arrTITLE.length;i++){
                if(Tools.checkEmail(arrTITLE[i])){		//邮箱格式不对就跳过
                    SimpleMailSender.sendEmail(strEM[0], strEM[1], strEM[2], strEM[3], arrTITLE[i], TITLE, CONTENT, TYPE);//调用发送邮件函数
                }else{
                    continue;
                }
            }
            System.out.println("成功");
            //发送成功
        } catch (Exception e) {
            e.printStackTrace();
            //发送失败
        }
    }


    @Override
    public void registerByMobile(User user) {

    }

    @Override
    @ApiMapping(value = "login",type = ApiMapping.Type.POST)
    public JSONObject login(User user) {
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
//        //先判断现有的token是否存在或者过期
        String token = CookieUtils.getCookieValue(request,"ZQ_TOKEN");
//        //如果有，先删掉
//        if(token==null||redisUtils.get(user2RedisPrefix+token)==null){
//            redisUtils.del(user2RedisPrefix+token);
//            Cookie cookie = new Cookie("ZQ_TOKEN", "");
//            cookie.setMaxAge(0);
//            response.addCookie(cookie);
//        }
        token = UUIDUtil.getUUID();
//        //生成token
//        reallyUser.setPassword(null);
        JSONObject userJson=JSONObject.parseObject(JSONObject.toJSONString(reallyUser));
//        //把用户信息写入redis,设置其时间
//        redisUtils.set(user2RedisPrefix+token,userJson,redisUserTimeout);//暂时一分钟;
//        //设置cookie时间，不设置的话cookie的有效期是关闭浏览器就失效。
//        //CookieUtils.setCookie(request, response, "ZQ_TOKEN", token,1000*60);
//        Cookie cookie = new Cookie("ZQ_TOKEN", token);
//        cookie.setMaxAge(cookieUserTimeout);
//        response.addCookie(cookie);

        //生成仅能使用一次的ucode，用以跨域传递cookie内的token
        //同源的话，上面那些就够了，不同源的话，上面那些都没用
        String uCode=UUIDUtil.getUUID();
        userJson.put("token",token);
        userJson.put("ucode",uCode);
        redisUtils.set(cookiePrefix+uCode,userJson);
        return userJson;
    }

    @Override
    public User getUser() {
        String token = CookieUtils.getCookieValue(request,"ZQ_TOKEN");
        JSONObject json = (JSONObject) redisUtils.get(user2RedisPrefix +token);
        if (json==null) {
            return null;
            //throw new ResultReturnException("此session已经过期，请重新登录",403);
        }
        //更新过期时间
//        redisUtils.expire(user2RedisPrefix+token, redisUserTimeout);
//        Cookie cookie=CookieUtils.getNewCookie(request,"ZO_TOKEN");
//        cookie.setMaxAge(cookieUserTimeout);
        return JSONObject.parseObject(JSONObject.toJSONString(json),User.class);
    }

    @Override
    public User getUserByToken(String token) {
        if(token==null){
            return null;
        }
        JSONObject json = (JSONObject) redisUtils.get(token);
        if (json==null) {
            return null;
           //throw new ResultReturnException("此session已经过期，请重新登录",403);
        }
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
    }

    /**
     * 强制某人下线
     * @param token
     */
    @Override
    public void logout(String token) {
        redisUtils.del(user2RedisPrefix+token);
        Cookie cookie = new Cookie("ZQ_TOKEN", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    /**
     * 邮箱验证页面
     * @return
     */
    private static String emailPage(int code){
        String str="<!DOCTYPE html>\n" +
                "<html lang=\"zh\">\n" +
                "\t<head>\n" +
                "\t\t<meta charset=\"UTF-8\">\n" +
                "\t\t<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
                "\t\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "\t\t<title>hoppinzq</title>\n" +
                "\t</head>\n" +
                "\t<body style=\"background: #1d1f20;\">\n" +
                "\t\t<div style=\"width: 100vw;\n" +
                "\t\t\t\theight: 100vh;\n" +
                "\t\t\t\tdisplay: flex;\n" +
                "\t\t\t\tjustify-content: center;\n" +
                "\t\t\t\tflex-direction: column;\">\n" +
                "\t\t\t<div style=\"width: 100%;\n" +
                "\t\t\t\tdisplay: flex;\t\n" +
                "\t\t\t\tjustify-content: center;\">\n" +
                "\t\t\t\t<h1 style=\"position: relative;\n" +
                "\t\t\t\ttext-align: center;\n" +
                "\t\t\t\tfloat: left;\n" +
                "\t\t\t\tcolor: #5FCB71;\n" +
                "\t\t\t\twidth: 100%;\n" +
                "\t\t\t\tfont-family: 'Inconsolata', Consolas, monospace;\n" +
                "\t\t\t\tfont-size: 4em;\n" +
                "\t\t\t\tmargin: 100px;\">欢迎注册本网站，下面是激活码,有效期5分钟</h1>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t<div style=\"width: 100%;\n" +
                "\t\t\t\tdisplay: flex;\t\n" +
                "\t\t\t\tjustify-content: center;\">\n" +
                "\t\t\t\t<h1 style=\"background: #5FCB71;\n" +
                "        border: 4px solid #c4decb;\n" +
                "    color: #fff;\n" +
                "\t\t\t\tcursor: pointer;\n" +
                "\t\t\t\tdisplay: block;\n" +
                "\t\t\t\tfont-size: 16px;\n" +
                "\t\t\t\tfont-weight: 400;\n" +
                "\t\t\t\tline-height: 45px;\n" +
                "\t\t\t\tmargin-right: 2em;\n" +
                "\t\t\t\ttext-align: center;\n" +
                "\t\t\t\tmax-width: 160px;\n" +
                "\t\t\t\tposition: relative;\n" +
                "\t\t\t\ttext-decoration: none;\n" +
                "\t\t\t\ttext-transform: uppercase;\n" +
                "\t\t\t\tvertical-align: middle;\n" +
                "\t\t\t\twidth: 100%;\n" +
                "\t\t\t\ttext-align: center;\n" +
                "\t\t\t\tfloat: left;\"> \n" +
                "\t\t\t\t\t<svg style=\"height: 45px;\n" +
                "\t\t\t\tleft: 0;\n" +
                "\t\t\t\tposition: absolute;\n" +
                "\t\t\t\ttop: 0;\n" +
                "\t\t\t\twidth: 100%;\">\n" +
                "\t\t\t\t\t\t<rect x=\"0\" y=\"0\" fill=\"none\" transition=\"all 450ms linear 0s\" width=\"100%\" height=\"100%\" stroke=\"#5FCB71\" stroke-width=3 stroke-dasharray=\"422, 0\"></rect>\n" +
                "\t\t\t\t\t</svg>\n" +
                "\t\t\t\t\t"+code+" \n" +
                "\t\t\t\t</h1>\n" +
                "\t\t\t</div>\n" +
                "\t\t</div>\n" +
                "\t</body>\n" +
                "</html>\n";
        return str;
    }
}
