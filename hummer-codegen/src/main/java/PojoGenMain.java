import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PojoGenMain {
    public static void main(String[] args) {
        final Logger log = LoggerFactory.getLogger("test");
        List<String> warnings = new ArrayList<>();
        boolean overwrite = true;
        String genCfg = "/generatorConfig.xml";
        File configFile = new File(PojoGenMain.class.getResource(genCfg).getFile());
        log.info("File is {}", configFile);
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = null;
        try {
            config = cp.parseConfiguration(configFile);
        } catch (IOException | XMLParserException e) {
            e.printStackTrace();
        }
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = null;
        try {
            assert config != null;
            myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try {
            assert myBatisGenerator != null;
            myBatisGenerator.generate(null);
            log.info("Generation Successful! ");
        } catch (SQLException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}  