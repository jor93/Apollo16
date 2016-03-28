package com.example.jor.hospital;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CalendarView.OnDateChangeListener;

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

    // Represents the date in the format name of day, day. month
    private DateFormat df = new SimpleDateFormat("EEEE, dd. MMMM");
    private static DateFormat df_global = new SimpleDateFormat("EEEE, dd. MMMM yyyy");

    // listview object
    private ListView listView;

    private List<Event> eventsForDay;

    //dummy data for events
    String[] values = new String[] {
            "event01",
            "event02",
            "event03",
            "event04",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        today = (TextView) findViewById(R.id.calendar_textView_today);

        calender = (CalendarView) findViewById(R.id.calenderView);
        calender.setOnDateChangeListener(new OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                savedDate = Calendar.getInstance();
                savedDate.set(year, month, dayOfMonth);
                today.setText(formatDate(savedDate.getTime()));
            }
        });


        Calendar c = Calendar.getInstance();
        if(savedDate == null) today.setText(formatDate(c.getTime()));
        else today.setText((formatDate(savedDate.getTime())));

        listView = (ListView) findViewById(R.id.calendar_listView_Events);


        // Defined Array values to show in ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        // Assign adapter to ListView
        listView.setAdapter(adapter);
        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item index
                int itemPosition = position;
                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);
                // TODO Open new intent with data here!!!

            }
        });
    }

    private void showEvent(int id){
        Intent showEvent = new Intent(this, ShowEvent.class);
        showEvent.putExtra("event_id", id);
        startActivity(showEvent);
    }

    // adding the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calendar_activity_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // adding actionlistener to the menu entry
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save:
                goToday();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToday(){
        Calendar todayDate = Calendar.getInstance();
        calender.setDate(todayDate.getTime().getTime());
    }

    // returns date in right format
    public String formatDate(Date d){
        return df.format(d);
    }

    // returns date in right format
    public static String formatDateGlobal(Date d){
        return df_global.format(d);
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

}

