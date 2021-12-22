package com.hoppinzq.service.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * @ClassName： BeetlProperties
 * @Description： 这个类用来映射yml文件的自定义配置属性，用到commons-lang3相关依赖
 * @Author： xie jing
 * @Date： 2019/5/23
 * @Vision： 1.0
 */

@ConfigurationProperties(prefix = BeetlProperties.BEETLCONF_PREFIX)
public class BeetlProperties {

    public static final String BEETLCONF_PREFIX = "beetl";

    private String delimiterStatementStart;

    private String delimiterStatementEnd;


    @Value("${spring.mvc.view.prefix}")
    private String prefix;

    public static String getBeetlconfPrefix() {
        return BEETLCONF_PREFIX;
    }

    public String getDelimiterStatementStart() {
        return delimiterStatementStart;
    }

    public void setDelimiterStatementStart(String delimiterStatementStart) {
        this.delimiterStatementStart = delimiterStatementStart;
    }

    public String getDelimiterStatementEnd() {
        return delimiterStatementEnd;
    }

    public void setDelimiterStatementEnd(String delimiterStatementEnd) {
        this.delimiterStatementEnd = delimiterStatementEnd;
    }


    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Properties getProperties() {
        Properties properties = new Properties();
        if (StringUtils.isNotBlank(delimiterStatementStart)) {
            if (delimiterStatementStart.startsWith("\\")) {
                delimiterStatementStart = delimiterStatementStart.substring(1);
            }
            properties.setProperty("DELIMITER_STATEMENT_START", delimiterStatementStart);
        }
        if (StringUtils.isNotBlank(delimiterStatementEnd)) {
            properties.setProperty("DELIMITER_STATEMENT_END", delimiterStatementEnd);
        } else {
            properties.setProperty("DELIMITER_STATEMENT_END", "null");
        }

        return properties;
    }
}
