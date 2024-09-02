package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.SqlQuery;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@SuppressWarnings("deprecation")
public class UserDaoHibernateImpl implements UserDao {
    private final Session session = Util.getDBConnection(UserDaoHibernateImpl.class);
    private final SqlQuery sqlQuery = new SqlQuery();

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {

        executeTransaction(sqlQuery.getQuery("createTable"));
    }

    @Override
    public void dropUsersTable() {
        executeTransaction(sqlQuery.getQuery("dropTable"));
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
        session.clear();
    }
}
