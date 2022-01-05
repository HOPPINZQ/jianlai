package com.hoppinzq.service.interfaceService;

import java.io.IOException;
import java.io.InputStream;

public interface FileService {
    void sendLargeStream(InputStream in) throws IOException;
}
