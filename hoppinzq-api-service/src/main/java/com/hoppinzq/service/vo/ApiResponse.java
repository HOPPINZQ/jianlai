package com.hoppinzq.service.vo;


import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class ApiResponse<T> implements Serializable {
    private static final long serialVersionUID = 2783377098145240357L;
    private Integer code;
    private String msg;
    private T data;

    public ApiResponse() {
        this.code = 200;
        this.msg = "success";
        this.data = null;
    }

    public ApiResponse(Integer code, T data, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ApiResponse<T> data(T data) {
        return data(data, "操作成功");
    }

    public static <T> ApiResponse<T> data(T data, String msg) {
        return data(200, data, msg);
    }

    public static <T> ApiResponse<T> data(int code, T data, String msg) {
        return new ApiResponse(code, data, data == null ? "暂无承载数据" : msg);
    }

    public static <T> ApiResponse<T> success(String msg) {
        return new ApiResponse(200, null, msg);
    }


    public static <T> ApiResponse<T> fail(int code, String msg) {
        return new ApiResponse(code, (Object) null, msg);
    }


    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getmsg() {
        return this.msg;
    }

    public void setmsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("msg",msg);
        jsonObject.put("data",data);
        return jsonObject.toString();
    }
    public static ApiResponse error(Integer code, String msg) {
        ApiResponse result =new ApiResponse();
        result.setCode(code);
        result.setmsg(msg);
        return result;
    }

    public static ApiResponse error(ErrorEnum errorEnum) {
        ApiResponse result = new ApiResponse();
        result.setCode(errorEnum.code);
        result.setmsg(errorEnum.msg);
        return result;
    }
}

