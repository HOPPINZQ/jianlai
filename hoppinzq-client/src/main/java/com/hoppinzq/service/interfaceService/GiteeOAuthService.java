package com.hoppinzq.service.interfaceService;

import java.io.UnsupportedEncodingException;

public interface GiteeOAuthService {

    String getAccessToken(String code,String redirect_uri) throws UnsupportedEncodingException;
}
