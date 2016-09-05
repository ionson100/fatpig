package com.omsk.bitnic.fatpig;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.settings.ion.mylibrary.Reanimator;
import com.settings.ion.mylibrary.iListenerСhanges;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Model.OneEat;
import orm.Configure;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawer;

    private static final int MENU_UPDATE_PECENT = 1;

    private static final int MENU_DELETE_UPDATE_LAST_EAT = 2;

    public static final String BROADCAST_ACTION = "sasdjkdjasdjdikjausdu";

    public static List<Integer> LISTTRAFIC = new ArrayList<>();


    public static final String PARAM_LATITUDE = "latitude";
    public static final String PARAM_LONGITUDE = "longitude";
    public static final String PARAM_DATE = "asjkdj";
    public static final String PARAM_SPEED = "asjfgkdj";
    public static final String PARAM_ALTITUDE = "asj232fgkdj";


    private FloatingActionButton fab;
    Settings mSettings;


    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        mSettings = Settings.getSettings();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        ////////////////////////////////////////////////////////////////////
        findViewById(R.id.image1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.getSettings().setStateSystem(StateSystem.USER_SETTINGS, MainActivity.this);
            }
        });

        findViewById(R.id.image2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.getSettings().setStateSystem(StateSystem.CALCULATOR, MainActivity.this);
            }
        });

        findViewById(R.id.image3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.getSettings().setStateSystem(StateSystem.PRODUCT, MainActivity.this);
            }
        });

        findViewById(R.id.image4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.getSettings().setStateSystem(StateSystem.TRACK, MainActivity.this);
            }
        });


        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ///////////////////////////////////////////////////////////////
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ImageButton buttonMenu = (ImageButton) findViewById(R.id.menu);
        if (buttonMenu != null) {
            buttonMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
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
        final Activity activity = this;
        Reanimator.onSetListenerСhanges(new iListenerСhanges() {
            @Override
            public void OnCallListen(Class aClass, String fieldName, Object oldValue, Object newValue) {
                if (aClass == Settings.class) {
                    FillData.fill(activity);
                }
            }
        });

        startService(new Intent(this, MyServiceWach.class));


        showHelp();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void showHelp() {
        if (Settings.getSettings().isShowHelp) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (LISTTRAFIC.size() == 0) {
                if (mSettings.getSateSystem() != StateSystem.HOME) {
                    mSettings.setStateSystem(StateSystem.HOME, this);
                } else {
                    //super.onBackPressed();
                    finish();
                }

            } else {
                LISTTRAFIC.remove(LISTTRAFIC.size() - 1);
                if (LISTTRAFIC.size() == 0) {
                    mSettings.setStateSystem(StateSystem.HOME, this);
                } else {
                    mSettings.setStateSystem(LISTTRAFIC.get(LISTTRAFIC.size() - 1), this);
                    LISTTRAFIC.remove(LISTTRAFIC.size() - 1);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TrackSettings.getCore().statusTrack.equals("1")) {
            if (!Utils.isMyServiceRunning(MyServiceGeo.class, this)) {
                startService(new Intent(this, MyServiceGeo.class));
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.nav_user: {
                Settings.getSettings().setStateSystem(StateSystem.USER_SETTINGS, this);
                break;
            }
            case R.id.nav_product: {
                Settings.getSettings().setStateSystem(StateSystem.PRODUCT, this);
                break;
            }
            case R.id.nav_home: {
                Settings.getSettings().setStateSystem(StateSystem.HOME, this);
                break;
            }
            case R.id.nav_work: {
                Settings.getSettings().setStateSystem(StateSystem.WORK, this);
                break;
            }
            case R.id.nav_map: {
                Settings.getSettings().setStateSystem(StateSystem.MAP, this);
                break;
            }
            case R.id.nav_treack: {
                Settings.getSettings().setStateSystem(StateSystem.TRACK, this);
                break;
            }
            case R.id.nav_treack_show: {
                Settings.getSettings().setStateSystem(StateSystem.TRACK_SHOW, this);
                break;
            }
            case R.id.nav_settings_core: {
                Settings.getSettings().setStateSystem(StateSystem.SETTINGS, this);
                break;
            }
            case R.id.nav_life: {
                Settings.getSettings().setStateSystem(StateSystem.LIFE, this);
                break;
            }
            case R.id.nav_settings_button_eat: {
                Settings.getSettings().setStateSystem(StateSystem.BUTTON_EAT, this);
                break;
            }
            case R.id.nav_settings_button_work: {
                Settings.getSettings().setStateSystem(StateSystem.BUTTON_WORK, this);
                break;
            }
            case R.id.nav_calculator: {
                Settings.getSettings().setStateSystem(StateSystem.CALCULATOR, this);
                break;
            }
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
                menu.add(0, MENU_DELETE_UPDATE_LAST_EAT, 0, R.string.menu2);
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_UPDATE_PECENT: {
                Settings.getSettings().setStateSystem(StateSystem.SETTINGS, this);
                break;
            }
            case MENU_DELETE_UPDATE_LAST_EAT: {
                List<OneEat> oneEatList = Configure.getSession().getList(OneEat.class, null);
                Collections.sort(oneEatList, new Comparator<OneEat>() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public int compare(OneEat lhs, OneEat rhs) {
                        return Integer.compare(lhs.id, rhs.id);
                    }
                });
                if (oneEatList.size() != 0) {
                    OneEat last = oneEatList.get(oneEatList.size() - 1);
                    Configure.getSession().delete(last);
                    FillData.fill(this);
                }

                break;
            }
        }
        return super.onContextItemSelected(item);
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
