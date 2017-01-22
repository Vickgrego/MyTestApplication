package solutions.boost.tvprogramm;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import solutions.boost.adaptersfragments.ChannelAdapter;
import solutions.boost.database.DataBaseHelper;

public class ChannelsForCategoryActivity extends AppCompatActivity
{
    private DataBaseHelper dataHelper;

    //views
    private ListView listView;

    private String KEY_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels_for_category);

        dataHelper = DataBaseHelper.getInstance(this);
        listView = (ListView) findViewById(R.id.channel_in_category_listview);

        Intent i = getIntent();
        KEY_ID = i.getStringExtra("KEY_ID");
    }

    @Override
    public void onStart()
    {
        super.onStart();

        setAdapterAndList();
    }


    private void setAdapterAndList()
    {
        Cursor cursor = dataHelper.getChannelsInCategory(KEY_ID);

        if(cursor !=null)
        {
            ChannelAdapter adapter = new ChannelAdapter
                    (getApplicationContext(), R.layout.channel_item_layout, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

            listView.setAdapter(adapter);
            //cursor.close();
        }
    }
}
