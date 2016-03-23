package com.example.jor.hospital.db.objects;

/**
 * Created by jor on 22.03.2016.
 */
public class Room {
    private int room_id;

    public Room(){}

    public Room(int room_id) {
        this.room_id = room_id;
    }

    public int getId() {
        return room_id;
    }

    public void setId(int id) {
        this.room_id = id;
    }

    @Override
    public String toString() {
        return "Room{" +
                "room_id=" + room_id +
                '}';
    }
}