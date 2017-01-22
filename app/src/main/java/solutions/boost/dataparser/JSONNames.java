package solutions.boost.dataparser;

/**
 * Created by Irene on 13.01.2017.
 * saves JSON names
 */
public class JSONNames
{
    //http://52.50.138.211:8080/ChanelAPI/chanels - channel list
    public static final String ID = "id";
    public  static final String NAME = "name";
    public  static final String URL = "url";
    public  static final String PICT_URL = "picture";
    public  static final String CHANNEL_CATEGORY_ID = "category_id";

    // http://52.50.138.211:8080/ChanelAPI/categories - category list
    public static final String ID_CATEGORY = "id";
    public static final String CATEGORY_TITLE = "title";
    public static final String CATEGORY_PICT_URL = "picture";

    //http://52.50.138.211:8080/ChanelAPI/programs/
    public static final String CHANNEL_PROGRAMS_ID = "channel_id";
    public static final String PROGRAMS_DATA = "date";
    public static final String PROGRAMS_TIME = "time";
    public static final String PROGRAMS_TITLE = "title";
    public static final String PROGRAMS_DESCRIPTION = "description";

}
