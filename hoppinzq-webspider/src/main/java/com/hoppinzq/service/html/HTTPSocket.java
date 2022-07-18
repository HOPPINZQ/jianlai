package com.hoppinzq.service.html;


import com.hoppinzq.service.log.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * @author: zq
 */
public class HTTPSocket extends HTTP {
    public HTTPSocket() {
    }

    public void writeString(OutputStream outputStream, String str) throws IOException {
        outputStream.write(str.getBytes());
        outputStream.write("\r\n".getBytes());
        Log.log(2, "Socket Out:" + str);
    }

    public synchronized void lowLevelSend(String httpLink, String type) throws UnknownHostException, IOException {
        byte[] bytes = new byte[1024];
        int port = 80;
        boolean isC = false;
        Socket socket = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;

        try {
            URL url;
            if (httpLink.toLowerCase().startsWith("https")) {
                httpLink = "http" + httpLink.substring(5);
                url = new URL(httpLink);
                if (url.getPort() == -1) {
                    port = 443;
                }

                isC = true;
            } else {
                url = new URL(httpLink);
            }

            if (url.getPort() != -1) {
                port = url.getPort();
            }

            if (isC) {
                socket = SSL.getSSLSocket(url.getHost(), port);
            } else {
                socket = new Socket(url.getHost(), port);
            }

            socket.setSoTimeout(super._timeout);
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            String method;
            if (type == null) {
                method = "GET ";
            } else {
                method = "POST ";
            }

            String file = url.getFile();
            if (file.length() == 0) {
                file = "/";
            }

            method = method + file + " HTTP/1.0";
            this.writeString(outputStream, method);
            if (type != null) {
                this.writeString(outputStream, "Content-Length: " + type.length());
            }

            this.writeString(outputStream, "Host: " + url.getHost());
            int i = 0;
            StringBuffer stringBuffer = new StringBuffer();

            Attribute attribute;
            do {
                attribute = super._clientHeaders.get(i++);
                if (attribute != null) {
                    stringBuffer.append(attribute.getName());
                    stringBuffer.append(':');
                    stringBuffer.append(attribute.getValue());
                    stringBuffer.append("\r\n");
                    Log.log(2, "Client Header:" + attribute.getName() + "=" + attribute.getValue());
                }
            } while(attribute != null);

            if (stringBuffer.length() >= 0) {
                outputStream.write(stringBuffer.toString().getBytes());
            }

            this.writeString(outputStream, "");
            if (type != null) {
                Log.log(2, "Socket Post(" + type.length() + " bytes):" + new String(type));
                outputStream.write(type.getBytes());
            }

            super._header.setLength(0);
            int i1 = 0;

            int i2;
            for(boolean b = false; !b; super._header.append((char)i2)) {
                i2 = inputStream.read();
                if (i2 == -1) {
                    b = true;
                }

                switch(i2) {
                    case 10:
                        if (i1 == 0) {
                            b = true;
                        }

                        i1 = 0;
                    case 13:
                        break;
                    default:
                        ++i1;
                }
            }

            this.parseHeaders();
            Attribute attribute1 = super._serverHeaders.get("Content-length");
            int i3 = 0;

            try {
                if (attribute1 != null) {
                    i3 = Integer.parseInt(attribute1.getValue());
                }
            } catch (Exception e) {
                Log.logException("Bad value for content-length:", e);
            }

            super._body.setLength(0);
            int size = super._maxBodySize;
            int i4;
            if (i3 != 0) {
                while(i3-- > 0) {
                    i4 = inputStream.read(bytes);
                    if (i4 < 0) {
                        break;
                    }

                    super._body.append(new String(bytes, 0, i4, "8859_1"));
                    --size;
                    if (size < 0 && super._maxBodySize != -1) {
                        break;
                    }
                }
            } else {
                do {
                    i4 = inputStream.read(bytes);
                    if (i4 < 0) {
                        break;
                    }

                    super._body.append(new String(bytes, 0, i4, "8859_1"));
                    --size;
                } while((size >= 0 || super._maxBodySize == -1) && i4 != 0);
            }

            Log.log(1, "Socket Page Back:" + super._body + "\r\n");
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception exception) {
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception exception) {
                }
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception exception) {
                }
            }

        }
    }

    HTTP copy() {
        return new HTTPSocket();
    }
}

