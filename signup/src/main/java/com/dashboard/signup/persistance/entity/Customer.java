package com.dashboard.signup.persistance.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.bson.Document;

public class Customer {

    @Id
    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String userName;

    private String password;

    private String userAccess;

    public Customer(){}

    public Customer(Customer user){
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.email = user.email;
        this.userName = user.userName;
        this.password = user.password;
        this.userAccess = user.userAccess;
    }
    public Customer(String firstName, String lastName, String email, String userName, String password, String userAccess){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.userAccess = userAccess;
    }

    public static Customer fromDocument(Document document) {
        Customer customer = new Customer();
        customer.setId(document.getLong("id"));
        customer.setUserName(document.getString("userName"));
        customer.setFirstName(document.getString("firstName"));
        customer.setLastName(document.getString("lastName"));
        customer.setEmail(document.getString("email"));
        customer.setPassword(document.getString("password"));
        customer.setUserAccess(document.getString("userAccess"));
        return customer;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserAccess() { return userAccess; }

    public void setUserAccess(String userAccess) { this.userAccess = userAccess; }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%s, firstName='%s', lastName='%s', email='%s']]",
                id, firstName, lastName, email);
    }


}
