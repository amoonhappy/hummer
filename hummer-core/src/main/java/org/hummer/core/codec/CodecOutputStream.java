package org.hummer.core.codec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class CodecOutputStream extends ServletOutputStream {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private final boolean doEncode;
    private final String mode;
    private final byte[] singleByte = new byte[1];
    private HttpServletResponse response;
    private OutputStream out;
    protected byte[] buf;
    protected int count;
    private static final int MAX_ARRAY_SIZE = 2147483639;

    public CodecOutputStream(HttpServletResponse response, OutputStream out, String mode, boolean doEncode) {
        this.response = response;
        this.out = out;
        this.mode = mode;
        this.doEncode = doEncode;
        this.buf = new byte[32];
    }

    public void write(int b) throws IOException {
        this.singleByte[0] = (byte) b;
        this.write(this.singleByte, 0, 1);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (off >= 0 && off <= b.length && len >= 0 && off + len - b.length <= 0) {
            this.ensureCapacity(this.count + len);
            System.arraycopy(b, off, this.buf, this.count, len);
            this.count += len;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity - this.buf.length > 0) {
            this.grow(minCapacity);
        }

    }

    private void grow(int minCapacity) {
        int oldCapacity = this.buf.length;
        int newCapacity = oldCapacity << 1;
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }

        if (newCapacity - 2147483639 > 0) {
            newCapacity = hugeCapacity(minCapacity);
        }

        this.buf = Arrays.copyOf(this.buf, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) {
            throw new OutOfMemoryError();
        } else {
            return minCapacity > 2147483639 ? 2147483647 : 2147483639;
        }
    }

    public void flush() throws IOException {
        byte[] b = new byte[this.count];
        System.arraycopy(this.buf, 0, b, 0, this.count);
        String src = new String(b);
        String content;
        if (this.doEncode) {
            this.logger.debug("编码前的Body为：" + src);
            content = Coder.enc(src, this.mode);
        } else {
            this.logger.debug("解码前的Body为：" + src);
            content = Coder.dec(src, this.mode);
        }

        byte[] buf = content.getBytes();
        int len = buf.length;
        this.response.setContentLength(len);
        this.out.write(buf, 0, len);
        this.out.flush();
    }

    public void close() throws IOException {
        this.flush();
        this.out.close();
    }

    public boolean isReady() {
        return false;
    }

    public void setWriteListener(WriteListener writeListener) {
    }
}
