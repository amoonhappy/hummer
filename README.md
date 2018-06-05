# Hummer Framework

a lightweighted java framework based on Spring MVC + Hummer + Mybatis with below core features:

####Core Module
1. core and local concept implemented which is a flexiable way to support Product + Customization development
2. AOP based on CGLIB implementation
3. Simple Transaction Management Supported (7 transaction types), by Transactional Annotation and Transaction AOP apis
4. Mybatis integrated + configurable DataSource (Default, Druid and Hikari) support via simple configuration
5. Method Level Cache (based on Redis) implemented, by ICacheable interface and CacheEvict(s) Annotation
6. Spring MVC integrated

####Code Generation Module
1. Mybatis Generator integrated
