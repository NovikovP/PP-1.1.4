package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {

    // Переключение режимов JDBC vs Hibernate
    // false -use UserDaoHibernateImpl
    // true  -use UserDaoJDBCImpl
    public static final boolean IS_JDBC = true;

    public static void main(String[] args) {
        // реализуйте алгоритм здесь
        UserService us = new UserServiceImpl();
        us.createUsersTable();

        us.saveUser("Sherlock", "Holmes", (byte) 40);
        us.saveUser("John", "Watson", (byte) 30);
        us.saveUser("Microft", "Holmes", (byte) 45);
        us.saveUser("Iren", "Adler", (byte) 30);
        us.saveUser("Tobias", "Gregson", (byte) 50);
        us.saveUser("Sebastian", "Moran", (byte) 52);
        us.saveUser("James", "Moriarty", (byte) 47);

        us.getAllUsers().forEach(System.out::println);

        //us.cleanUsersTable();
        //us.dropUsersTable();
    }
}
