package test.dahun.mobileplay.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import test.dahun.mobileplay.tab.FirstCoverFragment;
import test.dahun.mobileplay.tab.SecondCoverFragment;

public class CoverPagerAdapter extends FragmentStatePagerAdapter {
    public CoverPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new FirstCoverFragment();
            case 1: return new SecondCoverFragment();
            default: return null;
        }
    }
}