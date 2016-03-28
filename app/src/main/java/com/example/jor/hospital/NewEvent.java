package com.example.jor.hospital;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;

public class NewEvent extends AppCompatActivity{

    private Calendar c = Calendar.getInstance();
    private int startYear = c.get(Calendar.YEAR);
    private int startMonth = c.get(Calendar.MONTH);
    private int startDay = c.get(Calendar.DAY_OF_MONTH);
    private int startHour = c.get(Calendar.HOUR_OF_DAY);

    private Calendar fromDate;
    private Calendar toDate;

    // UI references
    private TextView eventName;
    private TextView room;
    private CheckBox wholeDay;
    private Spinner spinner;
    private Object notification;
    private Button btnFrom;
    private Button btnTo;
    private Button btnHourFrom;
    private Button btnHourTo;
    private TextView patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        // constructing UI
        eventName = (TextView) findViewById(R.id.newEvent_editText_name);
        room = (TextView) findViewById(R.id.newEvent_editText_room);
        btnFrom = (Button) findViewById(R.id.newEvent_button_from);
        btnTo = (Button) findViewById(R.id.newEvent_button_to);
        btnHourFrom = (Button)  findViewById(R.id.newEvent_button_from_hour);
        btnHourTo = (Button)  findViewById(R.id.newEvent_button_to_hour);
        patient = (TextView) findViewById(R.id.newEvent_editText_part);

        // constructing checkbox with listener
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

        // adding default values for the notifications
        addItemsToSpinner();

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

    // logic for checking and saving a new event
    private void saveEvent(){
        boolean cancel = false;
        View focusView = null;

        // Reset errors.
        eventName.setError(null);
        room.setError(null);
        btnFrom.setError(null);

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
        // Check if user entered a fromDate
        if(fromDate == null){
            this.btnFrom.setError(getString(R.string.error_field_required));
            focusView = this.btnFrom;
            cancel = true;
        }

        // Check if user entered a toDate
        if(toDate == null){
            this.btnTo.setError(getString(R.string.error_invalid_dates));
            focusView = this.btnTo;
            cancel = true;
        }
        // Check if user entered a patient
        String patient = this.patient.getText().toString();
        if(TextUtils.isEmpty(patient)){
            this.patient.setError(getString(R.string.error_field_required));
            focusView = this.patient;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // TODO create event
        }

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

    // adding values from xml to spinner
    public void addItemsToSpinner(){
        spinner = (Spinner) findViewById(R.id.newEvent_spinner_notification);
        spinner.setOnItemSelectedListener(new SpinnerActivity());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.arrray_notification, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }



    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            notification = parent.getItemAtPosition(pos);
        }
        public void onNothingSelected(AdapterView<?> parent) {}
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
                btnHourFrom.setText(Calender.formatDateGlobal(c.getTime()));
                break;
            case R.id.newEvent_button_to_hour:
                btnHourTo.setText(Calender.formatDateGlobal(c.getTime()));
                break;
        }
    }


    public class StartDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        private int mode;

        public StartDatePicker(int mode){
            this.mode = mode;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
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
                } else if(mode == 1){
                    toDate = Calendar.getInstance();
                    if(toDate.compareTo(fromDate)<0) {
                        toDate.set(fromDate.get(Calendar.YEAR), fromDate.get(Calendar.MONTH), fromDate.get(Calendar.DAY_OF_MONTH));
                        writeDateToButton(fromDate, R.id.newEvent_button_to);
                    }
                    else {
                        toDate.set(year, monthOfYear, dayOfMonth);
                        writeDateToButton(toDate, R.id.newEvent_button_to);
                    }
                }
            }
            else {
                fromDate = Calendar.getInstance();
                fromDate.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                writeDateToButton(c, R.id.newEvent_button_from);
            }
        }
    }

    public class StartTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        private int mode;

        public StartTimePicker(int mode){
            this.mode = mode;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            TimePickerDialog dialog = new TimePickerDialog(NewEvent.this, this, startHour, 0, true);
            return dialog;

        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if(mode == 0) {
                fromDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                fromDate.set(Calendar.MINUTE, minute);
            } else if(mode == 1) {
                if(hourOfDay < toDate.get(Calendar.HOUR_OF_DAY)){
                    writeDateToButton(fromDate, R.id.newEvent_button_to);
                }
                else {

                    writeDateToButton(toDate, R.id.newEvent_button_to);
                }
            }
        }
    }
}

