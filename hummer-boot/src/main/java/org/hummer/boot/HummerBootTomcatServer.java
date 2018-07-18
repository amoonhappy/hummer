package org.hummer.boot;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.hummer.core.config.intf.IConfiguration;
import org.hummer.core.container.HummerContainer;
import org.hummer.core.util.Log4jUtils;
import org.hummer.core.util.StringUtil;
import org.slf4j.Logger;

//TODO: to support https?
public class HummerBootTomcatServer {
    private static final Logger log = Log4jUtils.getLogger(HummerBootJettyServer.class);
    private static final String HUMMER_BOOT_SERVER_PORT = "hummer.boot.server.port";
    private static final String HUMMER_BOOT_SERVER_HOME = "hummer.boot.server.home";
    private static final String HUMMER_BOOT_SERVER_WEBAPP = "hummer.boot.server.webapp";
    private static final String DEFAULT_PORT = "8080";
    private static final String DEFAULT_HOME = "/Users/jeff/IdeaProjects/hummer/hummer-boot";
    private static final String DEFAULT_WEBAPP = "/src/main/webapp";
    private static final String HUMMER_BOOT_SERVER_MIN_THREADS = "hummer.boot.server.minThreads";
    private static final String HUMMER_BOOT_SERVER_MAX_THREADS = "hummer.boot.server.maxThreads";
    private static final String DEFAULT_MIN_THREADS = "10";
    private static final String DEFAULT_MAX_THREADS = "200";
    private static final String HUMMER_BOOT_SERVER_MAX_IDLE_TIME = "hummer.boot.server.maxIdleTime";
    private static final String HUMMER_BOOT_SERVER_CONTEXT_ROOT = "hummer.boot.server.contextRoot";
    private static final String WEB_XML_PATH = "/WEB-INF/web.xml";
    private static final String DEFAULT_MAX_IDLE_TIME = "10000";
    private static final String DEFAULT_CONTEXT_ROOT = "/";
//    static final int port = 9080;
//    static final String docBase = "e:/tmp/tomcat";

    public static void main(String[] args) throws Exception {
        HummerContainer container = HummerContainer.getInstance();

        IConfiguration config = container.getConfigManager().getArchConfig();
        String configPort = (String) config.getValue(HUMMER_BOOT_SERVER_PORT);
        String configBootHome = (String) config.getValue(HUMMER_BOOT_SERVER_HOME);
        String configWebHome = (String) config.getValue(HUMMER_BOOT_SERVER_WEBAPP);
        String configMinThread = (String) config.getValue(HUMMER_BOOT_SERVER_MIN_THREADS);
        String configMaxThread = (String) config.getValue(HUMMER_BOOT_SERVER_MAX_THREADS);
        String configMaxIdleTime = (String) config.getValue(HUMMER_BOOT_SERVER_MAX_IDLE_TIME);
        String configContextRoot = (String) config.getValue(HUMMER_BOOT_SERVER_CONTEXT_ROOT);

        int port = Integer.parseInt(DEFAULT_PORT);
        if (StringUtil.isNumeric(configPort)) {
            port = Integer.parseInt(configPort);
        }

        if (StringUtil.isEmpty(configWebHome)) {
            configWebHome = DEFAULT_WEBAPP;
        }
        if (StringUtil.isEmpty(configBootHome)) {
            configBootHome = DEFAULT_HOME;
        }

        // web访问的根路径http://ip:port/，相当于项目名,/即忽略项目名
        String webrootIndex = configBootHome + configWebHome;
        String descriptor = webrootIndex + WEB_XML_PATH;


        if (!StringUtil.isNumeric(configMinThread)) {
            configMinThread = DEFAULT_MIN_THREADS;
        }
        if (!StringUtil.isNumeric(configMaxThread)) {
            configMaxThread = DEFAULT_MAX_THREADS;
        }
        if (!StringUtil.isNumeric(configMaxIdleTime)) {
            configMaxIdleTime = DEFAULT_MAX_IDLE_TIME;
        }
        if (StringUtil.isEmpty(configWebHome)) {
            configContextRoot = DEFAULT_CONTEXT_ROOT;
        }


        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(configBootHome);

        tomcat.setPort(port);
        tomcat.getHost().setAutoDeploy(true);
        tomcat.getHost().setAppBase(webrootIndex);
        tomcat.getHost().setDeployOnStartup(true);
        tomcat.enableNaming();
        Context webContext = tomcat.addWebapp(configContextRoot, webrootIndex);
        webContext.setAltDDName(descriptor);
        tomcat.start();
        tomcat.getServer().await();
    }

}
