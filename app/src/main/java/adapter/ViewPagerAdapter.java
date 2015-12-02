package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fragment.*;

/**
 * Created by kelsiedong on 12/2/15.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    String[] title = {"Avg", "Friend-liness", "Skill", "Team-work", "Fun-factor"};
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        // Returns the number of tabs
        return title.length;
    }

    @Override
    public Fragment getItem(int position) {
        // Returns a new instance of the fragment
        switch (position) {
            case 0:
                TopAvg.type = 0;
                return new TopAvg();
            case 1:
                TopAvg.type = 1;
                return new TopAvg();
            case 2:
                TopAvg.type = 2;
                return new TopAvg();
            case 3:
                TopAvg.type = 3;
                return new TopAvg();
            case 4:
                TopAvg.type = 4;
                return new TopAvg();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
