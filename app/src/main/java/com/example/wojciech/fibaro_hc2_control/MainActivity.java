package com.example.wojciech.fibaro_hc2_control;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.wojciech.fibaro_hc2_control.fragments.DevicesFragment;
import com.example.wojciech.fibaro_hc2_control.fragments.HC2InfoFragment;
import com.example.wojciech.fibaro_hc2_control.fragments.RoomsFragment;
import com.example.wojciech.fibaro_hc2_control.fragments.SectionsFragment;
import com.example.wojciech.fibaro_hc2_control.model.Device;
import com.example.wojciech.fibaro_hc2_control.model.HC2;
import com.example.wojciech.fibaro_hc2_control.model.Room;
import com.example.wojciech.fibaro_hc2_control.model.Section;
import com.example.wojciech.fibaro_hc2_control.service.ServiceHC2;
import com.example.wojciech.fibaro_hc2_control.utils.ConnectivityUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements SectionsFragment.OnSectionsFragmentInteractionListener,
        RoomsFragment.OnRoomsFragmentInteractionListener,
        DevicesFragment.OnDevicesFragmentInteractionListener {
    private String TAG = MainActivity.class.getSimpleName();


    private CharSequence mTitle;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //doesnt need to be used anymore
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Setup Navigation drawer

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawerLayout.setDrawerListener(drawerToggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }


        if (savedInstanceState == null) {
            if (navigationView != null)
                navigationView.getMenu().performIdentifierAction(R.id.nav_home, 0);
        } else {
            //cvList = savedInstanceState.getParcelableArrayList(KEY_CV);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        scheduleAlarm();
        IntentFilter filter = new IntentFilter(ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        cancelAlarm();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {

        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

    }

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        Fragment fragment = null;
                        String tag;
                        switch (menuItem.getItemId()) {

                            case R.id.nav_home:
                                if (ConnectivityUtil.isNetworkAvailable(MainActivity.this)) {
                                    tag = HC2InfoFragment.class.getSimpleName();
                                    fragment = getSupportFragmentManager().findFragmentByTag(tag);
                                    if (fragment == null)
                                        fragment = HC2InfoFragment.newInstance();
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.container, fragment, tag)
                                            .commit();

                                    Intent i = new Intent(MainActivity.this, ServiceHC2.class);
                                    i.putExtra(ServiceHC2.EXTRA_REQUESTED_ACTION, ServiceHC2.ServiceHC2Action.GetInfo.getValue());
                                    startService(i);
                                } else {
                                    Toast.makeText(MainActivity.this, "Please check the Internet connection.", Toast.LENGTH_SHORT).show();
                                }

                                break;
                            //Settings
                            case R.id.nav_sections:
                                if (ConnectivityUtil.isNetworkAvailable(MainActivity.this)) {
                                    tag = SectionsFragment.class.getSimpleName();
                                    fragment = getSupportFragmentManager().findFragmentByTag(tag);
                                    if (fragment == null)
                                        fragment = SectionsFragment.newInstance();
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.container, fragment, tag)
                                            .commit();

                                    Intent i = new Intent(MainActivity.this, ServiceHC2.class);
                                    i.putExtra(ServiceHC2.EXTRA_REQUESTED_ACTION, ServiceHC2.ServiceHC2Action.GetSections.getValue());
                                    startService(i);
                                } else {
                                    Toast.makeText(MainActivity.this, "Please check the Internet connection.", Toast.LENGTH_SHORT).show();
                                }
                                break;

                            case R.id.nav_rooms:
                                if (ConnectivityUtil.isNetworkAvailable(MainActivity.this)) {
                                    tag = RoomsFragment.class.getSimpleName();
                                    fragment = getSupportFragmentManager().findFragmentByTag(tag);
                                    if (fragment == null)
                                        fragment = RoomsFragment.newInstance();
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.container, fragment, tag)
                                            .commit();

                                    Intent i = new Intent(MainActivity.this, ServiceHC2.class);
                                    i.putExtra(ServiceHC2.EXTRA_REQUESTED_ACTION, ServiceHC2.ServiceHC2Action.GetRooms.getValue());
                                    startService(i);
                                } else {
                                    Toast.makeText(MainActivity.this, "Please check the Internet connection.", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.nav_devices:
                                if (ConnectivityUtil.isNetworkAvailable(MainActivity.this)) {
                                    tag = DevicesFragment.class.getSimpleName();
                                    fragment = getSupportFragmentManager().findFragmentByTag(tag);
                                    if (fragment == null)
                                        fragment = DevicesFragment.newInstance();
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.container, fragment, tag)
                                            .commit();

                                    Intent i = new Intent(MainActivity.this, ServiceHC2.class);
                                    i.putExtra(ServiceHC2.EXTRA_REQUESTED_ACTION, ServiceHC2.ServiceHC2Action.GetDevices.getValue());
                                    startService(i);
                                } else {
                                    Toast.makeText(MainActivity.this, "Please check the Internet connection.", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        drawerToggle.syncState();
                        return true;
                    }
                });
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.nav_hmc_info);
                break;
            case 2:
                mTitle = getString(R.string.nav_sections);
                break;
            case 3:
                mTitle = getString(R.string.nav_rooms);
                break;
            case 4:
                mTitle = getString(R.string.nav_devices);
                break;
            case 5:
                mTitle = getString(R.string.nav_hmc_info);
                break;
        }
        setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    public static final String ACTION = "com.example.wojciech.fibaro_hc2_control.broadcast";

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                int res = intent.getIntExtra(ServiceHC2.RESULT_CODE, RESULT_CANCELED);
                if (res == Activity.RESULT_OK) {
                    int action = intent.getIntExtra(ServiceHC2.RESULT_ACTION, 0);

                    switch (ServiceHC2.ServiceHC2Action.fromInteger(action)) {
                        case Update:
                            break;
                        case GetSections:
                            try {
                                String tag = SectionsFragment.class.getSimpleName();
                                SectionsFragment fragment = (SectionsFragment) getSupportFragmentManager().findFragmentByTag(tag);
                                if (fragment != null) {
                                    ArrayList<Section> arrayList = intent.getParcelableArrayListExtra(ServiceHC2.RESULT_PARCEL);
                                    if (arrayList != null)
                                        fragment.update(arrayList);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case GetSection:
                            try {
                                String tag = RoomsFragment.class.getSimpleName();
                                ArrayList<Room> arrayList = intent.getParcelableArrayListExtra(ServiceHC2.RESULT_PARCEL);

                                Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
                                if (fragment == null)
                                    fragment = RoomsFragment.newInstance(arrayList);
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.container, fragment)
                                        .addToBackStack(tag)
                                        .commit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case GetRooms:
                            try {
                                String tag = RoomsFragment.class.getSimpleName();
                                RoomsFragment fragment = (RoomsFragment) getSupportFragmentManager().findFragmentByTag(tag);
                                if (fragment != null) {
                                    ArrayList<Room> arrayList = intent.getParcelableArrayListExtra(ServiceHC2.RESULT_PARCEL);
                                    if (arrayList != null)
                                        fragment.update(arrayList);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case GetRoom:
                            try {
                                String tag = DevicesFragment.class.getSimpleName();
                                ArrayList<Device> arrayListR = intent.getParcelableArrayListExtra(ServiceHC2.RESULT_PARCEL);

                                Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
                                if (fragment == null)
                                    fragment = DevicesFragment.newInstance(arrayListR);

                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.container, fragment)
                                        .addToBackStack(tag)
                                        .commit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            break;
                        case GetDevices:
                            try {
                                String tag = DevicesFragment.class.getSimpleName();
                                DevicesFragment fragment = (DevicesFragment) getSupportFragmentManager().findFragmentByTag(tag);
                                if (fragment != null) {
                                    ArrayList<Device> arrayList = intent.getParcelableArrayListExtra(ServiceHC2.RESULT_PARCEL);
                                    if (arrayList != null) {
                                        fragment.update(arrayList);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case GetDevice:
                            break;
                        case CallActionSwitch:
                            break;
                        case CallActionDimm:
                            break;
                        case RefreshStates:
                            break;
                        case GetInfo:
                            try {
                                String tag = HC2InfoFragment.class.getSimpleName();
                                HC2InfoFragment fragment = (HC2InfoFragment) getSupportFragmentManager().findFragmentByTag(tag);
                                if (fragment != null) {
                                    HC2 hC2 = intent.getParcelableExtra(ServiceHC2.RESULT_PARCEL);
                                    if (hC2 != null)
                                        fragment.setUpData(hC2);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }


                } else {

                    Log.d(TAG, "GETTING");
                }
                return;
            }
        }
    };

    @Override
    public void onFragmentInteraction(Device device) {
        Toast.makeText(this, "DEVICE CLICKED: " + device.name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentInteraction(Room room) {
        if (ConnectivityUtil.isNetworkAvailable(this)) {
            Intent i = new Intent(this, ServiceHC2.class);
            Toast.makeText(this, "Loading devices...", Toast.LENGTH_SHORT).show();
            i.putExtra(ServiceHC2.EXTRA_REQUESTED_ACTION, ServiceHC2.ServiceHC2Action.GetRoom.getValue());
            i.putExtra(ServiceHC2.EXTRA_REQUESTED_OBJECT, room);
            startService(i);
        } else {
            Toast.makeText(this, "Please check the Internet connection.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onFragmentInteraction(Section section) {
        if (ConnectivityUtil.isNetworkAvailable(this)) {
            Toast.makeText(this, "Section CLICKED: " + section.name, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, ServiceHC2.class);
            Toast.makeText(this, "Loading rooms...", Toast.LENGTH_SHORT).show();
            i.putExtra(ServiceHC2.EXTRA_REQUESTED_ACTION, ServiceHC2.ServiceHC2Action.GetSection.getValue());
            i.putExtra(ServiceHC2.EXTRA_REQUESTED_OBJECT, section);
            startService(i);
        } else {
            Toast.makeText(this, "Please check the Internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    public static class UpdaterReceiver extends BroadcastReceiver {
        public static final int REQUEST_CODE = 12345;
        public static final String ACTION = "com.example.wojciech.fibaro_hc2_control.alarm";

        public UpdaterReceiver() {

        }

        // Triggered by the Alarm periodically (starts the service to run task)
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!cancel)
            if (ConnectivityUtil.isNetworkAvailable(context)) {
                Intent i = new Intent(context, ServiceHC2.class);
                Toast.makeText(context, "Update", Toast.LENGTH_SHORT).show();


                i.putExtra(ServiceHC2.EXTRA_REQUESTED_ACTION, ServiceHC2.ServiceHC2Action.Update.getValue());
                context.startService(i);
            } else {
                Toast.makeText(context, "Please check the Internet connection.", Toast.LENGTH_SHORT).show();
            }

        }

    }

    // Setup a recurring alarm every half hour
    public void scheduleAlarm() {
        cancel=false;
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), UpdaterReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, UpdaterReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        int interval = 10 * 1000;
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                interval, pIntent);
    }

    public static boolean cancel = false;
    public void cancelAlarm() {
        Intent intent = new Intent(getApplicationContext(), BroadcastReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, UpdaterReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
        cancel=true;
    }
}
