package com.example.jor.hospital.db;

import android.provider.BaseColumns;

/**
 * Created by jor on 22.03.2016.
 */
public final class Tables{

    public Tables(){
        //empty constructor
        //should never be instantiated
    }

    /***********************************************************************
     *** Doctor Table													 ***
     ***********************************************************************/
    //Represents the rows of a table
    public static abstract class DoctorEntry implements BaseColumns{
        //Table name
        public static final String TABLE_DOCTOR = "doctor";

        //Doctor Column names
        public static final String KEY_DOCTOR_ID = "doctor_id";
        public static final String KEY_GENDER = "gender";
        public static final String KEY_LASTNAME = "lastname";
        public static final String KEY_FIRSTNAME = "firstname";
        public static final String KEY_USERNAME = "username";
        public static final String KEY_PASSWORD = "password";

        //Table Doctor create statement
        public static final String CREATE_TABLE_DOCTOR = "CREATE TABLE "
                + TABLE_DOCTOR + "("
                + DoctorEntry.KEY_DOCTOR_ID + " INTEGER PRIMARY KEY,"
                + DoctorEntry.KEY_GENDER + " BOOLEAN NOT NULL, "
                + DoctorEntry.KEY_LASTNAME + " TEXT NOT NULL, "
                + DoctorEntry.KEY_FIRSTNAME + " TEXT NOT NULL, "
                + DoctorEntry.KEY_USERNAME + " TEXT NOT NULL, "
                + DoctorEntry.KEY_PASSWORD + " TEXT NOT NULL"
                + ");";
    }

    /***********************************************************************
     *** Room Table													     ***
     ***********************************************************************/
    public static abstract class RoomEntry implements BaseColumns {
        //Table name
        public static final String TABLE_ROOM = "room";

        //Room Column names
        public static final String KEY_ROOM_ID = "room_id";

        //Table Room create statement
        public static final String CREATE_TABLE_ROOM = "CREATE TABLE "
                + TABLE_ROOM + "("
                + RoomEntry.KEY_ROOM_ID + " INTEGER PRIMARY KEY"
                + ");";
    }

    /***********************************************************************
     *** Patient Table													 ***
     ***********************************************************************/
    //Represents the rows of a table
    public static abstract class PatientEntry implements BaseColumns{
        //Table name
        public static final String TABLE_PATIENT = "patient";

        //Patient Column names
        public static final String KEY_PATIENT_ID = "patient_id";
        public static final String KEY_GENDER = "gender";
        public static final String KEY_LASTNAME = "lastname";
        public static final String KEY_FIRSTNAME = "firstname";
        public static final String KEY_BIRTHDATE = "birthdate";
        public static final String KEY_ADDRESS = "address";
        public static final String KEY_CITY = "city";
        public static final String KEY_AHV_NUMBER = "ahv_number";
        //Patient foreign keys
        public static final String KEY_ROOM = "room_fk";

        //Table Patient create statement
        public static final String CREATE_TABLE_PATIENT = "CREATE TABLE "
                + TABLE_PATIENT + "("
                + PatientEntry.KEY_PATIENT_ID + " INTEGER PRIMARY KEY,"
                + PatientEntry.KEY_GENDER + " BOOLEAN NOT NULL, "
                + PatientEntry.KEY_LASTNAME + " TEXT NOT NULL, "
                + PatientEntry.KEY_FIRSTNAME + " TEXT NOT NULL, "
                + PatientEntry.KEY_BIRTHDATE + " TEXT NOT NULL, "
                + PatientEntry.KEY_ADDRESS + " TEXT NOT NULL, "
                + PatientEntry.KEY_CITY + " TEXT NOT NULL, "
                + PatientEntry.KEY_AHV_NUMBER + " TEXT NOT NULL,"
                + PatientEntry.KEY_ROOM + " INT, "
                + "FOREIGN KEY (" + KEY_ROOM + ") REFERENCES " + RoomEntry.TABLE_ROOM + " (" + RoomEntry.KEY_ROOM_ID + ")"
                + ");";
    }

    /***********************************************************************
     *** Treatment Table											     ***
     ***********************************************************************/
    //Represents the rows of a table
    public static abstract class TreatmentEntry implements BaseColumns{
        //Table name
        public static final String TABLE_TREATMENT = "treatment";

        //Treatment Column names
        public static final String KEY_TREATMENT_ID = "treatment_id";
        public static final String KEY_TREATMENT = "treatment";

        //Table Treatment create statement
        public static final String CREATE_TABLE_TREATMENT = "CREATE TABLE "
                + TABLE_TREATMENT + "("
                + TreatmentEntry.KEY_TREATMENT_ID + " INTEGER PRIMARY KEY,"
                + TreatmentEntry.KEY_TREATMENT + " TEXT NOT NULL"
                + ");";
    }


    /***********************************************************************
     *** History Table											         ***
     ***********************************************************************/
    //Represents the rows of a table
    public static abstract class HistoryEntry implements BaseColumns{
        //Table name
        public static final String TABLE_HISTORY = "history";

        //Patient Column names
        public static final String KEY_HISTORY_ID = "history_id";
        public static final String KEY_NOTES = "notes";
        public static final String KEY_START_DATE = "start_date";
        public static final String KEY_END_DATE = "end_date";
        //History foreign keys
        public static final String KEY_PATIENT = "patient_fk";
        public static final String KEY_DOCTOR = "doctor_fk";
        public static final String KEY_TREATMENT = "treatment_fk";

        //Table History create statement
        public static final String CREATE_TABLE_HISTORY = "CREATE TABLE "
                + TABLE_HISTORY + "("
                + HistoryEntry.KEY_HISTORY_ID + " INTEGER PRIMARY KEY,"
                + HistoryEntry.KEY_PATIENT + " INT NOT NULL, "
                + HistoryEntry.KEY_DOCTOR + " INT NOT NULL,"
                + HistoryEntry.KEY_TREATMENT + " INT NOT NULL,"
                + HistoryEntry.KEY_NOTES + " TEXT, "
                + HistoryEntry.KEY_START_DATE + " TEXT, "
                + HistoryEntry.KEY_END_DATE + " TEXT, "
                + "FOREIGN KEY (" + KEY_PATIENT + ") REFERENCES " + PatientEntry.TABLE_PATIENT + " (" + PatientEntry.KEY_PATIENT_ID + "), "
                + "FOREIGN KEY (" + KEY_DOCTOR + ") REFERENCES " + DoctorEntry.TABLE_DOCTOR + " (" + DoctorEntry.KEY_DOCTOR_ID + "), "
                + "FOREIGN KEY (" + KEY_TREATMENT + ") REFERENCES " + TreatmentEntry.TABLE_TREATMENT + " (" + TreatmentEntry.KEY_TREATMENT_ID + ") "
                + ");";
    }

    /***********************************************************************
     *** Event Table													 ***
     ***********************************************************************/
    //Represents the rows of a the table event
    public static abstract class EventEntry implements BaseColumns{
        //Table name
        public static final String TABLE_EVENT = "event";

        //Event Column names
        public static final String KEY_EVENT_ID = "event_id";
        public static final String KEY_EVENTNAME = "eventname";
        public static final String KEY_FROMDATE = "fromdate";
        public static final String KEY_TODATE = "todate";
        public static final String KEY_FROMTIME = "fromtime";
        public static final String KEY_TOTIME = "totime";
        public static final String KEY_NOTIFICATION = "notification";
        public static final String KEY_DESCRIPTION = "description";
        //Event foreign keys
        public static final String KEY_ROOM = "room_fk";
        public static final String KEY_PATIENT = "patient_fk";
        public static final String KEY_DOCTOR = "doctor_fk";

        //Table event create statement
        public static final String CREATE_TABLE_EVENT = "CREATE TABLE "
                + TABLE_EVENT + "("
                + EventEntry.KEY_EVENT_ID + " INTEGER PRIMARY KEY,"
                + EventEntry.KEY_EVENTNAME + " TEXT NOT NULL, "
                + EventEntry.KEY_FROMDATE + " INT NOT NULL, "
                + EventEntry.KEY_TODATE + " INT NOT NULL, "
                + EventEntry.KEY_FROMTIME + " INT, "
                + EventEntry.KEY_TOTIME + " INT, "
                + EventEntry.KEY_NOTIFICATION + " INT NOT NULL, "
                + EventEntry.KEY_DESCRIPTION + " TEXT, "
                + EventEntry.KEY_ROOM + " INT NOT NULL, "
                + EventEntry.KEY_PATIENT + " INT NOT NULL, "
                + EventEntry.KEY_DOCTOR + " INT NOT NULL, "
                + "FOREIGN KEY (" + KEY_ROOM + ") REFERENCES " + RoomEntry.TABLE_ROOM + " (" + RoomEntry.KEY_ROOM_ID + "), "
                + "FOREIGN KEY (" + KEY_PATIENT + ") REFERENCES " + PatientEntry.TABLE_PATIENT + " (" + PatientEntry.KEY_PATIENT_ID + "), "
                + "FOREIGN KEY (" + KEY_DOCTOR + ") REFERENCES " + DoctorEntry.TABLE_DOCTOR + " (" + DoctorEntry.KEY_DOCTOR_ID + ") "
                + ");";
    }
}
