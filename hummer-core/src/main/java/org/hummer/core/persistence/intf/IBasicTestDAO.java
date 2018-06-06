package org.hummer.core.persistence.intf;

import org.hummer.core.model.intf.IModel;
import org.hummer.core.model.intf.ISingleStringPKModel;

import java.util.List;

public interface IBasicTestDAO {
    int insertModel(IModel iModel);

    void deleteModel(ISingleStringPKModel iModel);

    IModel getModel(ISingleStringPKModel iModel);

    int updateModel(ISingleStringPKModel iModel);

    List getAllModels();

}
