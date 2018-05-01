package org.hummer.core.model.intf;

import java.sql.Blob;

/**
 * Created by IntelliJ IDEA. User: Jeff.Zhou Date: 2005-1-7 Time: 11:16:04
 */
public interface IBlobModel extends ILobModel {
    public Blob getBlob();

    public void setBlob(Blob input);
}
