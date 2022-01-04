package com.hoppinzq.hoppinzqcode.file.base.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.hoppinzqcode.bean.FileEnitiy;
import com.hoppinzq.hoppinzqcode.constants.ProjectParm;
import com.hoppinzq.hoppinzqcode.file.base.service.FileService;
import com.hoppinzq.hoppinzqcode.util.UUIDUtil;
import com.hoppinzq.service.util.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;


/**
 * 提供基本文件上传接口
 */
@Controller
@RequestMapping("/baseFile")
public class BaseFileController {

    @Value("${service.outip}")
    public String ip;
    public static final String zqKey="zhangqi";
    @Autowired
    private FileService fileService;

    /**
     * 文件下载，重定向至真正的下载接口
     * @param file_id
     * @return
     */
    @RequestMapping("/downloadFile/{file_id}")
    public String redirectDownload(@PathVariable String file_id){
        String file_jm = EncryptUtil.AESencode((EncryptUtil.DESencode(""+file_id,zqKey)),zqKey);
        return "redirect:http://127.0.0.1:8090/baseFile/"+file_jm+"/"+ new Date().getTime() +"/downloadFile";
    }

    /**
     * 文件下载真正接口
     * @param file_jm
     * @param request
     * @param response
     */
    @RequestMapping("/{file_jm}/{time}/downloadFile")
    public void downloadTest(@PathVariable String file_jm, HttpServletRequest request, HttpServletResponse response) throws IOException {
        OutputStream out = null;
        String file_jim= EncryptUtil.DESdecode((EncryptUtil.AESdecode(file_jm, zqKey)),zqKey);
        // 输出到浏览器（下载）
        try {
            out=response.getOutputStream();
            if(file_jim==null){
                response.setHeader("Content-Type", "text/html;charset=utf-8");
                String str="403警告!!!!!!!!!!!";
                byte[] b = str.getBytes();
                out.write(b);
            }else{
                FileEnitiy fileMap=fileService.queryFileById(file_jim);
                if(fileMap==null){
                    response.setHeader("Content-Type", "text/html;charset=utf-8");
                    String str="未能找到该文件。或者该文件是私密文件";
                    byte[] b = str.getBytes();
                    out.write(b);
                }else{
                    fileMap.downloadPlus();
                    fileService.updateFile(fileMap);
                    String fileName=fileMap.getFile_name()+fileMap.getFile_type();
                    String filePath=fileMap.getFile_path();
                    // 这种写法是服务器上用的，因为要把文件下载到本地，只要确定文件名称即可，会让用户自己选择保存在本机的哪个地方
                    String userAgent = request.getHeader("User-Agent");
                    // 针对IE或者以IE为内核的浏览器：
                    response.setContentType("application/multipart/form-data");
                    response.setHeader("Content-disposition", "attachment;filename=" + fileName);
                    /*  设置文件ContentType类型，这样设置，会自动判断下载文件类型   */
                    //out = response.getOutputStream();
                    //获取服务器文件
                    File file = new File(filePath);
                    InputStream ins = new FileInputStream(file);
                    byte[] b = new byte[1024];
                    int len;
                    while((len = ins.read(b)) > 0){
                        out.write(b,0,len);
                    }
                }
            }
        } catch (Exception e) {
            response.setHeader("Content-Type", "text/html;charset=utf-8");
            String str="文件服务器异常";
            byte[] b = str.getBytes();
            out.write(b);
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (Exception e) {

            }
        }
    }

    @PostMapping(value = "/blogFileUploadFj")
    @ResponseBody
    public JSONObject blogFileUploadFj(MultipartFile file) {
        JSONObject jsonObject=new JSONObject();
        try{
            String fileName = file.getOriginalFilename();
            String fileType=fileName.substring(fileName.lastIndexOf("."));
            String fileName_pre=fileName.split(fileType)[0];
            String fileID=UUIDUtil.getUUID();
            String last_fileName=fileID+fileType;
            String last_videoName__= ProjectParm.resourcesPath +"blog"+File.separator+ last_fileName;
            file.transferTo(new File(last_videoName__));
            FileEnitiy fileEnitiy=new FileEnitiy();
            fileEnitiy.setFile_id(fileID);
            fileEnitiy.setFile_name(fileName_pre);
            fileEnitiy.setFile_type(fileType);
            fileEnitiy.setFile_name_change(last_fileName);
            fileEnitiy.setFile_path(last_videoName__);
            fileService.insertFile(fileEnitiy);

            JSONObject fileJSON=new JSONObject();
            fileJSON.put("fileCurrentName",fileName_pre);
            fileJSON.put("fileType",fileType);
            fileJSON.put("fileRealName",last_fileName);
            fileJSON.put("fileId",fileID);
            fileJSON.put("filePath","http://"+ip+":8090/blog/"+last_fileName);
            jsonObject.put("success",1);
            jsonObject.put("data",fileJSON);
        }catch (Exception ex){
            ex.printStackTrace();
            jsonObject.put("success",0);
        }
        return jsonObject;
    }

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
            String last_fileName__= ProjectParm.resourcesPath +"blog"+File.separator+ last_fileName;
            img.transferTo(new File(last_fileName__));

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
    public JSONObject hello(@RequestParam(value = "editormd-image-file", required = false) MultipartFile attach) {
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
