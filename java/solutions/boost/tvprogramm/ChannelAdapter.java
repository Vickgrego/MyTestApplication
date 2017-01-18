package solutions.boost.tvprogramm;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import solutions.boost.channelstruct.Channel;

/**
 * Created by Irene on 14.01.2017.
 * creates adapter to populate channel listview
 */
public class ChannelAdapter extends ArrayAdapter<Channel>
{
    private List<Channel> mList;
    protected final static int ITEM_LIST_LAYOUT = R.layout.channel_item_layout;
    private Context context;

    //loader for load bitmap with url and populate imageView
    private ImageLoader loader;

    //View elements for each item
    private ViewHolder viewHolder;

    public ChannelAdapter(Context context, int resource, List<Channel> objects)
    {
        super(context, resource, objects);
        mList = objects;
        this.context = context;

        //loader
        loader = (ImageLoader) ChannelPictLoader.getInstance(context).getImageLoader();
    }

    @Override
    //the first try to implement method gives very poor performance, so
    // use ViewHolder pattern for efficiency!!!!
    public View getView(int position, View view, ViewGroup parent)
    {
        //reuse view for efficiency
        if(view == null)
        {
            view = LayoutInflater.from(context).inflate(ITEM_LIST_LAYOUT, parent, false);
            viewHolder = new ViewHolder();

            //init all views once
            viewHolder.textView = (TextView) view.findViewById(R.id.channelname);
            viewHolder.image = (NetworkImageView) view.findViewById(R.id.channelpict);
            viewHolder.progressBar = (ProgressBar) view.findViewById(R.id.channel_progressBar);

            //set Tag to find holder in future
            view.setTag(viewHolder);
        }
        else       //avoided calling findViewById() on resource everytime just use the viewHolder
        {
            viewHolder = (ViewHolder) view.getTag();
        }

        //using Channel class get value into View
        final Channel channel = mList.get(position);

        //load pict for each channel
        //get picture from cache using Key - id of channel
        //logic is next: if bitmap is null, than start Volley ImageRequest and save this image into cache
        //else, if it not null, than get it from cache
        //cache has int-Bitmap value, where key is Id of Channel
        /*if(cache.size() == 0)
        {
            //make Progressbar visible
            viewHolder.progressBar.setVisibility(View.VISIBLE);

            loader.getVolleyImage(channel.getPicture(),//channel getPicture method returns URL of picture
                    new DownloadsManager.ImageVolleyCallback()
                    {
                        @Override
                        public void onImageSuccess(Bitmap bitmap)
                        {
                            cache.put(channel.getId(), bitmap);
                            viewHolder.image.setImageBitmap(cache.get(channel.getId()));

                            //turn off progressbar when image is load
                            viewHolder.progressBar.setVisibility(View.INVISIBLE);
                        }
                        @Override
                        public void onImageFailure(VolleyError error)
                        {
                            viewHolder.image.setImageResource(R.drawable.image_load_error); // set some default drawable
                        }
                    }
            );
        }
        else
        {
            viewHolder.image.setImageBitmap(cache.get(channel.getId()));
        }*/

        //using volley networkImageView - cool!!!!!!!!
        viewHolder.image.setImageUrl(channel.getPicture(), loader);

        viewHolder.textView.setText(channel.getName());

        return view;
    }

    private static class ViewHolder
    {
        TextView textView;
        NetworkImageView image;
        ProgressBar progressBar;
    }
}
