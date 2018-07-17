package org.hummer.core.codec;

import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AESHelper {
    private static final String CipherMode = "AES/ECB/PKCS5Padding";

    public AESHelper() {
    }

    private static SecretKeySpec createKey(String password) {
        byte[] data = null;
        if (password == null) {
            password = "";
        }

        StringBuffer sb = new StringBuffer(32);
        sb.append(password);

        while (sb.length() < 32) {
            sb.append("0");
        }

        if (sb.length() > 32) {
            sb.setLength(32);
        }

        try {
            data = sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var4) {
            var4.printStackTrace();
        }

        return new SecretKeySpec(data, "AES");
    }

    public static byte[] encrypt(byte[] content, String password) {
        try {
            SecretKeySpec key = createKey(password);
            System.out.println(key);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(1, key);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public static String encrypt(String content, String password) {
        byte[] data = null;

        try {
            data = content.getBytes("UTF-8");
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        data = encrypt(data, password);
        String result = byte2hex(data);
        return result;
    }

    public static byte[] decrypt(byte[] content, String password) {
        try {
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(2, key);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String content, String password) {
        byte[] data = null;

        try {
            data = hex2byte(content);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        data = decrypt(data, password);
        if (data == null) {
            return null;
        } else {
            String result = null;

            try {
                result = new String(data, "UTF-8");
            } catch (UnsupportedEncodingException var5) {
                var5.printStackTrace();
            }

            return result;
        }
    }

    public static String byte2hex(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        String tmp = "";

        for (int n = 0; n < b.length; ++n) {
            tmp = Integer.toHexString(b[n] & 255);
            if (tmp.length() == 1) {
                sb.append("0");
            }

            sb.append(tmp);
        }

        return sb.toString().toUpperCase();
    }

    private static byte[] hex2byte(String inputString) {
        if (inputString != null && inputString.length() >= 2) {
            inputString = inputString.toLowerCase();
            int l = inputString.length() / 2;
            byte[] result = new byte[l];

            for (int i = 0; i < l; ++i) {
                String tmp = inputString.substring(2 * i, 2 * i + 2);
                result[i] = (byte) (Integer.parseInt(tmp, 16) & 255);
            }

            return result;
        } else {
            return new byte[0];
        }
    }

    public static File encryptFile(MultipartFile sourceFile, String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        File encrypfile = null;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            inputStream = sourceFile.getInputStream();
            String fileName = sourceFile.getOriginalFilename();
            String prefix = fileName.substring(fileName.lastIndexOf("."));
            String sfix = fileName.substring(fileName.lastIndexOf(".") + 1);
            encrypfile = File.createTempFile(prefix, "." + sfix);
            outputStream = new FileOutputStream(encrypfile);
            SecretKeySpec key = createKey(password);
            System.out.println(key);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(1, key);
            CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
            byte[] cache = new byte[1024];
            boolean var12 = false;

            int nRead;
            while ((nRead = cipherInputStream.read(cache)) != -1) {
                outputStream.write(cache, 0, nRead);
                outputStream.flush();
            }

            cipherInputStream.close();
        } catch (FileNotFoundException var28) {
            var28.printStackTrace();
        } catch (IOException var29) {
            var29.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException var27) {
                var27.printStackTrace();
            }

            try {
                outputStream.close();
            } catch (IOException var26) {
                var26.printStackTrace();
            }

        }

        return encrypfile;
    }

    public File decryptFile(File sourceFile, String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        File decryptFile = null;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            String fileName = sourceFile.getName();
            String prefix = fileName.substring(fileName.lastIndexOf("."));
            String sfix = fileName.substring(fileName.lastIndexOf(".") + 1);
            decryptFile = File.createTempFile(prefix, sfix);
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(2, key);
            inputStream = new FileInputStream(sourceFile);
            outputStream = new FileOutputStream(decryptFile);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
            byte[] buffer = new byte[1024];

            int r;
            while ((r = inputStream.read(buffer)) >= 0) {
                cipherOutputStream.write(buffer, 0, r);
            }

            cipherOutputStream.close();
        } catch (IOException var26) {
            var26.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException var25) {
                var25.printStackTrace();
            }

            try {
                outputStream.close();
            } catch (IOException var24) {
                var24.printStackTrace();
            }

        }

        return decryptFile;
    }
}