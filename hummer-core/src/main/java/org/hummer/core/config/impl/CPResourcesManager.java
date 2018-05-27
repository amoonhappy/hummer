package org.hummer.core.config.impl;

import org.apache.commons.lang.StringUtils;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CPResourcesManager {
    static final String CORE_PREFIX = "core/config/";
    static final String LOCAL_PREFIX = "local/config/";
    private static Logger log = Log4jUtils.getLogger(CPResourcesManager.class);
    private static CPResourcesManager instance = new CPResourcesManager();

    private CPResourcesManager() {
    }

    public static CPResourcesManager getInstance() {
        return instance;
    }

    public InputStream getCore(String comp_id) throws IOException {
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

    private JarFile getJar(String comp_id) {
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
        InputStream in;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        String temp = LOCAL_PREFIX + fileName;
        URL url = cl.getResource(temp);
        assert url != null;
        in = url.openStream();
        return in;
    }
}
