package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static Util INSTANCE;
    Connection connection;

    private Util() {
        String URL = "jdbc:postgresql://localhost:5432/postgres";
        String USER = "postgres";
        String PASSWORD = "1";

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Возвращает единственный экземпляр класса Connection
    // реализуйте настройку соеденения с БД
    public static Connection getConnection() {
        if(INSTANCE == null) {
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

}
