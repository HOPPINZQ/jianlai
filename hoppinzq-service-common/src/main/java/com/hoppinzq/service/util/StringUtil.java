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
        //尝试精确到秒的转换
        try{
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time=dateFormat.parse(toString(object)).getTime();
        }catch (Exception e){
            //不能转换，返回null
        }
        //尝试精确到天的转换
        if (time==null){
            try {
                SimpleDateFormat dateFormat2=new SimpleDateFormat("yyyy-MM-dd");
                time=dateFormat2.parse(toString(object)).getTime();
            }catch (Exception e){
                //不能转换，返回null
            }
        }
        return time;
    }

    public static Integer toInt(Object object){
        Integer result=null;
        try {
            result=Integer.valueOf(toString(object));
        }catch (Exception e){
            //不能转换（object为null，为“”，为非法值），返回null
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

    //生成32位的UUID
    public static String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    //验证用户输入是否合法  返回true说明通过校验
    public static boolean verifyParam(String param) {
        if (param == null || "".equals(param)) {
            return true;
        }
        String regEx = "[`~!@$%^&*+=|{}';',\\[\\].<>?~！@#￥%……&*+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(param);
        return !m.find();
    }

    /**
     * 判断一个字符串是否有值，空格也不算有值
     * @param str 字符串
     * @return boolean
     */
    public static boolean availableStr(String str){
        return str != null && !"".equals(str) && !"".equals(str.trim());
    }

    /**
     * 验证文件名是否正确
     * @param param 文件名
     * @return 正确返回true
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
     * 验证没有后缀名的文件名是否正确
     * @param param 文件名
     * @return 正确返回true
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
     * 验证文件路径是否正确
     * @param param 文件路径
     * @return 正确返回true
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
     * 处理文件路径
     * @param path 文件路径
     * @return 处理过的文件路径
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
     * 过滤字符，白名单外的字符返回%
     * @param aChar 要过滤的字符
     * @return 返回字符
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
     * 输入输出处理
     * @param input 输入输出字符
     * @return 处理后结果
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
     * 过滤字符串的特殊字符
     * @param str 要过滤的字符串
     * @return 过滤后的字符串
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
        Pattern pa = Pattern.compile("[`~!@$%^&*()\\+\\=\\{}|:\"?><【】\\/\\r\\n]");//[`~!@$%^&*()\+\=\{}|:"?><【】\/r\/n]
        Matcher ma = pa.matcher(resValue);
        if(ma.find()){
            resValue = ma.replaceAll("");
        }
        return resValue;
    }

    /**
     * 过滤Map的value值
     * @param param 要过滤的map
     * @return 过滤结果
     */
    public static Map<String,String> filterMapValue(Map<String,String> param){
        Map<String,String> res = new HashMap<>();
        for (Map.Entry<String,String> keyValue:param.entrySet()){
            res.put(keyValue.getKey(),filterStr(keyValue.getValue()));
        }
        return res;
    }

    /**
     * 过滤map的value值
     * @param param 要过滤的map
     * @return 过滤结果
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
     * 过滤Map的list集合中的字符串
     * @param param 要过滤的map集合
     * @return 过滤结果
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
     * 过滤List<entity>list实体类中的字符串
     * @param param 实体类list集合
     * @throws Exception 异常
     */
    public static void filterListObject(List param) throws Exception{
        if (param != null&&param.size()>=1){
            for (int i = 0; i < param.size();i++){
                Field[] fields = param.get(i).getClass().getDeclaredFields();
                for (int j = 0;j<fields.length;j++){
                    // 将属性的首字母大写
                    String name = fields[j].getName().replaceFirst(fields[j].getName().substring(0, 1)
                            , fields[j].getName().substring(0, 1).toUpperCase());
                    // 获取属性类型
                    String type = fields[j].getGenericType().toString();
                    if (type.equals("class java.lang.String")){
                        // 如果type是类类型，则前面包含"class "，后面跟类名
                        //该属性有对应的getter和setter方法
                        Method[] methods = param.get(i).getClass().getMethods();//改类的所有public方法
                        if (hasMethod(methods,"get"+name)&&hasMethod(methods,"set"+name)){
                            Method m = param.get(i).getClass().getMethod("get" + name);
                            // 调用getter方法获取属性值
                            String value = (String) m.invoke(param.get(i));
                            //调用setter方法赋属性值
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
     *验证数据库名称
     * @param name 要验证的数据库名称
     * @return true 通过验证
     */
    public static boolean checkForDatabase(String name) {
        return name == null || name.matches("^[a-zA-Z][a-zA-Z0-9_]*$");
    }

    /**
     * 判断数组中是否有对应方法名的方法
     * @param methods 方法数组
     * @param name 方法名
     * @return 存在则返回true，不存在相应的方法则返回false
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
     * 过滤字符串数组中的特殊字符
     * @param param 要过滤的字符串数组
     * @return 过滤的结果
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
     * 过滤字符串集合中的特殊字符
     * @param param 要过滤的特殊字符串
     * @return 过滤的结果
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
     * 判断一个字符在字符串中出现的次数
     * @param str 字符串
     * @param ch 字符
     * @return 字符出现的次数
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
     * uuid白名单，白名单外的返回%
     * @param aChar 要判断的字符
     * @return 返回字符
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
     * 过滤uuid中的特殊字符
     * @param uuid 要过滤的字符串
     * @return 过滤结构
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
     * 几乎不过滤任何东西
     * @param param 要过滤的字符
     * @return 过滤结果
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
            /*常用字符*/
            case '(':
                ch = '(';
                break;
            case ')':
                ch =  ')';
                break;
            case '（':
                ch =  '（';
                break;
            case '）':
                ch = '）';
                break;
            case ':':
                ch = ':';
                break;
            case '：':
                ch = '：';
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
            /*英文字符*/
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
            /*中文字符*/
            case '·':
                ch = '·';
                break;
            case '！':
                ch = '！';
                break;
            case '￥':
                ch = '￥';
                break;
            case '—':
                ch = '—';
                break;
            case '【':
                ch = '【';
                break;
            case '】':
                ch = '】';
                break;
            case '、':
                ch = '、';
                break;
            case '；':
                ch = '；';
                break;
            case '“':
                ch = '“';
                break;
            case '”':
                ch = '”';
                break;
            case '‘':
                ch = '‘';
                break;
            case '’':
                ch = '’';
                break;
            case '《':
                ch = '《';
                break;
            case '》':
                ch = '》';
                break;
            case '，':
                ch = '，';
                break;
            case '。':
                ch = '。';
                break;
            case '？':
                ch = '？';
                break;
            default:
                ch = '_';
        }
        return ch;
    }
    /**
     * 过滤字符串中的特殊字符，仅保留汉字、英文字母和中英文的括号、下划线、中划线，其它字符变为下划线
     * @param param 要过滤的字符
     * @return 过滤结果
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
            case '（':
                ch =  '（';
                break;
            case '）':
                ch = '）';
                break;
            case ':':
                ch = ':';
                break;
            case '：':
                ch = '：';
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
     * 过滤字符串中的特殊字符，仅保留汉字、英文字母和中英文的括号
     * @param str 要过滤的字符
     * @return 过滤结果
     */
    public static String filterChAndEn(String str){
        if (str == null) {
            return null;
        }
        StringBuilder cleanString = new StringBuilder();
        for (int i=0; i < str.length();i++){
            if (isChineseChar(str.charAt(i))){
                //汉字
                cleanString.append(str.charAt(i));
            }else if (Character.isLetterOrDigit(str.charAt(i))){
                //英文字母和数字
                cleanString.append(str.charAt(i));
            }else if ('('==str.charAt(i)){
                cleanString.append('(');
            }else if (')'==str.charAt(i)){
                cleanString.append(')');
            }else if ('）'==str.charAt(i)){
                cleanString.append('）');
            }else if ('（'==str.charAt(i)){
                cleanString.append('（');
            }else if ('_'==str.charAt(i)){
                cleanString.append('_');
            }
        }
        return cleanString.toString();
    }

    /**
     * 判断字符是否在白名单内，是则返回字符本身，不是则返回%
     * 汉字、英文字符、中英文括号和_组成的白名单
     * @param chr 要做判断的字符
     * @return 返回字符
     */
    private static char cleanChAndEn(char chr){
        char ch = '%';//不在白名单内的字符返回%
        if (isChineseChar(chr)){
            //汉字
            ch = chr;
        }else if (Character.isLetterOrDigit(chr)){
            //英文字母和数字
            ch = chr;
        }else if ('('==chr){
            ch = '(';
        }else if (')'==chr){
            ch = ')';
        }else if ('）'==chr){
            ch = '）';
        }else if ('（'==chr){
            ch = '（';
        }else if ('_'==chr){
            ch = '_';
        }
        return ch;
    }

    /**
     * 判断一个字符是否是汉字
     * PS：中文汉字的编码范围：[\u4e00-\u9fa5]
     * @param ch 需要判断的字符
     * @return 是汉字(true), 不是汉字(false)
     */
    private static boolean isChineseChar(char ch) {
        return String.valueOf(ch).matches("[\u4e00-\u9fa5]");
    }
}

