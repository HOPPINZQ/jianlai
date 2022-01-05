package com.hoppinzq.service.file.base.service;

import com.alibaba.fastjson.JSON;
import com.hoppinzq.service.bean.FileEnitiy;
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
    public void insertFile(FileEnitiy enitiy){
        dao.insertFile(enitiy);
    }

    public FileEnitiy queryFileById(String file_id){
        Map map=dao.queryFileById(file_id);
        FileEnitiy fileEnitiy= JSON.parseObject(JSON.toJSONString(map), FileEnitiy.class);
        return fileEnitiy;
    }

    public void updateFile(FileEnitiy enitiy){
     dao.updateFile(enitiy);
    }


    public List<FileEnitiy> queryFile(){
        List<Map> list=dao.queryFile();
        List<FileEnitiy> fileEnitiyList=new ArrayList<>();
        for (Map map:list) {
            FileEnitiy fileEnitiy= JSON.parseObject(JSON.toJSONString(map), FileEnitiy.class);
            fileEnitiyList.add(fileEnitiy);
        }
        return fileEnitiyList;
    }


}


