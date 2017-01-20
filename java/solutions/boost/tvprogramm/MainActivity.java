package solutions.boost.tvprogramm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import solutions.boost.channelstruct.Channel;
import solutions.boost.database.DataBaseHelper;
import solutions.boost.dataparser.JSONParser;
import solutions.boost.internetservice.DownloadsManager;
import solutions.boost.internetservice.ServerService;


public class MainActivity extends AppCompatActivity
{
    //sql database creator
    private DataBaseHelper dataBase;

    //init views
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    //tabs adapter
    private PagerAdapter mTabPagerAdapter;

    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                Toast.makeText(getApplicationContext(), "Load is complete", Toast.LENGTH_LONG ).show();
                setTabsViewPager();
            }
        };

        //create instance of database here
        dataBase = DataBaseHelper.getInstance(getApplicationContext());
        if (dataBase.getChannelsCount() == 0)
        {
            //check if internet available
            if(DownloadsManager.isOnline(getApplicationContext()))
            {
                //start service to download
                Intent iService = new Intent(this.getApplicationContext(), ServerService.class);
                startService(iService);
            }
            else
            {
                showNoInternetDialog();
            }
        }

        //tablayout and viewpager
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_mainactivity);
    }

    //retrieve data from database and display it into listview
    //I do it in onStart, because it guarantees it always shows updated database
    //after return from stop
    //register Service and BroadcastReceiver
    protected void onStart()
    {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ServerService.SERVICE_FILTER);
        registerReceiver(mReceiver, filter);

        if (dataBase.getChannelsCount() == 0)
        {
            setTabsViewPager();
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();

    }

    //save all data
    //unregister broadcastreceiver and stop service
    protected void onStop()
    {
        super.onStop();
        unregisterReceiver(mReceiver);
    }


    //set tabs and viewPager
    private void setTabsViewPager()
    {
        //set tabs and viewpager only in there is data into data base
        //if(dataBase != null && dataBase.getChannelsCount() > 0)
        //
            //init tabFragmentPageAdapter
             mTabPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), getApplicationContext());

             //set Adapter to view pager
             mViewPager.setAdapter(mTabPagerAdapter);

             //set tablayout with viewpager
             mTabLayout.setupWithViewPager(mViewPager);

             // adding functionality to tab and viewpager to manage each other when a page is changed or when a tab is selected
             mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        //
    }

    //show alert dialog about no internet connection
    private void showNoInternetDialog()
    {
        //Instantiate an AlertDialog.Builder with its constructor
        //new ContextThemeWrapper(this, R.style.myDialog - solution from StackOverflow
        //it's because my activity style has Theme.AppCompat.Light.NoActionBar
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title);

        // Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_mainactivity, menu);
        return true;
    }

    //it should sort a list of values inside fragment
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
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
