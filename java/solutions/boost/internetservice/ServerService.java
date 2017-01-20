package solutions.boost.internetservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.android.volley.VolleyError;

import org.json.JSONArray;

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

        //broadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //here load data from server in background task
        //and save data into data base
        //must populate three data table
        //1-st set channel table in database
        String url1 = DownloadsManager.CHANNELS_URL;
        String url2 = DownloadsManager.CATEGORIES_URL;
        String url3 = DownloadsManager.PROGRAMM_URL + System.currentTimeMillis();
        setChannelTable(url1, url2, url3);

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

    public void setChannelTable(String URL1, String URL2, String URL3)
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
                    JSONParser.parseJSONtoChannelTable(result, dataBase);
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
                    @Override
                    public void onSuccess(JSONArray result)
                    {
                        JSONParser.parseJSONtoCategoriesTable(result, dataBase);
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

        mLoadManager.getVolleyResponse(URL3,
                new DownloadsManager.VolleyCallback()
                {
                    //iterate through result and get JSON array
                    //parse JSON array to SQL database
                    @Override
                    public void onSuccess(JSONArray result)
                    {
                        JSONParser.parseJSONtoProgramsTable(result, dataBase);
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
