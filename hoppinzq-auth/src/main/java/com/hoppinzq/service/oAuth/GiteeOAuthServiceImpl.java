package com.hoppinzq.service.oAuth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.aop.annotation.ServiceRegister;
import com.hoppinzq.service.bean.User;
import com.hoppinzq.service.dao.UserDao;
import com.hoppinzq.service.exception.UserException;
import com.hoppinzq.service.constant.AuthConstant;
import com.hoppinzq.service.interfaceService.GiteeOAuthService;
import com.hoppinzq.service.property.GiteeProperty;
import com.hoppinzq.service.util.RedisUtils;
import com.hoppinzq.service.util.UUIDUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: zq
 */
@ServiceRegister
public class GiteeOAuthServiceImpl implements GiteeOAuthService, Serializable {

    private static final long serialVersionUID = 2783377098145240357L;

    @Autowired
    private HttpClientComm httpClientComm;
    @Autowired
    private GiteeProperty giteeProperty;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisUtils redisUtils;

    private static final String authorization_code="authorization_code";
    private static final String refresh_token="refresh_token";

    //https://gitee.com/oauth/authorize?client_id={client_id}&redirect_uri={redirect_uri}&response_type=code
    //https://gitee.com/oauth/authorize?client_id={client_id}&redirect_uri={redirect_uri}&response_type=code&scope=user_info

    /**
     * 获取access_token跟refresh_token
     * @param code
     * @param redirect_uri
     * @return
     * @throws UnsupportedEncodingException
     * @throws UserException
     */
    public String getAccessToken(String code,String redirect_uri) throws UnsupportedEncodingException, UserException {
        String postUrl=AuthConstant.GITEE_OAUTH_TOKEN_URL+"?grant_type="+authorization_code +
                "&code=" +code+
                "&client_id="+giteeProperty.getCilent_id() +
                "&redirect_uri="+redirect_uri +
                "&client_secret="+giteeProperty.getClient_secret();
        String giteeRes=httpClientComm.post(postUrl);
        //{
        //    "access_token": "ee65047cb17f0fdeb8e64ecdc712049c",
        //    "token_type": "bearer",
        //    "expires_in": 86400,
        //    "refresh_token": "09f7942a85bbda4cfc23fdf6bb899ec6cb5c2c740608be63819df19cd15bc3b0",
        //    "scope": "user_info",
        //    "created_at": 1642472291
        //}
        //{
        //    "error": "invalid_grant",
        //    "error_description": "授权方式无效，或者登录回调地址无效、过期或已被撤销"
        //}
        return giteeRes;
    }

    /**
     * 使用refresh_token更新access_token
     * @param refresh_token
     * @return
     * @throws UnsupportedEncodingException
     */
    public String refreshToken(String refresh_token) throws UnsupportedEncodingException {
        String giteeRes=httpClientComm.post(AuthConstant.GITEE_OAUTH_REFRESH_TOKEN_URL+"?grant_type="+refresh_token+
                "&refresh_token="+refresh_token);
        //{
        //    "access_token": "a142c73141ebb0b64727090e0888c7b9",
        //    "token_type": "bearer",
        //    "expires_in": 86400,
        //    "refresh_token": "12fb24f0f373b25722fd222fd19348a5a9f119be51ccafed2a1aa58e23912c60",
        //    "scope": "user_info",
        //    "created_at": 1642472716
        //}
        return giteeRes;

    }

    /**
     * 使用access_token获取当前登录人
     * @param access_token
     * @return
     */
    public String getGiteeUser(String access_token){
        String giteeRes=httpClientComm.get(AuthConstant.GITEE_OPENAPI_URL+"?access_token="+access_token+"#/getV5User");
        //{
        //    "id": 5294558,
        //    "login": "hoppin",
        //    "name": "hoppin",
        //    "avatar_url": "https://portrait.gitee.com/uploads/avatars/user/1764/5294558_hoppin_1614907809.png",
        //    "url": "https://gitee.com/api/v5/users/hoppin",
        //    "html_url": "https://gitee.com/hoppin",
        //    "remark": "",
        //    "followers_url": "https://gitee.com/api/v5/users/hoppin/followers",
        //    "following_url": "https://gitee.com/api/v5/users/hoppin/following_url{/other_user}",
        //    "gists_url": "https://gitee.com/api/v5/users/hoppin/gists{/gist_id}",
        //    "starred_url": "https://gitee.com/api/v5/users/hoppin/starred{/owner}{/repo}",
        //    "subscriptions_url": "https://gitee.com/api/v5/users/hoppin/subscriptions",
        //    "organizations_url": "https://gitee.com/api/v5/users/hoppin/orgs",
        //    "repos_url": "https://gitee.com/api/v5/users/hoppin/repos",
        //    "events_url": "https://gitee.com/api/v5/users/hoppin/events{/privacy}",
        //    "received_events_url": "https://gitee.com/api/v5/users/hoppin/received_events",
        //    "type": "User",
        //    "blog": "https://blog.csdn.net/qq_41544289",
        //    "weibo": null,
        //    "bio": "Talk is cheap.Show me the code.",
        //    "public_repos": 13,
        //    "public_gists": 0,
        //    "followers": 4,
        //    "following": 4,
        //    "stared": 23,
        //    "watched": 53,
        //    "created_at": "2019-09-09T23:14:04+08:00",
        //    "updated_at": "2022-01-19T09:41:53+08:00",
        //    "email": null
        //}
        return giteeRes;
    }


    /**
     * 创建用户，用户的ID必须是long类型，否则需要转一下
     * @param code
     * @param redirect_url
     * @param refresh_token
     * @throws Exception
     */
    public JSONObject createGiteeUser(String code,String redirect_url,String refresh_token) throws Exception{
        String accressTokenStr=null;
        if(refresh_token==null){
            accressTokenStr=getAccessToken(code,redirect_url);

        }else{
            //只用于刷新access_token
            accressTokenStr=refreshToken(refresh_token);
        }
        if(accressTokenStr==null){
            throw new UserException("码云认证服务失败");
        }
        JSONObject accressTokenJson= JSON.parseObject(accressTokenStr);
        if(accressTokenJson.get("error_description")!=null){
            throw new UserException(String.valueOf(accressTokenJson.get("error_description")));
        }
        String access_token=String.valueOf(accressTokenJson.get("access_token"));

        String giteeUserStr=getGiteeUser(access_token);
        if(accressTokenStr==null){
            throw new UserException("码云认证服务失败");
        }
        JSONObject giteeUserJson=JSON.parseObject(giteeUserStr);
        if(giteeUserJson.get("message")!=null){
            throw new UserException(String.valueOf(giteeUserJson.get("message")));
        }
        User user=new User();
        user.setId(Long.parseLong(String.valueOf(giteeUserJson.get("id"))));
        user.setUsername(String.valueOf(giteeUserJson.get("name")));
//        DateFormat giteeDateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
//        user.setCreate(giteeDateFormat.parse(String.valueOf(giteeUserJson.get("created_at"))));
//        user.setUpdate(giteeDateFormat.parse(String.valueOf(giteeUserJson.get("updated_at"))));
        user.setEmail(String.valueOf(giteeUserJson.get("email")));
        user.setLogin_type("gitee");
        user.setUserright(0);
        user.setState(1);
        user.setDescription(String.valueOf(giteeUserJson.get("bio")));
        user.setImage(String.valueOf(giteeUserJson.get("avatar_url")));
        user.setExtra_message(giteeUserStr);
        userDao.insertOrUpdateUser(user);
        JSONObject userJson=JSONObject.parseObject(JSONObject.toJSONString(user));
        return userJson;
    }
}
