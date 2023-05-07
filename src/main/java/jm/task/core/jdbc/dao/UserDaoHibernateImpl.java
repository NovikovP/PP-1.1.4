package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static jm.task.core.jdbc.util.Util.getHibernateConnection;

@SuppressWarnings("SqlResolve")
public class UserDaoHibernateImpl implements UserDao {
    private static final Logger LOGGER = Logger.getLogger(UserDaoHibernateImpl.class.getName());
    private Session session = Util.getSession();
    //private final Session session = Util.getSession();
    //private final Session session = Util.getHibernateConnection();


    public UserDaoHibernateImpl() {

    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public void createUsersTable() {
        try {
            session.beginTransaction();//проверит, есть ли уже существующая транзакция, если да, то не создаст новую транзакцию
            session.createSQLQuery("""
                CREATE TABLE IF NOT EXISTS "users_hiber"(
                id        SERIAL,
                name      VARCHAR(100) NOT NULL,
                lastName  VARCHAR(100) NOT NULL,
                age SMALLINT
                ); """).executeUpdate();//транзакция.фиксация();
            session.getTransaction().commit();//возвращаемый результат;
            //session.close();

            LOGGER.info("Создана база данных dbITM с таблицей users_hiber");

        } catch (Exception e) {
            LOGGER.warning("Сбой при создании базы данных dbITM с таблицей users_hiber");
            LOGGER.warning(e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        try {
//            session.getTransaction().begin();
//            session.beginTransaction();
            session.getTransaction();

            session.createSQLQuery("""
                            DROP TABLE IF EXISTS "users_hiber" ;
                            """).executeUpdate();
            session.getTransaction().commit();

            LOGGER.info("Удалена таблица users_hiber в базе данных dbITM ");
            session.close();
        } catch (Exception e) {
            LOGGER.warning("Сбой при удалении таблицы users_hiber в базе данных dbITM");
            LOGGER.warning(e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try {
            User user = new User(name, lastName, age);
            session.beginTransaction();
            //session.getTransaction().begin();

            session.save(user);
            session.getTransaction().commit();
            //session.close();
            LOGGER.info("Пользователь " + name + ": cоздан и добавлен в базу данных");
        } catch (Exception e) {
            LOGGER.warning("Ошибка при добавлении записи в базу данных");
            LOGGER.warning(e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
//            Session session = getHibernateConnection();
            session.getTransaction();
            session.getTransaction().begin();
            Query query = session.createQuery("DELETE FROM User u WHERE u.id = :userId");
            query.setParameter("userId", id);
            query.executeUpdate();
            LOGGER.info("Пользователь удален из базы данных");
            session.getTransaction().commit();
            //session.close();
        } catch (Exception e) {
            LOGGER.warning("Ошибка при удалении пользователя по Id");
            LOGGER.warning(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getAllUsers() {
        try {
//            Session session = getHibernateConnection();
//            session.getTransaction();
            session.getTransaction().begin();
            Query query = session.createQuery("SELECT u FROM User u");
            List<User> users = query.getResultList();
            LOGGER.info("Прочитан список пользователей из БД");
            //session.close();
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
//            Session session = getHibernateConnection();
            session.getTransaction();
            Query query = session.createQuery("DELETE FROM User u");
            query.executeUpdate();
            LOGGER.info("Все пользователи удалены");
            //session.close();
        } catch (Exception e) {
            LOGGER.warning("Ошибка при удалении всех пользователей из БД");
            LOGGER.warning(e.getMessage());
        }
    }
}
