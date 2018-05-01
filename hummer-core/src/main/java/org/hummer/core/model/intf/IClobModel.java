package org.hummer.core.model.intf;

import java.sql.Clob;

/**
 * Created by IntelliJ IDEA. User: Jeff.Zhou Date: 2005-1-7 Time: 11:18:51
 */
public interface IClobModel extends ILobModel {
    public Clob getClob();

    public void setClob(Clob input);
}
