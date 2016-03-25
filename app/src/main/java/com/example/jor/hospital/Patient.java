package com.example.jor.hospital;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

public class Patient extends Navigation {

    String [] patients = {"14.03.2016 - Broken leg","11.09.2015 - Concussion", "01.01.2015 - Poisoning", "15.02.1999 - Broken arm", "18.01.1992 - Birth"};
    ListView lvD;
    ArrayAdapter<String> adapter;



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

        lvD = (ListView) findViewById(R.id.listViewDossier);



        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, patients);
        lvD.setAdapter(adapter);


        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Patient");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Patient");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Dossier");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Dossier");
        host.addTab(spec);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return super.onNavigationItemSelected(item);
    }

}
