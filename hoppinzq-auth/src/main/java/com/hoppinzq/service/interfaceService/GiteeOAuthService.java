package com.hoppinzq.service.interfaceService;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.exception.UserException;

import java.io.UnsupportedEncodingException;

public interface GiteeOAuthService {
    String getAccessToken(String code,String redirect_uri) throws UnsupportedEncodingException, UserException;
    String getGiteeUser(String access_token);
    String refreshToken(String refresh_token) throws UnsupportedEncodingException;
    JSONObject createGiteeUser(String code, String redirect_url, String refresh_token) throws Exception;
}
