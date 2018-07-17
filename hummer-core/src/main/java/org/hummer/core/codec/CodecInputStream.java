package org.hummer.core.codec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;

public class CodecInputStream extends ServletInputStream {
    protected final Log logger = LogFactory.getLog(this.getClass());
    protected byte[] buf;
    protected int pos;
    protected int mark = 0;
    protected int count;

    public CodecInputStream(byte[] buf) {
        this.buf = buf;
        this.pos = 0;
        this.count = buf.length;
    }

    public boolean isFinished() {
        return this.pos < this.count;
    }

    public boolean isReady() {
        return true;
    }

    public int read() throws IOException {
        return this.pos < this.count ? this.buf[this.pos++] & 255 : -1;
    }

    public void setReadListener(ReadListener readListener) {
    }
}

