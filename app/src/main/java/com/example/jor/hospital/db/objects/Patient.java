package com.example.jor.hospital.db.objects;

/**
 * Created by jor on 22.03.2016.
 */
public class Patient {
    private int patient_id;
    private boolean gender;
    private String lastname;
    private String firstname;
    private String birthdate;
    private String adress;
    private String city;
    private String ahv_number;
    private int room;

    public Patient(){}

    public Patient(boolean gender, String lastname, String firstname, String birthdate, String adress, String city, String ahv_number) {
        this.gender = gender;
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.adress = adress;
        this.city = city;
        this.ahv_number = ahv_number;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
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

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAhv_number() {
        return ahv_number;
    }

    public void setAhv_number(String ahv_number) {
        this.ahv_number = ahv_number;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public String getName(){ return getFirstname() + " " +getLastname(); }

    @Override
    public String toString() {
        return "Patient{" +
                "patient_id=" + patient_id +
                ", gender=" + gender +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", adress='" + adress + '\'' +
                ", city='" + city + '\'' +
                ", ahv_number='" + ahv_number + '\'' +
                ", room=" + room +
                '}';
    }
}
