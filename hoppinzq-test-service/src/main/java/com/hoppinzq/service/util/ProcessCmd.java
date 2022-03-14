package com.hoppinzq.service.util;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author:ZhangQi
 **/
@Component
public class ProcessCmd {


    public String getConsole(String cmd){
        String line = null;
        StringBuilder sb=new StringBuilder();
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(cmd);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
