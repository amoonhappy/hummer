package org.hummer.core.persistence.intf;

import org.hummer.core.model.intf.IModel;
import org.hummer.core.model.intf.ISingleLongPKModel;
import org.hummer.core.pagination.Pager;

public interface IBasicTestDAO {
    void insertModel(IModel iModel);

    void deleteModel(IModel iModel);

    IModel getModel(ISingleLongPKModel iModel);

    void updateModel(ISingleLongPKModel iModel);

    Pager getAllModels();

}
