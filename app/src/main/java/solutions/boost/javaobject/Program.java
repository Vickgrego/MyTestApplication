package solutions.boost.javaobject;

/**
 * Created on 19.01.2017.
 */
public class Program
{
    private int channel_progr_id;
    private String progr_data;
    private String progr_time;
    private String progr_title;
    private String progr_descript;

    public Program(){}

    public Program(int channel_progr_id, String progr_data, String progr_time, String progr_title, String progr_descript)
    {
        this.channel_progr_id = channel_progr_id;
        this.progr_data = progr_data;
        this.progr_time = progr_time;
        this.progr_title = progr_title;
        this.progr_descript = progr_descript;
    }

    public int getChannel_progr_id()
    {
        return channel_progr_id;
    }

    public void setChannel_progr_id(int channel_progr_id)
    {
        this.channel_progr_id = channel_progr_id;
    }

    public String getProgr_data()
    {
        return progr_data;
    }

    public void setProgr_data(String progr_data)
    {
        this.progr_data = progr_data;
    }

    public String getProgr_time()
    {
        return progr_time;
    }

    public void setProgr_time(String progr_time)
    {
        this.progr_time = progr_time;
    }

    public String getProgr_title()
    {
        return progr_title;
    }

    public void setProgr_title(String progr_title)
    {
        this.progr_title = progr_title;
    }

    public String getProgr_descript()
    {
        return progr_descript;
    }

    public void setProgr_descript(String progr_descript)
    {
        this.progr_descript = progr_descript;
    }
}
