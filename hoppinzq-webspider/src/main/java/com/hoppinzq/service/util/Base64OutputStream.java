package com.hoppinzq.service.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author: zq
 */
public class Base64OutputStream extends FilterOutputStream {
    private static char[] toBase64 = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private int _column = 0;
    private int _index = 0;
    private int[] _buffer = new int[3];

    public Base64OutputStream(OutputStream var1) {
        super(var1);
    }

    public void write(int var1) throws IOException {
        this._buffer[this._index] = var1;
        ++this._index;
        if (this._index == 3) {
            super.write(toBase64[(this._buffer[0] & 252) >> 2]);
            super.write(toBase64[(this._buffer[0] & 3) << 4 | (this._buffer[1] & 240) >> 4]);
            super.write(toBase64[(this._buffer[1] & 15) << 2 | (this._buffer[2] & 192) >> 6]);
            super.write(toBase64[this._buffer[2] & 63]);
            this._column += 4;
            this._index = 0;
            if (this._column >= 76) {
                super.write(10);
                this._column = 0;
            }
        }

    }

    public void flush() throws IOException {
        if (this._index == 1) {
            super.write(toBase64[(this._buffer[2] & 63) >> 2]);
            super.write(toBase64[(this._buffer[0] & 3) << 4]);
            super.write(61);
            super.write(61);
        } else if (this._index == 2) {
            super.write(toBase64[(this._buffer[0] & 252) >> 2]);
            super.write(toBase64[(this._buffer[0] & 3) << 4 | (this._buffer[1] & 240) >> 4]);
            super.write(toBase64[(this._buffer[1] & 15) << 2]);
            super.write(61);
        }

    }
}
