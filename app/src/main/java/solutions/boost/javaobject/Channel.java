package solutions.boost.javaobject;

import android.graphics.Bitmap;

/**
 * Created by Irene on 13.01.2017.
 * This class is responsible to contain a json info
 * and provide it into Java object
 */
public class Channel
{
    private int id;
    private String name;
    private String url;
    private String picture;
    private int category_id;
    private Bitmap channelPict;
    private int preferred;

    public Channel(){}

    public Channel(int id, String name, String url, String picture, int category_id, int pref)
    {
        this.id = id;
        this.name = name;
        this.url = url;
        this.picture = picture;
        this.category_id = category_id;
        this.preferred = pref;
    }

    public Channel (int id, String name, String url, String picture, int category_id, Bitmap pict)
    {
        this.id = id;
        this.name = name;
        this.url = url;
        this.picture = picture;
        this.category_id = category_id;
        this.channelPict = pict;
    }


    //<!-----------GETTER and SETTER------------------!>
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getPicture()
    {
        return picture;
    }

    public void setPicture(String picture)
    {
        this.picture = picture;
    }

    public int getCategory_id()
    {
        return category_id;
    }

    public void setCategory_id(int category_id)
    {
        this.category_id = category_id;
    }

    public Bitmap getChannelPict()
    {
        return channelPict;
    }

    public void setChannelPict(Bitmap channelPict)
    {
        this.channelPict = channelPict;
    }

    public int getPreferredKey()
    {
        return preferred;
    }

    public void setPreferredKey(int key)
    {
        preferred = key;
    }

}
