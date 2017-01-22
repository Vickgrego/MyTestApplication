package solutions.boost.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import solutions.boost.dataparser.JSONNames;

/**
 * Created on 14.01.2017.
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

    public void setChannelAsPreferred (int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PREF_CHANNEL, 1); //1 - to true

        // updating row
        db.update(TABLE_CHANNELS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });

        db.close();
    }

    public void unsetChannelAsPreferred (int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PREF_CHANNEL, 0); //1 - to true

        // updating row
        db.update(TABLE_CHANNELS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });

        db.close();
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
                new String[] { String.valueOf(1) }, //selection of preferred == 1
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }

    //add all channels to table
    public void addAllChannelsToTable(JSONArray channels)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues;
        //put all Channel's attribute (ID, NAME, URL, PICT_URL, CATEGORY_ID) into contentValues
        //but no preferred attribute
        //contentValues.put(KEY_ID, channel.getCategory_id());
        for (int i=0; i<channels.length(); i++)
        {
            try{
                JSONObject object = channels.getJSONObject(i);
                contentValues = new ContentValues();

                contentValues.put(KEY_NAME, object.getString(JSONNames.NAME));
                contentValues.put(KEY_URL, object.getString(JSONNames.URL));
                contentValues.put(KEY_PICT_URL, object.getString(JSONNames.PICT_URL));
                contentValues.put(KEY_CHANNEL_IN_CATEGORY_ID, object.getInt(JSONNames.CHANNEL_CATEGORY_ID));
                contentValues.put(KEY_PREF_CHANNEL, 0); //preferred is 0 be default

                db.insert(TABLE_CHANNELS, null, contentValues);
            } catch (Exception e)
            {

            }
        }

        db.close();
    }

    public void updateAllChannelsInTable(JSONArray channels)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues;

        for (int i=0; i<channels.length(); i++)
        {
            try{
                JSONObject object = channels.getJSONObject(i);
                contentValues = new ContentValues();

                contentValues.put(KEY_NAME, object.getString(JSONNames.NAME));
                contentValues.put(KEY_URL, object.getString(JSONNames.URL));
                contentValues.put(KEY_PICT_URL, object.getString(JSONNames.PICT_URL));
                contentValues.put(KEY_CHANNEL_IN_CATEGORY_ID, object.getInt(JSONNames.CHANNEL_CATEGORY_ID));

                db.update(TABLE_CHANNELS, contentValues, KEY_ID + " = ?",
                        new String[] { String.valueOf(object.getInt(JSONNames.ID)) });
            } catch (Exception e)
            {

            }
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

    /**------------ Category table -----------------------------*/
    //add all channels to table
    public void addAllCategoriesToTable(JSONArray categories)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues;

        for (int i=0; i<categories.length(); i++)
        {
           try{
               contentValues = new ContentValues();
               JSONObject object = categories.getJSONObject(i);

               contentValues.put(KEY_ID_FOR_CATEGORIES, object.getInt(JSONNames.ID_CATEGORY));
               contentValues.put(KEY_CATEGORY_TITLE, object.getString(JSONNames.CATEGORY_TITLE));
               contentValues.put(KEY_CATEGORY_PICT_URL, object.getString(JSONNames.CATEGORY_PICT_URL));

               db.insert(TABLE_CATEGORIES, null, contentValues);
           } catch (Exception e)
           {
               //TODO
           }
        }

        db.close();
    }

    public void updateAllCategoriesInTable(JSONArray channels)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues;

        for (int i=0; i<channels.length(); i++)
        {
            try{
                JSONObject object = channels.getJSONObject(i);
                contentValues = new ContentValues();

                contentValues.put(KEY_ID_FOR_CATEGORIES, object.getInt(JSONNames.ID_CATEGORY));
                contentValues.put(KEY_CATEGORY_TITLE, object.getString(JSONNames.CATEGORY_TITLE));
                contentValues.put(KEY_CATEGORY_PICT_URL, object.getString(JSONNames.CATEGORY_PICT_URL));

                db.update(TABLE_CATEGORIES, contentValues, KEY_ID_FOR_CATEGORIES + " = ?",
                        new String[] { String.valueOf(object.getInt(JSONNames.ID_CATEGORY)) });
            } catch (Exception e)
            {

            }
        }

        db.close();

    }

    // Getting cursor with all categories
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

    // Getting categories Count
    public int getCategoriesCount()
    {
        String countQuery = "SELECT * FROM " + TABLE_CATEGORIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();

        cursor.close();
        db.close();

        // return count
        return count;
    }


    /**------------ Programs table -----------------------------*/
    //add all channels to table
    public void addAllProgramsToTable(JSONArray programs)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues;
        //int i = 1;

        for (int i=0; i<programs.length(); i++)
        {
            contentValues = new ContentValues();
            try{
                JSONObject object = programs.getJSONObject(i);

                //contentValues.put(KEY_TABLE_PROGRAMS_ID, i);
                contentValues.put(KEY_CHANNEL_PROGRAMS_ID, object.getInt(JSONNames.CHANNEL_PROGRAMS_ID));
                contentValues.put(KEY_PROGRAMS_DATA, object.getString(JSONNames.PROGRAMS_DATA));
                contentValues.put(KEY_PROGRAMS_TIME, object.getString(JSONNames.PROGRAMS_TIME));
                contentValues.put(KEY_PROGRAMS_TITLE, object.getString(JSONNames.PROGRAMS_TITLE));
                contentValues.put(KEY_PROGRAMS_DESCRIPTION, object.getString(JSONNames.PROGRAMS_DESCRIPTION));
            } catch (Exception e)
            {
                //TODO
            }
            //i++;
            db.insert(TABLE_PROGRAMS, null, contentValues);
        }

        db.close();
    }

    public void updateAllProgramsToTable(JSONArray programs)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues;
        //int i = 1;

        for (int i=0; i<programs.length(); i++)
        {
            contentValues = new ContentValues();
            try{
                JSONObject object = programs.getJSONObject(i);

                //contentValues.put(KEY_TABLE_PROGRAMS_ID, i);
                contentValues.put(KEY_CHANNEL_PROGRAMS_ID, object.getInt(JSONNames.CHANNEL_PROGRAMS_ID));
                contentValues.put(KEY_PROGRAMS_DATA, object.getString(JSONNames.PROGRAMS_DATA));
                contentValues.put(KEY_PROGRAMS_TIME, object.getString(JSONNames.PROGRAMS_TIME));
                contentValues.put(KEY_PROGRAMS_TITLE, object.getString(JSONNames.PROGRAMS_TITLE));
                contentValues.put(KEY_PROGRAMS_DESCRIPTION, object.getString(JSONNames.PROGRAMS_DESCRIPTION));

                db.update(TABLE_PROGRAMS, contentValues, null, null);

            } catch (Exception e)
            {
                //TODO
            }

        }

        db.close();
    }

    //this method gives no result
    @SuppressWarnings("unused")
    public void addAllJSONProgramsToTable(List<JSONArray> programs)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues;
        int increment = 1;

        for (JSONArray array: programs)
        {
            try
            {
                for (int i=0; i<array.length(); i++)
                {
                    JSONObject object = array.getJSONObject(i);
                    contentValues = new ContentValues();

                    contentValues.put(KEY_TABLE_PROGRAMS_ID, increment);
                    contentValues.put(KEY_CHANNEL_PROGRAMS_ID, object.getInt(JSONNames.CHANNEL_PROGRAMS_ID));
                    contentValues.put(KEY_PROGRAMS_DATA, object.getString(JSONNames.PROGRAMS_DATA));
                    contentValues.put(KEY_PROGRAMS_TIME, object.getString(JSONNames.PROGRAMS_TIME));
                    contentValues.put(KEY_PROGRAMS_TITLE, object.getString(JSONNames.PROGRAMS_TITLE));
                    contentValues.put(KEY_PROGRAMS_DESCRIPTION, object.getString(JSONNames.PROGRAMS_DESCRIPTION));

                    Log.e("SQL",contentValues.toString());

                    increment++;
                    db.insert(TABLE_PROGRAMS, null, contentValues);
                }
            } catch (Exception e) //!!!! don't forget to handle an exception!!!!
            {
                //TODO
                Log.e("SQL", e.getMessage());
            }
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

    public Cursor getProgramForChannel_id_spec_data (int id, String date)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        //how to select cursor by few selection
        // found answer here
        // http://stackoverflow.com/questions/12422977/setting-multiple-selections-args-in-sqlite-database-query
        //  " AND " --- white space is important!!!
        Cursor cursor = db.query(TABLE_PROGRAMS,
                new String[] {KEY_TABLE_PROGRAMS_ID,
                        KEY_PROGRAMS_DATA,
                        KEY_PROGRAMS_TIME,
                        KEY_PROGRAMS_TITLE,
                        KEY_PROGRAMS_DESCRIPTION},
                KEY_CHANNEL_PROGRAMS_ID + "=?" + " AND " + KEY_PROGRAMS_DATA + "=?",         //selection here
                new String[] { String.valueOf(id), date },
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }

    // Getting cursor with all programs
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

