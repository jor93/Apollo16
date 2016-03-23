package com.example.jor.hospital.db.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jor.hospital.db.SQLiteDB;
import com.example.jor.hospital.db.Tables.PatientEntry;
import com.example.jor.hospital.db.Tables;
import com.example.jor.hospital.db.objects.Patient;


public class PatientAdapter {

    private SQLiteDatabase db;
    private Context context;

    public PatientAdapter(Context context){
        SQLiteDB sqliteHelper = SQLiteDB.getInstance(context);
        db = sqliteHelper.getWritableDatabase();
        this.context = context;
    }

    /**
     * Insert a new patient
     */
    public long createPatient(Patient patient){
        long id;
        ContentValues values = new ContentValues();
        values.put(PatientEntry.KEY_GENDER, patient.isGender() ? 1 : 0);
        values.put(PatientEntry.KEY_LASTNAME, patient.getLastname());
        values.put(PatientEntry.KEY_FIRSTNAME, patient.getFirstname());
        values.put(PatientEntry.KEY_BIRTHDATE, patient.getBirthdate());
        values.put(PatientEntry.KEY_ADDRESS, patient.getAdress());
        values.put(PatientEntry.KEY_CITY, patient.getCity());
        values.put(PatientEntry.KEY_AHV_NUMBER, patient.getAhv_number());
        values.put(PatientEntry.KEY_ROOM, patient.getRoom());
        id = this.db.insert(Tables.PatientEntry.TABLE_PATIENT, null, values);
        return id;
    }

    /**
     * Find one Patient by Id
     */
    public Patient getPatientById(long id){
        String sql = "SELECT * FROM " + Tables.PatientEntry.TABLE_PATIENT +
                " WHERE " + Tables.PatientEntry.KEY_PATIENT_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Patient patient = new Patient();
        patient.setPatient_id(cursor.getInt(cursor.getColumnIndex(PatientEntry.KEY_PATIENT_ID)));
        patient.setGender(cursor.getInt(cursor.getColumnIndex(PatientEntry.KEY_GENDER)) != 0);
        patient.setLastname(cursor.getString(cursor.getColumnIndex(PatientEntry.KEY_LASTNAME)));
        patient.setFirstname(cursor.getString(cursor.getColumnIndex(PatientEntry.KEY_FIRSTNAME)));
        patient.setBirthdate(cursor.getString(cursor.getColumnIndex(PatientEntry.KEY_BIRTHDATE)));
        patient.setAdress(cursor.getString(cursor.getColumnIndex(PatientEntry.KEY_ADDRESS)));
        patient.setCity(cursor.getString(cursor.getColumnIndex(PatientEntry.KEY_CITY)));
        patient.setAhv_number(cursor.getString(cursor.getColumnIndex(PatientEntry.KEY_AHV_NUMBER)));
        patient.setRoom(cursor.getInt(cursor.getColumnIndex(PatientEntry.KEY_ROOM)));

        return patient;
    }

    /**
     * Get all Patients
     */
    public List<Patient> getAllPatients(){
        List<Patient> patients = new ArrayList<Patient>();
        String sql = "SELECT * FROM " + PatientEntry.TABLE_PATIENT + " ORDER BY " + PatientEntry.KEY_LASTNAME;

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                Patient patient = new Patient();
                patient.setPatient_id(cursor.getInt(cursor.getColumnIndex(PatientEntry.KEY_PATIENT_ID)));
                patient.setGender(cursor.getInt(cursor.getColumnIndex(PatientEntry.KEY_GENDER)) != 0);
                patient.setLastname(cursor.getString(cursor.getColumnIndex(PatientEntry.KEY_LASTNAME)));
                patient.setFirstname(cursor.getString(cursor.getColumnIndex(PatientEntry.KEY_FIRSTNAME)));
                patient.setBirthdate(cursor.getString(cursor.getColumnIndex(PatientEntry.KEY_BIRTHDATE)));
                patient.setAdress(cursor.getString(cursor.getColumnIndex(PatientEntry.KEY_ADDRESS)));
                patient.setCity(cursor.getString(cursor.getColumnIndex(PatientEntry.KEY_CITY)));
                patient.setAhv_number(cursor.getString(cursor.getColumnIndex(PatientEntry.KEY_AHV_NUMBER)));
                patient.setRoom(cursor.getInt(cursor.getColumnIndex(PatientEntry.KEY_ROOM)));

                patients.add(patient);
            } while(cursor.moveToNext());
        }

        return patients;
    }

    /**
     *  Update a Patient
     */
    public int updatePatient(Patient patient){
        ContentValues values = new ContentValues();
        values.put(PatientEntry.KEY_GENDER, patient.isGender());
        values.put(PatientEntry.KEY_LASTNAME, patient.getLastname());
        values.put(PatientEntry.KEY_FIRSTNAME, patient.getFirstname());
        values.put(PatientEntry.KEY_BIRTHDATE, patient.getBirthdate());
        values.put(PatientEntry.KEY_ADDRESS, patient.getAdress());
        values.put(PatientEntry.KEY_CITY, patient.getCity());
        values.put(PatientEntry.KEY_AHV_NUMBER, patient.getAhv_number());
        values.put(PatientEntry.KEY_ROOM, patient.getRoom());

        return this.db.update(PatientEntry.TABLE_PATIENT, values, PatientEntry.KEY_PATIENT_ID + " = ?",
                new String[] { String.valueOf(patient.getPatient_id()) });
    }

    /**
     * Delete a Patient - this will also delete all records
     * for the patient
     */
    public void deletePatient(long id){
/*		RecordDataSource pra = new RecordDataSource(context);
		//get all records of the user
		List<Record> records = pra.getAllRecordsByPatient(id);

		for(Record record : records){
			pra.deleteRecord(record.getId());
		}*/

        //delete the patient
        this.db.delete(PatientEntry.TABLE_PATIENT, PatientEntry.KEY_PATIENT_ID + " = ?",
                new String[]{String.valueOf(id)});

    }

    /**
     * Delete all Patients - this will also delete all records
     * for the Patients
     */
    public void deleteAllPatients(){
        // TODO delete all other entries!!

        //delete all patients
        this.db.delete(PatientEntry.TABLE_PATIENT, null, null) ;

    }

}
