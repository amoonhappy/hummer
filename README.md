**# Hummer Framework**

a lightweight java framework based on Spring MVC + Hummer + Mybatis with below core features:
一个轻量级java后端架构，前端采用SpringMVC，后端采用Hummer+Spring，持久层采用Mybatis+Mybatis-Spring

**1. Core Module**
1. 【产品模式支持】支持基于core和local的产品代码配置能力，core作为标准产品模块，local作为依赖于core的定制模块，提供类似java中的overwrite模式的Bean实例化容器管理
2. 【AOP】提供基于CGLIB的HummerBean切面能力，HummerFramework内置4种拦截器，并采用Perl表达式进行方法名匹配
    a. TransactionInterceptor - 事务处理拦截器 
    b. CacheInterceptor - 缓存拦截器
    c. CacheEvictInterceptor - 缓存失效拦截器
    d. PerformanceTraceInterceptor - 性能日志拦截器
2. 【IOC】依赖注入，基于CGLIB和Spring提供HummerBean的发现，注册，自动装载，XML手动装载能力
    a. XML Bean 配置管理
    b. @Autowired声明+配置 - Bean自动装载（基于二级缓存解决Bean之间循环依赖的问题）
    c. @HummerPostAutowired+@Autowired声明 - 解决与SpringBean之间的相互依赖关系
    d. SpringBean的反向注入实现 - SpringMVC的Controller中会依赖HummerBean，支持自动装载
3. 【事务】基于Transaction拦截器和@Transactional申明提供7中事务类型的支持
    a. PROPAGATION_REQUIRED
    b. RROPAGATION_REQUIRES_NEW
    c. PROPAGATION_NESTED
    d. PROPAGATION_SUPPORTS
    e. PROPAGATION_NOT_SUPPORTED
    f. PROPAGATION_NEVER
    g. PROPAGATION_MANDATORY
4. 【缓存】基于Redis，通过下面几种Annotation和一个ICacheable接口提供灵活的Bean方法级别的缓存和缓存失效机制
    a. CacheEvict (cacheName, key) - 祛除特定名称和key的Cache
    b. CacheKey (cacheName,key) - 存放特定名称和key的Cache
    c. ICacheable - 空接口标明HummerBean会采用Cache拦截器
5. 【集成】提供下列开源工具的集成
    a. Spring/Spring MVC - 包括：Hummer初始化，双向注入等
    b. Mybatis/Mybatis Spring - 包括：Mapper接口自动注入
    c. Velocity - 在Spring 5.0版本之后不支持Velocity，在Hummer中加入支持
    d. Druid/Hikari - 数据链接库3种配置支持，并注入到Mybatis中
    e. 提供Shiro集成工具
    d. 提供Quartz集成工具
6. 【代码生成】通过Mybatis Generator和Velocity实现数据库表到各种代码的自动生成：
    a. Mybatis Interfaces and Mapper
    b. Spring MVC Controller
    d. HummerBean Services
    e. Dao and Dto
    f. Models
1. core and local concept implemented which is a flexible way to support Product + Customization development
2. AOP based on CGLIB implementation
3. Simple Transaction Management Supported (7 transaction types), by Transactional Annotation and Transaction AOP apis
4. Mybatis integrated + configurable DataSource (Default, Druid and Hikari) support via simple configuration
5. Method Level Cache (based on Redis) implemented, by ICacheable interface and CacheEvict(s) Annotation
6. Spring MVC integrated
7. Swagger2 Integrated
8. Add Velocity Support for Spring V5.0.x
9. 2 approaches to init Bean, a) xml config b) annotation config for IOC

**Code Generation Module**
1. Mybatis Generator integrated
2. Customized Code Generator added, for Controller/Services/Model etc, easy to extend 
