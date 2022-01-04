package com.hoppinzq.hoppinzqcode.file.base.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.hoppinzqcode.constants.ProjectParm;
import com.hoppinzq.hoppinzqcode.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;


/**
 * 提供基本文件上传接口
 */
@RestController
@RequestMapping("/baseFile")
public class BaseFileController {

    @Value("${service.outip}")
    public String ip;

    @PostMapping(value = "/fileUpload")
    @ResponseBody
    public JSONObject base(MultipartFile img) {
        JSONObject jsonObject=new JSONObject();
        try{
            String fileName = img.getOriginalFilename();
            String fileType=fileName.substring(fileName.lastIndexOf("."));
            String fileName_pre=fileName.split(fileType)[0];
            String fileID=UUIDUtil.getUUID();
            String last_fileName=fileID+fileType;
            String last_videoName__= ProjectParm.resourcesPath +"blog"+File.separator+ last_fileName;
            img.transferTo(new File(last_videoName__));

            JSONObject imgJSON=new JSONObject();
            imgJSON.put("fileCurrentName",fileName_pre);
            imgJSON.put("fileType",fileType);
            imgJSON.put("fileRealName",last_fileName);
            imgJSON.put("fileId",fileID);
            imgJSON.put("filePath","http://"+ip+":8090/blog/"+last_fileName);
            jsonObject.put("success",1);
            jsonObject.put("data",imgJSON);
        }catch (Exception ex){
            ex.printStackTrace();
            jsonObject.put("success",0);
        }
        return jsonObject;
    }


    /**
     * 富文本编辑器上传图片
     * @param fwbFileImg
     * @return
     */
    @PostMapping(value = "/fwb")
    @ResponseBody
    public JSONObject fwb(MultipartFile fwbFileImg) {
        JSONObject jsonObject=new JSONObject();
        try{
            String fileName = fwbFileImg.getOriginalFilename();
            String fileType=fileName.substring(fileName.lastIndexOf("."));
            String fileName_pre=fileName.split(fileType)[0];
            String fileID=UUIDUtil.getUUID();
            String last_fileName=fileID+fileType;
            String last_videoName__= ProjectParm.resourcesPath +"blog"+File.separator+ last_fileName;
            fwbFileImg.transferTo(new File(last_videoName__));

            JSONArray jsonArray=new JSONArray();
            JSONObject imgObject=new JSONObject();
            imgObject.put("url","http://"+ip+":8090/blog/"+last_fileName);
            imgObject.put("alt",fileName);
            //imgObject.put("href","#");
            jsonArray.add(imgObject);
            jsonObject.put("data",jsonArray);
            jsonObject.put("errno",0);
        }catch (Exception ex){
            ex.printStackTrace();
            jsonObject.put("errno",-1);
        }
        return jsonObject;
    }

    /**
     * markdown上传文件
     * @param attach
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/markdown", method = RequestMethod.POST)
    public JSONObject hello( @RequestParam(value = "editormd-image-file", required = false) MultipartFile attach) {
        JSONObject jsonObject=new JSONObject();
        try {
            String fileName = attach.getOriginalFilename();
            String fileType=fileName.substring(fileName.lastIndexOf("."));
            String fileName_pre=fileName.split(fileType)[0];
            String fileID=UUIDUtil.getUUID();
            String last_fileName=fileID+fileType;
            String last_videoName__= ProjectParm.resourcesPath +"blog"+File.separator+ last_fileName;
            attach.transferTo(new File(last_videoName__));

            jsonObject.put("success", 1);
            jsonObject.put("message", "上传成功");
            jsonObject.put("url", "http://"+ip+":8090/blog/"+last_fileName);
        } catch (Exception e) {
            jsonObject.put("success", 0);
        }

        return jsonObject;
    }




}
