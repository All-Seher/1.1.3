package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class Main {


    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        userService.dropUsersTable();                                           //проверка на отсутствие исключения, при удалении несуществующей таблицы
        userService.createUsersTable();
        userService.createUsersTable();                                         //проверка на отсутствие исключения, при создании уже существующей таблицы

        userService.saveUser("Ivan", "Ivanov", (byte) 20);
        userService.saveUser("Petr", "Petrov", (byte) 21);
        userService.saveUser("Sergey", "Sergeev", (byte) 22);
        userService.saveUser("Vasiliy", "Vasilev", (byte) 23);

        userService.removeUserById(1L);                                          //удаление пользователя по id

        List<User> users = userService.getAllUsers();
        users.forEach(System.out::println);

        userService.cleanUsersTable();
        userService.dropUsersTable();

        Util.closeDBConnection();

    }
}
