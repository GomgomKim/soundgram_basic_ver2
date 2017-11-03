package test.dahun.mobileplay.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import test.dahun.mobileplay.tab.CommentFragment;
import test.dahun.mobileplay.tab.CoverFragment;
import test.dahun.mobileplay.tab.MusicFragment;
import test.dahun.mobileplay.tab.PictureFragment;
import test.dahun.mobileplay.tab.VideoFragment;


/**
 * Created by jeongdahun on 2017. 7. 13..
 */

//Pager Adapter
public class ViewPagerAdapter extends FragmentStatePagerAdapter
{

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {


        switch(position){
            case 0:
                return new CoverFragment();
            case 1:
                return new MusicFragment();
            case 2:
                return new VideoFragment();
            case 3:
                return new PictureFragment();
            case 4:
                return new CommentFragment();

            default:
                return null;
        }

    }
    @Override
    public int getCount() {
        return 5;
    }


}