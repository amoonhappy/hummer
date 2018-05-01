/**
 * <p>Open Source Architecture Project -- Hummer            </p>
 * <p>Class Description                                     </p>
 * <p>                                                      </p>
 * <p>                                                      </p>
 * <p>Change History                                        </p>
 * <p>Author    Date      Description                       </p>
 * <p>                                                      </p>
 * <p>                                                      </p>
 *
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2006-2-17
 * @version 1.0
 */
package org.hummer.core.service.impl;

import org.hummer.core.model.intf.IBlobModel;
import org.hummer.core.model.intf.IClobModel;
import org.hummer.core.model.intf.IModel;
import org.hummer.core.persistence.impl.SearchCondition;
import org.hummer.core.persistence.intf.IBasicDAO;
import org.hummer.core.service.intf.IBasicService;
import org.hummer.core.service.intf.IService;

/**
 * @author jeff.zhou
 */
public class BasicService implements IBasicService {
    protected IService proxy = null;

    IBasicDAO dao = null;

    public void setBasicDAO(IBasicDAO dao) {
        this.dao = dao;
    }

    public ReturnValue delete(IModel model) throws Exception {
        ReturnValue ret = new ReturnValue();
        ret.setModel(dao.deleteObject(model));
        return ret;
    }

    public ReturnValue update(IModel model) throws Exception {
        ReturnValue ret = new ReturnValue();
        ret.setModel(dao.updateObject(model));
        return ret;
    }

    public ReturnValue update(IModel[] models, IModel condition) throws Exception {
        ReturnValue ret = new ReturnValue();
        SearchCondition sc = new SearchCondition();
        sc.setSearchModel(condition);
        // sc.
        ret.setModel(new Integer(dao.execUpdateSQL(sc)));
        return ret;
    }

    public ReturnValue add(IModel model) throws Exception {
        ReturnValue ret = new ReturnValue();
        ret.setModel(dao.addObject(model));
        return ret;
    }

    public ReturnValue addOrUpdate(IModel model) throws Exception {
        ReturnValue ret = new ReturnValue();
        ret.setModel(dao.saveObject(model));
        return ret;
    }

    public ReturnValue addOrUpdate(IModel model, boolean isInsert) throws Exception {
        ReturnValue ret = new ReturnValue();
        ret.setModel(dao.saveObject(model, isInsert));
        return ret;
    }

    public ReturnValue get(IModel model) throws Exception {
        ReturnValue ret = new ReturnValue();
        ret.setModel(dao.getObject(model));
        return ret;
    }

    public ReturnValue search(IModel model, SearchCondition searchCondition) throws Exception {
        ReturnValue ret = new ReturnValue();
        searchCondition.setSearchModel(model);
        ret.setModel(dao.searchByHQL(searchCondition));
        return ret;
    }

    public ReturnValue search(IModel model) throws Exception {
        ReturnValue ret = new ReturnValue();
        SearchCondition sc = new SearchCondition();
        sc.setSearchModel(model);
        ret.setModel(dao.searchByCriteria(sc));
        return ret;
    }

    public ReturnValue updateBlobModel(IBlobModel bmodel, byte[] input) throws Exception {
        ReturnValue ret = new ReturnValue();
        ret.setModel(dao.updateBlob(bmodel, input));
        return ret;
    }

    public ReturnValue updateClobModel(IClobModel cmodel, String input) throws Exception {
        ReturnValue ret = new ReturnValue();
        ret.setModel(dao.updateClob(cmodel, input));

        return ret;
    }

    public IService getProxy() {
        return proxy;
    }

    public void setProxy(IService proxy) {
        this.proxy = proxy;
    }

}
