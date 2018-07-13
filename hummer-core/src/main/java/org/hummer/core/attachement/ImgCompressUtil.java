package org.hummer.core.attachement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;

public class ImgCompressUtil {
    private Image img;
    private int width;
    private int height;

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws Exception {
        System.out.println("开始：" + new Date().toLocaleString());
        File img = new File("E:\\img\\fpic8541.jpg");
        InputStream in = new FileInputStream(img);
        ImgCompressUtil imgCom = new ImgCompressUtil(in);
        InputStream input = imgCom.resize(150, 150);

        File file = new File("E:\\img\\fp123.jpg");
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = input.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        input.close();
        System.out.println("结束：" + new Date().toLocaleString());
    }

    public ImgCompressUtil(String fileName) throws IOException {
        File file = new File(fileName);// 读入文件
        img = ImageIO.read(file); // 构造Image对象
        width = img.getWidth(null); // 得到源图宽
        height = img.getHeight(null); // 得到源图长
    }

    public ImgCompressUtil(InputStream input) throws IOException {
        img = ImageIO.read(input); // 构造Image对象
        width = img.getWidth(null); // 得到源图宽
        height = img.getHeight(null); // 得到源图长
    }

    /**
     * 强制压缩/放大图片到固定的大小
     *
     * @param w int 新宽度
     * @param h int 新高度
     */
    @SuppressWarnings("restriction")
    public InputStream resize(int w, int h) throws IOException {

        if (width / height > w / h) {
            h = (int) (height * w / width);
        } else {
            w = (int) (width * h / height);
        }

        // SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
        ByteArrayOutputStream out = new ByteArrayOutputStream();


        ImageIO.write(image, "jpeg", out);
        // 可以正常实现bmp、png、gif转jpg
        //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        //encoder.encode(image); // JPEG编码
        //out.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    public static byte[] resize(InputStream input, int s) throws IOException {
        BufferedImage img = ImageIO.read(input); // 构造Image对象
        int width = img.getWidth(null); // 得到源图宽
        int height = img.getHeight(null); // 得到源图长

        int m = Math.max(width, height);

        double scale = 1.0 * s / m;
        int w = new Double(width * scale).intValue();
        int h = new Double(height * scale).intValue();

        // SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ImageIO.write(image, "jpeg", out);
        // 可以正常实现bmp、png、gif转jpg
        //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        //encoder.encode(image); // JPEG编码
        //out.close();
        return out.toByteArray();
    }

    // 名称为.jpg, .png, .jpeg的为图片文件
    public static boolean isImageFile(String fileName) {
        if (fileName == null)
            return false;

        String fn = fileName.toLowerCase();
        if (fn.endsWith(".jpg") || fn.endsWith(".png") || fn.endsWith(".jpeg"))
            return true;
        else
            return false;
    }
}
