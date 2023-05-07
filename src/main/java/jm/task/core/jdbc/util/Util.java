package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Util {
    // реализуйте настройку соеденения с БД
    private static volatile Connection dbConnection;
    private static SessionFactory sessionFactory;

    private static final String DRIVER = PropertiesUtil.get("DB.DRIVER");
    private static final String URL = PropertiesUtil.get("DB.URL");
    private static final String DB_NAME = PropertiesUtil.get("DB.DB_NAME");
    private static final String USER_NAME = PropertiesUtil.get("DB.USER_NAME");
    private static final String PWD = PropertiesUtil.get("DB.PWD");

    private static final Logger LOGGER = Logger.getLogger(Util.class.getName());

    public static Connection getJdbcConnection() {
        while (dbConnection == null) {
            try {
                Class.forName(DRIVER);
            } catch (ClassNotFoundException cnfe) {
                LOGGER.warning("Ошибка загрузки драйвера");
                LOGGER.warning(cnfe.getMessage());
            }
            try {
                dbConnection = DriverManager.getConnection(
                        URL + DB_NAME, USER_NAME, PWD);
            } catch (SQLException sqlException) {
                LOGGER.warning("Сбой при установлении соединения с БД");
                LOGGER.warning(sqlException.getMessage());
            }
        }
        return dbConnection;
    }

    public static Session getHibernateConnection() {
        SessionFactory sessionFactory = new Configuration()
                .setProperty("hibernate.connection.driver_class", DRIVER)
                .setProperty("hibernate.connection.url", URL + DB_NAME)
                .setProperty("hibernate.connection.username", USER_NAME)
                .setProperty("hibernate.connection.password", PWD)
                //.setProperty( "hibernate.dialect", org.hibernate.dialect.PostgreSQL9Dialect)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
        return sessionFactory.getCurrentSession();
    }

    // -------------------------КОСТЫЛИ-----------------------------

    public static Session getSession() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration()
                        //.setProperty("hibernate.show_sql", "true")
                        .setProperty("hibernate.connection.driver_class", DRIVER)
                        .setProperty(Environment.URL, URL + DB_NAME)
                        .setProperty(Environment.USER, USER_NAME)
                        .setProperty(Environment.PASS, PWD)
                        .addAnnotatedClass(User.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (HibernateException hibernateException) {
                hibernateException.getStackTrace();
                throw new RuntimeException();
            }
        }
        return sessionFactory.openSession();
    }
}
