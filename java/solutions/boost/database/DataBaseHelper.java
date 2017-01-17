package solutions.boost.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import solutions.boost.channelstruct.Channel;
import solutions.boost.channelstruct.JSONNames;

/**
 * Created by Irene on 14.01.2017.
 * Helper to create SQL database to store data from OLL TV server
 */
public class DataBaseHelper extends SQLiteOpenHelper
{
    //data base singleton
    private static DataBaseHelper instance = null;
    private Context context;

    //database version
    private final static int DATABASE_VERSION = 1;
    //database name
    private final static String DATABASE_NAME = "AppDatabaseSQL";
    //name for table for channels
    private final static String TABLE_CHANNELS = "ChannelsTable";

    //!----for KEY variables use JSONNames from channelstruct----!
    private final static String KEY_ID = JSONNames.ID;
    private final static String KEY_NAME = JSONNames.NAME;
    private final static String KEY_URL = JSONNames.URL;
    private final static String KEY_PICT_URL = JSONNames.PICT_URL;
    private final static String KEY_CATEGORY_ID = JSONNames.CHANNEL_CATEGORY_ID;

    //don't access it
    //use getInstance instead
    private DataBaseHelper(Context context)
    {
        //create DATABASE
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    //create singleton of Data base
    public static DataBaseHelper getInstance(Context ctx)
    {
        if (instance == null)
        {
            instance = new DataBaseHelper(ctx.getApplicationContext());
        }
        return instance;
    }

    //create new channel table with columns for id, name, url, pict_url, category id
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_CHANNELS_TABLE = "CREATE TABLE " + TABLE_CHANNELS
                + "(" + KEY_ID + " INTEGER PRIMARY KEY," +            //id of channel
                KEY_NAME + " TEXT," +                         //name of channel
                KEY_URL + " TEXT," +                           //url of channel
                KEY_PICT_URL + " TEXT," +                      //url of picture for channel
                KEY_CATEGORY_ID + " INTEGER" + ")";               //category belongs channel


        db.execSQL(CREATE_CHANNELS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANNELS);

        // Create tables again
        onCreate(db);
    }

    /**<!--------------CRUD methods-----------------!>**/
    // Adding new channel
    //use ContentValues Android predefined class
    public void addChannelToTable(Channel channel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        //put all Channel's attribute (ID, NAME, URL, PICT_URL, CATEGORY_ID) into contentValues
        //contentValues.put(KEY_ID, channel.getId());
        contentValues.put(KEY_NAME, channel.getName());
        contentValues.put(KEY_URL, channel.getUrl());
        contentValues.put(KEY_PICT_URL, channel.getPicture());
        contentValues.put(KEY_CATEGORY_ID, channel.getCategory_id());

        //insert it to database
        db.insert(TABLE_CHANNELS, null, contentValues);
        db.close();
    }

    // Getting single channel
    //use Android Cursor for such operation
    public Channel getChannelFromTable(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CHANNELS,
                new String[] {KEY_ID, KEY_NAME, KEY_URL, KEY_PICT_URL, KEY_CATEGORY_ID},
                KEY_ID + "=?",
                new String[] { String.valueOf(id) },
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        //Channel constructor (int id, String name, String url, String pict, int category_id)
        Channel channel = new Channel (Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)));

        //close all
        cursor.close();
        db.close();

        // return channel
        return channel;
    }

    // Getting all channels
    public List<Channel> getAllChannelsInTable()
    {
        List<Channel> mList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CHANNELS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do {
                Channel channel = new Channel();
                channel.setId(Integer.parseInt(cursor.getString(0)));
                channel.setName(cursor.getString(1));
                channel.setUrl(cursor.getString(2));
                channel.setPicture(cursor.getString(3));
                channel.setCategory_id(Integer.parseInt(cursor.getString(4)));

                // Adding contact to list
                mList.add(channel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // return list
        return mList;
    }

    // Getting channels Count
    public int getChannelsCount()
    {
        String countQuery = "SELECT * FROM " + TABLE_CHANNELS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();

        cursor.close();
        db.close();

        // return count
        return count;
    }

    // Updating single channel
    public int updateChannelInTable(Channel channel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, channel.getId());
        values.put(KEY_NAME, channel.getName());
        values.put(KEY_URL, channel.getUrl());
        values.put(KEY_PICT_URL, channel.getPicture());
        values.put(KEY_CATEGORY_ID, channel.getCategory_id());

        // updating row
        return db.update(TABLE_CHANNELS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(channel.getId()) });
    }

    // Deleting single channel
    public void deleteChannelFromTable(Channel channel)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHANNELS, KEY_ID + " = ?",
                new String[] { String.valueOf(channel.getId()) });
        db.close();
    }
}

