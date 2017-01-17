package solutions.boost.tvprogramm;

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
    public final static String PROGRAMM_URL = "http://52.50.138.211:8080/ChanelAPI/programs/"; //+ timestamp

    private RequestQueue queue;
    private Context ctx;

    //singleton to ensure we run just one DownloadManager
    /**
     private DownloadsManager mManager;
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

     */

    public DownloadsManager(Context ctx)
    {
        this.ctx = ctx;
        //initiate request queue
        queue  = Volley.newRequestQueue(ctx);
    }

    //we need interface callback to get data from getVolleyResponseData
    //when it finished
    public interface VolleyCallback
    {
        void onSuccess(JSONArray result);
        void onFailure(VolleyError error);
    }
    public interface ImageVolleyCallback
    {
        void onImageSuccess(Bitmap bitmap);
        void onImageFailure(VolleyError error);
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

    //method to download icon for each item (channel, category)
    public final void getVolleyImage (String url, final ImageVolleyCallback callback)
    {
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>()
                {
                    @Override
                    public void onResponse(Bitmap bitmap)
                    {
                       callback.onImageSuccess(bitmap);
                    }
                },
                80, 50, null, Bitmap.Config.ARGB_4444, //int maxWidth, int maxHeight,  ScaleType scaleType, Config decodeConfig
                new Response.ErrorListener()
                {
                    public void onErrorResponse(VolleyError error)
                    {
                        callback.onImageFailure(error);
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
