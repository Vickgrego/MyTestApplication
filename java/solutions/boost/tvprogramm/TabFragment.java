package solutions.boost.tvprogramm;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import solutions.boost.channelstruct.Channel;
import solutions.boost.database.DataBaseHelper;

/**
 * Created on 16.01.2017.
 */
public class TabFragment extends Fragment
{
    private DataBaseHelper dataHelper;

    //views
    private ListView listView;

    //to know how to display list inside fragment(sorted or default)
    //is changed in OptionMenu in MainActivity
    public static boolean showSorted = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //save our Fragment if activity will change
        //but don't save view elements
        setRetainInstance(true);

        //get instance of database
        dataHelper = DataBaseHelper.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.tab_fragment_layout, container, false);
        listView = (ListView) v.findViewById(R.id.listview_fragment);
        return v;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        //populate list with data from sql database
        if(dataHelper.getChannelsCount() > 0)
        {
            setAdapterAndList();
        }

    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    public void setAdapterAndList()
    {
        ArrayList<Channel> array = new ArrayList<>();
        array.addAll(dataHelper.getAllChannelsInTable());

        //check if user decided to sort a list
        if(showSorted)
        {
            Collections.sort(array, new Comparator<Channel>()
            {
                @Override
                public int compare(Channel c1, Channel c2)
                {
                    return c1.getName().compareTo(c2.getName());
                }
            });
        }

        ChannelAdapter adapter = new ChannelAdapter
                (getActivity(), R.layout.channel_item_layout, array);
        listView.setAdapter(adapter);
    }

}
