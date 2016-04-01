package com.example.jor.hospital;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.jor.hospital.db.adapter.HistoryAdapter;
import com.example.jor.hospital.db.adapter.PatientAdapter;
import com.example.jor.hospital.db.adapter.TreatmentAdapter;
import com.example.jor.hospital.db.objects.History;
import com.example.jor.hospital.db.objects.Patient;
import com.example.jor.hospital.db.objects.Treatment;

import java.util.List;

public class ShowPatient extends Navigation {

    private String [] patients = {"14.03.2016 - Broken leg","11.09.2015 - Concussion", "01.01.2015 - Poisoning", "15.02.1999 - Broken arm", "18.01.1992 - Birth"};
    private ListView lvD;

    // db reference
    private int patient_id;
    private PatientAdapter pa;
    private HistoryAdapter ha;
    private List<History> allHistoryEntries;
    private HistoryAdap adapter;
    private TreatmentAdapter ta;


    // UI reference
    private TextView pat_name;
    private TextView pat_birth;
    private TextView pat_adress;
    private TextView pat_city;
    private TextView pat_ahv;
    private TextView pat_room;

    // data
    private Patient p;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

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
                newEntry();
            }
        });

        pa = new PatientAdapter(this);
        ha = new HistoryAdapter(this);
        ta = new TreatmentAdapter(this);

        // constructing UI
        pat_name = (TextView) findViewById(R.id.pat_name);
        pat_birth = (TextView) findViewById(R.id.pat_birth);
        pat_adress = (TextView) findViewById(R.id.pat_adress);
        pat_city = (TextView) findViewById(R.id.pat_city);
        pat_ahv = (TextView) findViewById(R.id.pat_ahv);
        pat_room = (TextView) findViewById(R.id.pat_room);



        Intent intent = getIntent();
        int i = intent.getIntExtra("patient_id",-1);
        if(i != -1){
            p =  pa.getPatientById(i);
            this.patient_id = p.getPatient_id();
        } else {
            p = null;
            this.patient_id = -1;
        }


        fillForm(p);

        lvD = (ListView) findViewById(R.id.listViewDossier);


        allHistoryEntries =  ha.getHistorysByPatient(patient_id);


        adapter = new HistoryAdap(this, allHistoryEntries);


        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("ShowPatient");
        spec.setContent(R.id.tab1);
        spec.setIndicator("ShowPatient");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Dossier");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Dossier");
        host.addTab(spec);

    }

    private void newEntry() {
        Intent newEntry = new Intent(this, NewPatEntry.class);
        startActivity(newEntry);
    }

    private void fillForm(Patient p){
        pat_name.setText(p.getFirstname() + " " + p.getLastname());
        pat_birth.setText(p.getBirthdate());
        pat_adress.setText(p.getAdress());
        pat_city.setText(p.getCity());
        pat_ahv.setText(p.getAhv_number());
        if (p.getRoom() == 0) {
            pat_room.setText("-");
        } else {
            pat_room.setText(p.getRoom());
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
        inflater.inflate(R.menu.showpatient_activity_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                editPatient(patient_id);
                return true;
            case R.id.action_delete:
                deletePatient(patient_id);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editPatient(int id){
        Intent editPatient = new Intent(this, NewPatient.class);
        editPatient.putExtra("patient_id", id);
        startActivity(editPatient);
    }

    private void deletePatient(int id){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        pa.deletePatient(patient_id);
                        goToPatientList();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this patient?").setNegativeButton("No", dialogClickListener).setPositiveButton("Yes", dialogClickListener).show();
    }

    private void goToPatientList(){
        Intent patList = new Intent(this, PatientlistActivity.class);
        startActivity(patList);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return super.onNavigationItemSelected(item);
    }


    // custom adapter class for listView
    public class HistoryAdap extends ArrayAdapter<History>  {

        public HistoryAdap(Context context, List<History> histories) {
            super(context, 0, histories);
        }




        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            History h = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_patient, parent, false);
            }
            // Lookup view for data population
            TextView name = (TextView) convertView.findViewById(R.id.item_patient_name);

            // Populate the data into the template view using the data object
            Treatment t = ta.getTreatmentById(h.getTreatment());

            name.setText(h.getStart_date() + " - " + t.getTreatment());
            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public Filter getFilter() {
            return super.getFilter();
        }
    }
}
