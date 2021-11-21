package com.hoppinzq.service.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 文件导出工具
 * @author abc
 * @date 2019/01/08
 */
public class FileExport {

    private static final Logger logger = LoggerFactory.getLogger(FileExport.class);

    /** CSV文件列分隔符 */
    private static final String CSV_COLUMN_SEPARATOR = ",";

    private static final String CSV_COLUM_TABLE = "\t";

    /** CSV文件列分隔符 */
    private static final String CSV_RN = "\r\n";

    /**
     * 导出Excel文件
     *
     * @param excelHeader
     *            导出文件中表格头
     * @param list
     *            导出的内容
     * @param response
     *            HttpServletResponse对象，用来获得输出流向客户端写导出的文件
     * @param sheetName
     *            Excel的sheet名称，加上时间戳作为导出文件的名称
     */
    public static void exportExcel(String [] excelHeader, List<Object> list, String sheetName, HttpServletResponse response, HttpServletRequest request) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);
        HSSFRow row = sheet.createRow((int) 0);
        /******设置单元格是否显示网格线******/
        sheet.setDisplayGridlines(false);

        /******设置头单元格样式******/
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        Font fontHeader = wb.createFont();
        fontHeader.setBold(true);
        fontHeader.setFontHeight((short) 240);
        style.setFont(fontHeader);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);

        /******设置头内容******/
        for (int i = 0; i < excelHeader.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue("  " +excelHeader[i] + "  ");
            cell.setCellStyle(style);
        }

        /******设置内容单元格样式******/
        HSSFCellStyle styleCell = wb.createCellStyle();
        Font fontCell = wb.createFont();
        fontCell.setColor(HSSFColor.BLACK.index);
        styleCell.setAlignment(HorizontalAlignment.CENTER);
        styleCell.setFont(fontCell);
        styleCell.setBorderBottom(BorderStyle.THIN);
        styleCell.setBorderLeft(BorderStyle.THIN);
        styleCell.setBorderRight(BorderStyle.THIN);
        styleCell.setBorderTop(BorderStyle.THIN);
        /******设置单元格内容******/
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            /******设置行高******/
            row.setHeightInPoints(20);
            Object[] obj = (Object[]) list.get(i);
            for (int j = 0; j < excelHeader.length; j++) {
                styleCell.setWrapText(false);
                HSSFCell cell = row.createCell(j);
                if (obj[j] != null){
                    cell.setCellValue(obj[j].toString());
                }else{
                    cell.setCellValue("");
                }
                //if(obj[j].toString().length()>20)
                //	styleCell.setWrapText(true);
                cell.setCellStyle(styleCell);
                sheet.autoSizeColumn(j);
            }
        }

        OutputStream ouputStream = null;
        try {

            String encoding = "UTF-8";
            /** 获取浏览器相关的信息 */
            String userAgent = request.getHeader("user-agent");
            /** 判断是否为msie浏览器 */
            if (userAgent.toLowerCase().indexOf("msie") != -1){
                encoding = "gbk";
            }

            response.setCharacterEncoding(encoding);
            response.setContentType("application/vnd.ms-excel");
            String fileName = sheetName;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHMMSS");
            fileName += (dateFormat.format(new Date())).toString()+".xls";
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, encoding));
            ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(ouputStream!=null) {
                    ouputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 导出CSV文件
     * @param dataList 集合数据
     * @param colNames 表头部数据
     * @param mapKey 查找的对应数据
     */
    public static boolean doExport(List<Map<String, Object>> dataList, String colNames, String mapKey, OutputStream os) {
        try {
            StringBuffer buf = new StringBuffer();

            String[] colNamesArr = null;
            String[] mapKeyArr = null;

            colNamesArr = colNames.split(",");
            mapKeyArr = mapKey.split(",");

            /******完成数据csv文件的封装******/
            /******输出列头******/
            for (int i = 0; i < colNamesArr.length; i++) {
                buf.append(colNamesArr[i]).append(CSV_COLUMN_SEPARATOR);
            }
            buf.append(CSV_RN);

            if (null != dataList) {
                /******输出数据******/
                for (int i = 0; i < dataList.size(); i++) {
                    for (int j = 0; j < mapKeyArr.length; j++) {
                        buf.append(dataList.get(i).get(mapKeyArr[j])).append(CSV_COLUM_TABLE).append(CSV_COLUMN_SEPARATOR);
                    }
                    buf.append(CSV_RN);
                }
            }
            /******写出响应******/
            os.write(buf.toString().getBytes("GBK"));
            os.flush();
            return true;
        } catch (Exception e) {
            logger.error("doExport错误...", e);
        }
        return false;
    }

    /**
     * 设置响应格式
     * @param fileName
     * @param response
     * @throws UnsupportedEncodingException
     */
    public static void responseSetProperties(String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        /******设置文件后缀******/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fn = fileName + sdf.format(new Date()).toString() + ".csv";
        /******读取字符编码******/
        String utf = "UTF-8";

        /******设置响应******/
        response.setContentType("application/ms-txt.numberformat:@");
        response.setCharacterEncoding(utf);
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, utf));
    }
}
