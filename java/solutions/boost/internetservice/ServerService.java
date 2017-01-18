package solutions.boost.internetservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

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

    @Override
    public void onCreate()
    {
        super.onCreate();

        //create load manager and database
        ctx = getApplicationContext();
        mLoadManager = new DownloadsManager(ctx);
        dataBase = DataBaseHelper.getInstance(ctx);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //here load data from server in background task
        //and save data into data base
        //must populate three data table
        //1-st set channel table in database
        setChannelTable(DownloadsManager.CHANNELS_URL);

        return super.onStartCommand(intent, flags, startId);
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

    public void setChannelTable(String URL)
    {
            //implement callback to get data from Volley response
            mLoadManager.getVolleyResponse(URL,
                    new DownloadsManager.VolleyCallback()
                    {
                        //iterate through result and get JSON array
                        //parse JSON array to SQL database
                        @Override
                        public void onSuccess(JSONArray result)
                        {
                            JSONParser.parseJSONtoChannelTable(result, dataBase);
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
