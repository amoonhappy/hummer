package org.hummer.boot;

import org.eclipse.jetty.jsp.JettyJspServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

//import org.apache.log4j.xml.DOMConfigurator;

public class HummerBootServer {
    public static final Logger log = Log4jUtils.getLogger(HummerBootServer.class);
    public static final int PORT = 8080;

    // web访问的根路径http://ip:port/，相当于项目名,/即忽略项目名
    public static final String CONTEXT = "/";

    private static final String DEFAULT_WEBAPP_PATH = "./hummer-boot/src/main/webapp";

    public static Server createServerIn(int port) throws IOException {

        // Establish Scratch directory for the servlet context (used by JSP compilation)
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File scratchDir = new File(tempDir.toString(), "embedded-jetty-jsp");

        if (!scratchDir.exists()) {
            if (!scratchDir.mkdirs()) {
                throw new IOException("Unable to create scratch directory: " + scratchDir);
            }
        }


        // 创建Server
        Server server = new Server();
        // 添加ThreadPool
        QueuedThreadPool queuedThreadPool = new QueuedThreadPool();
        queuedThreadPool.setName("queuedTreadPool");
        queuedThreadPool.setMinThreads(10);
        queuedThreadPool.setMaxThreads(200);
        server.addBean(queuedThreadPool);
        //server.setThreadPool(queuedThreadPool);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        connector.setSoLingerTime(10000);
        server.addConnector(connector);

        WebAppContext webContext = new WebAppContext(DEFAULT_WEBAPP_PATH, CONTEXT);
        // Manually call JettyJasperInitializer on context startup

        // Create / Register JSP Servlet (must be named "jsp" per spec)
        ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
        holderJsp.setInitOrder(0);
        holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
        holderJsp.setInitParameter("fork", "false");
        holderJsp.setInitParameter("xpoweredBy", "false");
        holderJsp.setInitParameter("compilerTargetVM", "1.8");
        holderJsp.setInitParameter("compilerSourceVM", "1.8");
        holderJsp.setInitParameter("keepgenerated", "true");
        webContext.addServlet(holderJsp, "*.jsp");

        // JSP 相关
//        webContext.setTempDirectory(tempDir);
//        webContext.setAttribute("javax.servlet.context.tempdir", tempDir);
        // 下面是为解决此次问题增加的代码
//        webContext.setAttribute("org.eclipse.jetty.containerInitializers", Arrays.asList(
//                new ContainerInitializer(new JettyJasperInitializer(), null)));
//        webContext.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
        webContext.setDescriptor("./src/main/webapp/WEB-INF/web.xml");
        webContext.setResourceBase(DEFAULT_WEBAPP_PATH);
        webContext.setClassLoader(Thread.currentThread().getContextClassLoader());

        server.setHandler(webContext);

        return server;
    }

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
//        IHummerContainer hummerContainer = HummerContainer.getInstance();
//        IBusinessServiceManager bsv = hummerContainer.getServiceManager();
        log.info("Starting Server.....");
        Server server = createServerIn(PORT);
        //server.stop();
//        server.setHandler(new HelloServlet());
        server.start();
        server.join();
    }
}
