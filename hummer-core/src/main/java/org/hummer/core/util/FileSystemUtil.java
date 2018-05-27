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

import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
public class FileSystemUtil {
    private static final Logger logger = Log4jUtils.getLogger(FileSystemUtil.class);

    public static Collection<FilesListModel> listFiles(String path) {
        File file = new File(path);
        List<FilesListModel> ls = new ArrayList<FilesListModel>();

        if (file == null) {
            return ls;
        }

        File[] files = file.listFiles();

        FilesListModel model = new FilesListModel();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                model = new FilesListModel();

                File subfile = files[i];
                model = convertFileToModel(subfile);
                ls.add(model);
            }
        } else {
            ls.add(model);
        }

        return ls;
    }

    private static FilesListModel convertFileToModel(File subfile) {
        assert (subfile != null);

        FilesListModel model = new FilesListModel();
        model.setCanRead(subfile.canRead());

        model.setDirectory(subfile.isDirectory());
        model.setFile(subfile.isFile());
        model.setHidden(subfile.isHidden());

        model.setLength(subfile.length());
        model.setName(subfile.getName());

        File parent = subfile.getParentFile();

        if (parent != null) {
            FilesListModel parentModel = convertFileToModel(parent);
            model.setParent(parentModel);
        }

        model.setPath(subfile.getPath());
        model.setWriteable(subfile.canWrite());

        return model;
    }

    public static String getParentPath(String fullpath) {
        File file = new File(fullpath);

        if (file == null) {
            return "";
        }

        if (file.getParentFile() == null) {
            return "";
        }

        return file.getParentFile().getPath();
    }

    public static String getFileExtention(String fileName) {
        String ret = null;
        if (fileName != null) {
            ret = StringUtil.substring(fileName, StringUtil.lastIndexOf(fileName, ".") + 1);
        }
        return ret;
    }
}
