package com.example.jor.hospital.db.objects;

/**
 * Created by jor on 22.03.2016.
 */
public class Doctor {

    private int doctor_id;
    private boolean gender;
    private String lastname;
    private String firstname;
    private String username;
    private String password;

    public Doctor(){}

    public Doctor(boolean gender, String lastname, String firstname, String username, String password) {
        this.gender = gender;
        this.lastname = lastname;
        this.firstname = firstname;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return doctor_id;
    }

    public void setId(int id) {
        this.doctor_id = id;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctor_id=" + doctor_id +
                ", gender=" + gender +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
