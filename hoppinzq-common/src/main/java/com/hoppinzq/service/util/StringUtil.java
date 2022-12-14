package com.hoppinzq.service.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static final int MAX_RESULT_SIZE = 2000;
    private static final Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", 2);
    private static final Pattern singleScriptPattern = Pattern.compile("</script>", 2);
    private static final Pattern singleScriptPattern2 = Pattern.compile("<script(.*?)>", 42);
    private static final Pattern evalScriptPattern = Pattern.compile("eval\\((.*?)\\)", 42);
    private static final Pattern onloadScriptPattern = Pattern.compile("onload(.*?)=", 42);
    private static final Pattern onmousScriptPattern = Pattern.compile("onmouse(.*?)=", 42);
    private static final Pattern alertScriptPattern = Pattern.compile("alert(\\s*?)\\((.*?)\\)", 42);
    private static final Pattern iframeScriptPattern = Pattern.compile("('|\"){0,2}><iframe(\\s)+(.*?)>|><iframe(\\s)+(.*?)>|<iframe(\\s)+(.*?)>", 42);
    private static final Pattern aTagPattern = Pattern.compile("(\"|'){0,2}><a(\\s)+(href=)+(.*?)>(.*?)</a>", 42);
    private static final Pattern otherScriptPattern = Pattern.compile("on(after|before){1}print(.*?)=|onload(.*?)=|on(before){0,1}unload(.*?)=|onerror(.*?)=|on(has|form){0,1}" +
            "change(.*?)=|onmessage(.*?)=|on(on|off){0,1}line(.*?)=|onpage(hide|show){1}(.*?)=|onpopstate(.*?)=|onre(do|size|set){1}(.*?)=|onstorage(.*?)=|onundo(.*?)=|onblur(.*?)=|" +
            "oncontextmenu(.*?)=|onfocus(.*?)=|on(form){0,1}input(.*?)=|oninvalid(.*?)=|onselect(.*?)=|onsubmit(.*?)=|onkey(up|down|press){1}(.*?)=|on(db){0,1}click(.*?)=|ondrag(end|" +
            "enter|leave|over|start){0,1}(.*?)=|ondrop{0,1}(.*?)=|onscroll{0,1}(.*?)", 42);
    private static final Pattern fontfamilyPattern = Pattern.compile("\"*\\s*style\\s*=\\s*\"*(foo|color|font-family|background){1}(:|\\=){1}" +
            "(e|\\\\[0]*65){1}(\\\\){0,1}(x|\\\\[0]*78){1}(\\\\){0,1}(p|\\\\[0]*70){1}(\\\\){0,1}(r|\\\\[0]*72){1}(\\\\){0,1}(e|\\\\[0]*65){1}(\\\\)" +
            "{0,1}(s|\\\\[0]*73){1}(\\\\){0,1}(s|\\\\[0]*73){1}(\\\\){0,1}(i|\\\\[0]*69){1}(\\\\){0,1}(o|\\\\[0]*6F){1}(\\\\){0,1}(n|\\\\[0]*6E){1}" +
            "\\((.*?(\\+)*\\{(toString|valueOf){1}\\:alert\\}|alert\\(.*?\\))\\)(\\!){0,1}", 42);
    public static String toString(Object object){
        return object == null ? "" : object.toString();
    }
    public static String toString(Object object,String defaultValue){
        return object == null ? defaultValue : object.toString();
    }

    public static Double formatDouble2(double d){
        BigDecimal bg= BigDecimal.valueOf(d).setScale(2, RoundingMode.HALF_UP);
        return bg.doubleValue();
    }

    public static Long toLongTime(Object object){
        Long time=null;
        //???????????????????????????
        try{
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time=dateFormat.parse(toString(object)).getTime();
        }catch (Exception e){
            //?????????????????????null
        }
        //???????????????????????????
        if (time==null){
            try {
                SimpleDateFormat dateFormat2=new SimpleDateFormat("yyyy-MM-dd");
                time=dateFormat2.parse(toString(object)).getTime();
            }catch (Exception e){
                //?????????????????????null
            }
        }
        return time;
    }

    public static Integer toInt(Object object){
        Integer result=null;
        try {
            result=Integer.valueOf(toString(object));
        }catch (Exception e){
            //???????????????object???null???????????????????????????????????????null
        }
        return result;
    }

    public static int toInt(Object obj,int defaultValue) {
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception e){
            return defaultValue;
        }
    }

    public static boolean isNotEmpty(Object obj) {
        return obj != null && !"".equals(obj.toString().trim());
    }

    //??????32??????UUID
    public static String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    //??????????????????????????????  ??????true??????????????????
    public static boolean verifyParam(String param) {
        if (param == null || "".equals(param)) {
            return true;
        }
        String regEx = "[`~!@$%^&*+=|{}';',\\[\\].<>?~???@#???%??????&*+|{}????????????????????????????????????]|\n|\r|  ";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(param);
        return !m.find();
    }

    /**
     * ?????????????????????????????????????????????????????????
     * @param str ?????????
     * @return boolean
     */
    public static boolean availableStr(String str){
        return str != null && !"".equals(str) && !"".equals(str.trim());
    }

    /**
     * ???????????????????????????
     * @param param ?????????
     * @return ????????????true
     */
    public static boolean verifyFileName(String param) {
        if (param == null || "".equals(param)||param.length()>255) {
            return false;
        }
        if (param.indexOf("..")>-1||param.indexOf("//")>-1||param.indexOf("////")>-1){
            return false;
        }
        String regEx = "[^*|\\:\"<>?]+\\.[^*|\\:\"<>?\u4E00-\u9FA5]+";
        return param.matches(regEx);
    }
    /**
     * ?????????????????????????????????????????????
     * @param param ?????????
     * @return ????????????true
     */
    public static boolean verifyPlainFileName(String param) {
        if (param == null || "".equals(param)||param.length()>255) {
            return false;
        }
        if (param.indexOf(".")>-1||param.indexOf("..")>-1||param.indexOf("\\")>-1||param.indexOf("/")>-1||param.indexOf("//")>-1||param.indexOf("////")>-1){
            return false;
        }
        return true;
    }
    /**
     * ??????????????????????????????
     * @param param ????????????
     * @return ????????????true
     */
    public static boolean verifyFilePath(String param) {
        if (param == null || "".equals(param)||param.length()>255) {
            return false;
        }
        if (param.indexOf("..")>-1||param.indexOf("//")>-1||param.indexOf("////")>-1){
            return false;
        }
        String regEx = "[^*|\"<>?\\.\u4E00-\u9FA5]+";
        return param.matches(regEx);
    }

    /**
     * ??????????????????
     * @param path ????????????
     * @return ????????????????????????
     */
    public static String filePathFilter(String path){
        if (path == null) {
            return null;
        }
        StringBuilder cleanString = new StringBuilder();
        for (int i = 0; i < path.length(); ++i) {
            cleanString.append(cleanChar(path.charAt(i)));
        }
        if (cleanString.indexOf("..")>0||cleanString.indexOf("//")>0||cleanString.indexOf("\\\\")>0||
                cleanString.indexOf("https")>0||cleanString.indexOf("http")>0){
            cleanString = new StringBuilder("error.jpg");
        }
        if (cleanString.indexOf("\\")>-1){
            cleanString =  new StringBuilder(cleanString.toString().replaceAll("\\\\",Matcher.quoteReplacement(File.separator)));
        }
        if (cleanString.indexOf("/")>-1){
            cleanString = new StringBuilder(cleanString.toString().replaceAll("/",Matcher.quoteReplacement(File.separator)));
        }
        return cleanString.toString();
    }

    /**
     * ??????????????????????????????????????????%
     * @param aChar ??????????????????
     * @return ????????????
     */
    private static char cleanChar(char aChar) {
        char result;
        // 0 - 9
        for (int i = 48; i < 58; ++i) {
            if (aChar == i) {
                return  (char) i;
            }
        }
        // 'A' - 'Z'
        for (int i = 65; i < 91; ++i) {
            if (aChar == i) {
                return (char) i;
            }
        }
        // 'a' - 'z'
        for (int i = 97; i < 123; ++i) {
            if (aChar == i) {
                return (char) i;
            }
        }
        // other valid characters
        switch (aChar) {
            case '/':
                result = '/';
                break;
            case '\\':
                result = '\\';
                break;
            case '.':
                result = '.';
                break;
            case '-':
                result = '-';
                break;
            case '_':
                result = '_';
                break;
            case ' ':
                result = ' ';
                break;
            case ':':
                result = ':';
                break;
            default:
                result = '%';
        }
        return result;
    }

    public static String connectParamFilter(String param){
        if (param == null) {
            return null;
        }
        StringBuilder cleanString = new StringBuilder();
        for (int i = 0; i < param.length(); ++i) {
            cleanString.append(cleanXmlChar(param.charAt(i)));
        }
        return cleanString.toString();
    }
    private static char cleanXmlChar(char aChar) {
        // 0 - 9
        for (int i = 48; i < 58; ++i) {
            if (aChar == i) {
                return (char) i;
            }
        }
        // 'A' - 'Z'
        for (int i = 65; i < 91; ++i) {
            if (aChar == i) {
                return (char) i;
            }
        }
        // 'a' - 'z'
        for (int i = 97; i < 123; ++i) {
            if (aChar == i) {
                return (char) i;
            }
        }
        //chinese
        for (int i=19968;i<=40869;i++){
            if(aChar==i){
                return (char) i;
            }
        }
        char ch;
        // other valid characters
        switch (aChar) {
            case '/':
                ch = '/';
                break;
            case '.':
                ch =  '.';
                break;
            case '-':
                ch =  '-';
                break;
            case '_':
                ch = '_';
                break;
            case ' ':
                ch = ' ';
                break;
            case '"':
                ch = '"';
                break;
            case '\'':
                ch = '\'';
                break;
            case '{':
                ch = '{';
                break;
            case '}':
                ch = '}';
                break;
            case ':':
                ch = ':';
                break;
            case '<':
                ch = '<';
                break;
            case '>':
                ch = '>';
                break;
            default:
                ch = '%';
        }
        return ch;
    }
    /**
     * ??????????????????
     * @param input ??????????????????
     * @return ???????????????
     */
    public static String filterInput(String input) {
        List<String> list = new ArrayList<String>();

        list.add("<");
        list.add(">");
        list.add("(");
        list.add(")");
        list.add("&");
        list.add("?");
        list.add(";");

        String encode = Normalizer.normalize(input, Normalizer.Form.NFKC);

        for (int i=0;i<list.size();i++) {
            encode = encode.replace(list.get(i), "");
        }

        return encode;
    }

    /**
     * ??????????????????????????????
     * @param str ?????????????????????
     * @return ?????????????????????
     */
    public static String filterStr(String str){
        String resValue = scriptPattern.matcher(str).replaceAll("");
        resValue = singleScriptPattern.matcher(resValue).replaceAll("");
        resValue = singleScriptPattern2.matcher(resValue).replaceAll("");
        resValue = evalScriptPattern.matcher(resValue).replaceAll("");
        resValue = onloadScriptPattern.matcher(resValue).replaceAll("");
        resValue = onmousScriptPattern.matcher(resValue).replaceAll("");
        resValue = alertScriptPattern.matcher(resValue).replaceAll("");
        resValue = iframeScriptPattern.matcher(resValue).replaceAll("");
        resValue = aTagPattern.matcher(resValue).replaceAll("");
        resValue = otherScriptPattern.matcher(resValue).replaceAll("");
        resValue = fontfamilyPattern.matcher(resValue).replaceAll("");
        Pattern pa = Pattern.compile("[`~!@$%^&*()\\+\\=\\{}|:\"?><??????\\/\\r\\n]");//[`~!@$%^&*()\+\=\{}|:"?><??????\/r\/n]
        Matcher ma = pa.matcher(resValue);
        if(ma.find()){
            resValue = ma.replaceAll("");
        }
        return resValue;
    }

    /**
     * ??????Map???value???
     * @param param ????????????map
     * @return ????????????
     */
    public static Map<String,String> filterMapValue(Map<String,String> param){
        Map<String,String> res = new HashMap<>();
        for (Map.Entry<String,String> keyValue:param.entrySet()){
            res.put(keyValue.getKey(),filterStr(keyValue.getValue()));
        }
        return res;
    }

    /**
     * ??????map???value???
     * @param param ????????????map
     * @return ????????????
     */
    public static Map<String,Object> filterMapValueObject(Map<String,Object> param){
        Map<String,Object> res = new HashMap<>();
        for (Map.Entry<String,Object> keyValue: param.entrySet()){
            if (keyValue.getValue() instanceof String){
                String value = StringUtil.toString(keyValue.getValue());
                res.put(keyValue.getKey(),filterStr(value));
            }else {
                res.put(keyValue.getKey(),keyValue.getValue());
            }
        }
        return res;
    }

    /**
     * ??????Map???list?????????????????????
     * @param param ????????????map??????
     * @return ????????????
     */
    public static List<Map<String,String>> filterListMap(List<Map<String,String>> param){
        if (param!=null&&param.size()>=1){
            List<Map<String,String>> res = new ArrayList<>();
            for (int i = 0;i<param.size();i++){
                Map<String,String> map = filterMapValue(param.get(i));
                res.add(map);
            }
            return res;
        }
        return param;
    }

    public static List<Map<String,Object>> filterListMapObj(List<Map<String,Object>> param){
        if (param != null&& param.size()>=1){
            List<Map<String,Object>> res = new ArrayList<>();
            for (int i = 0;i<param.size();i++){
                Map<String,Object> map = filterMapValueObject(param.get(i));
                res.add(map);
            }
            return res;
        }
        return param;
    }
    /**
     * ??????List<entity>list????????????????????????
     * @param param ?????????list??????
     * @throws Exception ??????
     */
    public static void filterListObject(List param) throws Exception{
        if (param != null&&param.size()>=1){
            for (int i = 0; i < param.size();i++){
                Field[] fields = param.get(i).getClass().getDeclaredFields();
                for (int j = 0;j<fields.length;j++){
                    // ???????????????????????????
                    String name = fields[j].getName().replaceFirst(fields[j].getName().substring(0, 1)
                            , fields[j].getName().substring(0, 1).toUpperCase());
                    // ??????????????????
                    String type = fields[j].getGenericType().toString();
                    if (type.equals("class java.lang.String")){
                        // ??????type??????????????????????????????"class "??????????????????
                        //?????????????????????getter???setter??????
                        Method[] methods = param.get(i).getClass().getMethods();//???????????????public??????
                        if (hasMethod(methods,"get"+name)&&hasMethod(methods,"set"+name)){
                            Method m = param.get(i).getClass().getMethod("get" + name);
                            // ??????getter?????????????????????
                            String value = (String) m.invoke(param.get(i));
                            //??????setter??????????????????
                            if (value != null) {
                                m = param.get(i).getClass().getMethod("set" + name, String.class);
                                m.invoke(param.get(i), filterStr(value));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     *?????????????????????
     * @param name ???????????????????????????
     * @return true ????????????
     */
    public static boolean checkForDatabase(String name) {
        return name == null || name.matches("^[a-zA-Z][a-zA-Z0-9_]*$");
    }

    /**
     * ????????????????????????????????????????????????
     * @param methods ????????????
     * @param name ?????????
     * @return ???????????????true????????????????????????????????????false
     */
    private static boolean hasMethod(Method[] methods,String name){
        if (methods==null||methods.length==0){
            return false;
        }
        for (int i =0; i < methods.length;i++){
            if (methods[i].getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    /**
     * ???????????????????????????????????????
     * @param param ???????????????????????????
     * @return ???????????????
     */
    public static String[] filterStrArr(String[] param){
        if (param == null){
            return null;
        }
        if (param.length >0){
            for (int i =0;i < param.length;i++){
                param[i] = filterStr(param[i]);
            }
        }
        return param;
    }

    /**
     * ???????????????????????????????????????
     * @param param ???????????????????????????
     * @return ???????????????
     */
    public static List<String> filterStrList(List<String> param){
        if (param==null||param.size()==0){
            return param;
        }
        List<String> res = new ArrayList<>();
        for (int i =0;i<(param.size()>MAX_RESULT_SIZE?MAX_RESULT_SIZE:param.size());i++){
            res.add(filterStr(param.get(i)));
        }
        return res;
    }

    /**
     * ????????????????????????????????????????????????
     * @param str ?????????
     * @param ch ??????
     * @return ?????????????????????
     */
    public static int getCharNumber(String str,String ch){
        if (str == null || str.length()==0 || ch.indexOf(ch)<0){
            return -1;
        }
        int count = 0;
        int index = 0;
        do{
            index = str.indexOf(ch,index);
            if (index>-1){
                count++;
                index = index+ch.length();
            }
        }while (index>-1);
        return count;
    }

    /**
     * uuid?????????????????????????????????%
     * @param aChar ??????????????????
     * @return ????????????
     */
    private static char cleanUUidChar(char aChar) {
        // 0 - 9
        for (int i = 48; i < 58; ++i) {
            if (aChar == i) {
                return (char) i;
            }
        }
        // 'A' - 'Z'
        for (int i = 65; i < 91; ++i) {
            if (aChar == i) {
                return (char) i;
            }
        }
        // 'a' - 'z'
        for (int i = 97; i < 123; ++i) {
            if (aChar == i) {
                return (char) i;
            }
        }
        if (aChar == '-'){
            return '-';
        }else if (aChar == '_'){
            return '_';
        }
        return '%';
    }

    /**
     * ??????uuid??????????????????
     * @param uuid ?????????????????????
     * @return ????????????
     */
    public static String filterUuid(String uuid){
        if (uuid == null) {
            return null;
        }
        StringBuilder cleanString = new StringBuilder();
        for (int i = 0; i < uuid.length(); i++) {
            cleanString.append(cleanUUidChar(uuid.charAt(i)));
        }
        return cleanString.toString();
    }

    public static List<String> filterListUuid(List<String> strList){
        List<String> res=new ArrayList<>();
        for (String oneString:strList){
            String newString=filterUuid(oneString);
            res.add(newString);
        }
        return res;
    }
    /**
     * ???????????????????????????
     * @param param ??????????????????
     * @return ????????????
     */
    public static String filterChAndEn3(String param){
        if (param == null) {
            return null;
        }
        StringBuilder cleanString = new StringBuilder();
        for (int i = 0; i < param.length(); ++i) {
            cleanString.append(cleanChAndEnChar3(param.charAt(i)));
        }
        return cleanString.toString();
    }
    private static char cleanChAndEnChar3(char aChar) {
        // 0 - 9
        for (int i = 48; i < 58; ++i) {
            if (aChar == i) {
                return (char) i;
            }
        }
        // 'A' - 'Z'
        for (int i = 65; i < 91; ++i) {
            if (aChar == i) {
                return (char) i;
            }
        }
        // 'a' - 'z'
        for (int i = 97; i < 123; ++i) {
            if (aChar == i) {
                return (char) i;
            }
        }
        //chinese
        for (int i=19968;i<=40869;i++){
            if(aChar==i){
                return (char) i;
            }
        }
        char ch;
        // other valid characters
        switch (aChar) {
            /*????????????*/
            case '(':
                ch = '(';
                break;
            case ')':
                ch =  ')';
                break;
            case '???':
                ch =  '???';
                break;
            case '???':
                ch = '???';
                break;
            case ':':
                ch = ':';
                break;
            case '???':
                ch = '???';
                break;
            case ' ':
                ch = ' ';
                break;
            case '_':
                ch = '_';
                break;
            case '-':
                ch = '-';
                break;
            case ',':
                ch = ',';
                break;
            /*????????????*/
            case '~':
                ch = '~';
                break;
            case '`':
                ch = '`';
                break;
            case '!':
                ch = '!';
                break;
            case '@':
                ch = '@';
                break;
            case '#':
                ch = '#';
                break;
            case '$':
                ch = '$';
                break;
            case '%':
                ch = '%';
                break;
            case '^':
                ch = '^';
                break;
            case '&':
                ch = '&';
                break;
            case '*':
                ch = '*';
                break;
            case '+':
                ch = '+';
                break;
            case '=':
                ch = '=';
                break;
            case '{':
                ch = '{';
                break;
            case '}':
                ch = '}';
                break;
            case '[':
                ch = '[';
                break;
            case ']':
                ch = ']';
                break;
            case '|':
                ch = '|';
                break;
            case '\\':
                ch = '\\';
                break;
            case ';':
                ch = ';';
                break;
            case '"':
                ch = '"';
                break;
            case '\'':
                ch = '\'';
                break;
            case '<':
                ch = '<';
                break;
            case '>':
                ch = '>';
                break;
            case '.':
                ch = '.';
                break;
            case '?':
                ch = '?';
                break;
            case '/':
                ch = '/';
                break;
            /*????????????*/
            case '??':
                ch = '??';
                break;
            case '???':
                ch = '???';
                break;
            case '???':
                ch = '???';
                break;
            case '???':
                ch = '???';
                break;
            case '???':
                ch = '???';
                break;
            case '???':
                ch = '???';
                break;
            case '???':
                ch = '???';
                break;
            case '???':
                ch = '???';
                break;
            case '???':
                ch = '???';
                break;
            case '???':
                ch = '???';
                break;
            case '???':
                ch = '???';
                break;
            case '???':
                ch = '???';
                break;
            case '???':
                ch = '???';
                break;
            case '???':
                ch = '???';
                break;
            case '???':
                ch = '???';
                break;
            case '???':
                ch = '???';
                break;
            case '???':
                ch = '???';
                break;
            default:
                ch = '_';
        }
        return ch;
    }
    /**
     * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * @param param ??????????????????
     * @return ????????????
     */
    public static String filterChAndEn2(String param){
        if (param == null) {
            return null;
        }
        StringBuilder cleanString = new StringBuilder();
        for (int i = 0; i < param.length(); ++i) {
            cleanString.append(cleanChAndEnChar2(param.charAt(i)));
        }
        return cleanString.toString();
    }

    private static char cleanChAndEnChar2(char aChar) {
        // 0 - 9
        for (int i = 48; i < 58; ++i) {
            if (aChar == i) {
                return (char) i;
            }
        }
        // 'A' - 'Z'
        for (int i = 65; i < 91; ++i) {
            if (aChar == i) {
                return (char) i;
            }
        }
        // 'a' - 'z'
        for (int i = 97; i < 123; ++i) {
            if (aChar == i) {
                return (char) i;
            }
        }
        //chinese
        for (int i=19968;i<=40869;i++){
            if(aChar==i){
                return (char) i;
            }
        }
        char ch;
        // other valid characters
        switch (aChar) {
            case '(':
                ch = '(';
                break;
            case ')':
                ch =  ')';
                break;
            case '???':
                ch =  '???';
                break;
            case '???':
                ch = '???';
                break;
            case ':':
                ch = ':';
                break;
            case '???':
                ch = '???';
                break;
            case ' ':
                ch = ' ';
                break;
            case '_':
                ch = '_';
                break;
            case '-':
                ch = '-';
                break;
            default:
                ch = '_';
        }
        return ch;
    }
    public static List<String> filterChAndEnList(List<String> param){
        List<String> res=new ArrayList<>();
        for (String oneString:param){
            String newString=filterChAndEn3(oneString);
            res.add(newString);
        }
        return res;
    }
    public static int filterChAndEnInt(int param){
        return Integer.parseInt(filterChAndEn3(Integer.toString(param)));
    }
    public static Map<String,Object> filterChAndEnMapObject(Map<String,Object> param){
        Map<String,Object> res = new HashMap<>();
        for (Map.Entry<String,Object> keyValue: param.entrySet()){
            String value = StringUtil.toString(keyValue.getValue());
            res.put(keyValue.getKey(),filterChAndEn3(value));
        }
        return res;
    }
    public static Map<String,String> filterChAndEnMapString(Map<String,String> param){
        Map<String,String> res = new HashMap<>();
        for (Map.Entry<String,String> keyValue:param.entrySet()){
            res.put(keyValue.getKey(),filterChAndEn3(StringUtil.toString(keyValue.getValue())));
        }
        return res;
    }
    public static List<Map<String,String>> filterChAndEnListMapString(List<Map<String,String>> param){
        if (param!=null&&param.size()>=1){
            List<Map<String,String>> res = new ArrayList<>();
            for (int i = 0;i<param.size();i++){
                Map<String,String> map = filterChAndEnMapString(param.get(i));
                res.add(map);
            }
            return res;
        }
        return param;
    }
    public static List<Map<String,Object>> filterChAndEnListMapObject(List<Map<String,Object>> param){
        if (param != null&& param.size()>=1){
            List<Map<String,Object>> res = new ArrayList<>();
            for (int i = 0;i<param.size();i++){
                Map<String,Object> map = filterChAndEnMapObject(param.get(i));
                res.add(map);
            }
            return res;
        }
        return param;
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????
     * @param str ??????????????????
     * @return ????????????
     */
    public static String filterChAndEn(String str){
        if (str == null) {
            return null;
        }
        StringBuilder cleanString = new StringBuilder();
        for (int i=0; i < str.length();i++){
            if (isChineseChar(str.charAt(i))){
                //??????
                cleanString.append(str.charAt(i));
            }else if (Character.isLetterOrDigit(str.charAt(i))){
                //?????????????????????
                cleanString.append(str.charAt(i));
            }else if ('('==str.charAt(i)){
                cleanString.append('(');
            }else if (')'==str.charAt(i)){
                cleanString.append(')');
            }else if ('???'==str.charAt(i)){
                cleanString.append('???');
            }else if ('???'==str.charAt(i)){
                cleanString.append('???');
            }else if ('_'==str.charAt(i)){
                cleanString.append('_');
            }
        }
        return cleanString.toString();
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????%
     * ??????????????????????????????????????????_??????????????????
     * @param chr ?????????????????????
     * @return ????????????
     */
    private static char cleanChAndEn(char chr){
        char ch = '%';//?????????????????????????????????%
        if (isChineseChar(chr)){
            //??????
            ch = chr;
        }else if (Character.isLetterOrDigit(chr)){
            //?????????????????????
            ch = chr;
        }else if ('('==chr){
            ch = '(';
        }else if (')'==chr){
            ch = ')';
        }else if ('???'==chr){
            ch = '???';
        }else if ('???'==chr){
            ch = '???';
        }else if ('_'==chr){
            ch = '_';
        }
        return ch;
    }

    /**
     * ?????????????????????????????????
     * PS?????????????????????????????????[\u4e00-\u9fa5]
     * @param ch ?????????????????????
     * @return ?????????(true), ????????????(false)
     */
    private static boolean isChineseChar(char ch) {
        return String.valueOf(ch).matches("[\u4e00-\u9fa5]");
    }
}

