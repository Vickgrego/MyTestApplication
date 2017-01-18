package solutions.boost.tvprogramm;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.VolleyError;

import org.json.JSONArray;

import solutions.boost.database.DataBaseHelper;
import solutions.boost.dataparser.JSONParser;
import solutions.boost.internetservice.DownloadsManager;
import solutions.boost.internetservice.ServerService;


public class MainActivity extends AppCompatActivity
{
    //sql database creator
    private DataBaseHelper dataBase;

    //init views
    private Toolbar toolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    //tabs adapter
    private PagerAdapter mTabPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create instance of database here
        dataBase = DataBaseHelper.getInstance(getApplicationContext());

        //init toolbar for to display in all android versions
        toolbar = (Toolbar) findViewById(R.id.toolbar_mainactivity);
        setSupportActionBar(toolbar);

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
        /**
         * if this database is empty
         * run DownloadManager and populate database
         */
        if (dataBase.getChannelsCount() == 0)
        {
            //check if internet available
            if(DownloadsManager.isOnline(getApplicationContext()))
            {
                //start service to download
                Intent intenService = new Intent(this.getApplicationContext(), ServerService.class);
                startService(intenService);
            }
            else
            {
                showNoInternetDialog();
            }
        }
        else
        {
            setTabsViewPager();
        }
    }


    //save all data
    //unregister broadcastreceiver and stop service
    protected void onStop()
    {
        super.onStop();
    }


    //set tabs and viewPager
    private void setTabsViewPager()
    {

        mTabLayout.addTab(mTabLayout.newTab().setText("Tab 1"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Tab 2"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Tab 3"));

        //set tabs and viewpager only in there is data into data base
        if(dataBase.getChannelsCount() > 0)
        {
            mTabPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), 3);

            mViewPager.setAdapter(mTabPagerAdapter);
            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
            mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
            {
                @Override
                public void onTabSelected(TabLayout.Tab tab)
                {
                    mViewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab)
                {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab)
                {

                }
            });
        }
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
            case R.id.default_sort:
            {
                if (TabFragment.showSorted != false)
                {
                    TabFragment.showSorted = false;
                }
                return true;
            }
            case R.id.abc_sort:
            {
                if (TabFragment.showSorted != true)
                {
                    TabFragment.showSorted = true;
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
