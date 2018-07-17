package org.hummer.core.codec;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES implements ICodec {
    private static String sKey = "hummer012345678";
    private static String ivParameter = "0392039203920300";
    private static final ICodec instance = new AES();

    private AES() {
    }

    public static ICodec getInstance() {
        return instance;
    }

    public String encrypt(String encData, String secretKey, String vector) throws Exception {
        if (secretKey == null) {
            return null;
        } else if (secretKey.length() != 16) {
            return null;
        } else {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] raw = secretKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec(vector.getBytes());
            cipher.init(1, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(encData.getBytes("utf-8"));
            return Base64.encodeBase64String(encrypted);
        }
    }

    public String encrypt(String src) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
        cipher.init(1, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(src.getBytes("utf-8"));
        return Base64.encodeBase64String(encrypted);
    }

    public String decrypt(String src) throws Exception {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(2, skeySpec, iv);
            byte[] encrypted1 = Base64.decodeBase64(src);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception var9) {
            return null;
        }
    }

    public String decrypt(String src, String key, String ivs) throws Exception {
        try {
            byte[] raw = key.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
            cipher.init(2, skeySpec, iv);
            byte[] encrypted1 = Base64.decodeBase64(src);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception var11) {
            return null;
        }
    }

    public String encodeBytes(byte[] bytes) {
        StringBuffer strBuf = new StringBuffer();

        for(int i = 0; i < bytes.length; ++i) {
            strBuf.append((char)((bytes[i] >> 4 & 15) + 97));
            strBuf.append((char)((bytes[i] & 15) + 97));
        }

        return strBuf.toString();
    }

    public static void main(String[] args) throws Exception {
        String s = getInstance().encrypt("");
        System.out.println("s:" + s);
        s = "WWbsbr4N6Q8GCgB0iuE6w9HDtfdtw4OE+8VHZYU4t2yKRXM+RY6n3d8/TytXoU3MN7R9aH7THuSWG5NUw9mPKqJkfBA47sQ6sQ8y/Fr4uoUO46dHwziC6LeN176fm3jjUePZUJ12WMuwgBTnzzxaMGE5vZcvlBFEmpf2ShxdK/rijgyZZcn1izYqLsZj02BxXKqIHaJ1pUsbVZKX/d+O/RaBy5zgVmIKHI5RNmKtPZb9cGxGstNtSs/XuHqbmnluqOnf/2SJafNySjsd2zcYdm4ixIPOr9S0M4aPRR8L0vFOVJvNC/jvNmj5EHbMni5urDos1hbOcoYgqljtQj9NK6cVReR4wBeSxT/rhM5eKOSZYPonQk1ZnfM2m+KPbErytYJKF02hh+qoPYcwxaFELe00f9Ll5FrkfzRP+F8tnOtI0UL7jxjlLtjP4OXU8uEzhVHpztHLZ56CGxzvPVtf5BdTIbjsyAxZrBGeO7YqGy4zyB/Ldiojc2m6NoBnKF1gRpVd2+g0I7H5UNKYX6q8kd3I1aW7NX0ZVjOtQopROoow/F1fOQ41m/RZvZKP1OLauus0FetqigYcsWqIB5JaP8eCO9xXxNc19hBhiyi2OF6cEAK8u4eDZxXYgpgCce06n6e0iYQJLDd6bKS2IUgx6LoJ7ejLbzIN6yI7jUejxzE0d0u9s348MjkApYbrp6wtCZK/H8z0l9NKEwnRo/oIqQ3HKSPlJMeshJ0RQSyLnv966akQ+D0yIOhao7F/EqboQPAA7sciJ2IUCUQWE+H0GQhIsxY05t2kZM4AMdhQC13kXN1K4O52kRDUX1FJVDuBRjCS2/sizTi5PmsFbUOrTetdAot8GxTY3XRsyN7wcJEkS2H9V4CkvwZ9Uk9KtmLowmA8RCM/6cg35Y6QnvDOhw/M6SDxBoKC5tXy0YnaHFaYju6Ux0lXOk53NbV9oS5eerq3vj92SrojkYarnPcqnizTE7mghSX7g5PW+6hDOGIMrASkH00JF+cNOUleIeBaA4H6g/7o/JBoK9FdJ0b3SDTj3p8eqXnrpoHOrXMwEzuoTTgi+ibJL8QusNpdBpZTgA4nJgNZ5v8Hnctd9L2vm6DCk5icfljUbRNd1hVMlw4kFop8hUKAgxPQuzD3ST5d7W0lIhNqaXNlH3f4AwoVZ9sEZ6suPsE4c/X1joSAmMBp3gP/wvHYGY7Vb+HrVpMy1ORFG/XXXZzDEwEBdBBvihpyvApwj0WGjyE58BKfa2Q=";
        s = getInstance().decrypt(s);
        System.out.println("s:" + s);
    }
}
