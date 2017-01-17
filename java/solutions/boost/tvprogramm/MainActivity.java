package solutions.boost.tvprogramm;

import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;

import solutions.boost.channelstruct.Channel;
import solutions.boost.database.DataBaseHelper;
import solutions.boost.dataparser.JSONParser;


public class MainActivity extends AppCompatActivity
{
    //loader manager to access server
    private DownloadsManager mLoadManager;
    //boolean to check if loader finished
    private boolean isLoaded = false;
    private boolean tabsAndPagerIsSet = false;

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

        //if it was changed to true -> make it false
        tabsAndPagerIsSet = false;

        //create instance of database here
        dataBase = DataBaseHelper.getInstance(getApplicationContext());
        /**
         * if this database is empty
         * run DownloadManager and populate database
         */
        if (dataBase.getChannelsCount() == 0)
        {
            //check if internet available
            if(DownloadsManager.isOnline(getApplicationContext()))
            {
                mLoadManager = new DownloadsManager(getApplicationContext());
                setDataBseAndView(DownloadsManager.CHANNELS_URL);
            }
            else
            {
                //Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_LONG).show();
                showNoInternetDialog();
            }
        }

        //init toolbar for to display in all android versions
        toolbar = (Toolbar) findViewById(R.id.toolbar_mainactivity);
        setSupportActionBar(toolbar);

        //tablayout and viewpager
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_mainactivity);

        //initialize tabs and viewPager after database in created
        //but before it setted into LoadManager
        //but if loadManager is not loaded, leave this work for loadManager
        if(isLoaded && !tabsAndPagerIsSet)
        {
            setTabsViewPager();
        }
        //setTabsViewPager();
    }

    //retrieve data from database and display it into listview
    //I do it in onStart, because it guarantees it always shows updated database
    //after return from stop
    //register Service and BroadcastReceiver
    protected void onStart()
    {
        super.onStart();
    }


    //save all data
    //unregister broadcastreceiver and stop service
    protected void onStop()
    {
        super.onStop();
    }


    private void setDataBseAndView(String URL)
    {
        //implement callback to get data from Volley response
        mLoadManager.getVolleyResponse(URL,
                new DownloadsManager.VolleyCallback()
                {
                    //iterate through result and get JSON array
                    //parse JSON array to List<channel>
                    @Override
                    public void onSuccess(JSONArray result)
                    {
                        JSONParser.parseJSONtoDatabase(result, dataBase);
                        isLoaded = true;

                        //try to populate tabs and pager if its are already initialised
                        //but if database created too fast, if it's possible
                        //there is nothing to populate
                        try
                        {
                            setTabsViewPager();
                        } catch (Exception e)
                        {
                            //it can give nullpointer exception
                        }
                    }

                    @Override
                    public void onFailure(VolleyError error)
                    {
                        dataBase.addChannelToTable(new Channel(0, "NO DATA", null, null, 0));
                        setTabsViewPager();
                    }
                });
    }

    //set tabs and viewPager
    private void setTabsViewPager()
    {
        tabsAndPagerIsSet = true;

        mTabLayout.addTab(mTabLayout.newTab().setText("Tab 1"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Tab 2"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Tab 3"));
        mTabPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), 3);


        mViewPager.setAdapter(mTabPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
