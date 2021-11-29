package com.hoppinzq.service.bean;

/**
 * @author:ZhangQi
 **/
public class FileInfo {

    private String name;
    private String contentType;
    private String submittedFileName;
    private Boolean isFileUploadSuccess;

    public Boolean getFileUploadSuccess() {
        return isFileUploadSuccess;
    }

    public void setFileUploadSuccess(Boolean fileUploadSuccess) {
        isFileUploadSuccess = fileUploadSuccess;
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
}
