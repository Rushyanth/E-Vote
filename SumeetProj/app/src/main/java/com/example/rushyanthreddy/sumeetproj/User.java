package com.example.rushyanthreddy.sumeetproj;

/**
 * Created by Rushyanth Reddy on 3/18/2017.
 */
public class User {
    private String firstName,lastName,phoneNumber,emailId;

    User(String firstName, String lastname, String phoneNumber, String emailId){
        this.firstName=firstName;
        this.lastName=lastname;
        this.phoneNumber=phoneNumber;
        this.emailId=emailId;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName(){return lastName;}
    public String getPhoneNumber(){return phoneNumber;}
    public String getEmail() {
        return emailId;
    }

}
