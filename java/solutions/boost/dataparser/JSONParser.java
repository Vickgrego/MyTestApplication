package solutions.boost.dataparser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import solutions.boost.channelstruct.Channel;
import solutions.boost.channelstruct.JSONNames;
import solutions.boost.database.DataBaseHelper;

/**
 * Created by Irene on 13.01.2017.
 * Parse JSON data, what we got from server,
 * into Java object
 * see Channel.class
 */
public class JSONParser
{
    //iterates through JSON response from server
    //creates each instance of Channel/Categories
    //and saves instance to database
    @SuppressWarnings("unused")
    public static void parseJSONtoChannelTable(JSONArray response, DataBaseHelper dataBase)
    {
        try {
            JSONArray array = response;

            //iterate
            for (int i=0; i<array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                Channel channel = new Channel();

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

                //save instance of channel to database
                dataBase.addChannelToTable(channel);
            }

        } catch (Exception e) //!!!! don't forget to handle an exception!!!!
        {
            //TODO
            e.printStackTrace();
        }
    }

}
