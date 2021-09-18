package com.hoppinzq.service.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author:ZhangQi
 **/
public class JSONUtil {

    private JSONUtil() {
    }

    /**
     * 得到格式化json数据
     */
    public static String format(String jsonStr) {
        int level = 0;
        StringBuffer jsonForMatStr = new StringBuffer();
        for (int i = 0; i < jsonStr.length(); i++) {
            char c = jsonStr.charAt(i);
            if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
            }
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c + "\n");
                    level++;
                    break;
                case ',':
                    char d = jsonStr.charAt(i - 1);
                    char d1=' ';
                    if(i<jsonStr.length()-1){
                        d1=jsonStr.charAt(i + 1);
                    }
                    if (d == '"' || d == ']' || ((d >= '0' && d <= '9')&&(d1 == '"'))) {
                        jsonForMatStr.append(c + "\n");
                    } else {
                        jsonForMatStr.append(c);
                    }
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }
        return jsonForMatStr.toString();
    }

    private static String getLevelStr(int level) {
        StringBuffer levelStr = new StringBuffer();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("\t");
        }
        return levelStr.toString();
    }


    public static final ObjectMapper JSON_MAPPER = newObjectMapper(), JSON_MAPPER_WEB = newObjectMapper();

    private static ObjectMapper newObjectMapper() {
        ObjectMapper result = new ObjectMapper();
        result.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        result.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        result.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        result.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);	//不输出value=null的属性
        result.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        result.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

        return result;
    }

    public static ObjectMapper getObjectMapper() {
        return JSON_MAPPER;
    }

    public static String writeValueAsString(Object value) {
        try {
            return value == null ? null : JSON_MAPPER.writeValueAsString(value);
        } catch (IOException e) {
            throw new IllegalArgumentException(e); // TIP: 原则上，不对异常包装，这里为什么要包装？因为正常情况不会发生IOException
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object value) throws IllegalArgumentException {
        return convertValue(value, Map.class);
    }

    public static <T> T convertValue(Object value, Class<T> clazz) throws IllegalArgumentException {
        if (StringUtils.isEmpty(value)){
            return null;
        }
        try {
            if (value instanceof String) {
                value = JSON_MAPPER.readTree((String) value);
            }
            return JSON_MAPPER.convertValue(value, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

	public static void main(String[] args) {
		Map<String, String> data = new HashMap<>();
		data.put("aa", "11");
		data.put("bb", "22");
		data.put("cc", "33");
		System.err.println(transMapToString(data));
		StringBuffer retStr = new StringBuffer("https://www.baidu.com");
		StringBuffer sb = new StringBuffer();
		retStr.append("?");
		if (data != null) {
			Iterator i = data.entrySet().iterator();

			while (i.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry) i.next();
				sb.append("&").append((String) entry.getKey()).append("=").append((String) entry.getValue());
			}
			String param = sb.substring(1);
			retStr.append(param);
		}

		System.out.println(retStr);//https://www.baidu.com?aa=11&bb=22&cc=33
	}



    /**
     * 返回值:String
     */
    public static String transMapToString(Map map){
        java.util.Map.Entry entry;
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        for(Iterator iterator = map.entrySet().iterator(); iterator.hasNext();)
        {
            entry = (java.util.Map.Entry)iterator.next();
            sb.append(entry.getKey().toString()).append( ":" ).append(null==entry.getValue()?"":
                    entry.getValue().toString()).append (iterator.hasNext() ? "," : "");
        }
        sb.append("}");
        return sb.toString();
    }


}
