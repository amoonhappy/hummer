package org.hummer.core.config.intf;

public interface IXMLBusinessServiceConfig extends IXMLBeanConfig {
    public String getTransactionType();

    public void setTransactionType(String type);
}