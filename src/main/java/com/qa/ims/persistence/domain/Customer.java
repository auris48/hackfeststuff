package com.qa.ims.persistence.domain;

import java.util.Objects;

public class Customer extends DomainType {

    private Long id;
    private String firstName;
    private String surname;
    private String address;
    private String postcode;
    private String phone;

    public Customer() {
    }


    public Customer(String[] data) {
        this.id=Long.parseLong(data[0]);
        this.firstName=data[1];
        this.surname=data[2];
        this.address=data[3];
        this.postcode=data[4];
        this.phone=data[5];
    }

    public Customer(Long id) {
        this.id = id;
    }

    public Customer(String firstName, String surname) {
        this.setFirstName(firstName);
        this.setSurname(surname);
    }

    public Customer(Long id, String firstName, String surname) {
        this.setId(id);
        this.setFirstName(firstName);
        this.setSurname(surname);
    }

    public Customer(String firstName, String surname, String postcode, String address, String phone) {
        this.firstName = firstName;
        this.surname = surname;
        this.postcode = postcode;
        this.address = address;
        this.phone = phone;
    }

    public Customer(Long id, String firstName, String surname, String postcode, String address, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.postcode = postcode;
        this.address = address;
        this.phone = phone;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
    public String toString() {
        return id + "," + firstName + "," + surname + "," + address + "," + postcode + "," + phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) && Objects.equals(firstName, customer.firstName) && Objects.equals(surname, customer.surname) && Objects.equals(postcode, customer.postcode) && Objects.equals(address, customer.address) && Objects.equals(phone, customer.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, surname,  address, postcode, phone);
    }

    @Override
    public Object[] getFields() {
        return new Object[]{id, firstName, surname, address, postcode, phone};
    }

    public static String[] getAllFields(){
        return new String[]{"id", "Name", "Surname", "Address", "Postcode", "Phone"};
    }
}
