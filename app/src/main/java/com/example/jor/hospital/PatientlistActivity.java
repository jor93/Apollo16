package com.example.jor.hospital;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.jor.hospital.db.adapter.PatientAdapter;
import com.example.jor.hospital.db.objects.Patient;

import java.util.List;

public class PatientlistActivity extends Navigation {

    private ListView lv;
    private List<Patient> allPatients;
    private SearchView sv;

    private PatientAdapter pa;
    private PatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientlist);


        pa = new PatientAdapter(this);

        // Add Toolbar
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


        lv = (ListView) findViewById(R.id.listView);


        allPatients =  pa.getAllPatients();

        adapter = new PatAdapter(this, allPatients);



        lv.setAdapter(adapter);
        lv.setTextFilterEnabled(true);
        sv = (SearchView) findViewById(R.id.searchView);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                Patient itemValue = (Patient) lv.getItemAtPosition(position);
                showPatient(itemValue.getPatient_id());
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPatient();
            }
        });




    }

    private void newPatient() {
        Intent newPatient = new Intent(this, NewPatient.class);
        startActivity(newPatient);
    }


    private void showPatient(int id){
        Intent showPatient = new Intent(this, ShowPatient.class);
        showPatient.putExtra("patient_id", id);
        startActivity(showPatient);
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return super.onNavigationItemSelected(item);
    }


    // custom adapter class for listView
    public static class PatAdapter extends ArrayAdapter<Patient> implements Filterable{

        public PatAdapter(Context context, List<Patient> patients) {
            super(context, 0, patients);
        }




        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Patient p = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_patient, parent, false);
            }
            // Lookup view for data population
            TextView name = (TextView) convertView.findViewById(R.id.item_patient_name);

            // Populate the data into the template view using the data object
            name.setText(p.getLastname() + " " + p.getFirstname());
            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public Filter getFilter() {
            return super.getFilter();
        }
    }

}
