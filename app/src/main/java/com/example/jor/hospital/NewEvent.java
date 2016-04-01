package com.example.jor.hospital;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.jor.hospital.db.adapter.EventAdapter;
import com.example.jor.hospital.db.adapter.PatientAdapter;
import com.example.jor.hospital.db.adapter.RoomAdapter;
import com.example.jor.hospital.db.objects.Event;
import com.example.jor.hospital.db.objects.Patient;
import com.example.jor.hospital.db.objects.Room;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewEvent extends Navigation{

    // data
    private Calendar fromDate;
    private Calendar toDate;
    private int notification;
    private int startYear;
    private int startMonth;
    private int startDay;
    private int startHour;
    private List<Room> rooms;
    private RoomAdap adapter;
    private List<Patient> patients;
    private PatientAdap pAdapter;

    // UI references
    private TextView eventName;
    private AutoCompleteTextView room;
    private CheckBox wholeDay;
    private Spinner spinner;
    private Button btnFrom;
    private Button btnTo;
    private Button btnHourFrom;
    private Button btnHourTo;
    private AutoCompleteTextView patient;
    private TextView desc;

    // db reference
    private EventAdapter ea;
    private RoomAdapter ra;
    private PatientAdapter pa;
    private int patient_id;
    private boolean updating = false;
    private int event_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        setTitle("New event");

        // constructing db reference
        ea = new EventAdapter(this);
        ra = new RoomAdapter(this);
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

        // constructing UI
        eventName = (TextView) findViewById(R.id.newEvent_editText_name);
        room = (AutoCompleteTextView) findViewById(R.id.newEvent_autoTextView_room);
        btnFrom = (Button) findViewById(R.id.newEvent_button_from);
        btnTo = (Button) findViewById(R.id.newEvent_button_to);
        btnHourFrom = (Button)  findViewById(R.id.newEvent_button_from_hour);
        btnHourTo = (Button)  findViewById(R.id.newEvent_button_to_hour);
        patient = (AutoCompleteTextView) findViewById(R.id.newEvent_autoTextView_part);
        desc = (TextView) findViewById(R.id.newEvent_editText_desc);

        wholeDay = (CheckBox) findViewById(R.id.newEvent_checkBox_dayevent);
        wholeDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnHourFrom.setEnabled(false);
                    btnHourTo.setEnabled(false);
                } else {
                    btnHourFrom.setEnabled(true);
                    btnHourTo.setEnabled(true);
                }
            }
        });

        rooms = ra.getAllRooms();
        adapter = new RoomAdap(this, rooms);
        adapter.setNotifyOnChange(true);
        room.setAdapter(adapter);
        room.setThreshold(1);

        patients = pa.getAllPatients();
        pAdapter = new PatientAdap(this, patients);
        patient.setAdapter(pAdapter);
        patient.setThreshold(1);

        addItemsToSpinner();

        Intent intent = getIntent();
        int i = intent.getIntExtra("event_id",-1);
        Calendar temp = ((Calendar)getIntent().getSerializableExtra("date"));
        if(i != -1){
            Event e =  ea.getEventById(i);
            updating = true;
            this.event_id = e.getEvent_id();
            fillForm(e);
        } else{
            this.event_id = -1;
            this.fromDate = temp;
            startYear = fromDate.get(Calendar.YEAR);
            startMonth = fromDate.get(Calendar.MONTH);
            startDay = fromDate.get(Calendar.DAY_OF_MONTH);
            startHour = fromDate.get(Calendar.HOUR_OF_DAY);
            writeDateToButton(fromDate, R.id.newEvent_button_from);
        }
    }

    private void startAlarm(Event e, Calendar c) {
        Calendar now = Calendar.getInstance();
        if(c.before(now)) return;
        AlarmReceiver.id = e.getEvent_id();
        AlarmReceiver.s = e.getEventname();
        long reminderTime = c.getTimeInMillis() - AlarmReceiver.notValues[e.getNotificiation()];
        AlarmReceiver.time = reminderTime;

        Intent intentAlarm = new Intent(this,AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTime, PendingIntent.getBroadcast(this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
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

    // fill form with event
    public void fillForm(Event e){
        // name
        eventName.setText(e.getEventname().toString());
        // room
        room.setText(e.getRoom() + "");
        // dates
        Calendar from = extractDate(e.getFromDate());
        writeDateToButton(from, R.id.newEvent_button_from);
        Calendar to = extractDate(e.getToDate());
        writeDateToButton(to, R.id.newEvent_button_to);
        Calendar fromTime = null;
        Calendar toTime = null;
        if(e.getFromTime() == -1)
            wholeDay.setChecked(true);
        else {
            fromTime = extractTime(e.getFromTime());
            writeDateToButton(fromTime, R.id.newEvent_button_from_hour);
            toTime = extractTime(e.getToTime());
            writeDateToButton(toTime, R.id.newEvent_button_to_hour);
        }
        // notification
        spinner.setSelection(e.getNotificiation());
        // patient
        PatientAdapter pa = new PatientAdapter(this);
        Patient p = pa.getPatientById(e.getPatient());
        patient_id = p.getPatient_id();
        patient.setText(p.getName());
        // desc
        desc.setText(e.getDescription().toString());
        // putting time and date togehter
        this.fromDate = Calendar.getInstance();
        this.toDate = Calendar.getInstance();
        this.fromDate.set(from.get(Calendar.YEAR), from.get(Calendar.MONTH), from.get(Calendar.DAY_OF_MONTH),
                (fromTime != null ? fromTime.get(Calendar.HOUR_OF_DAY) : 0),
                (fromTime != null ? fromTime.get(Calendar.MINUTE) : 0));
        this.toDate.set(to.get(Calendar.YEAR), to.get(Calendar.MONTH), to.get(Calendar.DAY_OF_MONTH),
                (toTime != null ? toTime.get(Calendar.HOUR_OF_DAY) : 0),
                (toTime != null ? toTime.get(Calendar.MINUTE) : 0));
        startYear = fromDate.get(Calendar.YEAR);
        startMonth = fromDate.get(Calendar.MONTH);
        startDay = fromDate.get(Calendar.DAY_OF_MONTH);
        startHour = fromDate.get(Calendar.HOUR_OF_DAY);
    }

    // checking and saving a new event
    private void saveEvent(){
        boolean cancel = false;
        View focusView = null;

        // Reset errors.
        eventName.setError(null);
        room.setError(null);
        btnFrom.setError(null);
        btnTo.setError(null);
        btnHourFrom.setError(null);
        btnHourTo.setError(null);
        patient.setError(null);

        // Check if user entered an eventname
        String name = this.eventName.getText().toString();
        if(TextUtils.isEmpty(name)){
            eventName.setError(getString(R.string.error_field_required));
            focusView = eventName;
            cancel = true;
        }
        // Check if user entered an room
       String room = this.room.getText().toString();
        if(TextUtils.isEmpty(room)){
            this.room.setError(getString(R.string.error_field_required));
            focusView = this.room;
            cancel = true;
        }

        // Check if user entered an valid room
        if(!TextUtils.isEmpty(room)) {
            if (!checkRoom(rooms, Integer.parseInt(room))) {
                this.room.setError(getString(R.string.error_invalid_room));
                focusView = this.room;
                cancel = true;
            }
        }

        // Check if user entered a fromDate
        if(fromDate == null){
            this.btnFrom.setError(getString(R.string.error_field_required));
            focusView = this.btnFrom;
            cancel = true;
            if(!wholeDay.isChecked()){
                this.btnHourFrom.setError(getString(R.string.error_field_required));
            }
        }

        // Check if user entered a toDate
        if(toDate == null){
            this.btnTo.setError(getString(R.string.error_invalid_dates));
            focusView = this.btnTo;
            cancel = true;
            if(!wholeDay.isChecked()){
                this.btnHourTo.setError(getString(R.string.error_field_required));
            }
        }

        // Check if user entered an hour if whole day is unchecked
        if(!wholeDay.isChecked()){
            if(this.btnHourFrom.getText().toString().compareTo(getString(R.string.prompt_hour_from)) == 0){
                this.btnHourFrom.setError(getString(R.string.error_field_required));
                focusView = this.btnHourFrom;
                cancel = true;
            }
            if(this.btnHourTo.getText().toString().compareTo(getString(R.string.prompt_hour_to)) == 0){
                this.btnHourTo.setError(getString(R.string.error_field_required));
                focusView = this.btnHourTo;
                cancel = true;
            }
        }

        // Check if user entered right dates
        if(toDate != null && fromDate != null) {
            if (toDate.before(fromDate)) {
                this.btnTo.setError(getString(R.string.error_invalid_dates));
                if (!wholeDay.isChecked())
                    this.btnHourTo.setError(getString(R.string.error_invalid_dates));
                focusView = this.btnTo;
                cancel = true;
            }
        }

        // Check if user entered a patient
        String patient = this.patient.getText().toString();
        if(TextUtils.isEmpty(patient)){
            this.patient.setError(getString(R.string.error_field_required));
            focusView = this.patient;
            cancel = true;
        }

        // Check if user entered an valid patient
        if(!TextUtils.isEmpty(patient)) {
            if (!checkPatient(patients, patient)) {
                this.patient.setError(getString(R.string.error_invalid_patient));
                focusView = this.patient;
                cancel = true;
            }
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Event e = null;
            if(updating)
                e = ea.getEventById(event_id);
            else
                e = new Event();

            // required fields
            e.setEventname(this.eventName.getText().toString().trim());
            e.setRoom(Integer.parseInt(this.room.getText().toString().trim()));
            e.setFromDate(parseDateForDB(fromDate));
            e.setToDate(parseDateForDB(toDate));
            e.setNotificiation(this.notification);
            e.setPatient(checkPatientID(patients, patient));
            e.setDoctor(IdCollection.doctor_id);

            // optionals fields
            e.setDescription(this.desc.getText().toString().trim());
            if(!wholeDay.isChecked()){
                e.setFromTime(parseTimeForDB(fromDate));
                e.setToTime(parseTimeForDB(toDate));
            }else {
                e.setFromTime(-1);
                e.setToTime(-1);
            }

            if(updating)ea.updateEvent(e);
            else ea.createEvent(e);


            startAlarm(e,fromDate);
            goBackToCal();
        }
    }

    // check if room exists
    public boolean checkRoom(List<Room> list, int value){
        boolean exists = false;
        for (Room r : list){
            if(r.getId() == value)
                exists = true;
        }
        return exists;
    }

    // check if patient exists
    public boolean checkPatient(List<Patient> list, String value){
        boolean exists = false;
        for (Patient r : list){
            if(r.getName().compareTo(value) == 0)
                exists = true;
        }
        return exists;
    }

    // get patient id from list
    private int checkPatientID(List<Patient> list, String value){
        int index = -1;
        for (Patient r : list){
            if(r.getName().compareTo(value) == 0)
                index = r.getPatient_id();
        }
        return index;
    }

    // go back to overview
    public void goBackToCal(){
        Intent cal = new Intent(this, Calender.class);
        cal.putExtra("startDate", fromDate);
        cal.putExtra("updatedEvent", updating);
        startActivity(cal);
    }

    // creating Calender object with date
    public static Calendar extractDate(int date){
        Calendar c = Calendar.getInstance();
        String d =  date+"";
        int year = Integer.parseInt(d.substring(0, 4));
        int month = Integer.parseInt(d.substring(4, 6))-1;
        int day = Integer.parseInt(d.substring(6, 8));
        c.set(year, month, day);
        return c;
    }

    // creating Calender object with time
    public static Calendar extractTime(int time){
        Calendar c = Calendar.getInstance();
        String d = time+"";
        if(d.length() == 3)
            d = "0"+d;
        int hour = Integer.parseInt(d.substring(0, 2));
        int min = Integer.parseInt(d.substring(2, 4));
        c.set(Calendar.HOUR_OF_DAY,hour);
        c.set(Calendar.MINUTE,min);
        return c;
    }

    // make date ready to write in db
    public static int parseDateForDB(Calendar c){
        return Integer.parseInt(c.get(Calendar.YEAR) + ""
                + (c.get(Calendar.MONTH) < 10 ? 0 + "" + (c.get(Calendar.MONTH) + 1) : (c.get(Calendar.MONTH) + 1)) + ""
                + (c.get(Calendar.DAY_OF_MONTH) < 10 ? 0 + "" + c.get(Calendar.DAY_OF_MONTH) : c.get(Calendar.DAY_OF_MONTH)));
    }

    // make time ready to write in db
    public static int parseTimeForDB(Calendar c){
            return Integer.parseInt(c.get(Calendar.HOUR_OF_DAY) + ""
                + (c.get(Calendar.MINUTE) < 10 ? 0 + "" + c.get(Calendar.MINUTE) : c.get(Calendar.MINUTE)));
    }

    // method to write data in correct form to the buttons
    private void writeDateToButton(Calendar c, int id){
        switch (id){
            case R.id.newEvent_button_from:
                btnFrom.setText(Calender.formatDateGlobal(c.getTime()));
                break;
            case R.id.newEvent_button_to:
                btnTo.setText(Calender.formatDateGlobal(c.getTime()));
                break;
            case R.id.newEvent_button_from_hour:
                btnHourFrom.setText(Calender.formatTimeGlobal(c.getTime()));
                break;
            case R.id.newEvent_button_to_hour:
                btnHourTo.setText(Calender.formatTimeGlobal(c.getTime()));
                break;
        }
    }

    // adding values from xml to spinner
    public void addItemsToSpinner(){
        spinner = (Spinner) findViewById(R.id.newEvent_spinner_notification);
        spinner.setOnItemSelectedListener(new SpinnerActivity());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.arrray_notification, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // remove time to ensure check
    public static Calendar removeTime(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    // starting datepicker fragment
    // 0=from; 1=to
    public void showStartDateDialog(View v){
        DialogFragment dialogFragment = null;
        switch (v.getId()) {
            case R.id.newEvent_button_from:
                    dialogFragment = new StartDatePicker(0);
                break;
            case R.id.newEvent_button_to:
                dialogFragment = new StartDatePicker(1);
                break;
        }
        dialogFragment.show(getFragmentManager(), "start_date_picker");
    }

    // starting timepicker fragment
    // 0=from; 1=to
    public void showStartTimeDialog(View v){
        DialogFragment dialogFragment = null;
        switch (v.getId()) {
            case R.id.newEvent_button_from_hour:
                dialogFragment = new StartTimePicker(0);
                break;
            case R.id.newEvent_button_to_hour:
                dialogFragment = new StartTimePicker(1);
                break;
        }
        dialogFragment.show(getFragmentManager(), "start_time_picker");
    }


    public class StartDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        private int mode;

        public StartDatePicker(int mode){
            this.mode = mode;
        }

        @Override
        public void onDestroyView() {
            if (getDialog() != null && getRetainInstance())
                getDialog().setDismissMessage(null);
            super.onDestroyView();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            setRetainInstance(true);
            // Use the current date as the default date in the picker
            DatePickerDialog dialog = new DatePickerDialog(NewEvent.this, this, startYear, startMonth, startDay);
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            if(c.get(Calendar.DAY_OF_MONTH) < dayOfMonth || c.get(Calendar.MONTH) < monthOfYear || c.get(Calendar.YEAR) < year){
                if(mode == 0) {
                    fromDate = Calendar.getInstance();
                    fromDate.set(year, monthOfYear, dayOfMonth);
                    writeDateToButton(fromDate, R.id.newEvent_button_from);
                    startYear = fromDate.get(Calendar.YEAR);
                    startMonth = fromDate.get(Calendar.MONTH);
                    startDay = fromDate.get(Calendar.DAY_OF_MONTH);
                } else if(mode == 1){
                    if(fromDate == null) return;
                    toDate = Calendar.getInstance();
                    toDate.set(year, monthOfYear, dayOfMonth);
                    Calendar first = removeTime(fromDate);
                    Calendar second = removeTime(toDate);
                    if(!first.before(second)) {
                        toDate.set(fromDate.get(Calendar.YEAR), fromDate.get(Calendar.MONTH), fromDate.get(Calendar.DAY_OF_MONTH));
                        writeDateToButton(toDate, R.id.newEvent_button_to);
                    }
                    else {
                        toDate.set(year, monthOfYear, dayOfMonth);
                        writeDateToButton(toDate, R.id.newEvent_button_to);
                    }
                }
            }
            else {
                if(mode == 0) {
                    fromDate = Calendar.getInstance();
                    fromDate.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    writeDateToButton(c, R.id.newEvent_button_from);
                } else{
                    if(fromDate == null) return;
                    toDate = Calendar.getInstance();
                    toDate.set(fromDate.get(Calendar.YEAR), fromDate.get(Calendar.MONTH), fromDate.get(Calendar.DAY_OF_MONTH));
                    writeDateToButton(toDate, R.id.newEvent_button_to);
                }
            }
        }
    }

    public class StartTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        private int mode;

        public StartTimePicker(int mode){
            this.mode = mode;
        }

        @Override
        public void onDestroyView() {
            if (getDialog() != null && getRetainInstance())
                getDialog().setDismissMessage(null);
            super.onDestroyView();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            setRetainInstance(true);
            // Use the current date as the default date in the picker
            TimePickerDialog dialog = new TimePickerDialog(NewEvent.this, this, startHour, 0, true);
            return dialog;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if(fromDate == null) return;
            if(mode == 0) {
                fromDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                fromDate.set(Calendar.MINUTE, minute);
                writeDateToButton(fromDate, R.id.newEvent_button_from_hour);
                startHour = fromDate.get(Calendar.HOUR_OF_DAY);
            } else if(mode == 1) {
                if(toDate == null) return;
                if(fromDate.get(Calendar.YEAR) == toDate.get(Calendar.YEAR) &&
                        fromDate.get(Calendar.DAY_OF_YEAR) == toDate.get(Calendar.DAY_OF_YEAR)){
                    if(fromDate.get(Calendar.HOUR_OF_DAY) > hourOfDay){
                        toDate.set(Calendar.HOUR_OF_DAY, fromDate.get(Calendar.HOUR_OF_DAY));
                        toDate.set(Calendar.MINUTE, fromDate.get(Calendar.MINUTE));
                        writeDateToButton(toDate, R.id.newEvent_button_to_hour);
                    }
                    else {
                        toDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        toDate.set(Calendar.MINUTE, minute);
                        writeDateToButton(toDate, R.id.newEvent_button_to_hour);
                    }
                } else{
                    toDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    toDate.set(Calendar.MINUTE, minute);
                    writeDateToButton(toDate, R.id.newEvent_button_to_hour);
                }
            }
        }
    }

    public static class RoomAdap extends ArrayAdapter<Room> implements Filterable{

        private ArrayList<Room> items;
        private ArrayList<Room> itemsAll;
        private ArrayList<Room> suggestions;

        public RoomAdap(Context context, List<Room> rooms) {
            super(context, 0, rooms);
            this.items = (ArrayList<Room>) rooms;
            this.itemsAll = (ArrayList<Room>) items.clone();
            this.suggestions = new ArrayList<Room>();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Room r = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_room, parent, false);
            }
            // Lookup view for data population
            TextView nr = (TextView) convertView.findViewById(R.id.item_room_number);
            // Populate the data into the template view using the data object
            nr.setText(r.getId()+"");
            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public Filter getFilter() {
            return nameFilter;
        }

        Filter nameFilter = new Filter() {
            public String convertResultToString(Object resultValue) {
                String str = ((Room) (resultValue)).getId()+"";
                return str;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    suggestions.clear();
                    for (Room r : itemsAll) {
                        String s = r.getId() + "";
                        if (s.startsWith(constraint.toString().toLowerCase())) {
                            suggestions.add(r);
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
                ArrayList<Room> filteredList = (ArrayList<Room>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    for (Room c : filteredList) {
                        add(c);
                    }
                    notifyDataSetChanged();
                }
            }
        };
    }


    public static class PatientAdap extends ArrayAdapter<Patient> implements Filterable{

        private ArrayList<Patient> items;
        private ArrayList<Patient> itemsAll;
        private ArrayList<Patient> suggestions;

        public PatientAdap(Context context, List<Patient> pats) {
            super(context, 0, pats);
            this.items = (ArrayList<Patient>) pats;
            this.itemsAll = (ArrayList<Patient>) items.clone();
            this.suggestions = new ArrayList<Patient>();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Patient r = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_room, parent, false);
            }
            // Lookup view for data population
            TextView nr = (TextView) convertView.findViewById(R.id.item_room_number);
            // Populate the data into the template view using the data object
            nr.setText(r.getName());
            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public Filter getFilter() {
            return nameFilter;
        }

        Filter nameFilter = new Filter() {
            public String convertResultToString(Object resultValue) {
                String str = ((Patient) (resultValue)).getName();
                return str;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    suggestions.clear();
                    for (Patient r : itemsAll) {
                        String s = r.getName().toLowerCase();
                        if (s.contains(constraint.toString().toLowerCase())) {
                            suggestions.add(r);
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
                ArrayList<Patient> filteredList = (ArrayList<Patient>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    for (Patient c : filteredList) {
                        add(c);
                    }
                    notifyDataSetChanged();
                }
            }
        };
    }

    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            notification = pos;
        }
        public void onNothingSelected(AdapterView<?> parent) {}
    }



}

