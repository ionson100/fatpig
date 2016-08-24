package com.omsk.bitnic.fatpig;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Script;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.ContextMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.settings.ion.mylibrary.Reanimator;
import com.settings.ion.mylibrary.iListenerСhanges;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Model.OneEat;
import orm.Column;
import orm.Configure;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MENU_UPDATE_PECENT = 1;
    private static final int MENU_DELETE_UPDATE_LAST_EAT = 2;
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       // ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
       //         this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
      //  drawer.setDrawerListener(toggle);
       // toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ///////////////////////////////////////////////////////////////
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



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(
                    mSettings.getSateSystem()==StateSystem.WORK||
                            mSettings.getSateSystem()==StateSystem.MAP||
                    mSettings.getSateSystem()==StateSystem.SETTINGS||
                    mSettings.getSateSystem()==StateSystem.PRODUCT||
                    mSettings.getSateSystem()==StateSystem.USER_SETTINGS
                    ){
                    mSettings.setStateSystem(StateSystem.HOME,this
                );
            }else {
                super.onBackPressed();
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
        }
        else if (id == R.id.nav_settings_core) {
            Settings.getSettings().setStateSystem(StateSystem.SETTINGS,this);
        }else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        switch (v.getId()) {
            case R.id.panel2:
                menu.add(0, MENU_UPDATE_PECENT, 0, "Изменить процент уменьшения калорий");
                break;
            case R.id.panel3:
                menu.add(0,MENU_DELETE_UPDATE_LAST_EAT, 0, "Отрыгнуть последний кусок жратвы");
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
                OneEat last=oneEatList.get(oneEatList.size()-1);
                Configure.getSession().delete(last);
                FillData.fill(this);
                break;
            }
        }
        return super.onContextItemSelected(item);
    }


}
