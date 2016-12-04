package com.example.victor.final_project_ee408;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.NumberPicker;

import API.SimulationManager;
import fragments.general_info;
import fragments.graph;
import fragments.run_multiple;
import fragments.run_once;
import fragments.sensors;
import fragments.simulation_setup;
import static android.R.attr.fragment;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Defining fragments
    private general_info general_info_fragment = new general_info();
    private graph graph_fragment = new graph();
    private run_multiple run_multiple_fragment = new run_multiple();
    private run_once run_once_fragment = new run_once();
    private sensors sensors_fragment = new sensors();
    private simulation_setup simulation_setup_fragment = new simulation_setup();

    private NumberPicker np;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Loads each fragment, in turn, initializing the data
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, run_multiple_fragment).commit();
        fragmentManager.beginTransaction().replace(R.id.flContent, run_once_fragment).commit();
        fragmentManager.beginTransaction().replace(R.id.flContent, sensors_fragment).commit();
        fragmentManager.beginTransaction().replace(R.id.flContent, simulation_setup_fragment).commit();
        general_info_fragment.passSimValues(simulation_setup_fragment);
        run_multiple_fragment.passSimValues(simulation_setup_fragment);
        graph_fragment.passRunMultiple(run_multiple_fragment);
        fragmentManager.beginTransaction().replace(R.id.flContent, graph_fragment).commit();
        fragmentManager.beginTransaction().replace(R.id.flContent, general_info_fragment).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.general_info) {
            fragment = general_info_fragment;
        } else if (id == R.id.sensors) {
            fragment = sensors_fragment;
        } else if (id == R.id.graph) {
            fragment = graph_fragment;
        } else if (id == R.id.run_once) {
            fragment = run_once_fragment;
            SimulationManager.getSimulationSetup().setObservation(simulation_setup_fragment.getObservationName());
            SimulationManager.getSimulationSetup().setSensorCount(simulation_setup_fragment.getNumberOfSensors());
            SimulationManager.getSimulationSetup().setTheta(simulation_setup_fragment.getThetaValue());
            SimulationManager.getSimulationSetup().setPower(simulation_setup_fragment.getPowerValue());
            SimulationManager.getSimulationSetup().setVarianceN(simulation_setup_fragment.getNVariance());
            SimulationManager.getSimulationSetup().setVarianceV(simulation_setup_fragment.getVVariance());
            SimulationManager.getSimulationSetup().setK(simulation_setup_fragment.getKValue());
            SimulationManager.getSimulationSetup().setRician(simulation_setup_fragment.ricianChannels());
            SimulationManager.getSimulationSetup().setUniform(simulation_setup_fragment.uniformAlphas());

            SimulationManager.runSimulation();
        } else if (id == R.id.run_multiple) {
            fragment = run_multiple_fragment;
        } else if (id == R.id.simulation_setup) {
            fragment = simulation_setup_fragment;
        }

        if(fragment !=run_once_fragment) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}
