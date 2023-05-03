package jm.task.core.jdbc.util;


import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Util {
    // реализуйте настройку соеденения с БД
    private static volatile Connection dbConnection;
    private static String driver = "org.postgresql.Driver";
    private static String url = "jdbc:postgresql://localhost:5432/";
    private static String dbName = "dbITM";
    private static String userName = "postgres";
    private static String pwd = "12qwasZX)";


    private static final Logger LOGGER = Logger.getLogger(Util.class.getName());

    public static Connection getJdbcConnection() {
        while (dbConnection == null) {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException cnfe) {
                LOGGER.warning("Ошибка загрузки драйвера");
                LOGGER.warning(cnfe.getMessage());
            }
            try {
                dbConnection = DriverManager.getConnection(
                        url + dbName, userName, pwd);
            } catch (SQLException sqlException) {
                LOGGER.warning("Сбой при установлении соединения с БД");
                LOGGER.warning(sqlException.getMessage());
            }
        }
        return dbConnection;
    }

    public static Session getHibernateConnection() {
        SessionFactory sessionFactory = new Configuration()
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
        return sessionFactory.getCurrentSession();
    }

}
