package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
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

        us.saveUser("Шерлок", "Холмс", (byte) 40);
        us.saveUser("Джон", "Ватсон", (byte) 30);
        us.saveUser("Майкрофт", "Холмс", (byte) 45);
        us.saveUser("Ирен", "Адлер", (byte) 30);
        us.saveUser("Тобиас", "Грегсон", (byte) 50);
        us.saveUser("Себастьян", "Моран", (byte) 52);
        us.saveUser("Джеймс", "Мориарти", (byte) 47);

        us.removeUserById(2);
        us.getAllUsers().forEach(System.out::println);

        us.cleanUsersTable();
        us.dropUsersTable();
    }
}
