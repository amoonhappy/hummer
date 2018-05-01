package org.hummer.core.model.intf;

import java.util.Date;

/**
 * Package:
 * <p>
 * Title: com.col.architect.persistence.model
 * </p>
 * <p>
 * Project: ProjectName
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Company
 * </p>
 *
 * @author jeff.zhou Date: 2005-4-18 Time: 10:13:02
 * @version 1.0
 */
public interface IChangeLogModel extends IModel {
    public String getCrtUser();

    public void setCrtUser(String crtUser);

    public Date getCrtDate();

    public void setCrtDate(Date crtDate);

    public String getUpdUser();

    public void setUpdUser(String updUser);

    public Date getUpdDate();

    public void setUpdDate(Date updDate);
}
