package jm.task.core.jdbc.model;

import lombok.Data;

import javax.persistence.*;

@SuppressWarnings({"SpellCheckingInspection", "JpaDataSourceORMInspection"})
@Entity
@Table(name = "usersHiber", schema = "dbITM")
@Data
public class User {
    @Id
    @GeneratedValue(generator = "increment")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "age")
    private Byte age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Byte getAge() {
        return age;
    }

    public void setAge(Byte age) {
        this.age = age;
    }

    public User() {

    }

    public User(String name, String lastName, Byte age) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
    }

    public User(Long id, String name, String lastName, Byte age) {
        this(name, lastName, age);
        this.id = id;
    }
}
