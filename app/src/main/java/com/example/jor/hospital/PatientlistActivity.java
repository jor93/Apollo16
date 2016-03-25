package com.example.jor.hospital;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.Arrays;

public class PatientlistActivity extends Navigation {

    String [] patients = {"Robert Johner","Sandro Mathier", "Dennis Kraft", "Gerd Zurbriggen", "Mathias Cina", "Yan Florey", "Gregor Mounir", "Samuel Montani", "Patric Berclaz", "David Naselli", "David Baron", "Sebastian Cina", "Etienne Glenz", "Natal Montani", "Fabian Brunner"};
    ArrayAdapter<String> adapter;
    ListView lv;
    SearchView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientlist);


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
        sv = (SearchView) findViewById(R.id.searchView);

        Arrays.sort(patients);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, patients);

        lv.setAdapter(adapter);


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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Button to add new patient", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




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



}
