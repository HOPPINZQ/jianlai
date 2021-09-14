package com.hoppinzq.service.util;

import java.io.*;
import java.util.List;

/**
 * @author:ZhangQi
 **/
//ffmpeg -i E:\baby_img\00.mp4 -i E:\baby_img\01.mp4 -c:v copy -c:a aac -strict experimental E:\baby_img\02.mp4
public class ConvertFlv {
    //public static void main(String[] args) {
    //    ConvertFlv.convert("E:\\baby_img\\1111.flv", "E:\\baby_img\\qwe.mp4");
    //}

    public static boolean convert(String inputFile, String outputFile) {
        if (!checkfile(inputFile)) {
            System.out.println(inputFile + "isnotfile");
            return false;
        }
        if (process(inputFile, outputFile)) {
            System.out.println("ok");
            return true;
        }
        return false;
    }

    //检查文件是否存在
    private static boolean checkfile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return false;
        }
        return true;
    }

    private static boolean process(String inputFile, String outputFile) {
        int type = checkContentType(inputFile);
        boolean status = false;
        if (type == 0) {
            status = processFLV(inputFile, outputFile);//直接将文件转为flv文件
        } else if (type == 1) {
            String avifilepath = processAVI(type, inputFile);
            if (avifilepath == null)
                return false;//avi文件没有得到
            status = processFLV(avifilepath, outputFile);//将avi转为flv
        }
        return status;
    }

    /**
     * 检查视频类型
     *
     * @paraminputFile
     * @returnffmpeg能解析返回0，不能解析返回1
     */
    private static int checkContentType(String inputFile) {
        String type = inputFile.substring(inputFile.lastIndexOf(".") + 1,
                inputFile.length())
                .toLowerCase();
        //ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
        if (type.equals("avi")) {
            return 0;
        } else if (type.equals("mpg")) {
            return 0;
        } else if (type.equals("wmv")) {
            return 0;
        } else if (type.equals("3gp")) {
            return 0;
        } else if (type.equals("mov")) {
            return 0;
        } else if (type.equals("mp4")) {
            return 0;
        } else if (type.equals("asf")) {
            return 0;
        } else if (type.equals("asx")) {
            return 0;
        } else if (type.equals("flv")) {
            return 0;
        }
        //对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
        //可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
        else if (type.equals("wmv9")) {
            return 1;
        } else if (type.equals("rm")) {
            return 1;
        } else if (type.equals("rmvb")) {
            return 1;
        }
        return 9;
    }

    /**
     * ffmepg:能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
     *
     * @return
     * @paraminputFile
     * @paramoutputFile
     */
    private static boolean processFLV(String inputFile, String outputFile) {
        if (!checkfile(inputFile)) {
            System.out.println(inputFile + "isnotfile");
            return false;
        }
        List<String> commend = new java.util.ArrayList<String>();
        //低精度
        commend.add("");
        commend.add("-i ");
        commend.add(inputFile);
        commend.add(" -ab");
        commend.add(" 128");
        commend.add(" -acodec");
        commend.add(" libmp3lame");
        commend.add(" -ac");
        commend.add(" 1");
        commend.add(" -ar");
        commend.add(" 22050");
        commend.add(" -qscale");
        commend.add(" 4");
        commend.add(" -s");
        commend.add(" 350x240");
        commend.add(" -r");
        commend.add(" 29.97");
        commend.add(" -b");
        commend.add(" 512");
        commend.add(" -y ");
        commend.add(outputFile);
        StringBuffer test = new StringBuffer();
        for (int i = 0; i < commend.size(); i++)
            test.append(commend.get(i) + "");
        System.out.println(test);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            builder.start();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mencoder:
     * 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
     * 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
     *
     * @return
     * @paramtype
     * @paraminputFile
     */
    private static String processAVI(int type, String inputFile) {
        File file = new File("");
        if (file.exists()) file.delete();
        List<String> commend = new java.util.ArrayList<String>();
        commend.add("");
        commend.add(inputFile);
        commend.add("-oac");
        commend.add("mp3lame");
        commend.add("-lameopts");
        commend.add("preset=64");
        commend.add("-ovc");
        commend.add("xvid");
        commend.add("-xvidencopts");
        commend.add("bitrate=600");
        commend.add("-of");
        commend.add("avi");
        commend.add("-o");
        commend.add("");
        StringBuffer test = new StringBuffer();
        for (int i = 0; i < commend.size(); i++)
            test.append(commend.get(i) + "");
        System.out.println(test);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process p = builder.start();
            /**
             *清空Mencoder进程的输出流和错误流
             *因为有些本机平台仅针对标准输入和输出流提供有限的缓冲区大小，
             *如果读写子进程的输出流或输入流迅速出现失败，则可能导致子进程阻塞，甚至产生死锁。
             */
            final InputStream is1 = p.getInputStream();
            final InputStream is2 = p.getErrorStream();
            new Thread() {
                public void run() {
                    BufferedReader br = new BufferedReader(new
                            InputStreamReader(is1));
                    try {
                        String lineB = null;
                        while ((lineB = br.readLine()) != null) {
                            if (lineB != null) System.out.println(lineB);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            new Thread() {
                public void run() {
                    BufferedReader br2 = new BufferedReader(new
                            InputStreamReader(is2));
                    try {
                        String lineC = null;
                        while ((lineC = br2.readLine()) != null) {
                            if (lineC != null) System.out.println(lineC);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            p.waitFor();
            System.out.println("whocares");
            return "";
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
}
