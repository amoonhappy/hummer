package org.hummer.core.config.intf;

import java.util.Map;

public interface IXMLBeanConfig extends IXMLConfiguration {
    // public List<String> getEnabledBeanIds();

    public String getBeanId();

    public void setBeanId(String beanId);

    public Class getBeanClass();

    public void setBeanClass(Class implClass);

    public Map<String, String> getProp2RefBeanIdMapping();

    public boolean isSingleton();

    public String getSingleton();

    public void setSingleton(String flag);

    public void regRefBeanId(String key, String refBeanId);

    public Map<String, String> getXMLProperteisValueMapping();

    public void regPropertiesValue(String propertiesName, String value);

    // public void regEnabledBeanIds(String propertyName,String value);
}