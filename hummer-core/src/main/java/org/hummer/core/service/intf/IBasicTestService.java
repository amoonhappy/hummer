package org.hummer.core.service.intf;

import org.hummer.core.model.intf.IModel;

import java.util.Collection;

public interface IBasicTestService {
    void insertModel(IModel iModel);

    void deleteMOdel(IModel iModel);

    IModel getModel(IModel iModel);

    Collection getAllModels();
}
