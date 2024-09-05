package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.SqlQuery;
import jm.task.core.jdbc.util.Util;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@NoArgsConstructor
public class UserDaoHibernateImpl implements UserDao {
    private final Session session = Util.getSession();

    @Override
    public void createUsersTable() {
        executeTransaction(SqlQuery.CREATE_TABLE.getKey());
    }

    @Override
    public void dropUsersTable() {
        executeTransaction(SqlQuery.DROP_TABLE.getKey());
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = User.builder().
                name(name).
                lastName(lastName)
                .age(age)
                .build();

        Transaction transaction = session.beginTransaction();
        session.persist(user);
        transaction.commit();
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = session.beginTransaction();
        session.createMutationQuery("delete from User where id = :id").setParameter("id", id).executeUpdate();
        transaction.commit();
    }

    @Override
    public List<User> getAllUsers() {
        return session.createQuery("from User", User.class).list();
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = session.beginTransaction();
        session.createMutationQuery("delete from User").executeUpdate();
        transaction.commit();
    }

    private void executeTransaction(String sql) {
        Transaction transaction = session.beginTransaction();
        session.createNativeQuery(sql, User.class).executeUpdate();
        transaction.commit();
        session.clear();
    }
}
