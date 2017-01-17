package solutions.boost.tvprogramm;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created on 16.01.2017.
 * adapter for tabs in main activity
 *
 */
public class TabFragmentPagerAdapter extends FragmentStatePagerAdapter
{
    private int count;

    public TabFragmentPagerAdapter(FragmentManager fm, int count)
    {
        super(fm);
        this.count = count;
    }

    @Override
    public int getCount()
    {
        return count;
    }

    @Override
    public Fragment getItem(int position)
    {
        return new TabFragment();
    }

}
