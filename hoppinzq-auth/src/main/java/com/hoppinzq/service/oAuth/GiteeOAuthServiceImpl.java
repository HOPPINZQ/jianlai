package com.hoppinzq.service.oAuth;

import com.hoppinzq.service.aop.annotation.ServiceRegister;
import com.hoppinzq.service.exception.UserException;
import com.hoppinzq.service.constant.AuthConstant;
import com.hoppinzq.service.interfaceService.GiteeOAuthService;
import com.hoppinzq.service.property.GiteeProperty;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

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
    private GiteeOAuthService giteeOAuthService;

    private static final String authorization_code="authorization_code";
    private static final String refresh_token="refresh_token";


    public String getAccessToken(String code,String redirect_uri) throws UnsupportedEncodingException, UserException {
        String postUrl=AuthConstant.GITEE_OAUTH_TOKEN_URL+"?grant_type="+authorization_code +
                "&code=" +code+
                "&client_id="+giteeProperty.getCilent_id() +
                "&redirect_uri="+redirect_uri +
                "&client_secret="+giteeProperty.getClient_secret();
        String giteeRes=httpClientComm.post(postUrl);
        if(giteeRes==null){
            throw new UserException("码云认证服务失败。");
        }
        System.err.println(giteeRes);
        //{
        //    "access_token": "ee65047cb17f0fdeb8e64ecdc712049c",
        //    "token_type": "bearer",
        //    "expires_in": 86400,
        //    "refresh_token": "09f7942a85bbda4cfc23fdf6bb899ec6cb5c2c740608be63819df19cd15bc3b0",
        //    "scope": "user_info",
        //    "created_at": 1642472291
        //}
        return giteeRes;
    }

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

    public String getGiteeUser(String access_token){
        String giteeRes=httpClientComm.get(AuthConstant.GITEE_OPENAPI_URL+"?access_token="+access_token+"#/getV5User");
        return giteeRes;
    }

}
