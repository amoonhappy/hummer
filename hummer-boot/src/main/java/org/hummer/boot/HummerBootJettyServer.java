package org.hummer.boot;

//import org.eclipse.jetty.jsp.JettyJspServlet;
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.server.ServerConnector;
//import org.eclipse.jetty.servlet.ServletHolder;
//import org.eclipse.jetty.util.thread.QueuedThreadPool;
//import org.eclipse.jetty.webapp.WebAppContext;
import org.hummer.core.config.intf.IConfiguration;
import org.hummer.core.container.impl.HummerContainer;
import org.hummer.core.container.intf.IHummerContainer;
import org.hummer.core.util.Log4jUtils;
import org.hummer.core.util.StringUtil;
import org.slf4j.Logger;

public class HummerBootJettyServer {
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

//    private static final String WEBROOT_INDEX = "./hummer-boot/src/main/webapp";

    //private static Server createServerIn() {
    private static Object createServerIn() {
        IHummerContainer container = HummerContainer.getInstance();

        IConfiguration config = container.getConfigManager().getArchConfig();
        String configPort = (String) config.getValue(HUMMER_BOOT_SERVER_PORT);
        String configBootHome = (String) config.getValue(HUMMER_BOOT_SERVER_HOME);
        String configWebHome = (String) config.getValue(HUMMER_BOOT_SERVER_WEBAPP);
        String configMinThread = (String) config.getValue(HUMMER_BOOT_SERVER_MIN_THREADS);
        String configMaxThread = (String) config.getValue(HUMMER_BOOT_SERVER_MAX_THREADS);
        String configMaxIdleTime = (String) config.getValue(HUMMER_BOOT_SERVER_MAX_IDLE_TIME);
        String configContextRoot = (String) config.getValue(HUMMER_BOOT_SERVER_CONTEXT_ROOT);

        int port = Integer.parseInt(DEFAULT_PORT);

        log.debug("[hummer-cfg-mail.properties]:{}={}", HUMMER_BOOT_SERVER_PORT, configPort);
        log.debug("[hummer-cfg-mail.properties]:{}={}", HUMMER_BOOT_SERVER_HOME, configBootHome);
        log.debug("[hummer-cfg-mail.properties]:{}={}", HUMMER_BOOT_SERVER_WEBAPP, configWebHome);
        log.debug("[hummer-cfg-mail.properties]:{}={}", HUMMER_BOOT_SERVER_MIN_THREADS, configMinThread);
        log.debug("[hummer-cfg-mail.properties]:{}={}", HUMMER_BOOT_SERVER_MAX_THREADS, configMaxThread);
        log.debug("[hummer-cfg-mail.properties]:{}={}", HUMMER_BOOT_SERVER_MAX_IDLE_TIME, configMaxIdleTime);
        log.debug("[hummer-cfg-mail.properties]:{}={}", HUMMER_BOOT_SERVER_CONTEXT_ROOT, configContextRoot);

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


//        // Establish Scratch directory for the servlet context (used by JSP compilation)
//        File tempDir = new File(System.getProperty("java.io.tmpdir"));
//        File scratchDir = new File(tempDir.toString(), "embedded-jetty-jsp");
//
//        if (!scratchDir.exists()) {
//            if (!scratchDir.mkdirs()) {
//                throw new IOException("Unable to create scratch directory: " + scratchDir);
//            }
//        }

        // 创建Server
//        Server server = new Server();
//        // 添加ThreadPool
//        QueuedThreadPool queuedThreadPool = new QueuedThreadPool();
//        queuedThreadPool.setName("queuedTreadPool");
//        queuedThreadPool.setMinThreads(Integer.parseInt(configMinThread));
//        queuedThreadPool.setMaxThreads(Integer.parseInt(configMaxThread));
//        server.addBean(queuedThreadPool);
//        ServerConnector connector = new ServerConnector(server);
//        connector.setPort(port);
//        connector.setSoLingerTime(Integer.parseInt(configMaxIdleTime));
//        server.addConnector(connector);
//
//        WebAppContext webContext = new WebAppContext(webrootIndex, configContextRoot);
//
//        // JSP support
//        enableJSPSupport(webContext);
//
////        webContext.setTempDirectory(tempDir);
////        webContext.setAttribute("javax.servlet.context.tempdir", tempDir);
//
//        // Load web.xml
//        webContext.setDescriptor(descriptor);
//
//        //webContext.setDescriptor("./src/main/webapp/WEB-INF/web.xml");
//        webContext.setResourceBase(webrootIndex);
//        webContext.setClassLoader(Thread.currentThread().getContextClassLoader());
//
//        server.setHandler(webContext);
//        log.info(server.dump());
//        return server;
        return null;
    }

//    private static void enableJSPSupport(WebAppContext webContext) {
//        // Create / Register JSP Servlet (must be named "jsp" per spec)
//        ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
//        holderJsp.setInitOrder(0);
//        holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
//        holderJsp.setInitParameter("fork", "false");
//        holderJsp.setInitParameter("xpoweredBy", "false");
//        holderJsp.setInitParameter("compilerTargetVM", "1.8");
//        holderJsp.setInitParameter("compilerSourceVM", "1.8");
//        holderJsp.setInitParameter("keepgenerated", "true");
//        webContext.addServlet(holderJsp, "*.jsp");
//    }
//
//    public static void main(String[] args) throws Exception {
//        log.info("Starting Embedded Hummer Jetty Server.....");
//        Server server = createServerIn();
//        server.stop();
//        server.start();
//        server.join();
//    }
}
