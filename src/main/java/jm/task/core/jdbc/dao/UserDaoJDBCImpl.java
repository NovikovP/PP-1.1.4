package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection conn;
    private static final Logger LOGGER = Logger.getLogger(UserDaoJDBCImpl.class.getName());

    public UserDaoJDBCImpl() {
        conn = Util.getJdbcConnection();
    }

    public void createUsersTable() {
        try {
            Statement stmt = conn.createStatement();
//            stmt.execute("CREATE TABLE dbITM.users (\n" +
//                    "       id bigint NOT NULL ,\n" +
//                    "       name character varying NOT NULL,\n" +
//                    "       lastName character varying NOT NULL,\n" +
//                    "       age bytea NOT NULL,\n" +
//                    "       PRIMARY KEY  (id)");

            stmt.execute("""
                create table if not exists "users"(
                id        serial,
                name      varchar(100) not null,
                lastName  varchar(100) not null,
                age smallint
                ); """);

            LOGGER.info("Создана база данных dbITM с таблицей users");
        } catch (SQLException sqlException) {
            LOGGER.warning("Сбой при создании базы данных dbITM с таблицей users");
            LOGGER.warning(sqlException.getMessage());
        }
    }

    public void dropUsersTable() {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DROP TABLE users");
            LOGGER.info("Удалена таблица users в БД dbITM");
        } catch (SQLException sqlException) {
            LOGGER.warning("Сбой при удалении таблицы users в БД dbITM");
            LOGGER.warning(sqlException.getMessage());
            // возможно тут неуместен rollback
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.warning("Ошибка при отмене операции удаления записи в БД");
                LOGGER.warning(ex.getMessage());
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "insert into users (name, lastname, age) " + "Values (?, ?, ?)");
            conn.setAutoCommit(false);
            stmt.setString(1, name);
            stmt.setString(2, lastName);
            stmt.setByte(3, age);
            stmt.addBatch();
            stmt.executeUpdate();

            conn.commit();
            System.out.printf("User с именем – %s добавлен в базу данных \n", name);
            LOGGER.info("Пользователь " + name + ": cоздан и добавлен в базу данных");
            conn.setAutoCommit(true);
        } catch (SQLException sqlException) {
            LOGGER.warning("Ошибка при добавлении записи в БД");
            LOGGER.warning(sqlException.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.warning("Ошибка при отмене операции записи в БД");
                LOGGER.warning(ex.getMessage());
            }
        }
    }

    public void removeUserById(long id) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM users where id = ?");
            conn.setAutoCommit(false);
            stmt.setLong(1, id);
            stmt.executeUpdate();
            conn.commit();
            LOGGER.info("Пользователь удален из базы данных");
            conn.setAutoCommit(true);
        } catch (SQLException sqlException) {
            LOGGER.warning("Ошибка при удалении пользователя по Id");
            LOGGER.warning(sqlException.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.warning("Ошибка при отмене операции удаления записи в БД");
                LOGGER.warning(ex.getMessage());
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> listUser = new LinkedList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT * FROM users;");
            while (rs.next()) {
                listUser.add(new User(rs.getLong(1), rs.getString(2),
                        rs.getString(3), rs.getByte(4)));
            }
            LOGGER.info("Прочитан список пользователей из БД");
        } catch (SQLException sqlException) {
            LOGGER.warning("Ошибка при считывании всех пользователей из БД");
            LOGGER.warning(sqlException.getMessage());
            // возможно тут неуместен rollback
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.warning("Ошибка при отмене операции получения всех записей в БД");
                LOGGER.warning(ex.getMessage());
            }
        }
        return listUser;
    }

    public void cleanUsersTable() {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("TRUNCATE table users");
            LOGGER.info("Все пользователи удалены");
        } catch (SQLException sqlException) {
            LOGGER.warning("Ошибка при удалении всех пользователей из БД");
            LOGGER.warning(sqlException.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.warning("Ошибка при отмене операции удаления всех записей в БД");
                LOGGER.warning(ex.getMessage());
            }
        }
    }
}
