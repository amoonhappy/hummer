package org.hummer.core.model.intf;

import java.io.Serializable;

/**
 * The model interface has no methods or fields and serves only to identify the
 * class is a model object
 * <p>
 * all the entities mapping to the database MUST implement this interface, below features depends on it:
 * 1. Redis Cache annotations and Evict strategy
 *
 * @author Jerry.Feng Date: 2004 14:07:39
 * @author Jeff Zhou Date: Date: 2018 21 June 14:07:39
 * @version $id: $
 */
public interface IModel extends Serializable {
    Long getId();

    void setId(Long id);
}
