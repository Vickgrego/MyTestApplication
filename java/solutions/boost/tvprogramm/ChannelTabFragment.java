package solutions.boost.tvprogramm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import solutions.boost.database.DataBaseHelper;

/**
 * Created on 16.01.2017.
 * Tab Fragment for list of channels
 */
public class ChannelTabFragment extends BasicTabFragment
{
    public void setAdapterAndList()
    {
        Cursor cursor = getDataBaseInstance().getCursorChannelsInTable();
        ListView list = getListViewInstance();

        if(cursor !=null)
        {
            ChannelAdapter adapter = new ChannelAdapter
                    (getContext(), R.layout.channel_item_layout, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

            list.setAdapter(adapter);
            //cursor.close();
        }

        //start Activity for each channel to show program schedule
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                TextView name = (TextView) view.findViewById(R.id.channelname);
                String s = name.getText().toString();

                TextView textid = (TextView) view.findViewById(R.id.id_container);
                String id_s = textid.getText().toString();

                Intent i = new Intent(getContext(), ProgramSchedule.class);
                i.putExtra("KEY_NAME", s); //with this key we retrieve program for this channel
                i.putExtra("KEY_ID", id_s);

                startActivity(i);
            }
        });


    }

}