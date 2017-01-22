package solutions.boost.tvprogramm;

import android.content.Intent;
import android.database.Cursor;
import android.provider.Settings;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import solutions.boost.adaptersfragments.ProgramAdapter;
import solutions.boost.database.DataBaseHelper;
import solutions.boost.internetservice.DownloadsManager;

public class ProgramScheduleActivity extends AppCompatActivity
{

    private TextView title;
    private TextView dateOfProgram;
    private ListView mListView;
    private DownloadsManager loader;
    private DataBaseHelper dataBaseHelper;

    private String date;
    private boolean showOnlyForToday;

    private int channel_id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_schedule);

        loader = DownloadsManager.getInstance(this);
        dataBaseHelper = DataBaseHelper.getInstance(getApplicationContext());

        title = (TextView) findViewById(R.id.textViewinschedule);
        dateOfProgram = (TextView) findViewById(R.id.date_of_program);
        dateOfProgram.setText(R.string.schedule_for_today);
        mListView = (ListView) findViewById(R.id.listview_in_schedule);

        showOnlyForToday = true;
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        date = setData();

        Intent starterIntent = getIntent();

        String name = starterIntent.getStringExtra("KEY_NAME");
        title.setText(name);

        String id_string = starterIntent.getStringExtra("KEY_ID");

        //find id of channel from table_channels by name
        //pass id to find all programs schedule for channel with such id
        channel_id = Integer.parseInt(id_string);

        setListView();
    }

    private void setListView()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {

                Cursor cursor;
                if(showOnlyForToday)
                    cursor = dataBaseHelper.getProgramForChannel_id_spec_data(channel_id, date);
                else
                    cursor = dataBaseHelper.getProgramForChannel_id(channel_id);

                if(cursor != null)
                {
                    Log.e("program schedule", String.valueOf(cursor.getCount()));

                    ProgramAdapter adapter = new ProgramAdapter(getApplicationContext(), R.layout.program_item_layout,
                            cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

                    mListView.setAdapter(adapter);
                }
                else
                {
                    Log.e("program schedule", "cursor == null");
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.program_schedule_menu, menu);
        return true;
    }

    //switch between today TV schedule and weeks
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.dayProgran:

                if(!showOnlyForToday)
                {
                    dateOfProgram.setText(R.string.schedule_for_today);
                    showOnlyForToday = true;
                }

                setListView();
                return true;

            case R.id.weekProgram:

                if(showOnlyForToday)
                {
                    dateOfProgram.setText(R.string.schedule_for_week);
                    showOnlyForToday = false;
                }

                setListView();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String setData()
    {
        String data_today;

        Date date = Calendar.getInstance().getTime();
        // Display a date in day, month, year format
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  //should give 22/01/2017
        data_today = formatter.format(date);

        Log.e("DATE", data_today);

        return data_today;
    }
}
