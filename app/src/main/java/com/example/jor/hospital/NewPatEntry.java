package com.example.jor.hospital;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.jor.hospital.db.adapter.DoctorAdapter;
import com.example.jor.hospital.db.adapter.EventAdapter;
import com.example.jor.hospital.db.adapter.HistoryAdapter;
import com.example.jor.hospital.db.adapter.PatientAdapter;
import com.example.jor.hospital.db.adapter.RoomAdapter;
import com.example.jor.hospital.db.adapter.TreatmentAdapter;
import com.example.jor.hospital.db.objects.Doctor;
import com.example.jor.hospital.db.objects.Event;
import com.example.jor.hospital.db.objects.History;
import com.example.jor.hospital.db.objects.Patient;
import com.example.jor.hospital.db.objects.Room;
import com.example.jor.hospital.db.objects.Treatment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class NewPatEntry extends Navigation {
    // data
    private List<Treatment> l_treat;
    private TreatAdap t_adap;

    // UI references
    private AutoCompleteTextView treat;
    private TextView histDesc;
    private TextView date;
    private AutoCompleteTextView doctor;



    // db reference
    private DoctorAdapter da;
    private PatientAdapter pa;
    private TreatmentAdapter ta;
    private HistoryAdapter ha;
    private int patient_id;
    private int treatment_id;
    private int doctor_id;
    private int history_id;
    private boolean updating = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_patentry);

        // constructing db reference
        ta = new TreatmentAdapter(this);
        da = new DoctorAdapter(this);
        pa = new PatientAdapter(this);
        ha = new HistoryAdapter(this);

        patient_id = IdCollection.patient_id;


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


        // constructing UI
        treat = (AutoCompleteTextView) findViewById(R.id.ph_treat);
        histDesc = (TextView) findViewById(R.id.ph_desc);
        date = (TextView) findViewById(R.id.ph_datefrom);
        doctor = (AutoCompleteTextView) findViewById(R.id.ph_doc);


        // new history or modify history
        Intent intent = getIntent();
        int i = intent.getIntExtra("history_id",-1);
        if(i != -1){
            History h =  ha.getHistoryById(i);
            updating = true;
            this.history_id = i;
            fillForm(h);
        } else{
            this.history_id = -1;
            doctor_id = IdCollection.doctor_id;
            Calendar c = Calendar.getInstance();
            date.setText(Calender.formatDateGlobal(c.getTime()));
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

    // adding the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.newevent_acitivity_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // adding actionlistener to the menu entry
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save:
                saveEvent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return super.onNavigationItemSelected(item);
    }


    // fill form with event
    public void fillForm(History h){

        // treatment
        Treatment t = ta.getTreatmentById(h.getTreatment());
        treat.setText(t.getTreatment());
        treatment_id = t.getTreatment_id();

        // desc
        histDesc.setText(h.getNotes().toString());

        // dates
        date.setText(h.getStart_date().toString());


        // doctor
        Doctor d = da.getDoctorById(h.getDoctor());
        doctor.setText(d.getName());
        doctor_id = d.getId();



    }



    // checking and saving a new patient entry
    private void saveEvent(){
        boolean cancel = false;
        View focusView = null;

        // Reset errors.
        treat.setError(null);
        histDesc.setError(null);
        date.setError(null);
        doctor.setError(null);


        // Check if user entered a from date
        String date = this.date.getText().toString();
        if(TextUtils.isEmpty(date)){
            this.date.setError(getString(R.string.error_field_required));
            focusView = this.date;
            cancel = true;
        }


        // Check if user entered a treatment
        String treatment = this.treat.getText().toString();
        if(TextUtils.isEmpty(treatment)){
            this.treat.setError(getString(R.string.error_field_required));
            focusView = this.treat;
            cancel = true;
        }

        // Check if user entered an valid treatment
        if(!TextUtils.isEmpty(treatment)) {
            if (!checkTreatment(l_treat, treatment)) {
                this.treat.setError(getString(R.string.error_invalid_treatment));
                focusView = this.treat;
                cancel = true;
            }
        }



        if (cancel) {
            focusView.requestFocus();
        } else {
            History h = null;
            if(updating)
                h = ha.getHistoryById(history_id);
            else
                h = new History();

            // required fields

            h.setPatient(this.patient_id);
            h.setTreatment(checkTreatmentID(l_treat, treatment));
            h.setNotes(this.histDesc.getText().toString().trim());
            h.setStart_date(this.date.getText().toString().trim());
            h.setDoctor(this.doctor_id);



            // optionals fields


            if(updating)ha.updateHistory(h);
            else ha.createHistory(h);


        }
    }

    // check if treatment exists
    public boolean checkTreatment(List<Treatment> list, String value){
        boolean exists = false;
        for (Treatment t : list){
            if(t.getTreatment().compareTo(value) == 0)
                exists = true;
        }
        return exists;
    }

    // get patient id from list
    private int checkTreatmentID(List<Treatment> list, String value){
        int index = -1;
        for (Treatment t : list){
            if(t.getTreatment().compareTo(value) == 0)
                index = t.getTreatment_id();
        }
        return index;
    }






    public static class TreatAdap extends ArrayAdapter<Treatment> implements Filterable {

        private ArrayList<Treatment> items;
        private ArrayList<Treatment> itemsAll;
        private ArrayList<Treatment> suggestions;

        public TreatAdap(Context context, List<Treatment> treats) {
            super(context, 0, treats);
            this.items = (ArrayList<Treatment>) treats;
            this.itemsAll = (ArrayList<Treatment>) items.clone();
            this.suggestions = new ArrayList<Treatment>();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Treatment t = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_room, parent, false);
            }
            // Lookup view for data population
            TextView nr = (TextView) convertView.findViewById(R.id.item_room_number);
            // Populate the data into the template view using the data object
            nr.setText(t.getTreatment());
            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public Filter getFilter() {
            return nameFilter;
        }

        Filter nameFilter = new Filter() {
            public String convertResultToString(Object resultValue) {
                String str = ((Treatment) (resultValue)).getTreatment();
                return str;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    suggestions.clear();
                    for (Treatment d : itemsAll) {
                        String s = d.getTreatment().toLowerCase();
                        if (s.contains(constraint.toString().toLowerCase())) {
                            suggestions.add(d);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                @SuppressWarnings("unchecked")
                ArrayList<Treatment> filteredList = (ArrayList<Treatment>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    for (Treatment c : filteredList) {
                        add(c);
                    }
                    notifyDataSetChanged();
                }
            }
        };
    }
}
