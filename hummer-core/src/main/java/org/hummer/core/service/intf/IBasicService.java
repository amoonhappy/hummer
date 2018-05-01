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
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2005-12-7
 * @version 1.0
 */
package org.hummer.core.service.intf;

import org.hummer.core.model.intf.IBlobModel;
import org.hummer.core.model.intf.IClobModel;
import org.hummer.core.model.intf.IModel;
import org.hummer.core.persistence.impl.SearchCondition;
import org.hummer.core.persistence.intf.IBasicDAO;
import org.hummer.core.service.impl.ReturnValue;

/**
 * @author jeff.zhou
 */
public interface IBasicService extends IService {
    public final static String SERVICE_NAME = "basicService";

    public IService getProxy();

    public void setProxy(IService proxy);

    public void setBasicDAO(IBasicDAO dao);

    public ReturnValue delete(IModel model) throws Exception;

    public ReturnValue update(IModel model) throws Exception;

    public ReturnValue update(IModel[] models, IModel condition) throws Exception;

    public ReturnValue add(IModel model) throws Exception;

    public ReturnValue addOrUpdate(IModel model) throws Exception;

    public ReturnValue addOrUpdate(IModel model, boolean isInsert) throws Exception;

    public ReturnValue get(IModel model) throws Exception;

    public ReturnValue search(IModel model, SearchCondition searchCondition) throws Exception;

    public ReturnValue search(IModel model) throws Exception;

    public ReturnValue updateBlobModel(IBlobModel bmodel, byte[] input) throws Exception;

    public ReturnValue updateClobModel(IClobModel cmodel, String input) throws Exception;
}
