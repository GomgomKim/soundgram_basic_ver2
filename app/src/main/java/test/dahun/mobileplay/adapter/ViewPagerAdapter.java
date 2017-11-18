package test.dahun.mobileplay.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import test.dahun.mobileplay.R;
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

    Context context;

    public ViewPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);

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