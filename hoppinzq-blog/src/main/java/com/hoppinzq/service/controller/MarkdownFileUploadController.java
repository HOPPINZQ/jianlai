package com.hoppinzq.service.controller;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.common.UserPrincipal;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(value = "/dd")
    public void dd(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 此方法获取文件字节流，在打包之后Linux系统无异
            InputStream input = this.getClass().getClassLoader().getResourceAsStream("docker.html");
            // 设置文件名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("docker.html", "utf-8"));
            // 响应流
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[2048];
            int len;
            while ((len = input.read(b)) != -1) {
                out.write(b, 0, len);
            }
            input.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @PostMapping("import2")
    public void im(MultipartFile excelFile) throws Exception{
        try {
            InputStream inputStream = excelFile.getInputStream();
            POIFSFileSystem fs = new POIFSFileSystem(inputStream);
            HSSFWorkbook workbook = new HSSFWorkbook(fs);
            //XSSFWorkbook workbook = new XSSFWorkbook(fs);
            HSSFSheet sheet = workbook.getSheetAt(0);
            int rowNum = sheet.getLastRowNum();// 行
            int cellNum;

            HSSFRow row;
            HSSFCell cell;
            String value = "";


            for (int i = 1; i <= rowNum; i++) {
                row = sheet.getRow(i);
                cellNum = row.getLastCellNum();// 列

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/import")
    public void importFile(MultipartFile file) throws Exception{
        Workbook wb =null;
        Sheet sheet = null;
        Row row = null;
        List<Map<String,String>> list = null;
        String cellData = null;
        String filePath = "D:\\text.xlsx";
        String columns[] = {"name","age","score"};
        wb = new XSSFWorkbook(file.getInputStream());
        if(wb != null){
            //用来存放表中数据
            list = new ArrayList<Map<String,String>>();
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            //获取第一行
            row = sheet.getRow(0);
            //获取最大列数
            int colnum = row.getPhysicalNumberOfCells();
            for (int i = 1; i<rownum; i++) {
                Map<String,String> map = new LinkedHashMap<String,String>();
                row = sheet.getRow(i);
                if(row !=null){
                    for (int j=0;j<colnum;j++){
                        cellData = (String) getCellFormatValue(row.getCell(j));
                        map.put(columns[j], cellData);
                    }
                }else{
                    break;
                }
                list.add(map);
            }
        }
        //遍历解析出来的list
        for (Map<String,String> map : list) {
            for (Map.Entry<String,String> entry : map.entrySet()) {
                System.out.print(entry.getKey()+":"+entry.getValue()+",");
            }
            System.out.println();
        }
    }

    public static Workbook readExcel(String filePath){
        Workbook wb = null;

        if(filePath==null){
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if(".xls".equals(extString)){
                return wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                return wb = new XSSFWorkbook(is);
            }else{
                return wb = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }
    public static Object getCellFormatValue(Cell cell){
        Object cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
                case Cell.CELL_TYPE_NUMERIC:{
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA:{
                    //判断cell是否为日期格式
                    if(DateUtil.isCellDateFormatted(cell)){
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    }else{
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }


}
