package com.example.jor.hospital.db.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jor.hospital.db.SQLiteDB;
import com.example.jor.hospital.db.Tables;
import com.example.jor.hospital.db.objects.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jor on 22.03.2016.
 */
public class RoomAdapter {

    private SQLiteDatabase db;
    private Context context;

    public RoomAdapter(Context context){
        SQLiteDB sqliteHelper = SQLiteDB.getInstance(context);
        db = sqliteHelper.getWritableDatabase();
        this.context = context;
    }

    /**
     * Insert a new room
     */
    public long createRoom(Room room){
        long id;
        ContentValues values = new ContentValues();
        values.put(Tables.RoomEntry.KEY_ROOM_ID, room.getId());
        id = this.db.insert(Tables.RoomEntry.TABLE_ROOM, null, values);
        return id;
    }

    /**
     * Find one Doctor by Id
     */
    public Room getRoomById(long id){
        String sql = "SELECT * FROM " + Tables.RoomEntry.TABLE_ROOM +
                " WHERE " + Tables.RoomEntry.KEY_ROOM_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        Room room = new Room();
        room.setId(cursor.getInt(cursor.getColumnIndex(Tables.RoomEntry.KEY_ROOM_ID)));
        return room;
    }

    /**
     * Get all Rooms
     */
    public List<Room> getAllRooms(){
        List<Room> rooms = new ArrayList<Room>();
        String sql = "SELECT * FROM " + Tables.RoomEntry.TABLE_ROOM + " ORDER BY " + Tables.RoomEntry.KEY_ROOM_ID;

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                Room room = new Room();
                room.setId(cursor.getInt(cursor.getColumnIndex(Tables.RoomEntry.KEY_ROOM_ID)));
                rooms.add(room);
            } while(cursor.moveToNext());
        }
        return rooms;
    }

    /**
     * Delete all Rooms
     */
    public void deleteAllRooms(){
        // TODO delete all other entries!!

        //delete all doctors
        this.db.delete(Tables.RoomEntry.TABLE_ROOM, null, null) ;

    }

}
