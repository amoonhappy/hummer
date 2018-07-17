package org.hummer.core.codec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Coder {
    private static final Log LOG = LogFactory.getLog(Coder.class);

    public Coder() {
    }

    public static String enc(String src, String mode) {
        Coder.EM encryptMode = Coder.EM.valueOf(mode);
        ICodec coder = encryptMode.getCoder();

        try {
            return coder.encrypt(src);
        } catch (Exception var5) {
            LOG.error(var5.getMessage(), var5);
            return null;
        }
    }

    public static String dec(String src, String mode) {
        Coder.EM encryptMode = Coder.EM.valueOf(mode);
        ICodec coder = encryptMode.getCoder();

        try {
            return coder.decrypt(src);
        } catch (Exception var5) {
            LOG.error(var5.getMessage(), var5);
            return null;
        }
    }

    private static enum EM {
        AEM("AEM"),
        REM("REM"),
        DEM("DEM"),
        RSA("RSA");

        private String mode;

        private EM(String mode) {
            this.mode = mode;
        }

        public ICodec getCoder() {
            if ("AEM".equals(this.mode)) {
                return AES.getInstance();
            } else if ("RSA".equals(this.mode)) {
                return new RSA().getInstance();
            } else {
                return AES.getInstance();
            }
        }
    }
}
