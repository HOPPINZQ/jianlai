package com.hoppinzq.service.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * markdown图片上传controller，因为markdown编辑器上传文件要求固定的返回值
 */
@RestController
@RequestMapping("/blogFile")
public class MarkdownFileUploadController {

    @PostMapping(value = "/markdown/fileUpload")
    public JSONObject videoUp(MultipartFile file, HttpServletRequest request) {
        JSONObject jsonObject=new JSONObject();
        try{
            String fileName = file.getOriginalFilename();
            file.transferTo(new File("D:/projectFile/markdown/"+fileName));
            jsonObject.put("success",1);
            jsonObject.put("message","上传成功");
            jsonObject.put("url","http://127.0.0.1:8809/markdown/"+fileName);
        }catch (Exception ex){
            ex.printStackTrace();
            jsonObject.put("success",0);
            jsonObject.put("message","上传失败");
        }
        return jsonObject;
    }


}
