package org.hummer.core.attachement;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import org.bson.types.ObjectId;
import org.hummer.core.exception.MongoDBExcepiton;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;


public class AttachmentUtil {

    public static final int SMALL_IMG_SIZE = 256;// 缩略小图的边长
    public static final int MIDDLE_IMG_SIZE = 1024;// 缩略中图的边长

    private AttachmentUtil() {
    }

    /**
     * 保存附件(业务bussinessId为String)
     */
    public static Map<String, Object> saveFile(MultipartFile file, String userId, String businessModel, String bussinessId, String remark) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 判断文件是否为空或者文件是否为空（txt文件没有内容）
        if (file == null || file.getSize() <= 0) {
            throw new MongoDBExcepiton("上传的附件不能为空！");
        }
        InputStream inputStream = null;
        try {
            map = saveToMongoDB(userId, businessModel, bussinessId, remark, file.getOriginalFilename(), file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new MongoDBExcepiton(e.getMessage());
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return map;
    }

    /**
     * 保存附件(File类型)
     */
    public static Map<String, Object> saveFile(File file, String userId, String businessModel, String bussinessId, String remark) {

        // 判断文件是否为空
        if (file == null || file.length() <= 0) {
            throw new MongoDBExcepiton("上传的附件不能为空！");
        }
        String fileName = file.getName();
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            return saveToMongoDB(userId, businessModel, bussinessId, remark, fileName, inputStream);
        } catch (FileNotFoundException e) {
            String msg = e.getMessage();
            throw new MongoDBExcepiton(msg);
        }
    }

    public static Map<String, Object> saveToMongoDB(String userId, String businessModel, String bussinessId, String remark, String filename,
                                                    byte[] data) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {

            // if (!isAcceptFileType(filename)) {
            // throw new MongoDBExcepiton("附件类型不允许！");
            // }

            GridFS gridFS = MongoDBUtil.getInstance().getGridFS();
            // 创建gridfs文件,并设置一些参数，保存到mongodb中
            GridFSFile gridFSFile = gridFS.createFile(data);
            Map<String, Object> attMap = new HashMap<String, Object>();
            attMap.put("filename", filename);
            attMap.put("businessModel", businessModel);
            attMap.put("businessId", bussinessId);
            attMap.put("createId", userId);
            attMap.put("createTime", new Date());
            attMap.put("uploadDate", new Date());
            attMap.put("remark", remark);
            attMap.put("contentType", filename.substring(filename.lastIndexOf(".") + 1));

            for (String key : attMap.keySet()) {
                gridFSFile.put(key, attMap.get(key));
            }

            if (StringUtils.isEmpty(businessModel) && StringUtils.isEmpty(bussinessId)) {
                gridFSFile.put("status", 0);// 没有业务模块名跟业务id，表示为临时文件
            } else {
                gridFSFile.put("status", 1);// 有业务模块名跟业务id，表示为保存的业务文件
            }
            gridFSFile.save();

            if (ImgCompressUtil.isImageFile(filename)) {
                /**
                 * 如果是图片文件，则进行图片的压缩处理
                 */

                // 获取原图的宽高
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
                if (img != null) {
                    int width = img.getWidth(null);
                    int height = img.getHeight(null);

                    String smallImg;// 小图id
                    String middleImg;// 中图id

                    if (width >= MIDDLE_IMG_SIZE || height >= MIDDLE_IMG_SIZE) {
                        /**
                         * 1、如果原图的宽高最大的一边小于或等于SMALL_IMG_SIZE，则设置小图、中图都为原图
                         */

                        // 1.1、压缩中图，并保存图片文件到mongoDB
                        byte[] middleImgData = ImgCompressUtil.resize(new ByteArrayInputStream(data), MIDDLE_IMG_SIZE);
                        GridFSFile middleImgFile = gridFS.createFile(middleImgData);
                        for (String key : attMap.keySet()) {
                            middleImgFile.put(key, attMap.get(key));
                        }
                        middleImgFile.put("imgSize", "m");
                        middleImgFile.save();

                        middleImg = middleImgFile.getId().toString();

                        // 1.2、压缩小图，并保存图片文件到mongoDB
                        byte[] smallImgData = ImgCompressUtil.resize(new ByteArrayInputStream(data), SMALL_IMG_SIZE);
                        GridFSFile smallImgFile = gridFS.createFile(smallImgData);
                        for (String key : attMap.keySet()) {
                            smallImgFile.put(key, attMap.get(key));
                        }
                        smallImgFile.put("imgSize", "s");
                        smallImgFile.save();

                        smallImg = smallImgFile.getId().toString();

                    } else if (width >= SMALL_IMG_SIZE || height >= SMALL_IMG_SIZE) {
                        /**
                         * 2、如果原图的宽高最大的一边小于或等于MIDDLE_IMG_SIZE且大于SMALL_IMG_SIZE
                         * ，则设置中图都为原图，小图通过原图压缩为SMALL_IMG_SIZE
                         */

                        // 2.1、原图即为中图
                        middleImg = gridFSFile.getId().toString();

                        // 2.2、压缩小图，并保存图片文件到mongoDB
                        byte[] smallImgData = ImgCompressUtil.resize(new ByteArrayInputStream(data), SMALL_IMG_SIZE);
                        GridFSFile smallImgFile = gridFS.createFile(smallImgData);
                        for (String key : attMap.keySet()) {
                            smallImgFile.put(key, attMap.get(key));
                        }
                        smallImgFile.put("imgSize", "s");
                        smallImgFile.save();

                        smallImg = smallImgFile.getId().toString();

                    } else {
                        /**
                         * 3、如果原图的宽高最大的一边大于MIDDLE_IMG_SIZE
                         * ，则小图跟中图都分别压缩为SMALL_IMG_SIZE、MIDDLE_IMG_SIZE
                         */

                        // 1、原图即为中图
                        middleImg = gridFSFile.getId().toString();

                        // 1、原图即为小图
                        smallImg = gridFSFile.getId().toString();
                    }
                    // 为monoDB中原图关联上中图跟小图，方便查找
                    gridFSFile.put("smallImg", smallImg);
                    gridFSFile.put("middleImg", middleImg);
                    gridFSFile.save();
                }

            }

            result.put("fileId", gridFSFile.getId().toString());
            result.put("attchmentUrl", "/api/v4/attachment/id/" + gridFSFile.getId().toString() + filename.substring(filename.lastIndexOf(".")));

        } catch (IOException e) {
            e.printStackTrace();
            throw new MongoDBExcepiton(e.getMessage());
        }
        return result;
    }

    /**
     * 保存附件（InputStream类型）
     */
    public static Map<String, Object> saveToMongoDB(String userId, String businessModel, String bussinessId, String remark, String filename,
                                                    InputStream inputStream) {
        Map<String, Object> result = new HashMap<String, Object>();
        byte[] data = new byte[0];// 附件的二进制流
        try {
            data = ByteToInputStream.input2byte(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return saveToMongoDB(userId, businessModel, bussinessId, remark, filename, data);
    }

    /**
     * @param id
     * @方法说明：根据附件id删除附件
     */
    public static void removeAttachment(String id) {
        GridFS gridFS = MongoDBUtil.getInstance().getGridFS();
        gridFS.remove(new ObjectId(id));
    }

    /**
     * 根据模块名称及模块id删除附件
     *
     * @param businessModel
     * @param businessId
     */
    public static void removeAttachment(String businessModel, String businessId) {
        GridFS gridFS = MongoDBUtil.getInstance().getGridFS();
        DBObject query = new BasicDBObject();
        query.put("businessModel", businessModel);
        query.put("businessId", businessId);
        gridFS.remove(query);
    }

    /**
     * 根据appkey查询对应的记录 单条
     */
    public static GridFSDBFile findByAppKey(String appKey) {
        GridFS gridFS = MongoDBUtil.getInstance().getGridFS();
        BasicDBObject queryObject = new BasicDBObject("appKey", appKey);
        GridFSDBFile gridFSDBFile = gridFS.findOne(queryObject);
        return gridFSDBFile;
    }

    /**
     * 根据fileId查询对应的记录 单条
     */
    public static GridFSDBFile findByFileId(String fileId) {
        GridFS gridFS = MongoDBUtil.getInstance().getGridFS();
        BasicDBObject queryObject = new BasicDBObject("fileId", fileId);
        GridFSDBFile gridFSDBFile = gridFS.findOne(queryObject);
        return gridFSDBFile;
    }

    public static Map<String, Object> saveToMongoDBByAppKey(String userId, String businessModel, String bussinessId, String remark, String filename,
                                                            InputStream inputStream, String appKey, String passWord) {

        // if (!isAcceptFileType(filename)) {
        // throw new MongoDBExcepiton("附件类型不允许！");
        // }

        GridFS gridFS = MongoDBUtil.getInstance().getGridFS();
        int pot = filename.lastIndexOf(".");
        // 创建gridfs文件,并设置一些参数，保存到mongodb中
        GridFSFile gridFSFile = gridFS.createFile(inputStream);
        gridFSFile.put("appKey", appKey);
        gridFSFile.put("passWord", passWord);
        gridFSFile.put("filename", filename);
        gridFSFile.put("businessModel", businessModel);
        gridFSFile.put("businessId", bussinessId);
        gridFSFile.put("createId", userId);
        gridFSFile.put("createTime", new Date());
        gridFSFile.put("uploadDate", new Date());
        gridFSFile.put("remark", remark);
        gridFSFile.put("contentType", filename.substring(pot));

        if (StringUtils.isEmpty(businessModel) && StringUtils.isEmpty(bussinessId)) {
            gridFSFile.put("status", 0);// 没有业务模块名跟业务id，表示为临时文件
        } else {
            gridFSFile.put("status", 1);// 有业务模块名跟业务id，表示为保存的业务文件
        }
        gridFSFile.save();

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("fileId", gridFSFile.getId().toString());
        result.put("attchmentUrl", "/api/v4/attachment/id/" + gridFSFile.getId().toString());

        System.out.println("----------------------------------url: " + "/api/v4/attachment/id/" + gridFSFile.getId().toString());
        return result;
    }

    /**
     * 判断是否属于配置中心允许上传的类型
     *
     * @param filename
     * @return
     */
    @SuppressWarnings("unused")
    private static boolean isAcceptFileType(String filename) {
        /**
         * 附件类型验证,如果配置中心没有设置附件类型，则允许上传所有的类型附件
         */
        String acceptFileType = "";// 附件允许的类型，格式为txt,pdf,doc,jpg,zip,rar
        if (!StringUtils.isEmpty(acceptFileType)) {
            Set<String> acceptTypes = new HashSet<String>();
            String[] fileTypeArr = acceptFileType.split(",");
            for (String type : fileTypeArr) {
                if (type != null && !type.trim().equals("")) {// 晒除掉空字符串
                    acceptTypes.add(type);
                }
            }
            int pot = filename.lastIndexOf(".");
            String fileType = filename.substring(pot + 1);
            if (!acceptTypes.contains(fileType)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 保存附件
     *
     * @param file          文件
     * @param userId        用户id
     * @param businessModel 业务模块
     * @param bussinessId   业务id
     * @param remark        备注
     * @return
     */
    public static Map<String, Object> saveFile(MultipartFile file, String userId, String businessModel, Long bussinessId, String remark)
            throws Exception {
        return saveFile(file, userId, businessModel, bussinessId.toString(), remark);
    }
}
