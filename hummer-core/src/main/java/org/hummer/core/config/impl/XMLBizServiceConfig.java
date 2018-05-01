package org.hummer.core.config.impl;

import org.hummer.core.config.intf.IConfiguration;
import org.hummer.core.config.intf.IXMLBusinessServiceConfig;
import org.hummer.core.util.StringUtil;

public class XMLBizServiceConfig extends XMLBeanConfig implements IXMLBusinessServiceConfig {

    private String transactionType;

    public IConfiguration overwriteBy(IConfiguration localConfig) {
        if (localConfig instanceof IXMLBusinessServiceConfig) {
            IXMLBusinessServiceConfig overwriteConfig = (IXMLBusinessServiceConfig) localConfig;
            this.overwriteConfig = overwriteConfig;
        }
        return this;
    }

    public String getTransactionType() {
        String ret = this.transactionType;
        if (overwriteConfig != null
                && !StringUtil.isEmpty(((XMLBizServiceConfig) overwriteConfig).getTransactionType())) {
            ret = ((XMLBizServiceConfig) overwriteConfig).getTransactionType();
        }
        return ret;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}