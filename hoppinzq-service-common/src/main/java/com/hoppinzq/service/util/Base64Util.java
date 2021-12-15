package com.hoppinzq.service.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class Base64Util {

    /**
     * base64转inputStream
     * @param base64string
     * @return
     */
    public static InputStream baseToInputStream(String base64string){
        ByteArrayInputStream stream = null;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bytes1 = decoder.decodeBuffer(base64string);
            stream = new ByteArrayInputStream(bytes1);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return stream;
    }

    /**
     * base64转inputStream
     * @param base64byte
     * @return
     */
    public static InputStream baseToInputStream(final byte[] base64byte){
        ByteArrayInputStream stream = null;
        try {
            byte[] bytes1 = Base64.decodeBase64(base64byte);
            stream = new ByteArrayInputStream(bytes1);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return stream;
    }

    public static String inputStreamToBase(InputStream inputStream) throws IOException {

        byte[] bytes = IOUtils.toByteArray(inputStream);
        String encoded = java.util.Base64.getEncoder().encodeToString(bytes);
        return encoded;
    }

//    // JUnit Test
//    public static void main(String[] args) throws Exception{
//        String s = "JTNDYmxvY2txdW90ZSUzRSUwQSUzQ3AlM0UlRTUlOUMlQTglRTglQkYlOTklRTklODclOEMlRTUlODYlOTklRTUlOEQlOUElRTUlQUUlQTIlRTglQUYlQjclRTklOTclQUUlRTklQTUlQkYlRTYlODglOTElRTUlOEUlQkIlM0MlMkZwJTNFJTBBJTNDJTJGYmxvY2txdW90ZSUzRSUwQQ==";
//        //System.out.println("The base64 encode string value is " + base64Encode(s));
//        System.err.println("The base64 decode string value is " + base64Decode(s));
//        String str = URLEncoder.encode("中国","utf-8");
//        System.out.println(str);
////解码
//        String str1= URLDecoder.decode(base64Decode(s), "UTF-8");
//        System.err.println(str1);
//    }

    // base64编码
    public static String base64Encode(String token) {
        byte[] encodedBytes = java.util.Base64.getEncoder().encode(token.getBytes());
        return new String(encodedBytes,java.nio.charset.Charset.forName("UTF-8"));
    }
    // base64解码
    public static String base64Decode(String token){
        byte[] decodedBytes = java.util.Base64.getDecoder().decode(token.getBytes());
        return new String(decodedBytes, java.nio.charset.Charset.forName("UTF-8"));
    }

}
