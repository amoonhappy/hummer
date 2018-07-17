package org.hummer.core.attachement;

import com.mongodb.*;
import com.mongodb.client.MongoIterable;
import com.mongodb.gridfs.GridFS;
import org.apache.commons.lang.StringUtils;
import org.hummer.core.container.HummerContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * MongoDB工具类 Mongo实例代表了一个数据库连接池，即使在多线程的环境中，一个Mongo实例对我们来说已经足够了
 * 注意Mongo已经实现了连接池，并且是线程安全的。 设计为单例模式， 因
 * MongoDB的Java驱动是线程安全的，对于一般的应用，只要一个Mongo实例即可， Mongo有个内置的连接池（默认为10个）
 * 对于有大量写和读的环境中，为了确保在一个Session中使用同一个DB时， DB和DBCollection是绝对线程安全的
 */
@SuppressWarnings("deprecation")
public class MongoDBUtil {

    public static Logger logger = LoggerFactory.getLogger(MongoDBUtil.class);

    private MongoClient mongoClient;

    private static final MongoDBUtil mongoDBUtil = new MongoDBUtil();

    private static String uri;

    private static String host;
    private static int port;

    private static MongoClientOptions options;

    private MongoDBUtil() {
        if (mongoClient == null) {
            mongoClient = getMongoClient();
        }
        logger.debug("=======================调用MongoDBUtil无参构造函数");
    }

	/*// 初始化mongoDB连接池客户端
	private static MongoClient getMongoClient() {
		MongoClientOptions.Builder builder = getMongoClientOptionsBuilder();
		MongoClient client = null;
		String confUri = "mongodb://172.16.23.169:10021/";
		String confHost = "";
		String confPort = "";
		if (confUri == null || confUri.length() == 0) {
			logger.error("[ERROR] Mongo地址为null。请在配置中心中配置mongodb.uri");

			if (StringUtils.isEmpty(confHost) || StringUtils.isEmpty(confPort)) { // 向下兼容。
				logger.error("[ERROR] Mongo的Host,Port配置为null。请在配置中心中配置mongodb.host, mongodb.port; 或者直接配置mongodb.uri", new IllegalArgumentException("mongo地址为空"));
				return client;
			}
		}

		if (!StringUtils.isEmpty(confUri)) {
			MongoClientURI connectionString = new MongoClientURI(confUri, builder);
			uri = confUri;
			options = builder.build();
			client = new MongoClient(connectionString);
		} else if (!StringUtils.isEmpty(confHost) && !StringUtils.isEmpty(confPort)) { // 向下兼容。
			host = confHost;
			port = Integer.parseInt(confPort);
			MongoClientOptions confOptions = builder.build();
			options = confOptions;
			client = new MongoClient(new ServerAddress(confHost, Integer.parseInt(confPort)), confOptions);
		}

		return client;
	}

	private static MongoClientOptions.Builder getMongoClientOptionsBuilder() {
		// 大部分用户使用mongodb都在安全内网下，但如果将mongodb设为安全验证模式，就需要在客户端提供用户名和密码：
		// boolean auth = db.authenticate(myUserName, myPassword);
		Builder builder = new MongoClientOptions.Builder();
		String connectionsPerHost = "300";
		if (connectionsPerHost != null && Integer.parseInt(connectionsPerHost) > 0) {
			builder.connectionsPerHost(Integer.parseInt(connectionsPerHost));// 连接池设置为300个连接,默认为100
		}
		String connectTimeout = "15000";
		if (connectTimeout != null && Integer.parseInt(connectTimeout) > 0) {
			builder.connectTimeout(Integer.parseInt(connectTimeout));// 连接超时，推荐>3000毫秒
		}
		String maxWaitTime = "5000";
		if (maxWaitTime != null && Integer.parseInt(maxWaitTime) > 0) {
			builder.maxWaitTime(Integer.parseInt(maxWaitTime)); // 最大等待可用连接的时间
		}
		String socketTimeout = "0";
		if (socketTimeout != null && Integer.parseInt(socketTimeout) >= 0) {
			builder.socketTimeout(Integer.parseInt(socketTimeout));// 套接字超时时间，0无限制
		}
		String maxConnectionIdleTime = "10000";
		if (maxConnectionIdleTime != null && Integer.parseInt(maxConnectionIdleTime) >= 0) {
			builder.maxConnectionIdleTime(Integer.parseInt(maxConnectionIdleTime));// 连接的最大闲置时间，0：表示无限制。
		}
		String maxConnectionLifeTime = "0";
		if (maxConnectionLifeTime != null && Integer.parseInt(maxConnectionLifeTime) >= 0) {
			builder.maxConnectionLifeTime(Integer.parseInt(maxConnectionLifeTime));// 连接的最大生存时间，0：表示无限制。
		}
		String threadsAllowedToBlockForConnectionMultiplier = "500";
		if (threadsAllowedToBlockForConnectionMultiplier != null && Integer.parseInt(threadsAllowedToBlockForConnectionMultiplier) > 0) {
			// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
			builder.threadsAllowedToBlockForConnectionMultiplier(Integer.parseInt(threadsAllowedToBlockForConnectionMultiplier));
		}
		return builder;
	}

	public static MongoDBUtil getInstall() {
		return getInstance();
	}

	*//**
     * 单例模式，获取MongoDBUtil实例
     *
     * @return
     *//*
	public static MongoDBUtil getInstance() {
		String confUri = "mongodb://172.16.23.169:10021/";
		MongoClientOptions confOptions = getMongoClientOptionsBuilder().build();
		if (confUri == null || confUri.length() == 0) {
			logger.error("[ERROR] Mongo地址为null。请在配置中心中配置mongodb.uri");

//			String confHost = ConfigUtil.get("mongodb.host");
//			String confHost = "";
//			int confPort = Integer.parseInt("");
//			if (StringUtils.isEmpty(confHost) || StringUtils.isEmpty(confPort)) { // 向下兼容。
//				return mongoDBUtil;
//			} else {
//				if (!confHost.equals(host) || confPort != port || !confOptions.equals(options)) {
//					logger.debug("============= rebuild mongoclient");
//					mongoDBUtil.mongoClient.close();
//					mongoDBUtil.mongoClient = getMongoClient();
//				} else {
//					logger.debug("============== reuse mongoclient:" + confHost + ":" + confPort);
//				}
//			}
			return mongoDBUtil;
		}

		if (!confUri.equals(uri) || !confOptions.equals(options)) {
			logger.debug("============= rebuild mongoclient");
			mongoDBUtil.mongoClient.close();
			mongoDBUtil.mongoClient = getMongoClient();
		} else {
			logger.debug("============== reuse mongoclient:" + confUri);
		}
		return mongoDBUtil;
	}

	// ------------------------------------共用方法---------------------------------------------------
	*//**
     * 获取DB实例 - 指定DB
     *
     * @param dbName
     * @return
     *//*
	public DB getDB(String dbName) {
		logger.debug("=======================getDB，数据库名称为：" + dbName);
		if (dbName != null && !"".equals(dbName)) {
			DB db = mongoClient.getDB(dbName);
			return db;
		}
		return null;
	}

	*//**
     * 查询DB下的所有表名
     *//*
	public List<String> getAllCollections(String dbName) {
		Set<String> colls = getDB(dbName).getCollectionNames();
		List<String> _list = new ArrayList<String>();
		for (String s : colls) {
			_list.add(s);
		}
		return _list;
	}

	*//**
     * 获取GridFS实例
     *
     * @return
     *//*
	public GridFS getGridFS() {
		logger.debug("====================即将获取GridFS======================");
		String dbName = "mxm_file";
		String collectionName = "fs";
		logger.debug("获取的 dbName , collectionName ====================>" + dbName + "\t" + collectionName);
		return new GridFS(this.getDB(dbName), collectionName);
	}

	public GridFS getGridFS(String dbName, String collectionName) {
		if (StringUtils.isEmpty(dbName) || StringUtils.isEmpty(collectionName)) {
			logger.error("[ERROR] mongodb的库名跟表名不能为空");
			return null;
		}
		logger.debug("====================即将获取GridFS======================");
		logger.debug("获取的 dbName , collectionName ====================>" + dbName + "\t" + collectionName);
		return new GridFS(this.getDB(dbName), collectionName);
	}

	*//**
     * 获取所有数据库名称列表
     *
     * @return
     *//*
	public MongoIterable<String> getAllDBNames() {
		MongoIterable<String> s = mongoClient.listDatabaseNames();
		return s;
	}

	*//**
     * 删除一个数据库
     *//*
	public void dropDB(String dbName) {
		getDB(dbName).dropDatabase();
	}

	*//**
     * 删除一个
     *
     * @param dbName
     * @param collName
     *//*
	public void dropCollection(String dbName, String collName) {
		getDB(dbName).getCollection(collName).drop();
	}

	*/

    /**
     * MongoClient的close方法会关闭底层连接，MongoClient的实例将变得不可用，我们应该根据程序的需要，适当的调用该方法，释放资源
     * 。
     *//*
	public void close() {
		if (mongoClient != null) {
			mongoClient.close();
			mongoClient = null;
		}
	}*/

    // 初始化mongoDB连接池客户端
    private static MongoClient getMongoClient() {
        MongoClientOptions.Builder builder = getMongoClientOptionsBuilder();
        MongoClient client = null;
        String confUri = (String) HummerContainer.getInstance().getConfigManager().getArchConfig().getValue("mongodb.uri");
        String confHost = (String) HummerContainer.getInstance().getConfigManager().getArchConfig().getValue("mongodb.host");
        String confPort = (String) HummerContainer.getInstance().getConfigManager().getArchConfig().getValue("mongodb.port");
        if (confUri == null || confUri.length() == 0) {
            logger.error("[ERROR] Mongo地址为null。请在配置中心中配置mongodb.uri");

            if (StringUtils.isEmpty(confHost) || StringUtils.isEmpty(confPort)) { // 向下兼容。
                logger.error("[ERROR] Mongo的Host,Port配置为null。请在配置中心中配置mongodb.host, mongodb.port; 或者直接配置mongodb.uri", new IllegalArgumentException("mongo地址为空"));
                return client;
            }
        }

        if (!StringUtils.isEmpty(confUri)) {
            MongoClientURI connectionString = new MongoClientURI(confUri, builder);
            uri = confUri;
            options = builder.build();
            client = new MongoClient(connectionString);
        } else if (!StringUtils.isEmpty(confHost) && !StringUtils.isEmpty(confPort)) { // 向下兼容。
            host = confHost;
            port = Integer.parseInt(confPort);
            MongoClientOptions confOptions = builder.build();
            options = confOptions;
            client = new MongoClient(new ServerAddress(confHost, Integer.parseInt(confPort)), confOptions);
        }

        return client;
    }

    private static MongoClientOptions.Builder getMongoClientOptionsBuilder() {
        // 大部分用户使用mongodb都在安全内网下，但如果将mongodb设为安全验证模式，就需要在客户端提供用户名和密码：
        // boolean auth = db.authenticate(myUserName, myPassword);
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        String connectionsPerHost = (String) HummerContainer.getInstance().getConfigManager().getArchConfig().getValue("mongodb.connectionsPerHost");
        if (connectionsPerHost != null && Integer.parseInt(connectionsPerHost) > 0) {
            builder.connectionsPerHost(Integer.parseInt(connectionsPerHost));// 连接池设置为300个连接,默认为100
        }
        String connectTimeout = (String) HummerContainer.getInstance().getConfigManager().getArchConfig().getValue("mongodb.connectTimeout");
        if (connectTimeout != null && Integer.parseInt(connectTimeout) > 0) {
            builder.connectTimeout(Integer.parseInt(connectTimeout));// 连接超时，推荐>3000毫秒
        }
        String maxWaitTime = (String) HummerContainer.getInstance().getConfigManager().getArchConfig().getValue("mongodb.maxWaitTime");
        if (maxWaitTime != null && Integer.parseInt(maxWaitTime) > 0) {
            builder.maxWaitTime(Integer.parseInt(maxWaitTime)); // 最大等待可用连接的时间
        }
        String socketTimeout = (String) HummerContainer.getInstance().getConfigManager().getArchConfig().getValue("mongodb.socketTimeout");
        if (socketTimeout != null && Integer.parseInt(socketTimeout) >= 0) {
            builder.socketTimeout(Integer.parseInt(socketTimeout));// 套接字超时时间，0无限制
        }
        String maxConnectionIdleTime = (String) HummerContainer.getInstance().getConfigManager().getArchConfig().getValue("mongodb.maxConnectionIdleTime");
        if (maxConnectionIdleTime != null && Integer.parseInt(maxConnectionIdleTime) >= 0) {
            builder.maxConnectionIdleTime(Integer.parseInt(maxConnectionIdleTime));// 连接的最大闲置时间，0：表示无限制。
        }
        String maxConnectionLifeTime = (String) HummerContainer.getInstance().getConfigManager().getArchConfig().getValue("mongodb.maxConnectionLifeTime");
        if (maxConnectionLifeTime != null && Integer.parseInt(maxConnectionLifeTime) >= 0) {
            builder.maxConnectionLifeTime(Integer.parseInt(maxConnectionLifeTime));// 连接的最大生存时间，0：表示无限制。
        }
        String threadsAllowedToBlockForConnectionMultiplier = (String) HummerContainer.getInstance().getConfigManager().getArchConfig().getValue("mongodb.threadsAllowedToBlockForConnectionMultiplier");
        if (threadsAllowedToBlockForConnectionMultiplier != null && Integer.parseInt(threadsAllowedToBlockForConnectionMultiplier) > 0) {
            // 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
            builder.threadsAllowedToBlockForConnectionMultiplier(Integer.parseInt(threadsAllowedToBlockForConnectionMultiplier));
        }
        return builder;
    }

    public static MongoDBUtil getInstall() {
        return getInstance();
    }

    /**
     * 单例模式，获取MongoDBUtil实例
     *
     * @return
     */
    public static MongoDBUtil getInstance() {
        String confUri = (String) HummerContainer.getInstance().getConfigManager().getArchConfig().getValue("mongodb.uri");
        MongoClientOptions confOptions = getMongoClientOptionsBuilder().build();
        if (confUri == null || confUri.length() == 0) {
            logger.error("[ERROR] Mongo地址为null。请在配置中心中配置mongodb.uri");

            String confHost = (String) HummerContainer.getInstance().getConfigManager().getArchConfig().getValue("mongodb.host");
            int confPort = Integer.parseInt((String) HummerContainer.getInstance().getConfigManager().getArchConfig().getValue("mongodb.port"));
            if (StringUtils.isEmpty(confHost) || org.springframework.util.StringUtils.isEmpty(confPort)) { // 向下兼容。
                logger.error("[ERROR] Mongo的Host,Port配置为null。请在配置中心中配置mongodb.host, mongodb.port", new IllegalArgumentException("mongo地址为空"));
                return mongoDBUtil;
            } else {
                if (!confHost.equals(host) || confPort != port || !confOptions.equals(options)) {
                    logger.debug("============= rebuild mongoclient");
                    mongoDBUtil.mongoClient.close();
                    mongoDBUtil.mongoClient = getMongoClient();
                } else {
                    logger.debug("============== reuse mongoclient:" + confHost + ":" + confPort);
                }
            }
            return mongoDBUtil;
        }

        if (!confUri.equals(uri) || !confOptions.equals(options)) {
            logger.debug("============= rebuild mongoclient");
            mongoDBUtil.mongoClient.close();
            mongoDBUtil.mongoClient = getMongoClient();
        } else {
            logger.debug("============== reuse mongoclient:" + confUri);
        }
        return mongoDBUtil;
    }

    // ------------------------------------共用方法---------------------------------------------------

    /**
     * 获取DB实例 - 指定DB
     *
     * @param dbName
     * @return
     */
    public DB getDB(String dbName) {
        logger.debug("=======================getDB，数据库名称为：" + dbName);
        if (dbName != null && !"".equals(dbName)) {
            DB db = mongoClient.getDB(dbName);
            return db;
        }
        return null;
    }

    /**
     * 查询DB下的所有表名
     */
    public List<String> getAllCollections(String dbName) {
        Set<String> colls = getDB(dbName).getCollectionNames();
        List<String> _list = new ArrayList<String>();
        for (String s : colls) {
            _list.add(s);
        }
        return _list;
    }

    /**
     * 获取GridFS实例
     *
     * @return
     */
    public GridFS getGridFS() {
        logger.debug("====================即将获取GridFS======================");
        String dbName = (String) HummerContainer.getInstance().getConfigManager().getArchConfig().getValue("mongodb.dbName");
        String collectionName = (String) HummerContainer.getInstance().getConfigManager().getArchConfig().getValue("mongodb.collectionName");
        logger.debug("获取的 dbName , collectionName ====================>" + dbName + "\t" + collectionName);
        return new GridFS(this.getDB(dbName), collectionName);
    }

    public GridFS getGridFS(String dbName, String collectionName) {
        if (StringUtils.isEmpty(dbName) || StringUtils.isEmpty(collectionName)) {
            logger.error("[ERROR] mongodb的库名跟表名不能为空");
            return null;
        }
        logger.debug("====================即将获取GridFS======================");
        logger.debug("获取的 dbName , collectionName ====================>" + dbName + "\t" + collectionName);
        return new GridFS(this.getDB(dbName), collectionName);
    }

    /**
     * 获取所有数据库名称列表
     *
     * @return
     */
    public MongoIterable<String> getAllDBNames() {
        MongoIterable<String> s = mongoClient.listDatabaseNames();
        return s;
    }

    /**
     * 删除一个数据库
     */
    public void dropDB(String dbName) {
        getDB(dbName).dropDatabase();
    }

    /**
     * 删除一个
     *
     * @param dbName
     * @param collName
     */
    public void dropCollection(String dbName, String collName) {
        getDB(dbName).getCollection(collName).drop();
    }

    /**
     * MongoClient的close方法会关闭底层连接，MongoClient的实例将变得不可用，我们应该根据程序的需要，适当的调用该方法，释放资源
     * 。
     */
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }
}
