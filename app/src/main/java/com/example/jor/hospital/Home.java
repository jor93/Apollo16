package com.example.jor.hospital;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jor.hospital.db.adapter.DoctorAdapter;
import com.example.jor.hospital.db.adapter.EventAdapter;
import com.example.jor.hospital.db.adapter.PatientAdapter;
import com.example.jor.hospital.db.objects.Doctor;
import com.example.jor.hospital.db.objects.Event;
import com.example.jor.hospital.db.objects.Patient;

import java.util.Calendar;
import java.util.List;

public class Home extends Navigation {

    // data
    private Doctor doctor;

    // UI refrence
    private ListView listView;
    private Home.HomeAdap adapter;
    private List<Event> events;

    // db reference
    private EventAdapter ea;
    private PatientAdapter pa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Home");

        // constructing db reference
        ea = new EventAdapter(this);
        pa = new PatientAdapter(this);

        // Add Navigation
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        onCreateDrawer();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        // End Navigation Part

        Intent intent = getIntent();
        DoctorAdapter da = new DoctorAdapter(this);
        int i = intent.getIntExtra("doctor_id",-1);
        if(i != -1) {
            doctor = da.getDoctorById(i);
            IdCollection.doctor_id = i;
        } else {
            doctor = null;
            IdCollection.doctor_id = -1;
        }

        if(doctor != null)
            IdCollection.doctor_name = doctor.getName();

        listView = (ListView) findViewById(R.id.home_listView_Events);
        events = ea.getNext3EventsByDoctor(IdCollection.doctor_id);
        adapter = new Home.HomeAdap(this, events);
        this.listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                Event itemValue = (Event) listView.getItemAtPosition(position);
                showEvent(itemValue.getEvent_id());
            }
        });

    }


    public Doctor getDoctor() {
        return doctor;
    }

    private void showEvent(int id){
        Intent showEvent = new Intent(this, ShowEvent.class);
        showEvent.putExtra("event_id", id);
        startActivity(showEvent);
    }

    @Override
    public void onResume() {
        super.onResume();
        events = ea.getNext3EventsByDoctor(IdCollection.doctor_id);
        adapter = new Home.HomeAdap(this, events);
        listView.setAdapter(adapter);
    }

    // adding the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_activity_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // adding actionlistener to the menu entry
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // go to login activity
    private void goToLogin(){
        IdCollection.doctor_id = -1;
        IdCollection.doctor_name = "";
        if(doctor != null) doctor = null;
        Intent login = new Intent(this, Login.class);
        startActivity(login);
    }

    // show confirmation button to log out
    private void logout(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        goToLogin();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?").setNegativeButton("No", dialogClickListener).setPositiveButton("Yes", dialogClickListener).show();
    }

    public class HomeAdap extends ArrayAdapter<Event> {

        public HomeAdap(Context context, List<Event> events) {
            super(context, 0, events);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Event e = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_home, parent, false);
            }
            // Lookup view for data population
            TextView date = (TextView) convertView.findViewById(R.id.item_event_day);
            TextView time = (TextView) convertView.findViewById(R.id.item_event_time);
            TextView name = (TextView) convertView.findViewById(R.id.item_event_name);
            TextView room = (TextView) convertView.findViewById(R.id.item_event_room);

            // Populate the data into the template view using the data object
            date.setText(Calender.formatDateGlobal(NewEvent.extractDate(e.getFromDate()).getTime()) + " ");
            if(e.getFromTime() == -1)
                time.setText("Whole day ");
            else
                time.setText(Calender.formatTimeGlobal(NewEvent.extractTime(e.getFromTime()).getTime()) + " ");
            name.setText(e.getEventname()+ " ");
            Patient p = pa.getPatientById(e.getPatient());
            room.setText("in Room " + e.getRoom() + " with " + p.getName() );
            // Return the completed view to render on screen
            return convertView;
        }
    }


    // Methods from Abstract Class

    @Override
    protected void onCreateDrawer() {
        super.onCreateDrawer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return super.onNavigationItemSelected(item);
    }
}
