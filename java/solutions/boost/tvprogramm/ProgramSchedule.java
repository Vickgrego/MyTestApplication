package solutions.boost.tvprogramm;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONArray;

import java.util.List;

import solutions.boost.channelstruct.Channel;
import solutions.boost.channelstruct.Program;
import solutions.boost.database.DataBaseHelper;
import solutions.boost.dataparser.JSONParser;
import solutions.boost.internetservice.DownloadsManager;

public class ProgramSchedule extends AppCompatActivity
{

    private TextView title;
    private ListView mListView;
    private DownloadsManager loader;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_sschedule);

        loader = DownloadsManager.getInstance(this);

        title = (TextView) findViewById(R.id.textViewinschedule);
        mListView = (ListView) findViewById(R.id.listview_in_schedule);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getApplicationContext());

        Intent starterIntent = getIntent();

        String name = starterIntent.getStringExtra("KEY_NAME");
        title.setText(name);

        String id_string = starterIntent.getStringExtra("KEY_ID");

        //find id of channel from table_channels by name
        //pass id to find all programs schedule for channel with such id
        int i = Integer.parseInt(id_string);

        setListView(i, dataBaseHelper);

    }

    private void setListView(int i, DataBaseHelper dataBaseHelper)
    {
        Cursor cursor = dataBaseHelper.getProgramForChannel_id(i);

        if(cursor != null)
        {
            ProgramAdapter adapter = new ProgramAdapter(getApplicationContext(), R.layout.program_item_layout,
                    cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

            mListView.setAdapter(adapter);
        }
        else
        {
            Log.e("program schedule", "cursor == null");
        }

    }
}
