package org.hummer.core.util;

import java.io.*;

/**
 * Utitlity methods for <code>Serializable</code> objects.
 */
public class SerializableUtil {
    /**
     * 将对象序列化成字节码
     *
     * @param obj
     * @return
     */
    public static byte[] toByte(Object obj) {
        ObjectOutputStream oos = null; //对象输出流
        ByteArrayOutputStream baos = null;//字节数组输出流

        byte[] bt = null;

        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            bt = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bt;
    }

    /**
     * 反序列化，将byte字节转换为对象
     *
     * @param bt byte[]
     * @return Object
     */
    public static Object toObject(byte[] bt) {
        ByteArrayInputStream bais = null;
        Object object = null;


        try {
            bais = new ByteArrayInputStream(bt);
            ObjectInputStream ois = new ObjectInputStream(bais);
            object = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return object;
    }

    /**
     * Clone a <code>Serializable</code> object; for use when a
     * <code>clone()</code> method is not available.
     *
     * @param obj object to clone
     * @return clone object
     * @throws RuntimeException
     */
    public static Object cloneSerializable(Object obj) {
        Object ret = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(StreamUtil.DEFAULT_BUFFER_SIZE);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();

            ByteArrayInputStream bis = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);

            try {
                ret = ois.readObject();
            } catch (ClassNotFoundException cnfe) {
                assert false;
            }

            ois.close();
        } catch (IOException ioex) {
            throw new RuntimeException("Unable to clone object: " + obj);
        }

        return ret;
    }

    public static byte[] toBytes(Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(StreamUtil.DEFAULT_BUFFER_SIZE);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
        } catch (IOException e) {
            throw new RuntimeException("Error serializing object: " + obj.getClass() + ".", e);
        }
        return bos.toByteArray();
    }

    public static Object toObject(InputStream binaryStream, final ClassLoader cl) {
        try {
            ObjectInputStream ois = new ObjectInputStream(binaryStream) {

                protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                    String name = desc.getName();
                    Class c = cl.loadClass(name);
                    return c;
                }
            };
            return ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object toObject(byte[] arr, final ClassLoader cl) {
        ByteArrayInputStream bis = new ByteArrayInputStream(arr);
        return toObject(bis, cl);
    }

}
