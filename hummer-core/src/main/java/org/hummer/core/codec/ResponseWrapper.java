package org.hummer.core.codec;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseWrapper extends HttpServletResponseWrapper {
    private String mode;
    private ServletOutputStream outputStream;

    public ResponseWrapper(HttpServletResponse response, String mode) {
        super(response);
        this.mode = mode;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (this.outputStream == null && !(this.outputStream instanceof CodecOutputStream)) {
            ServletOutputStream stream = super.getOutputStream();
            this.outputStream = new CodecOutputStream(this, stream, this.mode, true);
        }

        return this.outputStream;
    }

    public PrintWriter getWriter() throws IOException {
        if (this.outputStream == null && !(this.outputStream instanceof CodecOutputStream)) {
            ServletOutputStream stream = super.getOutputStream();
            this.outputStream = new CodecOutputStream(this, stream, this.mode, true);
        }

        return new PrintWriter(this.outputStream);
    }
}
