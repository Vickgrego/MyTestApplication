package solutions.boost.tvprogramm;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/**
 * Created on 14.01.2017.
 * creates adapter to populate channel listview
 */
public class CategoryAdapter extends ResourceCursorAdapter
{

    private LayoutInflater inflater;
    private Cursor cursor;
    //reference to my layout for every item
    private final static int ITEM_LIST_LAYOUT = R.layout.category_item_layout;
    private Context context;

    //loader for load bitmap with url and populate imageView
    private ImageLoader loader;


    public CategoryAdapter(Context context, int layout, Cursor cursor, int flags)
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
        viewHolder.textView = (TextView) view.findViewById(R.id.categorytitle);
        viewHolder.image = (NetworkImageView) view.findViewById(R.id.categorypict);
        viewHolder.idcontainer = (TextView) view.findViewById(R.id.category_id_container);

        //set Tag to find holder in future
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        int id = cursor.getInt(0);
        String id_s = String.valueOf(id);
        String name = cursor.getString(1);  //0 is id, 1 is name, 3 is url for pict
        String url = cursor.getString(2);

        //using volley networkImageView - cool!!!!!!!!
        viewHolder.image.setMaxHeight(50);
        viewHolder.image.setMaxWidth(80);
        viewHolder.image.setDefaultImageResId(R.drawable.image_load_error);
        viewHolder.image.setImageUrl(url, loader);

        viewHolder.idcontainer.setText(id_s);
        viewHolder.textView.setText(name);
    }


    private static class ViewHolder
    {
        TextView textView;
        TextView idcontainer;
        NetworkImageView image;
    }
}