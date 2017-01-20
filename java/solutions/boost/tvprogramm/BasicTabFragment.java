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
public class BasicTabFragment extends Fragment
{

    //title for tabs
    private String title;

    private DataBaseHelper dataHelper;

    //views
    private ListView listView;

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

        setAdapterAndList();

    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    //implement it in extended tabsFragments
    protected void setAdapterAndList()
    {

    }

    protected void setTitle(String title)
    {
        this.title = title;
    }

    protected String getTabsTitle()
    {
        return title;
    }

    protected DataBaseHelper getDataBaseInstance()
    {
        return dataHelper;
    }

    protected ListView getListViewInstance()
    {
        return listView;
    }

}