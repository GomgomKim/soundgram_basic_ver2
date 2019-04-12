package test.dahun.mobileplay.tab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.GallaryListAdapter;
import test.dahun.mobileplay.interfaces.ApplicationStatus;
import test.dahun.mobileplay.interfaces.ButtonInterface;

import static test.dahun.mobileplay.adapter.ViewPagerAdapter.setViewPagerTabListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class GallaryFragment extends Fragment {

    RelativeLayout layout = null;

    @BindView(R.id.gallary_list) ListView gallary_list;

    GallaryListAdapter gallaryListAdapter;


    public GallaryFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_gallary, container, false);
        ButterKnife.bind(this, layout);
        initSetting();
        //resizeLayout();
        return layout;
    }

    public void initSetting(){
        gallaryListAdapter = new GallaryListAdapter(getContext());
        gallaryListAdapter.addItem(1, "오빠야 M/V", R.drawable.video1, "OmjN_b07syo");
        gallaryListAdapter.addItem(1, "캡송 M/V", R.drawable.video1, "Zjh6mkkZsMo");
        gallaryListAdapter.addItem(1, "집 비던 날 M/V", R.drawable.video1, "BTsy1VWx6mM");
        gallaryListAdapter.addItem(1, "캡송 공연 (델리스파이스 부산 콘서트 게스트)", R.drawable.video1, "TtpYuDjfBV0");
        gallaryListAdapter.addItem(0, "", R.drawable.gallary1);
        gallaryListAdapter.addItem(0, "", R.drawable.gallary2);
        gallaryListAdapter.addItem(0, "", R.drawable.gallary3);
        gallaryListAdapter.addItem(0, "", R.drawable.gallary4);
        gallaryListAdapter.addItem(0, "", R.drawable.gallary5);
        gallaryListAdapter.addItem(0, "", R.drawable.gallary6);
        gallaryListAdapter.addItem(0, "", R.drawable.gallary7);
        gallaryListAdapter.addItem(0, "", R.drawable.gallary8);
        gallaryListAdapter.addItem(0, "", R.drawable.gallary9);
        gallaryListAdapter.addItem(0, "", R.drawable.gallary10);
        gallaryListAdapter.addItem(0, "", R.drawable.gallary11);
        gallaryListAdapter.addItem(0, "", R.drawable.gallary12);
        gallary_list.setAdapter(gallaryListAdapter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){ // 유저가 화면을 보고있을 때
            if(this.layout != null){
                ((ButtonInterface)getContext()).reset();
                ((ButtonInterface)getContext()).galleryOn();
            }
            return;
        }
        else
            Log.d("SetUserHint","Cover OFF");
    }



}
