package solutions.boost.tvprogramm;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import solutions.boost.database.DataBaseHelper;

/**
 * Created on 16.01.2017.
 * Tab Fragment for list of channels
 */
public class PreferredTabFragment extends BasicTabFragment
{

    public void setAdapterAndList()
    {
        Cursor cursor = getDataBaseInstance().getPreferredChannels();

        if(cursor != null)
        {
            PreferredChannelsAdapter adapter = new PreferredChannelsAdapter
                    (getContext(), R.layout.channel_item_layout, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            getListViewInstance().setAdapter(adapter);
        }
    }

}