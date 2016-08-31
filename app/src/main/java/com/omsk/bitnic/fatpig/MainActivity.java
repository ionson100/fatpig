package com.omsk.bitnic.fatpig;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;


import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.settings.ion.mylibrary.Reanimator;
import com.settings.ion.mylibrary.colorpicker.*;
import com.settings.ion.mylibrary.iListenerСhanges;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Model.OneEat;
import orm.Configure;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer ;
    public static List<Integer> LISTTRAFIC=new ArrayList<>();

    public static final String PARAM_LATITUDE = "latitude";
    public static final String PARAM_LONGITUDE ="longitude" ;

    private static final int MENU_UPDATE_PECENT = 1;
    private static final int MENU_DELETE_UPDATE_LAST_EAT = 2;
    public static final String BROADCAST_ACTION = "sasdjkdjasdjdikjausdu";
    public static final String PARAM_DATE = "asjkdj";
    Settings mSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        mSettings=Settings.getSettings();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        //         this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //  drawer.setDrawerListener(toggle);
        // toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ///////////////////////////////////////////////////////////////
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ImageButton buttonMenu= (ImageButton) findViewById(R.id.menu);
        if (buttonMenu != null) {
            buttonMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    }else{
                       drawer.openDrawer(GravityCompat.START);
                    }
                }
            });
        }
        ///////////////////////////////////////////////////////////////////////
        FactoryFragment.Action(this);

        registerForContextMenu(findViewById(R.id.panel2));
        registerForContextMenu(findViewById(R.id.panel3));
        //////////////////////
        final Activity activity=this;
        Reanimator.onSetListenerСhanges(new iListenerСhanges() {
            @Override
            public void OnCallListen(Class aClass, String fieldName, Object oldValue, Object newValue) {
                if (aClass == Settings.class) {
                    FillData.fill(activity);
                }
            }
        });

        startService(new Intent(this, MyServiceWach.class));



    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if(LISTTRAFIC.size()==0){
                if(mSettings.getSateSystem()!=StateSystem.HOME){
                    mSettings.setStateSystem(StateSystem.HOME,this);
                }else{
                    //super.onBackPressed();
                    finish();
                }

            }else{
                LISTTRAFIC.remove(LISTTRAFIC.size()-1);
                if(LISTTRAFIC.size()==0){
                    mSettings.setStateSystem(StateSystem.HOME,this);
                }else{
                    mSettings.setStateSystem(LISTTRAFIC.get(LISTTRAFIC.size()-1),this);
                    LISTTRAFIC.remove(LISTTRAFIC.size()-1);
                }
            }

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

    @Override
    protected void onResume() {
        super.onResume();
        if(TrackSettings.getCore().statusTrack.equals("1")){
            if(!Utils.isMyServiceRunning(MyServiceGeo.class,this)){
                startService(new Intent(this, MyServiceGeo.class));
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user) {
            Settings.getSettings().setStateSystem(StateSystem.USER_SETTINGS,this);
        }  else if (id == R.id.nav_home) {
            Settings.getSettings().setStateSystem(StateSystem.HOME,this);
        } else if (id == R.id.nav_product) {
            Settings.getSettings().setStateSystem(StateSystem.PRODUCT,this);
        } else if (id == R.id.nav_work) {
            Settings.getSettings().setStateSystem(StateSystem.WORK,this);
        } else if (id == R.id.nav_map) {
            Settings.getSettings().setStateSystem(StateSystem.MAP,this);
        }else if (id == R.id.nav_treack) {
            Settings.getSettings().setStateSystem(StateSystem.TRACK,this);
        } else if (id == R.id.nav_treack_show) {
            Settings.getSettings().setStateSystem(StateSystem.TRACK_SHOW,this);
        }
        else if (id == R.id.nav_settings_core) {
            Settings.getSettings().setStateSystem(StateSystem.SETTINGS,this);
        }else if (id == R.id.nav_life) {
            Settings.getSettings().setStateSystem(StateSystem.LIFE,this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        switch (v.getId()) {
            case R.id.panel2:
                menu.add(0, MENU_UPDATE_PECENT, 0, R.string.menu1);
                break;
            case R.id.panel3:
                menu.add(0,MENU_DELETE_UPDATE_LAST_EAT, 0, R.string.menu2);
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_UPDATE_PECENT:{
                Settings.getSettings().setStateSystem(StateSystem.SETTINGS,this);
                break;
            }
            case MENU_DELETE_UPDATE_LAST_EAT:{
                List<OneEat> oneEatList= Configure.getSession().getList(OneEat.class, null);
                Collections.sort(oneEatList, new Comparator<OneEat>() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public int compare(OneEat lhs, OneEat rhs) {
                        return Integer.compare(lhs.id, rhs.id);
                    }
                });
                if(oneEatList.size()!= 0){
                    OneEat last=oneEatList.get(oneEatList.size()-1);
                    Configure.getSession().delete(last);
                    FillData.fill(this);
                }

                break;
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Transceiver.send(FTrack.CHRONO,null);
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
    }
}
