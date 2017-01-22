package solutions.boost.tvprogramm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Calendar;

import solutions.boost.adaptersfragments.TabFragmentPagerAdapter;
import solutions.boost.database.DataBaseHelper;
import solutions.boost.internetservice.DownloadsManager;
import solutions.boost.internetservice.ServerService;


public class MainActivity extends AppCompatActivity
{
    //sql database creator
    private DataBaseHelper dataBase; //static instance of SQL data base

    //views
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    //tabs adapter
    private PagerAdapter mTabPagerAdapter;

    //layout for showing progressbar when loading data
    private FrameLayout loading;

    //receiver to receive a msg from service
    private BroadcastReceiver mReceiver; //- when ServerService broadcast msg - view should be refreshed
    //intent to start and stop ServerService
    private Intent iService;
    //alarm to start ServerService for to update database every few hours
    private AlarmManager mSynchroManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) //YES, I KNOW, IT'S QUIT A MESS !!!! ))))
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //just frame which is showing until tabs and viewer pager not shown
        loading = (FrameLayout) findViewById(R.id.loading_layout);
        loading.setVisibility(View.VISIBLE); //it's going to be invisible in method

        mReceiver = new BroadcastReceiver()
        {
            //load tabs and pagers is shown after service finish loading data
            @Override
            public void onReceive(Context context, Intent intent)
            {
                Toast.makeText(getApplicationContext(), "Load is complete", Toast.LENGTH_LONG ).show();
                setTabsViewPager();
            }
        };

        //set all views components, except tabs and viewer pages
        setBeautifulView();

        //I use AlarmManager to start ServerService several times per day for updating database
        startAlarmManager();
    }

    //I use this method according to
    // https://developer.android.com/reference/android/support/v4/app/ActionBarDrawerToggle.html
    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState)
    {
        super.onPostCreate(savedInstanceState, persistentState);
        //Call syncState() from your Activity's onPostCreate - google recommendation
        toggle.syncState();
    }

    //retrieve data from database and display it into listview
    //I do it in onStart, because it guarantees it always shows updated database
    //after return from onStop()
    //register BroadcastReceiver
    protected void onStart()
    {
        super.onStart();

        //if data base is empty (first run) start service
        //or show dialog to user if no internet connection
        if (dataBase.getChannelsCount() == 0)
        {
            //check if internet available
            if(DownloadsManager.isOnline(getApplicationContext()))
            {
                startIService();
            }
            else
            {
                showNoInternetDialog();
            }
        }
        else //else init tabs and viewer pager
        {
            setTabsViewPager();
        }

        //receiver is registered to wait for a msg from server
        IntentFilter filter = new IntentFilter(ServerService.SERVICE_FILTER);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    //save all data
    //unregister broadcastreceiver
    protected void onStop()
    {
        super.onStop();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        //when back pressed and navigation is open - close it
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_mainactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (toggle.onOptionsItemSelected(item)) //without it toggle button is not working
        {
            return true;
        }
        else
        {
            switch (item.getItemId())
            {
                case R.id.refresh:
                {
                    setTabsViewPager();
                    return true;
                }
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

    }

    //<!------------------------------------MY PRIVATE METHODS ----------------------------->
    private void setBeautifulView()
    {
        //create instance of database here
        dataBase = DataBaseHelper.getInstance(getApplicationContext());

        //-----------VIEWS----------
        //tablayout and viewpager
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_mainactivity);

        //icon for navigation view
        drawerLayout = (DrawerLayout) findViewById(R.id.Main_drawer_layout);
        //icon when pressed navigation view is open
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle); //listen when toggle is pressed

        //navigation menu
        navigationView = (NavigationView) findViewById(R.id.main_navigation_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.channel_list_menu:
                        TabLayout.Tab tab0 = mTabLayout.getTabAt(0);
                        tab0.select();
                        break;
                    case R.id.category_list_menu:
                        TabLayout.Tab tab1 = mTabLayout.getTabAt(1);
                        tab1.select();
                        break;
                    case R.id.preferred_list_menu:
                        TabLayout.Tab tab2 = mTabLayout.getTabAt(2);
                        tab2.select();
                        break;

                }

                //close navigation after item choosen
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            }
        });

        try
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            if(Build.VERSION.SDK_INT > 14)
                getSupportActionBar().setHomeButtonEnabled(true);

        } catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Your Android has no Action Bar", Toast.LENGTH_LONG).show();
        }
    }
    //set tabs and viewPager
    private void setTabsViewPager()
    {
        //it can be long operation
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                //init tabFragmentPageAdapter
                mTabPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), getApplicationContext());

                //set Adapter to view pager
                mViewPager.setAdapter(mTabPagerAdapter);

                //set tablayout with viewpager
                mTabLayout.setupWithViewPager(mViewPager);

                // adding functionality to tab and viewpager to manage each other
                // when a page is changed or when a tab is selected
                mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
                loading.setVisibility(View.INVISIBLE);


            }
        });

    }

    //method to start Service what is responsible for load data into sql database
    private void startIService()
    {
        iService = new Intent(this.getApplicationContext(), ServerService.class);
        startService(iService);
    }

    //method to send alarm through Alarm Service to start ServerService for auto loading new data
    private void startAlarmManager()
    {
        mSynchroManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(MainActivity.this, ServerService.class);
        PendingIntent mPendIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mSynchroManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime()+1000*100, AlarmManager.INTERVAL_HOUR, mPendIntent);
        Log.e("Alarm!", "start alarm");
    }

    //show alert dialog about no internet connection
    private void showNoInternetDialog()
    {
        //Instantiate an AlertDialog.Builder with its constructor
        //new ContextThemeWrapper(this, R.style.myDialog - solution from StackOverflow
        //it's because my activity style has different Theme
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));

        // Chain together various setter methods to set the dialog characteristics
        //if user click OK - point to wireless settings
        //if EXIT - exit app
        builder.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Intent i = new Intent(Settings.ACTION_SETTINGS);

                                if (i.resolveActivity(getPackageManager()) != null)
                                        startActivity(i);
                                finish();
                            }
                        })
                .setNegativeButton("EXIT", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        finish();
                    }
                })
                .setCancelable(false);


        // Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
