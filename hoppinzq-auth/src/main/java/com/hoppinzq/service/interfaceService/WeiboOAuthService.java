package com.hoppinzq.service.interfaceService;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.exception.UserException;

import java.io.UnsupportedEncodingException;

public interface WeiboOAuthService {

    String getAccessToken(String code,String redirect_uri) throws UnsupportedEncodingException, UserException;
    String getWeiboUser(String access_token,String uid);
    String refreshToken(String refresh_token) throws UnsupportedEncodingException;
    JSONObject createWeiboUser(String code, String redirect_url, String refresh_token) throws Exception;
}
