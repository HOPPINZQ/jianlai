package com.hoppinzq.service.interfaceService;

import com.hoppinzq.service.exception.UserException;

import java.io.UnsupportedEncodingException;

public interface GiteeOAuthService {

    String getAccessToken(String code,String redirect_uri) throws UnsupportedEncodingException, UserException;
}
