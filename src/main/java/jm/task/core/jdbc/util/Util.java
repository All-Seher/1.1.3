package jm.task.core.jdbc.util;

import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
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
    private Connection connection = null;
    private Session session = null;
    private static boolean isHibernate = false;


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
            Configuration configuration = new Configuration();
            configuration
                    .setProperties(loadProperties())
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

    public static <T> T getDBConnection(Class<?> clazz) {

        if (UserDaoJDBCImpl.class.isAssignableFrom(clazz)) {
            return (T) getConnection();
        }

        if (UserDaoHibernateImpl.class.isAssignableFrom(clazz)) {
            isHibernate = true;
            return (T) getSession();
        }

        throw new IllegalArgumentException("Unsupported class: " + clazz.getName());
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
    private static Connection getConnection() {
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

    private static Session getSession() {
        if (INSTANCE == null) {
            INSTANCE = new Util();
        }

        return INSTANCE.session;
    }

    private static void closeSession() {
        INSTANCE.session.close();
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("src\\main\\resources\\hibernate.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.getMessage();
        }
        return properties;
    }
}
