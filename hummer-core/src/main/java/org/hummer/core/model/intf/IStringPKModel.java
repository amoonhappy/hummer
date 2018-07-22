package org.hummer.core.model.intf;

import java.io.Serializable;

/**
 * Powered By Hummer Framework
 * Copyright: Copyright (c) 2018
 *
 * @ProjectName: hummer
 * @Description: java类作用描述
 * @Author: Jeff Zhou
 * @CreateDate: 2018/7/19 19:59
 * @UpdateUser: Jeff Zhou
 * @UpdateDate: 2018/7/19 19:59
 * @UpdateRemark: UpdateComments
 * @Version: 1.0
 **/
public interface IStringPKModel extends Serializable {
    String getId();

    void setId(String id);
}
