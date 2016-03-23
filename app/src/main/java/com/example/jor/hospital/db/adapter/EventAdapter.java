package com.example.jor.hospital.db.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jor.hospital.db.SQLiteDB;
import com.example.jor.hospital.db.objects.Event;
import com.example.jor.hospital.db.Tables.EventEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jor on 22.03.2016.
 */

public class EventAdapter {

    private SQLiteDatabase db;
    private Context context;

    public EventAdapter(Context context){
        SQLiteDB sqliteHelper = SQLiteDB.getInstance(context);
        db = sqliteHelper.getWritableDatabase();
        this.context = context;
    }

    /**
     * Insert a new event
     */
    public long createEvent(Event event){
        long id;
        ContentValues values = new ContentValues();
        values.put(EventEntry.KEY_EVENTNAME, event.getEventname());
        values.put(EventEntry.KEY_ROOM, event.getRoom());
        values.put(EventEntry.KEY_FROMDATE, event.getFromDate());
        values.put(EventEntry.KEY_TODATE, event.getToDate());
        values.put(EventEntry.KEY_NOTIFICATION, event.getNotificiation());
        values.put(EventEntry.KEY_PATIENT, event.getPatient());
        values.put(EventEntry.KEY_DESCRIPTION, event.getDescription());
        values.put(EventEntry.KEY_DOCTOR, event.getDoctor());
        id = this.db.insert(EventEntry.TABLE_EVENT, null, values);
        return id;
    }

    /**
     * Find one Event by Id
     */
    public Event getEventById(long id){
        String sql = "SELECT * FROM " + EventEntry.TABLE_EVENT +
                " WHERE " + EventEntry.KEY_EVENT_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Event event = new Event();
        event.setEvent_id(cursor.getInt(cursor.getColumnIndex(EventEntry.KEY_EVENT_ID)));
        event.setEventname(cursor.getString(cursor.getColumnIndex(EventEntry.KEY_EVENTNAME)));
        event.setRoom(cursor.getInt(cursor.getColumnIndex(EventEntry.KEY_ROOM)));
        event.setFromDate(cursor.getInt(cursor.getColumnIndex(EventEntry.KEY_FROMDATE)));
        event.setToDate(cursor.getInt(cursor.getColumnIndex(EventEntry.KEY_TODATE)));
        event.setNotificiation(cursor.getInt(cursor.getColumnIndex(EventEntry.KEY_NOTIFICATION)));
        event.setPatient(cursor.getInt(cursor.getColumnIndex(EventEntry.KEY_PATIENT)));
        event.setDescription(cursor.getString(cursor.getColumnIndex(EventEntry.KEY_DESCRIPTION)));
        event.setDoctor(cursor.getInt(cursor.getColumnIndex(EventEntry.KEY_DOCTOR)));
        return event;
    }

    /**
     * Get all Events by Doctor
     */
    public List<Event> getAllEventsByDoctor(long id){
        List<Event> events = new ArrayList<Event>();
        String sql = "SELECT * FROM " + EventEntry.TABLE_EVENT + " WHERE  " + EventEntry.KEY_DOCTOR + "=" + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                Event event = new Event();
                event.setEvent_id(cursor.getInt(cursor.getColumnIndex(EventEntry.KEY_EVENT_ID)));
                event.setEventname(cursor.getString(cursor.getColumnIndex(EventEntry.KEY_EVENTNAME)));
                event.setRoom(cursor.getInt(cursor.getColumnIndex(EventEntry.KEY_ROOM)));
                event.setFromDate(cursor.getInt(cursor.getColumnIndex(EventEntry.KEY_FROMDATE)));
                event.setToDate(cursor.getInt(cursor.getColumnIndex(EventEntry.KEY_TODATE)));
                event.setNotificiation(cursor.getInt(cursor.getColumnIndex(EventEntry.KEY_NOTIFICATION)));
                event.setPatient(cursor.getInt(cursor.getColumnIndex(EventEntry.KEY_PATIENT)));
                event.setDescription(cursor.getString(cursor.getColumnIndex(EventEntry.KEY_DESCRIPTION)));
                event.setDoctor(cursor.getInt(cursor.getColumnIndex(EventEntry.KEY_DOCTOR)));
                events.add(event);
            } while(cursor.moveToNext());
        }
        return events;
    }

    /**
     *  Update an Event
     */
    public int updateEvent(Event event){
        ContentValues values = new ContentValues();
        values.put(EventEntry.KEY_EVENTNAME, event.getEventname());
        values.put(EventEntry.KEY_ROOM, event.getRoom());
        values.put(EventEntry.KEY_FROMDATE, event.getFromDate());
        values.put(EventEntry.KEY_TODATE, event.getToDate());
        values.put(EventEntry.KEY_NOTIFICATION, event.getNotificiation());
        values.put(EventEntry.KEY_PATIENT, event.getPatient());
        values.put(EventEntry.KEY_DESCRIPTION, event.getDescription());
        values.put(EventEntry.KEY_DOCTOR, event.getDoctor());

        return this.db.update(EventEntry.CREATE_TABLE_EVENT, values, EventEntry.KEY_EVENT_ID + " = ?",
                new String[] { String.valueOf(event.getEvent_id()) });
    }

    /**
     * Delete an Event
     */
    public void deletePerson(long id){
        //delete the person
        this.db.delete(EventEntry.TABLE_EVENT, EventEntry.KEY_EVENT_ID + " = ?",
                new String[] { String.valueOf(id) });

    }

}

