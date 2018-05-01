package org.hummer.core.model.intf;

import java.io.Serializable;

/**
 * Description:
 *
 * @author Jeff.Zhou Date: 2004-10-29 9:50:33
 * @version $Id: $
 */
public interface ICompositePKModel extends IModel {
    public Serializable getComp_id();

    public void setComp_id(Serializable compId);
}
