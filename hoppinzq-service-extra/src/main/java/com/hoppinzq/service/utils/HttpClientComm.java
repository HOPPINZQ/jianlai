package com.hoppinzq.service.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Component
public class HttpClientComm {
    private Logger log = LoggerFactory.getLogger(HttpClientComm.class);

    /**
     * 客户端
     */
    private CloseableHttpClient httpclient;
    private PoolingHttpClientConnectionManager poolConnManager = null;
    /**
     * cookie信息
     */
    private Set<String> cookies;
    /**
     * 设置默认延迟时间
     */
    private RequestConfig requestconfig;
    /**
     * 返回存储数据
     */
    private String apiResponse;

    /**
     * 初始化客户端
     */
    public HttpClientComm() {
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslsf).build();
            poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            poolConnManager.setMaxTotal(640);
            poolConnManager.setDefaultMaxPerRoute(320);
            requestconfig = RequestConfig.custom().setConnectionRequestTimeout(40 * 1000).setSocketTimeout(30 * 1000).build();
            httpclient = HttpClients.custom()
                    .setConnectionManager(poolConnManager)
                    .setDefaultRequestConfig(requestconfig)
                    .setRetryHandler(new DefaultHttpRequestRetryHandler(2, false)).build();
            cookies = new HashSet<>();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化请求，设置请求时间
     */
    public HttpClientComm(int millisTimeOut) {
        httpclient = HttpClients.createDefault();
        requestconfig = RequestConfig.custom().setConnectionRequestTimeout(millisTimeOut).setSocketTimeout(millisTimeOut).build();
        cookies = new HashSet<>();
    }

    /**
     * 释放请求资源
     */
    public void close() {
        if (cookies != null) {
            cookies = null;
        }
        if (apiResponse != null) {
            apiResponse = null;
        }
        if (httpclient != null) {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                httpclient = null;
            }
        }
    }

    /**
     * get请求-携带header信息
     */
    public String getWithHeader(String url, Map<String, String> headers) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestconfig);
        headers.forEach(httpGet::addHeader);
        httpGet.addHeader("cookie", cookies.toString().substring(1, cookies.toString().length() - 1));
        CloseableHttpResponse response = null;

        try {
            response = httpclient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getResData(response, httpGet);
    }

    /**
     * get请求
     */
    public String get(String url) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestconfig);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getResData(response, httpGet);
    }

    /**
     * get请求-拼接请求参数
     */
    public String getWithParam(String url, JSONObject param) {
        HttpGet httpGet = new HttpGet(checkAndAppendParam(url, param));
        httpGet.setConfig(requestconfig);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getResData(response, httpGet);
    }

    /**
     * 参数写在URL上
     */
    public String getWithParam(String url,String param) {
        HttpGet httpGet = new HttpGet(url+"/"+param);
        httpGet.setConfig(requestconfig);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getResData(response, httpGet);
    }

    /**
     * 拼接get参数，返回新的url
     */
    private synchronized String checkAndAppendParam(String url, JSONObject param) {
        if (param == null || param.size() == 0) {
            return url;
        }
        String paramStr = param.toString();
        return url + "&" + paramStr.substring(1, paramStr.length() - 1)
                .replace(",", "&")
                .replace(":", "=")
                .replace("\"", "");
    }

    /**
     * post请求方式
     */
    public String post(String url, Map<String, String> headers, Map<String, Object> params, String charset) throws UnsupportedEncodingException {
        //设置请求方法及连接信息
        HttpPost httpPost = new HttpPost(url);
        //设置请求参数
        httpPost.setConfig(requestconfig);
        //设置请求头
        if(headers!=null){
            headers.forEach(httpPost::addHeader);
        }
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        BasicCookieStore cookie = new BasicCookieStore();
        HttpClients.custom().setDefaultCookieStore(cookie).build();
        httpPost.addHeader("cookie", cookies.toString().substring(1, cookies.toString().length() - 1));
        List<NameValuePair> pairs = null;
        if (params != null && !params.isEmpty()) {
            pairs = new ArrayList<>(params.size());
            for (String key : params.keySet()) {
                pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
            }
        }
        if (pairs != null && pairs.size() > 0) {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, charset));
        }
        //执行请求
        HttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getResData(response, httpPost);
    }

    /**
     * 返回请求结果
     */
    public String getResData(HttpResponse response, HttpRequestBase requestBase) {
        int status = 405;
        if (response != null) {
            status = response.getStatusLine().getStatusCode();
            for (Header header : response.getAllHeaders()) {
                if ("Set-Cookie".equalsIgnoreCase(header.getName())) {
                    cookies.add(header.getValue());
                }
            }
        }
        if (status != 200) {
            requestBase.abort();
        } else {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try {
                    apiResponse = EntityUtils.toString(entity, "utf-8");
                    //将data转换为ApiResponse
                    //apiResponse = JSONObject.parseObject(data, ApiResponse.class);
                    //关闭流
                    EntityUtils.consume(entity);
                } catch (ParseException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return apiResponse;
    }
}
