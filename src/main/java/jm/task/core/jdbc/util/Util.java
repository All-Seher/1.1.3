package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private static Util INSTANCE;
    private Connection connection;
    private Session session;
    private static boolean isHibernate;


    private Util() {
        //-----------------JDBC-----------------
        if (!isHibernate) {
            String URL = "jdbc:postgresql://localhost:5432/all_seher";
            String USER = "postgres";
            String PASSWORD = "1";

            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            //-----------------Hibernate-----------------
            Properties properties = new Properties();
            properties.setProperty("jakarta.persistence.jdbc.url", "jdbc:postgresql://localhost:5432/all_seher");
            properties.setProperty("jakarta.persistence.jdbc.user", "postgres");
            properties.setProperty("jakarta.persistence.jdbc.password", "1");

            Configuration configuration = new Configuration();
            configuration
                    .setProperties(properties)
                    .addPackage("jm.task.core.jdbc.model")
                    .addAnnotatedClass(User.class);

            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            try {
                session = configuration.buildSessionFactory(registry).openSession();
            } catch (Exception e) {
                StandardServiceRegistryBuilder.destroy(registry);
                throw new RuntimeException(e);
            }
        }
    }

    public static void closeDBConnection() {
        if (!isHibernate) {
            closeConnection();
        } else {
            closeSession();
        }
    }

    // Возвращает единственный экземпляр класса Connection
    // реализуйте настройку соеденения с БД
    public static Connection getConnection() {
        if (INSTANCE == null) {
            INSTANCE = new Util();
        }

        return INSTANCE.connection;
    }

    private static void closeConnection() {
        try {
            INSTANCE.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Session getSession() {
        isHibernate = true;
        if (INSTANCE == null) {
            INSTANCE = new Util();
        }

        return INSTANCE.session;
    }

    private static void closeSession() {
        INSTANCE.session.close();
    }
}
