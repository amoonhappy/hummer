package org.hummer.core.model;

import org.apache.commons.lang3.StringUtils;
import org.hummer.core.model.intf.ICompositePKModel;
import org.hummer.core.model.intf.IModel;
import org.hummer.core.util.BlankObjectUtil;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.*;

/**
 * @author Jerry.Feng Date: 2004 8:50:34
 * @version $id: $
 */
public class ModelFactory implements ObjectFactory {
    //public static final String OBJECT_GETCLASSMETHOD = "getClass";

    private static final int LEN_PACKAGE_PRE = ModelFactory.class.getPackage().getName().length() + 1;

    private final static DateFormat dateFormat = new java.text.SimpleDateFormat(System.getProperty("data.format",
            "yyyy/MM/dd HH:mm:ss"));
    private static ModelFactory modelFactory = new ModelFactory();
    /**
     * Logger for this class
     */
    private final Logger logger = Log4jUtils.getLogger(getClass());
    private ClassLoader loader;

    private String perfix;

    private Map<Class, Method[]> getMethodCache = new HashMap<Class, Method[]>();

    private Map<Class, Method[]> setMethodCache = new HashMap<Class, Method[]>();

    private Map<Class, Method[]> publicMethodCache = new HashMap<Class, Method[]>();

    // add by jeff
    private Map<Class, ArrayList<String>> columnNameCache = new HashMap<Class, ArrayList<String>>();

    private Map<Class<?>, Map<String, Class>> columnTypeCache = new HashMap<Class<?>, Map<String, Class>>();

    private ModelFactory() {
        perfix = "com.ial.hbs.model.";
        loader = ModelFactory.class.getClassLoader();
    }

    public static ModelFactory getInstance() {
        return modelFactory;
    }

    public String getModuleName(String className) {
        return className.substring(LEN_PACKAGE_PRE);
    }

    public Object get(String moduleName) {
        Object obj = null;

        try {
            String className = perfix + moduleName;

            Class<?> clazz = loader.loadClass(className);
            obj = clazz.newInstance();
        } catch (Exception ex) {
        }

        return obj;
    }

    /**
     * @param module
     * @param name
     * @return Object
     */
    public Object get(String module, String name) {
        Object obj = null;

        try {
            name = perfix + module + "." + name;

            Class<?> clazz = loader.loadClass(name);
            obj = clazz.newInstance();
        } catch (Exception ex) {

        }

        return obj;
    }

    // get the public method list named "getXXX"
    public Method[] getPGMethod(Class clazz) {
        Method[] methods = getMethodCache.get(clazz);

        if (methods == null) {
            initMehthods(clazz);
            methods = getMethodCache.get(clazz);
        }

        return methods;
    }

    // get the public method list named "setXXX"
    public Method[] getPSMethod(Class clazz) {
        Method[] methods = setMethodCache.get(clazz);

        if (methods == null) {
            initMehthods(clazz);
            methods = setMethodCache.get(clazz);
        }

        return methods;
    }

    // get the public method list named "setXXX/getXXX"
    public Method[] getPMethod(Class clazz) {
        Method[] methods = publicMethodCache.get(clazz);

        if (methods == null) {
            initMehthods(clazz);
            methods = publicMethodCache.get(clazz);
        }

        return methods;
    }

    public Collection<String> getColumnName(Class clazz) {
        Collection<String> ret = columnNameCache.get(clazz);

        if (ret == null) {
            initMehthods(clazz);
            ret = columnNameCache.get(clazz);
        }

        return ret;
    }

    public Map<String, Class> getColumnType(Class clazz) {
        Map<String, Class> ret = columnTypeCache.get(clazz);

        if (ret == null) {
            initMehthods(clazz);
            ret = columnTypeCache.get(clazz);
        }

        return ret;
    }

    private void initMehthods(Class clazz) {
        // TODO: analyse the fields in the class
        BeanInfo beanInfo = null;

        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException ex) {
        }

        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        int initLen = descriptors.length;
        ArrayList<Method> readList = new ArrayList<Method>(initLen);
        ArrayList<Method> writeList = new ArrayList<Method>(initLen);
        ArrayList<Method> publicList = new ArrayList<Method>();

        // ArrayList columnLocalNameList = newweb ArrayList();
        ArrayList<String> columnNameList = new ArrayList<String>();
        Map<String, Class> columnTypeMap = new HashMap<String, Class>();

        Method[] readMethods;
        Method[] writeMethods;
        Method[] publicMethods;

        if (descriptors == null) {
            readMethods = BlankObjectUtil.MethodArray;
            writeMethods = BlankObjectUtil.MethodArray;
            publicMethods = BlankObjectUtil.MethodArray;
        } else {
            for (int i = 0; i < initLen; i++) {
                PropertyDescriptor desc = descriptors[i];

                if (!StringUtils.equals("class", desc.getName())) {
                    // columnTypeList.add(desc.getPropertyType());
                    columnTypeMap.put(desc.getName(), desc.getPropertyType());
                    columnNameList.add(desc.getName());
                }

                if (!desc.getPropertyType().getName().equals("java.lang.Class")) {
                    Method writeMethod = desc.getWriteMethod();
                    writeList.add(writeMethod);

                    Method readMethod = desc.getReadMethod();
                    readList.add(readMethod);
                    publicList.add(readMethod);
                    publicList.add(writeMethod);
                }
            }

            readMethods = new Method[readList.size()];
            readList.toArray(readMethods);
            writeMethods = new Method[writeList.size()];
            writeList.toArray(writeMethods);
            publicMethods = new Method[publicList.size()];
            publicList.toArray(publicMethods);
        }

        columnTypeCache.put(clazz, columnTypeMap);
        columnNameCache.put(clazz, columnNameList);
        getMethodCache.put(clazz, readMethods);
        setMethodCache.put(clazz, writeMethods);
        publicMethodCache.put(clazz, publicMethods);
    }

    public String getPrimaryKey(IModel model) {
        String primaryKey;

        if (model instanceof IModel) {
            assert (model.getId() != null);
            primaryKey = "&id=" + model.getId().toString();
        } else if (model instanceof ICompositePKModel) {
            ICompositePKModel compModel = (ICompositePKModel) model;
            Object compId = compModel.getComp_id();
            StringBuffer sb = new StringBuffer();
            Method[] methods = ModelFactory.getInstance().getPGMethod(compId.getClass());

            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                String name = method.getName();
                name = StringUtils.uncapitalize(name.substring(3));

                try {
                    Object value = method.invoke(compId, new Object[0]);

                    if (value != null) {
                        sb.append('&').append("comp_id.").append(name).append('=');

                        if (value instanceof Date) {
                            sb.append(dateFormat.format(value));
                        } else {
                            sb.append(value);
                        }
                    }
                } catch (Exception ex) {
                    logger.error(ex.toString());
                }
            }

            primaryKey = sb.toString();
        } else {
            assert (false) : "the object in collection should be one of three models";
            primaryKey = "";
        }

        return primaryKey;
    }

    public Object getObjectInstance(Object arg0, Name arg1, Context arg2, Hashtable<?, ?> arg3) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}
