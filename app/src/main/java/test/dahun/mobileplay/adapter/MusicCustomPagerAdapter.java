package test.dahun.mobileplay.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import test.dahun.mobileplay.tab.SubMusicView;
import test.dahun.mobileplay.ui.PagerAdapter;

public class MusicCustomPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<Integer> album_arr;

    public MusicCustomPagerAdapter(Context context, ArrayList<Integer> arr) {
        this.context=context;
        album_arr = arr;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      View v = new SubMusicView(context, position, album_arr);
      container.addView(v);
      return v;
    }

    @Override
    public void destroyItem(ViewGroup container, final int position, Object object) {
      container.removeView((View) object);
    }

    @Override
    public int getCount() {
      return 6;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
      return arg0 == arg1;
    }




  }