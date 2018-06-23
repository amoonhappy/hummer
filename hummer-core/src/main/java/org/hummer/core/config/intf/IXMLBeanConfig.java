package org.hummer.core.config.intf;

import java.util.Map;

public interface IXMLBeanConfig extends IXMLConfiguration {
    // public List<String> getEnabledBeanIds();

    String getBeanId();

    void setBeanId(String beanId);

    Class getBeanClass();

    void setBeanClass(Class implClass);

    Map<String, String> getProp2RefBeanIdMapping();

    Map<String, String> getProp2SpringBeanIdMapping();

    boolean isSingleton();

    String getSingleton();

    void setSingleton(String flag);

    void regRefBeanId(String key, String refBeanId);

    void regSpringBeanId(String key, String springBeanId);


    Map<String, String> getXMLProperteisValueMapping();

    void regPropertiesValue(String propertiesName, String value);

    // public void regEnabledBeanIds(String propertyName,String value);
}