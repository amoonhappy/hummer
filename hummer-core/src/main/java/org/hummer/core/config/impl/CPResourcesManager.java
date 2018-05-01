package org.hummer.core.config.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hummer.core.util.Log4jUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CPResourcesManager {
    public static final String CORE_PREFIX = "core/config/";
    public static final String LOCAL_PREFIX = "local/config/";
    private static Logger log = Log4jUtils.getLogger(CPResourcesManager.class);
    private static CPResourcesManager instance = new CPResourcesManager();
    private Map<String, JarFile> compCache = new HashMap<String, JarFile>();

    private CPResourcesManager() {
    }

    public static CPResourcesManager getInstance() {
        return instance;
    }

    public InputStream getCore(String comp_id, String fileName) throws IOException {
        InputStream in = null;

        JarFile jarFile = getJar(comp_id);

        Enumeration<JarEntry> en = jarFile.entries();

        while (en.hasMoreElements()) {
            JarEntry je = en.nextElement();
            if ((StringUtils.contains(je.getName(), CORE_PREFIX) || StringUtils.contains(je.getName(), LOCAL_PREFIX))
                    && (je.getName().endsWith(".properties") || je.getName().endsWith(".xml"))) {
                log.info("-------:" + je.getName());
                in = jarFile.getInputStream(je);
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info("!!!!!!!!:" + line);
                }
                reader.close();
            }
        }

        return in;
    }

    public JarFile getJar(String comp_id) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        URL url = cl.getResource(comp_id + ".id");
        JarFile jarFile = null;

        if (url != null) {
            String temp1 = url.getPath();
            String jarURL = StringUtils.replace(temp1, "file:/", "");
            jarURL = StringUtils.replace(jarURL, "!/" + comp_id + ".id", "");


            try {
                jarFile = new JarFile(jarURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jarFile;
    }

    public InputStream getLocal(String fileName) throws IOException {
        InputStream in = null;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        String temp = LOCAL_PREFIX + fileName;
        URL url = cl.getResource(temp);
        in = url.openStream();
        return in;
    }
}
