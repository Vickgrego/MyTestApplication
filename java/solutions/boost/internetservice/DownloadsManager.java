package solutions.boost.internetservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

/**
 * downloads data from servers
 * using Google Volley lib https://github.com/mcxiaoke/android-volley/
 * http://52.50.138.211:8080/ChanelAPI/chanels - channel list
 * http://52.50.138.211:8080/ChanelAPI/categories - category list
 * http://52.50.138.211:8080/ChanelAPI/programs/<timestamp> - list of programs per day
 */
public class DownloadsManager
{
    public final static String CHANNELS_URL = "http://52.50.138.211:8080/ChanelAPI/chanels";
    public final static String CATEGORIES_URL = "http://52.50.138.211:8080/ChanelAPI/categories";
    public final static String PROGRAMM_URL = "http://52.50.138.211:8080/ChanelAPI/programs/"; //+ timestamp System.currentTimeMillis()

    private RequestQueue queue;
    private Context ctx;

    //singleton to ensure we run just one DownloadManager

     private static DownloadsManager mManager;
     private DownloadsManager(Context ctx)
     {
         this.ctx = ctx;
         //initiate request queue
         queue  = Volley.newRequestQueue(ctx);
     }
     public static DownloadsManager getInstance(Context ctx)
     {
         if (mManager == null)
         {
            mManager = new DownloadsManager(ctx.getApplicationContext());
         }
         return mManager;
     }

    //we need interface callback to get data from getVolleyResponseData
    //when it finished
    public interface VolleyCallback
    {
        void onSuccess(JSONArray result);
        void onFailure(VolleyError error);
    }

    //method to get data from server by provided url
    //using Volley lib
    //@param String
    public final void getVolleyResponse(String url, final VolleyCallback callback)
    {
        //request to GET data
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                       callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener()
                {
                @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        callback.onFailure(error);
                    }
                });
        queue.add(request);
    }

    //check if connection available
    public final static boolean isOnline(Context ctx)
    {
        ConnectivityManager CM = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = CM.getActiveNetworkInfo();

        if (info !=null && info.isConnectedOrConnecting())
            return true;
        else
            return false;
    }

}
