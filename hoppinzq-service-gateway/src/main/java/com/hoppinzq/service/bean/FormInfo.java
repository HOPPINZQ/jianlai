package com.hoppinzq.service.bean;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author:ZhangQi
 * 表单类
 **/
public class FormInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private long size;
    private String contentType;
    private String submittedFileName;
    private String inputStream;

    public String getInputStream() {
        return inputStream;
    }

    public void setInputStream(String inputStream) throws IOException {
        this.inputStream = inputStream;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getSubmittedFileName() {
        return submittedFileName;
    }

    public void setSubmittedFileName(String submittedFileName) {
        this.submittedFileName = submittedFileName;
    }

    public String toJsonString() {
        return "{" +
                "\"name\":\"" + name + '\"' +
                ", \"size\":\"" + size +'\"' +
                ", \"contentType\":\"" + contentType + '\"' +
                ", \"submittedFileName\":\"" + submittedFileName + '\"' +
                ", \"inputStream\":\"" + inputStream + '\"' +
                '}';
    }
}
