package com.hoppinzq.service.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:ZhangQi
 **/
public class CMDUtil {

    private CMDUtil(){}

    public static List getTaskList(){
        String cmdTask="tasklist";//
        String line = null;
        int lineIndex=0;
        int[] lineLength=new int[5];
        List taskList=new ArrayList();
        Runtime runtime = Runtime.getRuntime(); //得到本程序
        try {
            Process process = runtime.exec(cmdTask);  //该实例可用来控制进程并获得相关信息
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
            while ((line = bufferedReader.readLine()) != null) {
                Map taskMap=new HashMap();
                if(cmdTask.equals("tasklist")){//暂时不用
                    if(lineIndex==2){
                        String[] strs=line.split(" ");
                        for(int i=0;i<strs.length;i++){
                            lineLength[i]=strs[i].length();
                        }
                    }
                    if(line.trim()!=""&&lineIndex>2){
                        int startIndex=0;
                        int endIndex=lineLength[0];
                        taskMap.put("imageName",line.substring(startIndex,endIndex).trim());
                        startIndex=endIndex+1;
                        endIndex+=lineLength[1]+1;
                        taskMap.put("PID",line.substring(startIndex,endIndex).trim());
                        startIndex=endIndex+1;
                        endIndex+=lineLength[2]+1;
                        taskMap.put("sessionName",line.substring(startIndex,endIndex).trim());
                        startIndex=endIndex+1;
                        endIndex+=lineLength[3]+1;
                        taskMap.put("session",line.substring(startIndex,endIndex).trim());
                        startIndex=endIndex+1;
                        endIndex+=lineLength[4]+1;
                        taskMap.put("memoryUsage",line.substring(startIndex,endIndex).trim());
                        taskList.add(taskMap);
                    }
                    lineIndex++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskList;
    }


}
