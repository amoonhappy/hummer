package org.hummer.core.persistence.intf;

import org.hummer.core.model.intf.IModel;
import org.hummer.core.model.intf.ISingleLongPKModel;
import org.hummer.core.model.intf.ISingleStringPKModel;

import java.util.List;

public interface IBasicTestDAO {
    int insertModel(IModel iModel);

    void deleteModel(ISingleLongPKModel iModel);

    void deleteModel(ISingleStringPKModel iModel);

    IModel getModel(ISingleStringPKModel iModel);

    IModel getModel(ISingleLongPKModel iModel);

    int updateModel(ISingleStringPKModel iModel);

    int updateModel(ISingleLongPKModel iModel);

    List getAllModels();

    IModel getModel(Integer id);

}
