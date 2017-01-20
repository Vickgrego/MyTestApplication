package solutions.boost.tvprogramm;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created on 16.01.2017.
 * adapter for tabs in main activity
 * shows 3 tabs
 */
public class TabFragmentPagerAdapter extends FragmentStatePagerAdapter
{
    private final static int COUNT = 3;
    private ChannelTabFragment channelTab;
    private CategoriesTabFragment categoryTab;
    private PreferredTabFragment prefTab;

    //I need Context to get ref to string res, otherwise I have null pointer exception
    public TabFragmentPagerAdapter(FragmentManager fm, Context ctx)
    {
        super(fm);

        channelTab = new ChannelTabFragment();
        channelTab.setTitle(ctx.getString(R.string.navigation_channel_list));
        categoryTab = new CategoriesTabFragment();
        categoryTab.setTitle(ctx.getString(R.string.navigation_category_list));
        prefTab = new PreferredTabFragment();
        prefTab.setTitle(ctx.getString(R.string.navigation_preferred_list));
    }

    @Override
    public int getCount()
    {
        return COUNT;
    }

    @Override
    public Fragment getItem(int position)
    {
       switch (position)
        {
            case 0:
                return channelTab;   //iteration start from 0!!!!
            case 1:
                return categoryTab;
            case 2:
                return prefTab;
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return channelTab.getTabsTitle();
            case 1:
                return categoryTab.getTabsTitle();
            case 2:
                return prefTab.getTabsTitle();
        }

        return "Name";
    }
}