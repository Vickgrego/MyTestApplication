package solutions.boost.tvprogramm;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import solutions.boost.database.DataBaseHelper;

/**
 * Created on 20.01.2017.
 * creates adapter to populate channel listview
 */
public class ProgramAdapter extends ResourceCursorAdapter
{
    private LayoutInflater inflater;
    private Cursor cursor;
    //reference to my layout for every item
    private final static int ITEM_LIST_LAYOUT = R.layout.program_item_layout;
    private Context context;

    public ProgramAdapter (Context context, int layout, Cursor cursor, int flags)
    {
        super(context, ITEM_LIST_LAYOUT, cursor, flags);
        this.context = context;
        this.cursor = cursor;

        inflater = LayoutInflater.from(this.context);
    }


    public View newView (Context context, Cursor cursor, ViewGroup parent)
    {
        View view = inflater.inflate(ITEM_LIST_LAYOUT, parent, false);
        ViewHolder viewHolder = new ViewHolder();

        //init all views once
        viewHolder.data = (TextView) view.findViewById(R.id.dataprogram);
        viewHolder.time = (TextView) view.findViewById(R.id.timeprogram);
        viewHolder.title = (TextView) view.findViewById(R.id.titleprogram);
        viewHolder.descr = (TextView) view.findViewById(R.id.descriptionprogramm);

        //set Tag to find holder in future
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String data_s = cursor.getString(1);  //0 is data, 1 is time, 2 is title, 3 is descr
        String time_s = cursor.getString(2);
        String title_s = cursor.getString(3);
        String descr_s = cursor.getString(4);

        viewHolder.data.setText(data_s);
        viewHolder.time.setText(time_s);
        viewHolder.title.setText(title_s);
        viewHolder.descr.setText(descr_s);
    }


    private static class ViewHolder
    {
        TextView data;
        TextView time;
        TextView title;
        TextView descr;
    }
}