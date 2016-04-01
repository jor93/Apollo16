package com.example.jor.hospital;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jor.hospital.db.adapter.EventAdapter;
import com.example.jor.hospital.db.adapter.PatientAdapter;
import com.example.jor.hospital.db.objects.Event;
import com.example.jor.hospital.db.objects.Patient;

public class ShowEvent extends Navigation {

    // db reference
    private int event_id;
    private EventAdapter ea;
    private PatientAdapter pa;

    // UI reference
    private TextView date;
    private TextView wholeDay;
    private TextView eventName;
    private TextView not;
    private TextView room;
    private TextView patient;
    private TextView desc;

    // data
    private Event e;
    private String eventNameSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

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

        ea = new EventAdapter(this);
        pa = new PatientAdapter(this);
        // constructing UI
        date = (TextView) findViewById(R.id.showEvent_textView_date);
        wholeDay = (TextView) findViewById(R.id.showEvent_textView_wholeDay);
        eventName = (TextView) findViewById(R.id.showEvent_textView_name);
        not = (TextView) findViewById(R.id.showEvent_textView_notification);
        room = (TextView) findViewById(R.id.showEvent_textView_room);
        patient = (TextView) findViewById(R.id.showEvent_textView_patient);
        desc = (TextView)  findViewById(R.id.showEvent_textView_description);

        Intent intent = getIntent();
        int i = intent.getIntExtra("event_id",-1);
        if(i != -1){
            e =  ea.getEventById(i);
            this.event_id = e.getEvent_id();
            fillForm(e);
        } else {
            e = null;
            this.event_id = -1;
        }
        fillForm(e);
    }

    private void fillForm(Event e){
        date.setText(Calender.formatDateGlobal(NewEvent.extractDate(e.getFromDate()).getTime()));
        if(e.getFromTime() == -1){
            wholeDay.setText("Whole Day");
        } else{
            wholeDay.setText(Calender.formatTimeGlobal(NewEvent.extractTime(e.getFromTime()).getTime()) + " - " + Calender.formatTimeGlobal(NewEvent.extractTime(e.getToTime()).getTime()));
        }
        eventName.setText(e.getEventname());
        String [] array = getResources().getStringArray(R.array.arrray_notification);
        not.setText(array[e.getNotificiation()]);
        room.setText("Room " + e.getRoom());
        Patient p = pa.getPatientById(e.getPatient());
        patient.setText(p.getName());
        if(!TextUtils.isEmpty(e.getDescription()))
            desc.setText(e.getDescription());
        else
            desc.setText("-");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.showevent_activitiy_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }


    // adding actionlistener to the menu entry
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_edit:
                    editEvent(event_id);
                return true;
            case R.id.action_delete:
                    deleteEvent(event_id);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editEvent(int id){
        Intent editEvent = new Intent(this, NewEvent.class);
        editEvent.putExtra("event_id", id);
        startActivity(editEvent);
    }

    private void goToCal(){
        Intent cal = new Intent(this, Calender.class);
        cal.putExtra("deleted", true);
        cal.putExtra("eventName", eventNameSaved);
        startActivity(cal);
    }

    private void deleteEvent(int id){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        eventNameSaved = e.getEventname();
                        ea.deleteEvent(event_id);
                        goToCal();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this event?").setNegativeButton("No", dialogClickListener).setPositiveButton("Yes", dialogClickListener).show();
    }
}
