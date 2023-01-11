package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static SessionFactory sessionFactory = Util.getSessionFactory();

    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS Users ( id BIGINT PRIMARY KEY AUTO_INCREMENT,"
                    + "name VARCHAR(45), lastName VARCHAR(45), age INT(3))").executeUpdate();
            session.getTransaction().commit();

        } catch (RuntimeException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
            session.getTransaction().commit();

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = sessionFactory.getCurrentSession();
        try  {
            User user = new User(name, lastName, age);
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
        session.close();
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = sessionFactory.getCurrentSession();
        List<User> list = null;
        try {
            session.beginTransaction();
            list = session.createQuery("from User ").getResultList();
            session.getTransaction().commit();
            return list;
        } catch (RuntimeException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        session.close();
        return list;
    }

    @Override
    public void cleanUsersTable() {
        Session session = sessionFactory.getCurrentSession();
        {
            try {
                session.beginTransaction();
                session.createQuery("delete User").executeUpdate();
                session.getTransaction().commit();
            } catch (RuntimeException e) {
                session.getTransaction().rollback();
            }
            session.close();
        }
    }
}
