package com.hoppinzq.service.file.base.service;

import com.alibaba.fastjson.JSON;
import com.hoppinzq.service.bean.FileBean;
import com.hoppinzq.service.file.base.dao.BaseFileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author:ZhangQi
 **/
@Service
public class BaseFileService {

    @Autowired
    private BaseFileDao dao;

    @Async
    public void insertFile(FileBean fileBean){
        dao.insertFile(fileBean);
    }

    public FileBean queryFileById(String file_id){
        Map map=dao.queryFileById(file_id);
        FileBean fileBean= JSON.parseObject(JSON.toJSONString(map), FileBean.class);
        return fileBean;
    }

    public void updateFile(FileBean fileBean){
     dao.updateFile(fileBean);
    }


    public List<FileBean> queryFile(){
        List<Map> list=dao.queryFile();
        List<FileBean> fileBeanList=new ArrayList<>();
        for (Map map:list) {
            FileBean fileBean= JSON.parseObject(JSON.toJSONString(map), FileBean.class);
            fileBeanList.add(fileBean);
        }
        return fileBeanList;
    }


}


