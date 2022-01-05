package com.hoppinzq.service.bean;

import java.io.Serializable;

/**
 * @author:ZhangQi
 **/
public class FileEnitiy implements Serializable {
    private static final long serialVersionUID = 1L;

    private String file_id;
    private String file_name;
    private String file_path;
    private String file_date;
    private Integer file_like;
    private Integer file_download;
    private String file_name_change;
    private String file_type;
    private Integer file_isactive;
    private String file_description;
    private String file_volume;
    private int file_version=1;

    public String getFile_volume() {
        return file_volume;
    }

    public void setFile_volume(String file_volume) {
        this.file_volume = file_volume;
    }

    public int getFile_version() {
        return file_version;
    }

    public void setFile_version(int file_version) {
        this.file_version = file_version;
    }

    public void downloadPlus(){
        this.file_download++;
    }

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getFile_date() {
        return file_date;
    }

    public void setFile_date(String file_date) {
        this.file_date = file_date;
    }

    public Integer getFile_like() {
        return file_like;
    }

    public void setFile_like(Integer file_like) {
        this.file_like = file_like;
    }

    public Integer getFile_download() {
        return file_download;
    }

    public void setFile_download(Integer file_download) {
        this.file_download = file_download;
    }

    public String getFile_name_change() {
        return file_name_change;
    }

    public void setFile_name_change(String file_name_change) {
        this.file_name_change = file_name_change;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public Integer getFile_isactive() {
        return file_isactive;
    }

    public void setFile_isactive(Integer file_isactive) {
        this.file_isactive = file_isactive;
    }

    public String getFile_description() {
        return file_description;
    }

    public void setFile_description(String file_description) {
        this.file_description = file_description;
    }
}
