package com.example.jor.hospital.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jor on 22.03.2016.
 */

public class SQLiteDB extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    //Infos about database
    private static final String DATABASE_NAME = "apollo16.db";
    private static final int DATABASE_VERSION = 2;
    private static SQLiteDB instance;

    //use a singleton
    //we want always just one instance of the database
    private SQLiteDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    public static SQLiteDB getInstance(Context context) {
        if (instance == null) {
            instance = new SQLiteDB(context.getApplicationContext());
            //Enable foreign key support
            instance.db.execSQL("PRAGMA foreign_keys = ON;");
        }
        return instance;
    }

        /*
    Table creation list:
    1. Doctor
    2. Room
    3. ShowPatient
    4. Treatment
    5. History
    6. Event
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Tables.DoctorEntry.CREATE_TABLE_DOCTOR);
        db.execSQL(Tables.RoomEntry.CREATE_TABLE_ROOM);
        db.execSQL(Tables.PatientEntry.CREATE_TABLE_PATIENT);
        db.execSQL(Tables.TreatmentEntry.CREATE_TABLE_TREATMENT);
        db.execSQL(Tables.HistoryEntry.CREATE_TABLE_HISTORY);
        db.execSQL(Tables.EventEntry.CREATE_TABLE_EVENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop old tables
        db.execSQL("DROP TABLE IF EXISTS " + Tables.DoctorEntry.TABLE_DOCTOR);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.RoomEntry.TABLE_ROOM);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.PatientEntry.TABLE_PATIENT);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.TreatmentEntry.TABLE_TREATMENT);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.HistoryEntry.TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.EventEntry.TABLE_EVENT);

        //create new tables
        onCreate(db);
    }
}