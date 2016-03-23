package com.example.jor.hospital.db.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jor.hospital.db.SQLiteDB;
import com.example.jor.hospital.db.Tables;
import com.example.jor.hospital.db.objects.Treatment;
import com.example.jor.hospital.db.Tables.TreatmentEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jor on 22.03.2016.
 */
public class TreatmentAdapter {

    private SQLiteDatabase db;
    private Context context;

    public TreatmentAdapter(Context context){
        SQLiteDB sqliteHelper = SQLiteDB.getInstance(context);
        db = sqliteHelper.getWritableDatabase();
        this.context = context;
    }

    /**
     * Insert a new treatment
     */
    public long createTreatment(Treatment treatment){
        long id;
        ContentValues values = new ContentValues();
        values.put(TreatmentEntry.KEY_TREATMENT, treatment.getTreatment());
        id = this.db.insert(TreatmentEntry.TABLE_TREATMENT, null, values);
        return id;
    }

    /**
     * Find one Treatment by Id
     */
    public Treatment getTreatmentById(long id){
        String sql = "SELECT * FROM " + TreatmentEntry.TABLE_TREATMENT +
                " WHERE " + TreatmentEntry.KEY_TREATMENT_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Treatment treatment = new Treatment();
        treatment.setTreatment_id(cursor.getInt(cursor.getColumnIndex(TreatmentEntry.KEY_TREATMENT_ID)));
        treatment.setTreatment(cursor.getString(cursor.getColumnIndex(TreatmentEntry.KEY_TREATMENT)));
        return treatment;
    }

    /**
     * Get all Treatments
     */
    public List<Treatment> getAllTreatments(){
        List<Treatment> treatments = new ArrayList<Treatment>();
        String sql = "SELECT * FROM " + TreatmentEntry.TABLE_TREATMENT + " ORDER BY " + TreatmentEntry.KEY_TREATMENT_ID;

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                Treatment treatment = new Treatment();
                treatment.setTreatment_id(cursor.getInt(cursor.getColumnIndex(TreatmentEntry.KEY_TREATMENT_ID)));
                treatment.setTreatment(cursor.getString(cursor.getColumnIndex(TreatmentEntry.KEY_TREATMENT)));
                treatments.add(treatment);

            } while(cursor.moveToNext());
        }

        return treatments;
    }

    /**
     *  Update a Treatment
     */
    public int updateTreatment(Treatment treatment){
        ContentValues values = new ContentValues();
        values.put(TreatmentEntry.KEY_TREATMENT, treatment.getTreatment());


        return this.db.update(TreatmentEntry.TABLE_TREATMENT, values, TreatmentEntry.KEY_TREATMENT_ID + " = ?",
                new String[] { String.valueOf(treatment.getTreatment_id())});
    }

    /**
     * Delete a Treatment - this will also delete all records
     * for the Treatment
     */
    public void deleteTreatment(long id){
/*		RecordDataSource pra = new RecordDataSource(context);
		//get all records of the user
		List<Record> records = pra.getAllRecordsByDoctor(id);

		for(Record record : records){
			pra.deleteRecord(record.getId());
		}*/

        //delete the doctor
        this.db.delete(TreatmentEntry.TABLE_TREATMENT, TreatmentEntry.KEY_TREATMENT_ID + " = ?",
                new String[]{String.valueOf(id)});

    }


    /**
     * Delete all Treatments - this will also delete all records
     * for the treatment
     */
    public void deleteAllTreatments(){
        // TODO delete all other entries!!

        //delete all treatments
        this.db.delete(TreatmentEntry.TABLE_TREATMENT, null, null) ;

    }
}
