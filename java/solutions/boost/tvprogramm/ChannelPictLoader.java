package solutions.boost.tvprogramm;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created on 17.01.2017.
 * Custom image loader
 * using Volley
 * implementing Singleton pattern from https://developer.android.com/training/volley/requestqueue.html#singleton
 */
public class ChannelPictLoader
{
    //singleton
    private static ChannelPictLoader mInstance;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;


    private ChannelPictLoader(Context context)
    {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        //init cache with 8 part of available memory in phone
        //android docs recommends such size
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache()
                {
                    private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(cacheSize);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized ChannelPictLoader getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new ChannelPictLoader(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader()
    {
        return mImageLoader;
    }

}
