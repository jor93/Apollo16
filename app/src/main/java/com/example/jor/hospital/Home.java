package com.example.jor.hospital;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.jor.hospital.db.adapter.DoctorAdapter;
import com.example.jor.hospital.db.objects.Doctor;

public class Home extends AppCompatActivity {

    private Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        DoctorAdapter da = new DoctorAdapter(this);
        int i = intent.getIntExtra("doctor_id",-1);
        if(i != -1)
            doctor = da.getDoctorById(i);
        else
            doctor = null;




        // Hallo Robi du blöde Fotze
        // chasch das läsu?
        // Vertrüüsch mer das wirkli ah?

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

    private void goToLogin(){
        if(doctor != null) doctor = null;
        Intent login = new Intent(this, Login.class);
        startActivity(login);
    }

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
}
