package com.example.jor.hospital.db.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jor.hospital.db.SQLiteDB;
import com.example.jor.hospital.db.objects.History;
import com.example.jor.hospital.db.Tables.HistoryEntry;

import java.util.ArrayList;
import java.util.List;


public class  HistoryAdapter {

    private SQLiteDatabase db;
    private Context context;

    public HistoryAdapter(Context context){
        SQLiteDB sqliteHelper = SQLiteDB.getInstance(context);
        db = sqliteHelper.getWritableDatabase();
        this.context = context;
    }

    /**
     * Insert a new history
     */
    public long createHistory(History history){
        long id;
        ContentValues values = new ContentValues();
        values.put(HistoryEntry.KEY_PATIENT, history.getPatient());
        values.put(HistoryEntry.KEY_DOCTOR, history.getDoctor());
        values.put(HistoryEntry.KEY_TREATMENT, history.getTreatment());
        values.put(HistoryEntry.KEY_NOTES, history.getNotes());
        values.put(HistoryEntry.KEY_START_DATE, history.getStart_date());

        id = this.db.insert(HistoryEntry.TABLE_HISTORY, null, values);

        return id;
    }

    /**
     * Find one History by Id
     */
    public History getHistoryById(long id){
        String sql = "SELECT * FROM " + HistoryEntry.TABLE_HISTORY +
                " WHERE " + HistoryEntry.KEY_HISTORY_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        History history = new History();
        history.setHistory_id(cursor.getInt(cursor.getColumnIndex(HistoryEntry.KEY_HISTORY_ID)));
        history.setPatient(cursor.getInt(cursor.getColumnIndex(HistoryEntry.KEY_PATIENT)));
        history.setDoctor(cursor.getInt(cursor.getColumnIndex(HistoryEntry.KEY_DOCTOR)));
        history.setTreatment(cursor.getInt(cursor.getColumnIndex(HistoryEntry.KEY_TREATMENT)));
        history.setNotes(cursor.getString(cursor.getColumnIndex(HistoryEntry.KEY_NOTES)));
        history.setStart_date(cursor.getString(cursor.getColumnIndex(HistoryEntry.KEY_START_DATE)));


        return history;
    }

    /**
     * Get History entries by ShowPatient
     */
    public List<History> getHistorysByPatient(int patient_id){
        List<History> historys = new ArrayList<History>();
        String sql = "SELECT * FROM " + HistoryEntry.TABLE_HISTORY +
                " WHERE " + HistoryEntry.KEY_PATIENT + " = " + patient_id;

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                History history = new History();
                history.setHistory_id(cursor.getInt(cursor.getColumnIndex(HistoryEntry.KEY_HISTORY_ID)));
                history.setPatient(cursor.getInt(cursor.getColumnIndex(HistoryEntry.KEY_PATIENT)));
                history.setDoctor(cursor.getInt(cursor.getColumnIndex(HistoryEntry.KEY_DOCTOR)));
                history.setTreatment(cursor.getInt(cursor.getColumnIndex(HistoryEntry.KEY_TREATMENT)));
                history.setNotes(cursor.getString(cursor.getColumnIndex(HistoryEntry.KEY_NOTES)));
                history.setStart_date(cursor.getString(cursor.getColumnIndex(HistoryEntry.KEY_START_DATE)));
                historys.add(history);
            } while(cursor.moveToNext());
        }

        return historys;
    }


    /**
     * Get all Historys
     */
    public List<History> getAllHistorys(){
        List<History> historys = new ArrayList<History>();
        String sql = "SELECT * FROM " + HistoryEntry.TABLE_HISTORY;

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                History history = new History();
                history.setHistory_id(cursor.getInt(cursor.getColumnIndex(HistoryEntry.KEY_HISTORY_ID)));
                history.setPatient(cursor.getInt(cursor.getColumnIndex(HistoryEntry.KEY_PATIENT)));
                history.setDoctor(cursor.getInt(cursor.getColumnIndex(HistoryEntry.KEY_DOCTOR)));
                history.setTreatment(cursor.getInt(cursor.getColumnIndex(HistoryEntry.KEY_TREATMENT)));
                history.setNotes(cursor.getString(cursor.getColumnIndex(HistoryEntry.KEY_NOTES)));
                history.setStart_date(cursor.getString(cursor.getColumnIndex(HistoryEntry.KEY_START_DATE)));


                historys.add(history);
            } while(cursor.moveToNext());
        }

        return historys;
    }

    /**
     *  Update a History
     */
    public int updateHistory(History history){
        ContentValues values = new ContentValues();
        values.put(HistoryEntry.KEY_PATIENT, history.getPatient());
        values.put(HistoryEntry.KEY_DOCTOR, history.getDoctor());
        values.put(HistoryEntry.KEY_TREATMENT, history.getTreatment());
        values.put(HistoryEntry.KEY_NOTES, history.getNotes());
        values.put(HistoryEntry.KEY_START_DATE, history.getStart_date());
        return this.db.update(HistoryEntry.TABLE_HISTORY, values, HistoryEntry.KEY_HISTORY_ID + " = ?",
                new String[] { String.valueOf(history.getHistory_id()) });
    }

    /**
     * Delete a History - this will also delete all records
     * for the history
     */
    public void deleteHistory(long id){

        //delete the history
        this.db.delete(HistoryEntry.TABLE_HISTORY, HistoryEntry.KEY_HISTORY_ID + " = ?",
                new String[] { String.valueOf(id) });

    }

}
