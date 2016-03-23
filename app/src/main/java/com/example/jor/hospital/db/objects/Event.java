package com.example.jor.hospital.db.objects;

/**
 * Created by jor on 22.03.2016.
 */
public class Event {
    private int event_id;
    private String eventname;
    private int room;
    private int fromDate;
    private int toDate;
    private int notificiation;
    private int patient;
    private String description;
    private int doctor;

    public Event(){}

    public Event(int event_id, String eventname, int room, int fromDate, int toDate, int notificiation, int patient, String description, int doctor) {
        this.event_id = event_id;
        this.eventname = eventname;
        this.room = room;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.notificiation = notificiation;
        this.patient = patient;
        this.description = description;
        this.doctor = doctor;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public int getFromDate() {
        return fromDate;
    }

    public void setFromDate(int fromDate) {
        this.fromDate = fromDate;
    }

    public int getToDate() {
        return toDate;
    }

    public void setToDate(int toDate) {
        this.toDate = toDate;
    }

    public int getNotificiation() {
        return notificiation;
    }

    public void setNotificiation(int notificiation) {
        this.notificiation = notificiation;
    }

    public int getPatient() {
        return patient;
    }

    public void setPatient(int patient) {
        this.patient = patient;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDoctor() {
        return doctor;
    }

    public void setDoctor(int doctor) {
        this.doctor = doctor;
    }
}
