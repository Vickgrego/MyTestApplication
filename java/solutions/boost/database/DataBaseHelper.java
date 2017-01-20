package solutions.boost.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import solutions.boost.channelstruct.Category;
import solutions.boost.channelstruct.Channel;
import solutions.boost.channelstruct.JSONNames;
import solutions.boost.channelstruct.Program;

/**
 * Created by Irene on 14.01.2017.
 * Helper to create SQL database to store data from OLL TV server
 */
public class DataBaseHelper extends SQLiteOpenHelper
{
    private static String TAG = "from SQL";

    //data base singleton
    private static DataBaseHelper instance = null;
    private Context context;

    //database version
    private final static int DATABASE_VERSION = 1;
    //database name
    private final static String DATABASE_NAME = "AppDatabaseSQL";
    //name for tables
    private final static String TABLE_CHANNELS = "ChannelsTable";
    private final static String TABLE_CATEGORIES = "CategoriesTable";
    private final static String TABLE_PROGRAMS = "ProgramsTable";

    //!----for KEY variables use JSONNames from channelstruct----!
    private final static String KEY_ID = "_id";                             //cursor needs "_id"
    private final static String KEY_NAME = JSONNames.NAME;
    private final static String KEY_URL = JSONNames.URL;
    private final static String KEY_PICT_URL = JSONNames.PICT_URL;
    private final static String KEY_CHANNEL_IN_CATEGORY_ID = JSONNames.CHANNEL_CATEGORY_ID;
    //var for preferred channel's column
    private final static String KEY_PREF_CHANNEL = "preferred";

    //!----for KEY variables for categories----!
    private final static String KEY_ID_FOR_CATEGORIES = "_id";
    private final static String KEY_CATEGORY_TITLE = JSONNames.CATEGORY_TITLE;
    private final static String KEY_CATEGORY_PICT_URL = JSONNames.CATEGORY_PICT_URL;

    //!----for KEY variables for programs----!
    private final static String KEY_TABLE_PROGRAMS_ID = "_id";
    private final static String KEY_CHANNEL_PROGRAMS_ID = JSONNames.CHANNEL_PROGRAMS_ID;
    private final static String KEY_PROGRAMS_DATA = JSONNames.PROGRAMS_DATA;
    private final static String KEY_PROGRAMS_TIME = JSONNames.PROGRAMS_TIME;
    private final static String KEY_PROGRAMS_TITLE = JSONNames.PROGRAMS_TITLE;
    private final static String KEY_PROGRAMS_DESCRIPTION = JSONNames.PROGRAMS_DESCRIPTION;

    //no access to it
    //i use getInstance instead
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
                KEY_CHANNEL_IN_CATEGORY_ID + " INTEGER," +     //category belongs channel
                KEY_PREF_CHANNEL + " INTEGER" + ")";            //if set to 1-preferred, if no - set to 0

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES
                + "(" + KEY_ID_FOR_CATEGORIES + " INTEGER PRIMARY KEY," +     //id of category
                KEY_CATEGORY_TITLE + " TEXT," +                         //name of category
                KEY_CATEGORY_PICT_URL + " TEXT" + ")";                 //url of category's icon

        String CREATE_PROGRAMS_TABLE = "CREATE TABLE " + TABLE_PROGRAMS
                + "(" + KEY_TABLE_PROGRAMS_ID + " INTEGER PRIMARY KEY," +
                KEY_CHANNEL_PROGRAMS_ID + " INTEGER," +                 //id of channel
                KEY_PROGRAMS_DATA + " TEXT," +                         //date for program
                KEY_PROGRAMS_TIME + " TEXT," +                           //time of program
                KEY_PROGRAMS_TITLE + " TEXT," +                      //name of program
                KEY_PROGRAMS_DESCRIPTION + " TEXT" + ")";               //program content


        db.execSQL(CREATE_CHANNELS_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_PROGRAMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANNELS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRAMS);

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
        //contentValues.put(KEY_ID, channel.getCategory_id());
        contentValues.put(KEY_NAME, channel.getName());
        contentValues.put(KEY_URL, channel.getUrl());
        contentValues.put(KEY_PICT_URL, channel.getPicture());
        contentValues.put(KEY_CHANNEL_IN_CATEGORY_ID, channel.getCategory_id());
        contentValues.put(KEY_PREF_CHANNEL, channel.getPreferredKey());

        //insert it to database
        db.insert(TABLE_CHANNELS, null, contentValues);
        db.close();
    }

    public void setChannelAsPreferred (int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PREF_CHANNEL, 1); //1 - to true

        // updating row
        db.update(TABLE_CHANNELS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public void unsetChannelAsPreferred (int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PREF_CHANNEL, 0); //1 - to true

        // updating row
        db.update(TABLE_CHANNELS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    // Getting single channel
    //use Android Cursor for such operation
    public Cursor getChannelFromTable(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CHANNELS,
                new String[] {KEY_ID, KEY_NAME, KEY_URL, KEY_PICT_URL, KEY_CHANNEL_IN_CATEGORY_ID,
                KEY_PREF_CHANNEL},
                KEY_ID + "=?",
                new String[] { String.valueOf(id) },
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }

    //return preferred channels
    public Cursor getPreferredChannels()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CHANNELS,
                new String[] {KEY_ID, KEY_NAME, KEY_URL, KEY_PICT_URL, KEY_CHANNEL_IN_CATEGORY_ID,
                        KEY_PREF_CHANNEL},
                KEY_PREF_CHANNEL + "=?",
                new String[] { String.valueOf(1) },
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }

    //add all channels to table
    public void addAllChannelsToTable(List<Channel> channels)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues;
        //put all Channel's attribute (ID, NAME, URL, PICT_URL, CATEGORY_ID) into contentValues
        //but no preferred attribute
        //contentValues.put(KEY_ID, channel.getCategory_id());
        for (Channel channel: channels)
        {
            contentValues = new ContentValues();

            contentValues.put(KEY_NAME, channel.getName());
            contentValues.put(KEY_URL, channel.getUrl());
            contentValues.put(KEY_PICT_URL, channel.getPicture());
            contentValues.put(KEY_CHANNEL_IN_CATEGORY_ID, channel.getCategory_id());

            db.insert(TABLE_CHANNELS, null, contentValues);
        }

        db.close();
    }


    // Getting cursor with all channels
    public Cursor getCursorChannelsInTable()
    {
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CHANNELS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //db.close();

        // return list
        return cursor;
    }

    //return preferred channels
    public Cursor getChannelsInCategory(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CHANNELS,
                new String[] {KEY_ID, KEY_NAME, KEY_URL, KEY_PICT_URL, KEY_CHANNEL_IN_CATEGORY_ID,
                        KEY_PREF_CHANNEL},
                KEY_CHANNEL_IN_CATEGORY_ID + "=?",
                new String[] { id },
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }

    public int getChannel_id_byName(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CHANNELS,
                new String[] {KEY_ID},
                KEY_NAME + "=?",
                new String[] { name },
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        int i = cursor.getInt(0);

        cursor.close();
        db.close();

        return i;
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
        values.put(KEY_CHANNEL_IN_CATEGORY_ID, channel.getCategory_id());

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


    /**------------ Category table -----------------------------*/
    //add all channels to table
    public void addAllCategoriesToTable(List<Category> categories)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues;

        for (Category ctg: categories)
        {
            contentValues = new ContentValues();

            contentValues.put(KEY_ID_FOR_CATEGORIES, ctg.getCategory_id());
            contentValues.put(KEY_CATEGORY_TITLE, ctg.getTitle());
            contentValues.put(KEY_CATEGORY_PICT_URL, ctg.getCategory_pict_url());

            db.insert(TABLE_CATEGORIES, null, contentValues);
        }

        db.close();
    }

    // Getting cursor with all channels
    public Cursor getCursorCategoriesInTable()
    {
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //db.close();

        // return list
        return cursor;
    }


    /**------------ Programs table -----------------------------*/
    //add all channels to table
    public void addAllProgramsToTable(List<Program> programs)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues;
        int i = 1;

        for (Program program: programs)
        {
            contentValues = new ContentValues();

            //contentValues.put(KEY_TABLE_PROGRAMS_ID, i);
            contentValues.put(KEY_CHANNEL_PROGRAMS_ID, program.getChannel_progr_id());
            contentValues.put(KEY_PROGRAMS_DATA, program.getProgr_data());
            Log.v(TAG, program.getProgr_data());
            contentValues.put(KEY_PROGRAMS_TIME, program.getProgr_time());
            Log.v(TAG, program.getProgr_data());
            contentValues.put(KEY_PROGRAMS_TITLE, program.getProgr_title());
            Log.v(TAG, program.getProgr_time());
            contentValues.put(KEY_PROGRAMS_DESCRIPTION, program.getProgr_descript());
            Log.v(TAG, program.getProgr_descript());

            i++;
            db.insert(TABLE_PROGRAMS, null, contentValues);
        }

        db.close();
    }

    public Cursor getProgramForChannel_id(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PROGRAMS,
                new String[] {KEY_TABLE_PROGRAMS_ID,
                        KEY_PROGRAMS_DATA,
                        KEY_PROGRAMS_TIME,
                        KEY_PROGRAMS_TITLE,
                        KEY_PROGRAMS_DESCRIPTION},
                KEY_CHANNEL_PROGRAMS_ID + "=?",
                new String[] { String.valueOf(id) },
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }

    // Getting cursor with all channels
    public Cursor getCursorPrograms()
    {
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PROGRAMS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        return cursor;
    }

    // Getting channels Count
    public int getProgramsCount()
    {
        String countQuery = "SELECT * FROM " + TABLE_PROGRAMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();

        cursor.close();
        db.close();

        // return count
        return count;
    }
}

