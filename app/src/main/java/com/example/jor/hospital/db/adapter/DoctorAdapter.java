package com.example.jor.hospital.db.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.jor.hospital.Login;
import com.example.jor.hospital.db.SQLiteDB;
import com.example.jor.hospital.db.objects.Doctor;
import com.example.jor.hospital.db.Tables.DoctorEntry;

import java.util.ArrayList;
import java.util.List;


public class DoctorAdapter {

    private SQLiteDatabase db;
    private Context context;

    public DoctorAdapter(Context context){
        SQLiteDB sqliteHelper = SQLiteDB.getInstance(context);
        db = sqliteHelper.getWritableDatabase();
        this.context = context;
    }

    /**
     * Login for doctor
     */
    public boolean LoginDoctor(String username, String password){
        String sql = "SELECT "+ DoctorEntry.KEY_USERNAME +", " + DoctorEntry.KEY_PASSWORD+ " FROM " + DoctorEntry.TABLE_DOCTOR +
                " WHERE " + DoctorEntry.KEY_USERNAME + " = '" + username + "'"
                + " AND " + DoctorEntry.KEY_PASSWORD + " = '" + Login.getMD5(password) + "'";

        Cursor cursor = this.db.rawQuery(sql, null);
        if(cursor != null)
            if(cursor.getCount() <= 0) return false;
            else return true;
        else
            return false;
    }

    /**
     * Get ID by Login
     */
    public int GetIDByLogin(String username, String password){
        String sql = "SELECT "+ DoctorEntry.KEY_DOCTOR_ID + " FROM " + DoctorEntry.TABLE_DOCTOR +
                " WHERE " + DoctorEntry.KEY_USERNAME + " = '" + username + "'"
                + " AND " + DoctorEntry.KEY_PASSWORD + " = '" + Login.getMD5(password) + "'";


        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor != null){
            cursor.moveToFirst();
        }
            int id = cursor.getInt(cursor.getColumnIndex(DoctorEntry.KEY_DOCTOR_ID));
            return id;
    }

    /**
     * Insert a new doctor
     */
    public long createDoctor(Doctor doctor){
        long id;
        ContentValues values = new ContentValues();
        values.put(DoctorEntry.KEY_GENDER, doctor.isGender() ? 1 : 0);
        values.put(DoctorEntry.KEY_LASTNAME, doctor.getLastname());
        values.put(DoctorEntry.KEY_FIRSTNAME, doctor.getFirstname());
        values.put(DoctorEntry.KEY_USERNAME, doctor.getUsername());
        values.put(DoctorEntry.KEY_PASSWORD, doctor.getPassword());
        id = this.db.insert(DoctorEntry.TABLE_DOCTOR, null, values);
        return id;
    }

    /**
     * Find one Doctor by Id
     */
    public Doctor getDoctorById(long id){
        String sql = "SELECT * FROM " + DoctorEntry.TABLE_DOCTOR +
                " WHERE " + DoctorEntry.KEY_DOCTOR_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        Doctor doctor = new Doctor();
        doctor.setId(cursor.getInt(cursor.getColumnIndex(DoctorEntry.KEY_DOCTOR_ID)));
        doctor.setGender(cursor.getInt(cursor.getColumnIndex(DoctorEntry.KEY_GENDER)) != 0);
        doctor.setLastname(cursor.getString(cursor.getColumnIndex(DoctorEntry.KEY_LASTNAME)));
        doctor.setFirstname(cursor.getString(cursor.getColumnIndex(DoctorEntry.KEY_FIRSTNAME)));
        doctor.setUsername(cursor.getString(cursor.getColumnIndex(DoctorEntry.KEY_USERNAME)));
        doctor.setPassword(cursor.getString(cursor.getColumnIndex(DoctorEntry.KEY_PASSWORD)));
        return doctor;
    }

    /**
     * Get all Doctors
     */
    public List<Doctor> getAllDoctors(){
        List<Doctor> doctors = new ArrayList<Doctor>();
        String sql = "SELECT * FROM " + DoctorEntry.TABLE_DOCTOR + " ORDER BY " + DoctorEntry.KEY_DOCTOR_ID;

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                Doctor doctor = new Doctor();
                doctor.setId(cursor.getInt(cursor.getColumnIndex(DoctorEntry.KEY_DOCTOR_ID)));
                doctor.setGender(cursor.getInt(cursor.getColumnIndex(DoctorEntry.KEY_GENDER)) != 0);
                doctor.setLastname(cursor.getString(cursor.getColumnIndex(DoctorEntry.KEY_LASTNAME)));
                doctor.setFirstname(cursor.getString(cursor.getColumnIndex(DoctorEntry.KEY_FIRSTNAME)));
                doctor.setUsername(cursor.getString(cursor.getColumnIndex(DoctorEntry.KEY_USERNAME)));
                doctor.setPassword(cursor.getString(cursor.getColumnIndex(DoctorEntry.KEY_PASSWORD)));

                doctors.add(doctor);
            } while(cursor.moveToNext());
        }

        return doctors;
    }

    /**
     *  Update a Doctor
     */
    public int updateDoctor(Doctor doctor){
        ContentValues values = new ContentValues();
        values.put(DoctorEntry.KEY_GENDER, doctor.isGender() ? 1 : 0);
        values.put(DoctorEntry.KEY_LASTNAME, doctor.getLastname());
        values.put(DoctorEntry.KEY_FIRSTNAME, doctor.getFirstname());
        values.put(DoctorEntry.KEY_USERNAME, doctor.getUsername());
        values.put(DoctorEntry.KEY_PASSWORD, doctor.getPassword());


        return this.db.update(DoctorEntry.TABLE_DOCTOR, values, DoctorEntry.KEY_DOCTOR_ID + " = ?",
                new String[] { String.valueOf(doctor.getId()) });
    }

    /**
     * Delete a Doctor - this will also delete all records
     * for the doctor
     */
    public void deleteDoctor(long id){
    // TODO delete all other entries!!

        //delete the doctor
        this.db.delete(DoctorEntry.TABLE_DOCTOR, DoctorEntry.KEY_DOCTOR_ID + " = ?",
                new String[]{String.valueOf(id)});

    }

    /**
     * Delete all Doctor - this will also delete all records
     * for the doctor
     */
    public void deleteAllDoctors(){
        // TODO delete all other entries!!

        //delete all doctors
        this.db.delete(DoctorEntry.TABLE_DOCTOR, null, null) ;

    }

}
