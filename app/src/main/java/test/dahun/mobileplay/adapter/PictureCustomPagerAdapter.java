package test.dahun.mobileplay.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import test.dahun.mobileplay.tab.SubPictureView;
import test.dahun.mobileplay.ui.PagerAdapter;

public class PictureCustomPagerAdapter extends PagerAdapter {

    Context context;
    public PictureCustomPagerAdapter(Context context) {
        this.context=context;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      View v = new SubPictureView(context, position);
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