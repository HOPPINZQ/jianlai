package com.hoppinzq.service.controller;

import com.hoppinzq.service.utils.ExcelUtil;
import com.hoppinzq.service.utils.FileExport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class ExcelController {

    /**
     * @param @param  uuid
     * @param @param  request
     * @param @param  response
     * @param @return
     * @return Data
     * @throws
     * @Title: getExcelTemplate
     * @Description: 生成Excel模板并导出
     */
    @RequestMapping("/getExcelTemplate")
    public void getExcelTemplate(HttpServletRequest request, HttpServletResponse response) {

        String fileName = "D:\\111.xls"; //模板名称
        String[] handers = {"姓名", "性别", "证件类型", "证件号码", "服务结束时间", "参保地", "民族"}; //列标题

        //下拉框数据
        List<String[]> downData = new ArrayList();
        String[] str1={"qwe","asd","zxc","wer","dfg","cvb"};
        String[] str2 = {"01-汉族", "02-蒙古族", "03-回族", "04-藏族", "05-维吾尔族", "06-苗族", "07-彝族", "08-壮族", "09-布依族", "10-朝鲜族", "11-满族", "12-侗族", "13-瑶族", "14-白族", "15-土家族", "16-哈尼族", "17-哈萨克族", "18-傣族", "19-黎族", "20-傈僳族", "21-佤族", "22-畲族", "23-高山族", "24-拉祜族", "25-水族", "26-东乡族", "27-纳西族", "28-景颇族", "29-柯尔克孜族", "30-土族", "31-达斡尔族", "32-仫佬族", "33-羌族", "34-布朗族", "35-撒拉族", "36-毛难族", "37-仡佬族", "38-锡伯族", "39-阿昌族", "40-普米族", "41-塔吉克族", "42-怒族", "43-乌孜别克族", "44-俄罗斯族", "45-鄂温克族", "46-德昂族", "47-保安族", "48-裕固族", "49-京族", "50-塔塔尔族", "51-独龙族", "52-鄂伦春族", "53-赫哲族", "54-门巴族", "55-珞巴族", "56-基诺族", "98-外国血统", "99-其他"};
        downData.add(str1);
        downData.add(str2);
        downData.add(str2);
        String[] downRows = {"1","5","6"}; //下拉的列序号数组(序号从0开始)

        try {

            ExcelUtil.createExcelTemplate(fileName, handers, downData, downRows);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/downloadExcel")
    public void downloadExcel(HttpServletResponse response, HttpServletRequest request) {
        String [] excelHeader = {"姓名","手机号（必填）","渠道名","产品名","手机操作系统（IOS/安卓)","是否是XX数据"};
        List<Object> list = new ArrayList<>();
        Object[] obj1 = {"张三","173*****311‬","a1","A","IOS","是"};
        Object[] obj2 = {"李四","138*****742","a2","B","安卓","否"};
        list.add(obj1);
        list.add(obj2);
        FileExport.exportExcel(excelHeader, list, "XXX模板", response, request);
    }


}
