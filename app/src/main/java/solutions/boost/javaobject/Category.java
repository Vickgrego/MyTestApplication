package solutions.boost.javaobject;

import android.graphics.Bitmap;

/**
 * Created on 19.01.2017.
 */
public class Category
{
    private int category_id;
    private String category_title;
    private String category_pict_url;
    private Bitmap bitmap;

    public int getCategory_id()
    {
        return category_id;
    }

    public void setCategory_id(int category_id)
    {
        this.category_id = category_id;
    }

    public String getTitle()
    {
        return category_title;
    }

    public void setTitle(String title)
    {
        this.category_title = title;
    }

    public String getCategory_pict_url()
    {
        return category_pict_url;
    }

    public void setCategory_pict_url(String category_pict_url)
    {
        this.category_pict_url = category_pict_url;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }
}
