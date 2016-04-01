package com.example.jor.hospital;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;
import com.example.jor.hospital.db.adapter.EventAdapter;
import com.example.jor.hospital.db.objects.Event;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Calender extends Navigation {

    // UI reference
    private CalendarView calender;
    private TextView today;
    private Calendar savedDate;

    // data
    private DateFormat df = new SimpleDateFormat("EEEE, dd. MMMM");
    private static DateFormat df_global = new SimpleDateFormat("EEEE, dd. MMMM yyyy");
    private static DateFormat df_globalTime = new SimpleDateFormat("hh:mm a");

    // listview object
    private ListView listView;
    private List<Event> eventsForDay;
    private EventAdap adapter;

    // db reference
    private EventAdapter ea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        setTitle("Calendar");

        // constructing db reference
        ea = new EventAdapter(this);

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

        // constructing UI reference
        today = (TextView) findViewById(R.id.calendar_textView_today);
        calender = (CalendarView) findViewById(R.id.calenderView);
        calender.setOnDateChangeListener(new OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                savedDate.set(year, month, dayOfMonth);
                today.setText(formatDate(savedDate.getTime()));
                eventsForDay = ea.getAllEventsByDoctorForDay(IdCollection.doctor_id, NewEvent.parseDateForDB(savedDate));
                popNewDataToAdapter(eventsForDay);

            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                if(NewEvent.removeTime(savedDate).before(NewEvent.removeTime(c))) return;
                goToNewEvent(savedDate);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                Event itemValue = (Event) listView.getItemAtPosition(position);
                showEvent(itemValue.getEvent_id());
            }
        });

        // set data
        savedDate = Calendar.getInstance();
        today.setText((formatDate(savedDate.getTime())));
        eventsForDay =  ea.getAllEventsByDoctorForDay(IdCollection.doctor_id,NewEvent.parseDateForDB(savedDate));
        listView = (ListView) findViewById(R.id.calendar_listView_Events);
        popNewDataToAdapter(eventsForDay);

        // check intent
        Intent intent = getIntent();
        Calendar temp = ((Calendar)getIntent().getSerializableExtra("startDate"));
        boolean del = intent.getBooleanExtra("deleted", false);
        boolean updating = intent.getBooleanExtra("updatedEvent", false);
        String s = intent.getStringExtra("eventName");
        if(temp != null){
            calender.setDate(temp.getTime().getTime());
            eventsForDay = ea.getAllEventsByDoctorForDay(IdCollection.doctor_id,NewEvent.parseDateForDB(temp));
            popNewDataToAdapter(eventsForDay);
            if(updating)Toast.makeText(Calender.this, "Event updated", Toast.LENGTH_SHORT).show();
            else Toast.makeText(Calender.this, "Event created", Toast.LENGTH_SHORT).show();
        }
        if(del)
            Toast.makeText(Calender.this, "Event "+ s +" deleted", Toast.LENGTH_SHORT).show();

    }

    // assign new events to adapter
    private void popNewDataToAdapter(List<Event> events){
        adapter = new EventAdap(this, events);
        this.listView.setAdapter(adapter);
    }

    // go to a new event with date
    private void goToNewEvent(Calendar c){
        Intent showEvent = new Intent(this, NewEvent.class);
        showEvent.putExtra("date", c);
        startActivity(showEvent);
    }

    // show event in new intent
    private void showEvent(int id){
        Intent showEvent = new Intent(this, ShowEvent.class);
        showEvent.putExtra("event_id", id);
        startActivity(showEvent);
    }

    private void goToday(){
        Calendar todayDate = Calendar.getInstance();
        calender.setDate(todayDate.getTime().getTime());
        eventsForDay =  ea.getAllEventsByDoctorForDay(5,NewEvent.parseDateForDB(todayDate));

        popNewDataToAdapter(eventsForDay);
    }

    // returns date in right format
    public String formatDate(Date d){
        return df.format(d);
    }
    // returns date in right format
    public static String formatDateGlobal(Date d){
        return df_global.format(d);
    }
    // returns time in right format
    public static String formatTimeGlobal(Date d) { return df_globalTime.format(d); }

    // adding the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calendar_activity_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // adding Actionlistener to the menu entry
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_today:
                goToday();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        return super.onNavigationItemSelected(item);
    }

    // custom event adapter class for listView
    public static class EventAdap extends ArrayAdapter<Event>{

        public EventAdap(Context context, List<Event> events) {
            super(context, 0, events);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // create Event
            Event e = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
            }
            // Lookup view for data population
            TextView time = (TextView) convertView.findViewById(R.id.item_event_time);
            TextView name = (TextView) convertView.findViewById(R.id.item_event_name);
            // Populate data
            if(e.getFromTime() == -1)
                time.setText("Whole day");
            else
                time.setText(Calender.formatTimeGlobal(NewEvent.extractTime(e.getFromTime()).getTime()));
            name.setText(e.getEventname());
            return convertView;
        }
    }
}

