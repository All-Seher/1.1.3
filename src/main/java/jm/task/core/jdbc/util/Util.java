package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
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
    private final Connection connection;
    private final SessionFactory sessionFactory;


    private Util() {
        //-----------------JDBC-----------------
        String URL = "jdbc:postgresql://localhost:5432/all_seher";
        String USER = "postgres";
        String PASSWORD = "1";

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
            sessionFactory = configuration.buildSessionFactory(registry);
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw new RuntimeException(e);
        }
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

    // Возвращает единственный экземпляр класса Connection
    // реализуйте настройку соеденения с БД
    public static Connection getConnection() {
        if (INSTANCE == null) {
            INSTANCE = new Util();
        }

        return INSTANCE.connection;
    }

    public static void closeConnection() {
        try {
            INSTANCE.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (INSTANCE == null) {
            INSTANCE = new Util();
        }

        return INSTANCE.sessionFactory;
    }

    public static void closeSessionFactory() {
        INSTANCE.sessionFactory.close();
    }
}
