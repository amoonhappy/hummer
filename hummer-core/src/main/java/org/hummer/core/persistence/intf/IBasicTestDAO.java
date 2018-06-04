package org.hummer.core.persistence.intf;

import org.hummer.core.model.intf.IModel;
import org.hummer.core.model.intf.ISingleStringPKModel;
import org.hummer.core.pagination.Pager;

import java.sql.SQLException;

public interface IBasicTestDAO {
    int insertModel(IModel iModel) throws SQLException;

    void deleteModel(ISingleStringPKModel iModel);

    IModel getModel(ISingleStringPKModel iModel);

    int updateModel(ISingleStringPKModel iModel);

    Pager getAllModels();

}
