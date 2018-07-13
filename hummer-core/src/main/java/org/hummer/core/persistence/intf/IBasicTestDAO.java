package org.hummer.core.persistence.intf;

import org.hummer.core.model.intf.IModel;

import java.util.List;

public interface IBasicTestDAO {
    int insertModel(IModel iModel);

    void deleteModel(IModel iModel);

    IModel getModel(IModel iModel);

    int updateModel(IModel iModel);

    List getAllModels();

    IModel getModel(Long id);

}
