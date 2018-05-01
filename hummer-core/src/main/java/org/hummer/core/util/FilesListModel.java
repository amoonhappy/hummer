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

import java.net.URL;
import java.util.Date;

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
public class FilesListModel {
    private String name;

    private String path;

    private URL url;

    private long length;

    private boolean isWriteable;

    private boolean isHidden;

    private boolean isFile;

    private boolean isDirectory;

    private boolean canRead;

    private Date lastModified;

    private Date date;

    private FilesListModel parent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public boolean isWriteable() {
        return isWriteable;
    }

    public void setWriteable(boolean writeable) {
        isWriteable = writeable;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public FilesListModel getParent() {
        return parent;
    }

    public void setParent(FilesListModel parent) {
        this.parent = parent;
    }

    public String getIsDirectory() {
        return this.isDirectory() + "";
    }
}
