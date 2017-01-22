package solutions.boost.dataparser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import solutions.boost.javaobject.Program;

/**
 * Created on 13.01.2017.
 * Parse JSON data, what we got from server,
 * into Java object
 * see Channel.class
 * !------------------------- I don't use this class anymore-----------------------!!!
 */
public class JSONParser
{
    private static String TAG = "PARSER";

    //iterates through JSON response from server
    //creates each instance of Channel/Categories
    //and saves instance to database
    @SuppressWarnings("unused")
    /*public static void parseJSONtoChannelTable(JSONArray response, DataBaseHelper dataBase)
    {
        try {
            //create array with size equals to JSONarray
            List<Channel> channels = new ArrayList<>();
            Channel channel;

            //iterate
            for (int i=0; i<response.length(); i++)
            {
                JSONObject object = response.getJSONObject(i);
                channel = new Channel();

                //channel's id int
                channel.setId(object.getInt(JSONNames.ID));
                //channel's string name
                channel.setName(object.getString(JSONNames.NAME));
                //channel's string url
                channel.setUrl(object.getString(JSONNames.URL));
                //channel's string picture
                channel.setPicture(object.getString(JSONNames.PICT_URL));
                //channel's int category_id
                channel.setCategory_id(object.getInt(JSONNames.CHANNEL_CATEGORY_ID));

                //channels.add(channel);
                channels.add(channel);
            }

            //save instance of channel to database
           dataBase.addAllChannelsToTable(channels);

        } catch (Exception e) //!!!! don't forget to handle an exception!!!!
        {
            //TODO
            e.printStackTrace();
        }
    }*/

    /*public static void parseJSONtoCategoriesTable(JSONArray response, DataBaseHelper dataBase)
    {
        try {
            //create array with size equals to JSONarray
            List<Category> ctgs = new ArrayList<>();
            Category ctg;

            //iterate
            for (int i=0; i<response.length(); i++)
            {
                JSONObject object = response.getJSONObject(i);
                ctg = new Category();

                //channel's id int
                ctg.setCategory_id(object.getInt(JSONNames.ID_CATEGORY));
                //channel's string name
                ctg.setTitle(object.getString(JSONNames.CATEGORY_TITLE));
                //channel's string url
                ctg.setCategory_pict_url(object.getString(JSONNames.CATEGORY_PICT_URL));
                //channel's string picture

                //channels.add(channel);
                ctgs.add(ctg);
            }

            //save instance of channel to database
            //dataBase.addAllCategoriesToTable(ctgs);

        } catch (Exception e) //!!!! don't forget to handle an exception!!!!
        {
            //TODO
            e.printStackTrace();
        }
    }*/

   /* public static void parseJSONtoProgramsTable(JSONArray response, DataBaseHelper dataBase)
    {
        try {
            //create array with size equals to JSONarray
            List<Program> programs = new ArrayList<>();
            Program prog;

            //iterate
            for (int i=0; i<response.length(); i++)
            {
                JSONObject object = response.getJSONObject(i);
                prog = new Program();

                //channel's id int
                prog.setChannel_progr_id(object.getInt(JSONNames.CHANNEL_PROGRAMS_ID));
                //channel's string name
                prog.setProgr_data(object.getString(JSONNames.PROGRAMS_DATA));
                //channel's string url
                prog.setProgr_time(object.getString(JSONNames.PROGRAMS_TIME));
                //channel's string picture
                prog.setProgr_title(object.getString(JSONNames.PROGRAMS_TITLE));
                //channel's int category_id
                prog.setProgr_descript(object.getString(JSONNames.PROGRAMS_DESCRIPTION));

                //channels.add(channel);
                programs.add(prog);
                Log.e(TAG, "OK");
            }

            //save instance of programs to database
            dataBase.addAllProgramsToTable(programs);

        } catch (Exception e) //!!!! don't forget to handle an exception!!!!
        {
            //TODO
            Log.e(TAG, e.getMessage());
        }
    }*/

    public static List <Program> parseJSONtoProgram (JSONArray response)
    {
        try {
            List<Program> list = new ArrayList<>();
            Program prog;

            //iterate
            for (int i=0; i<response.length(); i++)
            {
                JSONObject object = response.getJSONObject(i);
                prog = new Program();

                //channel's id int
                prog.setChannel_progr_id(object.getInt(JSONNames.CHANNEL_PROGRAMS_ID));
                //channel's string name
                prog.setProgr_data(object.getString(JSONNames.PROGRAMS_DATA));
                //channel's string url
                prog.setProgr_time(object.getString(JSONNames.PROGRAMS_TIME));
                //channel's string picture
                prog.setProgr_title(object.getString(JSONNames.PROGRAMS_TITLE));
                //channel's int category_id
                prog.setProgr_descript(object.getString(JSONNames.PROGRAMS_DESCRIPTION));

                //channels.add(channel);
                list.add(prog);
                Log.e(TAG, "OK");
            }

            return list;

        } catch (Exception e) //!!!! don't forget to handle an exception!!!!
        {
            //TODO
            Log.e(TAG, e.getMessage());
        }

        return null;
    }

}
