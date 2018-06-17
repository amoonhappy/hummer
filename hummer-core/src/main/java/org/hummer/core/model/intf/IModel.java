package org.hummer.core.model.intf;

import java.io.Serializable;

/**
 * The model interface has no methods or fields and serves only to identify the
 * class is a model object
 *
 * @author Jerry.Feng Date: 2004 14:07:39
 * @version $id: $
 */
public interface IModel extends Serializable {
    String getId();
}
