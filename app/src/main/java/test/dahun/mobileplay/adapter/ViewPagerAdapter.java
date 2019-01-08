package test.dahun.mobileplay.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import test.dahun.mobileplay.main.MainActivity;
import test.dahun.mobileplay.tab.CoverFragment;
import test.dahun.mobileplay.tab.GallaryFragment;
import test.dahun.mobileplay.tab.ListFragment;
import test.dahun.mobileplay.tab.MusicFragment;
import test.dahun.mobileplay.tab.ThanksToFragment;

//Pager Adapter
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public static MainActivity.SetViewPagerTabListener setViewPagerTabListener;
    public ViewPagerAdapter(FragmentManager fragmentManager, MainActivity.SetViewPagerTabListener setViewPagerTabListener){
        super(fragmentManager);
        this.setViewPagerTabListener=setViewPagerTabListener;
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
                return new ListFragment();
            case 2:
                return new MusicFragment();
            case 3:
                return new GallaryFragment();
            case 4:
                return new ThanksToFragment();
            default:
                return null;
        }
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return 5;
    }


}