/**
 * <p>Open Source Architecture Project -- hummer-common            </p>
 * <p>Class Description                                     </p>
 * <p>                                                      </p>
 * <p>                                                      </p>
 * <p>Change History                                        </p>
 * <p>Author    Date      Description                       </p>
 * <p>                                                      </p>
 * <p>                                                      </p>
 * Copyright (c) 2004-2007 hummer-common, All rights reserved
 *
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a>
 * Date: 2005-12-7
 * @version 1.0
 */
package org.hummer.core.util;

/**
 * <br>
 * <br>
 * <ul>
 * <li>
 * <li>
 * </ul>
 * <br>
 *
 * @author jeff
 * @version <br>
 * 1.0.0 2007-6-23
 * @since 1.0.0
 */
public class FileInfo {
    private String filename;

    private String filePath;

    private String extension;

    public FileInfo() {
    }

    public FileInfo(String filename, String filePath) {
        this.filename = filename;
        this.filePath = filePath;

        int index = filename.lastIndexOf('.');

        if (index > 0) {
            extension = filename.substring(index + 1);
        } else {
            extension = "";
        }
    }

    public FileInfo(String path) {
        String filename;
        String filePath;

        int index = path.lastIndexOf('/');

        if (index > 0) {
            filePath = path.substring(0, index + 1);
            filename = path.substring(index + 1);
        } else {
            filePath = "/";
            filename = path;
        }

        this.filename = filename;
        this.filePath = filePath;

        index = filename.lastIndexOf('.');

        if (index > 0) {
            extension = filename.substring(index + 1);
        } else {
            extension = "";
        }
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
