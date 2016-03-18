package com.example.jor.hospital;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private CheckBox wholeDay;
    private Spinner spinner;
    private Object notification;
    private Button btnFrom;
    private Button btnTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        wholeDay = (CheckBox) findViewById(R.id.newEvent_checkBox_dayevent);
        wholeDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Button timePickerFrom = (Button) findViewById(R.id.newEvent_button_from_hour);
                Button timePickerTo = (Button) findViewById(R.id.newEvent_button_to_hour);

                if(isChecked){
                    timePickerFrom.setEnabled(false);
                    timePickerTo.setEnabled(false);
                }else{
                    timePickerFrom.setEnabled(true);
                    timePickerTo.setEnabled(true);
                }
            }
        });

        // Adding default values for the notifications
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

    private void saveEvent(){

    }

    // starting datepicker fragment
    public void showStartDateDialog(View v){
        DialogFragment dialogFragment = new StartDatePicker();
        dialogFragment.show(getFragmentManager(), "start_date_picker");
    }

    // starting timepicker fragment
    public void showStartTimeDialog(View v){
        DialogFragment dialogFragment = new StartTimePicker();
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


    public class StartDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            DatePickerDialog dialog = new DatePickerDialog(NewEvent.this, this, startYear, startMonth, startDay);
            return dialog;

        }
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            if(c.get(Calendar.DAY_OF_MONTH) < dayOfMonth || c.get(Calendar.MONTH) < monthOfYear || c.get(Calendar.YEAR) < year){
                fromDate = Calendar.getInstance();
                fromDate.set(year,monthOfYear,dayOfMonth);
                btnFrom = (Button) findViewById(R.id.newEvent_button_from);
                btnFrom.setText(Calender.formatDateGlobal(fromDate.getTime()));
            }
            else
                Log.d("adsfasdfasfasdf", "error date is past");
            startYear = year;
            startMonth = monthOfYear;
            startDay = dayOfMonth;
        }
    }

    public class StartTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            TimePickerDialog dialog = new TimePickerDialog(NewEvent.this, this, startHour, 0, true);
            return dialog;

        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO return the time choosen by user
        }
    }
}

