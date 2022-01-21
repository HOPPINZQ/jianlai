package com.hoppinzq.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.aop.annotation.ApiMapping;
import com.hoppinzq.service.aop.annotation.ApiServiceMapping;
import com.hoppinzq.service.aop.annotation.ServiceRegister;
import com.hoppinzq.service.bean.User;
import com.hoppinzq.service.dao.UserDao;
import com.hoppinzq.service.exception.UserException;
import com.hoppinzq.service.interfaceService.LoginService;
import com.hoppinzq.service.mail.SimpleMailSender;
import com.hoppinzq.service.util.*;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
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
    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

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


    /**
     * 普通注册
     * @param user
     * @throws UserException
     */
    @Override
    @ApiMapping(value = "register", title = "注册用户", description = "注册用户",type = ApiMapping.Type.POST)
    public void register(User user) throws UserException {
        if(user.getId()==0L){
            user.setId(snowflakeIdWorker.getSequenceId());
        }
        user.MD5encode();
        if(redisUtils.get(user2RedisPrefix+user.getUsername())!=null){
            throw new UserException("该用户名已存在！但是尚未激活，可以等待该用户过期或者重新注册！");
        }
        if(userDao.isUser(user.getUsername(),null,null)>0){
            throw new UserException("该用户名已存在！");
        }
        userDao.insertOrUpdateUser(user);
    }

    /**
     * 更新用户状态
     * @param user
     * @throws UserException
     */
    @ApiMapping(value = "activeUser", title = "激活用户", description = "激活用户",type = ApiMapping.Type.GET)
    public void activeUser(User user) throws UserException {
        userDao.insertOrUpdateUser(user);
    }


    /**
     * 手机注册
     * @param user
     * @throws UserException
     */
    @Override
    @ApiMapping(value = "registerBySms", title = "通过手机注册用户", description = "通过手机注册用户",type = ApiMapping.Type.POST)
    public void registerBySms(User user) throws UserException {
        String userName=user.getUsername();
        String phone=user.getPhone();
        if(!StringUtil.isNotEmpty(phone)){
            throw new UserException("没有手机号！");
        }
        if(!Tools.checkMobileNumber(user.getPhone())){
            throw new UserException("不正确的手机号！");
        }
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
            if(user.getId()==0L){
                user.setId(snowflakeIdWorker.getSequenceId());
            }
            user.MD5encode();
            //没有验证码，第一次注册
            if(redisUtils.get(user2RedisPrefix+userName)!=null){
                throw new UserException("该用户正在激活中！");
            }
            if(userDao.isUser(userName,null,null)>0){
                throw new UserException("该用户名已存在！");
            }
            if(userDao.isUser(null,null,phone)>0){
                throw new UserException("该手机号已经注册过账号！");
            }
            int codeMobile=Tools.getRandomNum();
            user.setCode(codeMobile);
            redisUtils.set(user2RedisPrefix+userName,JSONObject.toJSON(user),userRegisterTimeout+10);//多存10s
            //发送手机号
            try {
                Credential cred = new Credential("AKID61g5D4ex0oiSw95kAI9j25qKXUAZhZAU", "5tRiiRt4yby4tRiiRt51g4B2cvxiCQs3");
                HttpProfile httpProfile = new HttpProfile();
                httpProfile.setReqMethod("POST");
                httpProfile.setConnTimeout(60);
                httpProfile.setEndpoint("sms.tencentcloudapi.com");
                ClientProfile clientProfile = new ClientProfile();
                clientProfile.setSignMethod("HmacSHA256");
                clientProfile.setHttpProfile(httpProfile);
                SmsClient client = new SmsClient(cred, "ap-guangzhou",clientProfile);
                SendSmsRequest req = new SendSmsRequest();
                String sdkAppId = "1400622274";
                req.setSmsSdkAppId(sdkAppId);
                String signName = "hoppinzq个人网站";
                req.setSignName(signName);
                String senderid = "";
                req.setSenderId(senderid);
                String sessionContext = "session";
                req.setSessionContext(sessionContext);
                String extendCode = "";
                req.setExtendCode(extendCode);
                String templateId = "1277765";
                req.setTemplateId(templateId);
                String[] phoneNumberSet = {"+86"+phone};
                req.setPhoneNumberSet(phoneNumberSet);
                String[] templateParamSet = {String.valueOf(codeMobile),"5"};//5分钟
                req.setTemplateParamSet(templateParamSet);
                SendSmsResponse res = client.SendSms(req);
                //{"SendStatusSet":[{"SerialNo":"2645:206228275816424242714718217","PhoneNumber":"+8615028582175","Fee":1,"SessionContext":"session","Code":"Ok","Message":"send success","IsoCode":"CN"}],"RequestId":"9d775339-e0ac-48df-be61-936db15abcb1"}
                System.err.println(SendSmsResponse.toJsonString(res));
                //9d775339-e0ac-48df-be61-936db15abcb1
                System.err.println(res.getRequestId());
            } catch (TencentCloudSDKException e) {
                throw new UserException("发送短信失败:"+e);
            }
        }
    }

    /**
     * 邮箱注册
     * @param user
     * @throws UserException
     */
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
            if(user.getId()==0L){
                user.setId(new SnowflakeIdWorker().getSequenceId());
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
            //发送成功
        } catch (Exception e) {
            throw new RuntimeException("发送邮箱失败！");
            //发送失败
        }
    }


    /**
     * 跨域登录，服务与认证服务不是同源，需要认证服务返回具有一次性使用的ucdoe，用以获取用户信息
     * redis同源，可以实现不同源
     * 服务必须实现该方法返回的ucode解析工作。
     * @param user
     * @return
     */
    @Override
    @ApiMapping(value = "login",type = ApiMapping.Type.POST)
    public JSONObject login(User user) {
        String mobile=user.getPhone();
        if(!StringUtil.isNotEmpty(mobile)){
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
            String token = UUIDUtil.getUUID();
            JSONObject userJson=JSONObject.parseObject(JSONObject.toJSONString(reallyUser));
            if(user.getIsRemember()!=0){
                Cookie cookieUserName = new Cookie("ZQ_USER_NAME", user.getUsername());
                cookieUserName.setMaxAge(cookieUserTimeout);
                response.addCookie(cookieUserName);
            }else{
                Cookie cookieUserName = new Cookie("ZQ_USER_NAME", "");
                cookieUserName.setMaxAge(0);
                response.addCookie(cookieUserName);
            }
            userDao.userActiveChange(user.getUsername(),user.getPhone(),1);
            String uCode=UUIDUtil.getUUID();
            userJson.put("token",token);
            userJson.put("ucode",uCode);
            redisUtils.set(cookiePrefix+uCode,userJson);
            return userJson;
        }else{
            if(!Tools.checkMobileNumber(mobile)){
                throw new RuntimeException("手机号不正确!");
            }
            List<User> userList= userDao.queryUser(user);
            if(userList.size()==0){
                throw new RuntimeException("用户不存在!");
            }

            //手机验证
            return null;
        }
    }

    /**
     * 不跨域登录，需要认证服务与服务同属于一个IP
     * redis同源，可以实现不同源
     * @param user
     */
    @Override
    @ApiMapping(value = "login_not_ky",type = ApiMapping.Type.POST)
    public void login_not_ky(User user) {
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
        //将现有用户踢下线，可以注释不踢下线以实现一个用户多个登录状态
        logout(token);
        token = UUIDUtil.getUUID();
        //生成token
        reallyUser.setPassword(null);
        JSONObject userJson=JSONObject.parseObject(JSONObject.toJSONString(reallyUser));
        if(user.getIsRemember()!=0){
            Cookie cookieUserName = new Cookie("ZQ_USER_NAME", user.getUsername());
            cookieUserName.setMaxAge(cookieUserTimeout);
            response.addCookie(cookieUserName);
        }else{
            Cookie cookieUserName = new Cookie("ZQ_USER_NAME", "");
            cookieUserName.setMaxAge(0);
            response.addCookie(cookieUserName);
        }
        userDao.userActiveChange(user.getUsername(),user.getPhone(),1);
        //把用户信息写入redis,设置其时间
        redisUtils.set(user2RedisPrefix+token,userJson,redisUserTimeout);//暂时一分钟;
        //设置cookie时间，不设置的话cookie的有效期是关闭浏览器就失效。
        //CookieUtils.setCookie(request, response, "ZQ_TOKEN", token,1000*60);
        Cookie cookie = new Cookie("ZQ_TOKEN", token);
        cookie.setMaxAge(cookieUserTimeout);
        response.addCookie(cookie);
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
    public User getUserByCode(String ucode) {
        if(ucode==null){
            return null;
        }
        JSONObject json = (JSONObject) redisUtils.get(cookiePrefix+ucode);
        redisUtils.del(cookiePrefix+ucode);
        if (json==null) {
            return null;
        }
        return JSONObject.parseObject(JSONObject.toJSONString(json),User.class);
    }

    @Override
    public User getUserByToken(String token) {
        if(token==null){
            return null;
        }
        User user =  (User)redisUtils.get(token);
        if (user==null) {
            return null;
           //throw new ResultReturnException("此session已经过期，请重新登录",403);
        }
        return user;
    }

    @Override
    @ApiMapping(value = "logout")
    public void logout() {
        String token = CookieUtils.getCookie(request,"ZQ_TOKEN");
//        User user =  (User)redisUtils.get(token);
//        userDao.userActiveChange(user.getUsername(),user.getPhone(),0);
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
        if(token!=null){
            //更新用户状态为离线
//            User user =  (User)redisUtils.get(token);
//            userDao.userActiveChange(user.getUsername(),user.getPhone(),0);
            redisUtils.del(user2RedisPrefix+token);
            Cookie cookie = new Cookie("ZQ_TOKEN", "");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
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
