package com.qa.ims.persistence.domain;

import java.util.Objects;

public class Driver {
    private Long id;
    private String firstName;
    private String surname;

    public Driver(Long id, String firstName, String surname) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
    }

    public Driver(String firstName, String surname) {
        this.firstName = firstName;
        this.surname = surname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Driver)) return false;
        Driver driver = (Driver) o;
        return Objects.equals(id, driver.id) && Objects.equals(firstName, driver.firstName) && Objects.equals(surname, driver.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, surname);
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
