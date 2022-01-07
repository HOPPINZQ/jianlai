package com.hoppinzq.service.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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
}
