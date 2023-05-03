package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static jm.task.core.jdbc.util.Util.getHibernateConnection;

@SuppressWarnings("SqlResolve")
public class UserDaoHibernateImpl implements UserDao {
    private static final Logger LOGGER = Logger.getLogger(UserDaoHibernateImpl.class.getName());

    public UserDaoHibernateImpl() {

    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public void createUsersTable() {
        try {
            Session session = getHibernateConnection();
            session.getTransaction().begin();

            session.createSQLQuery(""" 
                    create table if not exists usersHiber(
                    id        serial,
                    name      varchar(100) not null,
                    lastName  varchar(100) not null,
                    age smallint
                    );
                    """).executeUpdate();

            LOGGER.info("Создана база данных dbITM с таблицей usersHiber");
            session.close();
        } catch (Exception e) {
            LOGGER.warning("Сбой при создании базы данных dbITM с таблицей usersHiber");
            LOGGER.warning(e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        try {
            Session session = getHibernateConnection();
            session.getTransaction().begin();
            session.createSQLQuery(
                    "DROP TABLE usersHiber").executeUpdate();
            LOGGER.info("Удалена таблица usersHiber в базе данных dbITM ");
            session.close();
        } catch (Exception e) {
            LOGGER.warning("Сбой при удалении таблицы usersHiber в базе данных dbITM");
            LOGGER.warning(e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try {
            User user = new User(name, lastName, age);
            Session session = getHibernateConnection();
            session.getTransaction().begin();
            session.persist(user);
            session.getTransaction().commit();
            session.close();
            System.out.printf("User с именем – %s добавлен в базу данных \n", name);
            LOGGER.info("Пользователь " + name + ": cоздан и добавлен в базу данных");
        } catch (Exception e) {
            LOGGER.warning("Ошибка при добавлении записи в базу данных");
            LOGGER.warning(e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
            Session session = getHibernateConnection();
            session.getTransaction().begin();
            Query query = session.createQuery("delete from User u where u.id = :userId");
            query.setParameter("userId", id);
            query.executeUpdate();
            LOGGER.info("Пользователь удален из базы данных");
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            LOGGER.warning("Ошибка при удалении пользователя по Id");
            LOGGER.warning(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getAllUsers() {
        try {
            Session session = getHibernateConnection();
            session.getTransaction().begin();
            Query query = session.createQuery("select u from User u");
            List<User> users = query.getResultList();
            LOGGER.info("Прочитан список пользователей из БД");
            session.close();
            return users;
        } catch (Exception e) {
            LOGGER.warning("Ошибка при считывании всех пользователей из БД");
            LOGGER.warning(e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public void cleanUsersTable() {
        try {
            Session session = getHibernateConnection();
            session.getTransaction().begin();
            Query query = session.createQuery("delete from User u");
            query.executeUpdate();
            LOGGER.info("Все пользователи удалены");
            session.close();
        } catch (Exception e) {
            LOGGER.warning("Ошибка при удалении всех пользователей из БД");
            LOGGER.warning(e.getMessage());
        }
    }
}
