package jm.task.core.jdbc.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SqlQuery {
    private Properties properties;

    public SqlQuery() {
        properties = new Properties();
        properties = loadQuery();
    }

    private Properties loadQuery() {
        try (FileInputStream input = new FileInputStream("src/main/resources/sql.properties")) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }

    public String getQuery(String key) {
        return properties.getProperty(key);
    }
}
