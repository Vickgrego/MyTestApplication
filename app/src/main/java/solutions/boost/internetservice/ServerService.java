package solutions.boost.internetservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import solutions.boost.javaobject.Program;
import solutions.boost.database.DataBaseHelper;
import solutions.boost.dataparser.JSONParser;

/**
 * Created on 18.01.2017.
 * is responsible for load data from server
 * and store it into data base
 */
public class ServerService extends Service
{
    private DownloadsManager  mLoadManager;
    private DataBaseHelper dataBase;
    private Context ctx;

    private final static String TAG = "Service Running";

    public static final String SERVICE_FILTER = "com.boost.solution.internetservice.DONE";

    //private LocalBroadcastManager broadcastManager;

    @Override
    public void onCreate()
    {
        super.onCreate();

        //create load manager and database
        ctx = getApplicationContext();
        mLoadManager = DownloadsManager.getInstance(ctx);
        dataBase = DataBaseHelper.getInstance(ctx);

        Log.e(TAG, "onCreate Service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if(DownloadsManager.isOnline(getApplicationContext()))
        {
            //here load data from server in background task
            //and save data into data base
            //must populate three data table
            //1-st set channel table in database
            String url1 = DownloadsManager.CHANNELS_URL;
            String url2 = DownloadsManager.CATEGORIES_URL;

            setChannelTable(url1, url2); //start Volley thread to load data for Table of channels and categories

            setProgramsForWeekTable(); //start Volley thread to populate Table of Programs

            Log.e(TAG, "start loading Service");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void sendResult()
    {
        Intent intent = new Intent();
        intent.setAction(SERVICE_FILTER);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    private void setChannelTable(String URL1, String URL2)
    {
           //implement callback to get data from Volley response
            mLoadManager.getVolleyResponse(URL1,
            new DownloadsManager.VolleyCallback()
            {
                //iterate through result and get JSON array
                //parse JSON array to SQL database
                @Override
                public void onSuccess(JSONArray result)
                {
                    if(dataBase.getChannelsCount() == 0)
                        dataBase.addAllChannelsToTable(result);
                    else
                        dataBase.updateAllChannelsInTable(result);

                    sendResult();
                }

                @Override
                public void onFailure(VolleyError error)
                {
                    //dataBase.addChannelToTable(new Channel(0, "NO DATA", null, null, 0));
                    //handle error
                    //error.getMessage() == 404
                    //TODO
                }
            });

        mLoadManager.getVolleyResponse(URL2,
                new DownloadsManager.VolleyCallback()
                {
                    //iterate through result and get JSON array
                    //parse JSON array to SQL database
                    //create new if count is 0, else update
                    @Override
                    public void onSuccess(JSONArray result)
                    {
                        if(dataBase.getCategoriesCount() == 0)
                        {
                            dataBase.addAllCategoriesToTable(result);
                        }
                        else
                            dataBase.updateAllCategoriesInTable(result);
                    }

                    @Override
                    public void onFailure(VolleyError error)
                    {
                        //dataBase.addChannelToTable(new Channel(0, "NO DATA", null, null, 0));
                        //handle error
                        //error.getMessage() == 404
                        //TODO
                    }
                });

    }


    private void setProgramsForWeekTable()
    {
        //program's schedule for week
        long day = System.currentTimeMillis();
        long day2 = day + 1000 * 60 * 60 * 24; //seconds in day
        long day3 = day2 + 1000 * 60 * 60 * 24; //seconds in day
        long day4 = day3 + 1000 * 60 * 60 * 24; //seconds in day
        long day5 = day4 + 1000 * 60 * 60 * 24; //seconds in day
        long day6 = day5 + 1000 * 60 * 60 * 24; //seconds in day
        long day7 = day6 + 1000 * 60 * 60 * 24; //seconds in day

        String urlday = DownloadsManager.PROGRAMM_URL + day;
        String urlday2 = DownloadsManager.PROGRAMM_URL + day2;
        String urlday3 = DownloadsManager.PROGRAMM_URL + day3;
        String urlday4 = DownloadsManager.PROGRAMM_URL + day4;
        String urlday5 = DownloadsManager.PROGRAMM_URL + day5;
        String urlday6 = DownloadsManager.PROGRAMM_URL + day6;
        String urlday7 = DownloadsManager.PROGRAMM_URL + day7;

        String[] urls = {urlday, urlday2, urlday3, urlday4, urlday5, urlday6, urlday7};

        for (int i=0; i<urls.length; i++)
        {
            if(dataBase.getProgramsCount() == 0)
                setProgramsTable(urls[i]);
            else
                updateProgramsTable(urls[i]);
        }

    }

    private void setProgramsTable(String Url)
    {

        mLoadManager.getVolleyResponse(Url,
                new DownloadsManager.VolleyCallback()
                {
                    //iterate through result and get JSON array
                    //parse JSON array to SQL database
                    @Override
                    public void onSuccess(JSONArray result)
                    {
                        dataBase.addAllProgramsToTable(result);
                    }

                    @Override
                    public void onFailure(VolleyError error)
                    {
                        //dataBase.addChannelToTable(new Channel(0, "NO DATA", null, null, 0));
                        //handle error
                        //error.getMessage() == 404
                        //TODO
                    }
                });
    }

    private void updateProgramsTable(String Url)
    {

        mLoadManager.getVolleyResponse(Url,
                new DownloadsManager.VolleyCallback()
                {
                    //iterate through result and get JSON array
                    //parse JSON array to SQL database
                    @Override
                    public void onSuccess(JSONArray result)
                    {
                        dataBase.updateAllProgramsToTable(result);
                    }

                    @Override
                    public void onFailure(VolleyError error)
                    {
                        //dataBase.addChannelToTable(new Channel(0, "NO DATA", null, null, 0));
                        //handle error
                        //error.getMessage() == 404
                        //TODO
                    }
                });
    }
}
