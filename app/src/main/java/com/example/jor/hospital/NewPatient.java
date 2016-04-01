package com.example.jor.hospital;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jor.hospital.db.adapter.PatientAdapter;
import com.example.jor.hospital.db.objects.Event;
import com.example.jor.hospital.db.objects.Patient;
import com.example.jor.hospital.db.objects.Room;

import java.util.Calendar;
import java.util.List;

public class NewPatient extends Navigation {

    // UI references
    private Spinner gender;
    private TextView lName;
    private TextView fName;
    private TextView bDate;
    private TextView adress;
    private TextView city;
    private TextView ahv_number;

    // db reference
    private PatientAdapter pa;
    private boolean updating = false;
    private int patient_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_patient);

        pa = new PatientAdapter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Add Navigation
        onCreateDrawer();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        // End Navigation Part


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


        // constructing UI
        gender = (Spinner) findViewById(R.id.spinner_gender);
        lName = (TextView) findViewById(R.id.lname);
        fName = (TextView) findViewById(R.id.fname);
        bDate = (TextView) findViewById(R.id.bdate);
        adress = (TextView) findViewById(R.id.adress);
        city = (TextView) findViewById(R.id.city);
        ahv_number = (TextView) findViewById(R.id.ahv_no);



        // new patient or modify patient
        Intent intent = getIntent();
        int i = intent.getIntExtra("patient_id",-1);
        if(i != -1){
            Patient p =  pa.getPatientById(i);
            updating = true;
            this.patient_id = i;
            toolbar.setTitle("Update Patient");
            fillForm(p);
        } else{
            this.patient_id = -1;
            toolbar.setTitle("Add Patient");
        }

    }


    @Override
    protected void onCreateDrawer() {
        super.onCreateDrawer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.newevent_acitivity_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save:
                savePatient();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return super.onNavigationItemSelected(item);
    }


    // fill form with patient
    public void fillForm(Patient p){

        // gender
        if (p.isGender() == true) {
            gender.setSelection(0);
        } else {
            gender.setSelection(1);
        }

        // lastname
        lName.setText(p.getLastname().toString());

        // firstname
        fName.setText(p.getFirstname().toString());

        // birthday
        bDate.setText(p.getBirthdate().toString());

        // adress
        adress.setText(p.getAdress().toString());

        // city
        city.setText(p.getCity().toString());

        // ahv-number
        ahv_number.setText(p.getAhv_number().toString());

    }

    // checking and saving a new patient
    private void savePatient(){
        boolean cancel = false;
        View focusView = null;

        // Reset errors.
        lName.setError(null);
        fName.setError(null);
        bDate.setError(null);
        adress.setError(null);
        city.setError(null);
        ahv_number.setError(null);


        // Check if user entered lastname
        String name = this.lName.getText().toString();
        if(TextUtils.isEmpty(name)){
            lName.setError(getString(R.string.error_field_required));
            focusView = lName;
            cancel = true;
        }

        // Check if user entered firstname
        name = this.fName.getText().toString();
        if(TextUtils.isEmpty(name)){
            fName.setError(getString(R.string.error_field_required));
            focusView = fName;
            cancel = true;
        }

        // Check if user entered birthdate
        name = this.bDate.getText().toString();
        if(TextUtils.isEmpty(name)){
            bDate.setError(getString(R.string.error_field_required));
            focusView = bDate;
            cancel = true;
        }

        // Check if user entered adress
        name = this.adress.getText().toString();
        if(TextUtils.isEmpty(name)){
            adress.setError(getString(R.string.error_field_required));
            focusView = adress;
            cancel = true;
        }

        // Check if user entered city
        name = this.city.getText().toString();
        if(TextUtils.isEmpty(name)){
            city.setError(getString(R.string.error_field_required));
            focusView = city;
            cancel = true;
        }


        // Check if user entered ahv-number
        name = this.ahv_number.getText().toString();
        if(TextUtils.isEmpty(name)){
            ahv_number.setError(getString(R.string.error_field_required));
            focusView = ahv_number;
            cancel = true;
        }


        // check if user entered all dates
        //if()

        if (cancel) {
            focusView.requestFocus();
        } else {
            Patient p = null;
            if(updating)
                p = pa.getPatientById(patient_id);
            else
                p = new Patient();
            // required fields
            if (this.gender.getSelectedItemPosition() == 0) {
                p.setGender(true);
            } else {
                p.setGender(false);
            }

            p.setLastname(this.lName.getText().toString());
            p.setFirstname(this.fName.getText().toString());
            p.setBirthdate(this.bDate.getText().toString());
            p.setAdress(this.adress.getText().toString());
            p.setCity(this.city.getText().toString());
            p.setAhv_number(this.city.getText().toString());


            if(updating) pa.updatePatient(p);
            else patient_id = (int) pa.createPatient(p);

            goToPatient();

        }

    }

    // open Patient after insert / update
    public void goToPatient(){
        Intent pat = new Intent(this, ShowPatient.class);
        pat.putExtra("patient_id", patient_id);
        startActivity(pat);
    }

}
