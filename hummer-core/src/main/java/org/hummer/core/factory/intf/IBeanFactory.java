package org.hummer.core.factory.intf;

import org.hummer.core.exception.BeanException;
import org.hummer.core.exception.NoBeanDefinationException;
//import org.springframework.beans.factory.FactoryBean;
//import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public interface IBeanFactory {
    /**
     * Return an instance, which may be shared or independent, of the specified
     * bean.
     * <p>
     * This method allows a Spring BeanFactory to be used as a replacement for
     * the Singleton or Prototype design pattern. Callers may retain references
     * to returned objects in the case of Singleton beans.
     * <p>
     * Translates aliases back to the corresponding canonical bean name. Will
     * ask the parent factory if the bean cannot be found in this factory
     * instance.
     *
     * @param name the name of the bean to retrieve
     * @return an instance of the bean
     * @throws NoBeanDefinationException if there is no bean definition with the specified name
     * @throws BeanException             if the bean could not be obtained
     */
    public Object getBean(String name) throws BeanException;

    /**
     * Return an instance, which may be shared or independent, of the specified
     * bean.
     * <p>
     * Behaves the same as {@link #getBean(String)}, but provides a measure of
     * type safety by throwing a BeanNotOfRequiredTypeException if the bean is
     * not of the required type. This means that ClassCastException can't be
     * thrown on casting the result correctly, as can happen with
     * {@link #getBean(String)}.
     * <p>
     * Translates aliases back to the corresponding canonical bean name. Will
     * ask the parent factory if the bean cannot be found in this factory
     * instance.
     *
     * @param name         the name of the bean to retrieve
     * @param requiredType type the bean must match. Can be an interface or superclass of
     *                     the actual class, or <code>null</code> for any match. For
     *                     example, if the value is <code>Object.class</code>, this
     *                     method will succeed whatever the class of the returned
     *                     instance.
     * @return an instance of the bean
     * @throws NoBeanDefinationException     if the bean is not of the required type
     * @throws NoSuchBeanDefinitionException if there's no such bean definition
     * @throws BeanException                 if the bean could not be created
     */
    public Object getBean(String name, Class requiredType) throws BeanException;

    /**
     * Does this bean factory contain a bean with the given name? More
     * specifically, is {@link #getBean} able to obtain a bean instance for the
     * given name?
     * <p>
     * Translates aliases back to the corresponding canonical bean name. Will
     * ask the parent factory if the bean cannot be found in this factory
     * instance.
     *
     * @param name the name of the bean to query
     * @return whether a bean with the given name is defined
     */
    public boolean containsBean(String name);

    /**
     * Is this bean a shared singleton? That is, will {@link #getBean} always
     * return the same instance?
     * <p>
     * Note: This method returning <code>false</code> does not clearly
     * indicate independent instances. It indicates non-singleton instances,
     * which may correspond to a scoped bean as well. Use the
     * {@link #isPrototype} operation to explicitly check for independent
     * instances.
     * <p>
     * Translates aliases back to the corresponding canonical bean name. Will
     * ask the parent factory if the bean cannot be found in this factory
     * instance.
     *
     * @param name the name of the bean to query
     * @return whether this bean corresponds to a singleton instance
     * @throws NoBeanDefinationException if there is no bean with the given name
     * @see #getBean
     * @see #isPrototype
     */
    public boolean isSingleton(String name) throws NoBeanDefinationException;

    /**
     * Check whether the bean with the given name matches the specified type.
     * More specifically, check whether a {@link #getBean} call for the given
     * name would return an object that is assignable to the specified target
     * type.
     * <p>
     * Translates aliases back to the corresponding canonical bean name. Will
     * ask the parent factory if the bean cannot be found in this factory
     * instance.
     *
     * @param name       the name of the bean to query
     * @param targetType the type to match against
     * @return <code>true</code> if the bean type matches, <code>false</code>
     * if it doesn't match or cannot be determined yet
     * @throws NoBeanDefinationException if there is no bean with the given name
     * @see #getBean
     * @see #getType
     * @since 2.0.1
     */
    public boolean isTypeMatch(String name, Class targetType) throws NoBeanDefinationException;

    /**
     * Determine the type of the bean with the given name. More specifically,
     * determine the type of object that {@link #getBean} would return for the
     * given name.
     * <p>
     * For a {@link FactoryBean}, return the type of object that the
     * FactoryBean creates, as exposed by {@link FactoryBean#getObjectType()}.
     * <p>
     * Translates aliases back to the corresponding canonical bean name. Will
     * ask the parent factory if the bean cannot be found in this factory
     * instance.
     *
     * @param name the name of the bean to query
     * @return the type of the bean, or <code>null</code> if not determinable
     * @throws NoBeanDefinationException if there is no bean with the given name
     * @see #getBean
     * @see #isTypeMatch
     * @since 1.1.2
     */
    public Class getType(String name) throws NoBeanDefinationException;
}
