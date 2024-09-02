package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@SuppressWarnings("deprecation")
public class UserDaoHibernateImpl implements UserDao {
    private final Session session = Util.getSessionFactory().openSession();

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users
                (
                    id         SERIAL PRIMARY KEY,
                    first_name VARCHAR(20) NOT NULL,
                    last_name  VARCHAR(20) NOT NULL,
                    age        SMALLINT    NOT NULL
                )
                """;

        executeTransaction(sql);
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";

        executeTransaction(sql);
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        Transaction transaction = session.beginTransaction();
        session.persist(user);
        transaction.commit();
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = session.beginTransaction();
        session.createQuery("delete from User where id = :id").setParameter("id", id).executeUpdate();
        transaction.commit();
    }

    @Override
    public List<User> getAllUsers() {
        return session.createQuery("from User", User.class).list();
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = session.beginTransaction();
        session.createQuery("delete from User").executeUpdate();
        transaction.commit();
    }

    private void executeTransaction(String sql) {
        Transaction transaction = session.beginTransaction();
        session.createNativeQuery(sql).executeUpdate();
        transaction.commit();
    }
}
