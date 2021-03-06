package solutions.boost.tvprogramm;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import solutions.boost.channelstruct.Channel;
import solutions.boost.database.DataBaseHelper;

/**
 * Created on 14.01.2017.
 * creates adapter to populate channel listview
 */
public class ChannelAdapter extends ResourceCursorAdapter
{

    private LayoutInflater inflater;
    private Cursor cursor;
    //reference to my layout for every item
    private final static int ITEM_LIST_LAYOUT = R.layout.channel_item_layout;
    private Context context;

    //loader for load bitmap with url and populate imageView
    private ImageLoader loader;




    public ChannelAdapter (Context context, int layout, Cursor cursor, int flags)
    {
        super(context, ITEM_LIST_LAYOUT, cursor, flags);
        this.context = context;
        this.cursor = cursor;

        inflater = LayoutInflater.from(this.context);
        //loader
        loader = ChannelPictLoader.getInstance(context).getImageLoader();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View view = inflater.inflate(ITEM_LIST_LAYOUT, parent, false);
        ViewHolder viewHolder = new ViewHolder();

        //init all views once
        viewHolder.textView = (TextView) view.findViewById(R.id.channelname);
        viewHolder.image = (NetworkImageView) view.findViewById(R.id.channelpict);
        viewHolder.checker = (CheckBox) view.findViewById(R.id.channelcheckBox);
        viewHolder.id_container = (TextView) view.findViewById(R.id.id_container);

        //set Tag to find holder in future
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor)
    {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        int id = cursor.getInt(0);
        String id_s = String.valueOf(id);
        String name = cursor.getString(1);  //0 is id, 1 is name, 3 is url for pict
        String url = cursor.getString(3);

        viewHolder.checker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int checked = cursor.getInt(5);
                DataBaseHelper data = DataBaseHelper.getInstance(context);

                int id = Integer.parseInt(cursor.getString(0));

                if(checked == 0)
                    data.setChannelAsPreferred(id);
                else if (checked ==1)
                    data.unsetChannelAsPreferred(id);
            }

        });


        //using volley networkImageView - cool!!!!!!!!
        viewHolder.image.setMaxHeight(50);
        viewHolder.image.setMaxWidth(80);
        viewHolder.image.setDefaultImageResId(R.drawable.image_load_error);
        viewHolder.image.setImageUrl(url, loader);

        viewHolder.textView.setText(name);
        viewHolder.id_container.setText(id_s);
    }


    private static class ViewHolder
    {
        TextView textView;
        NetworkImageView image;
        CheckBox checker;
        TextView id_container; //invisible
    }
}