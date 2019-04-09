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

import static test.dahun.mobileplay.adapter.ViewPagerAdapter.setViewPagerTabListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class GallaryFragment extends Fragment {

    RelativeLayout layout = null;

    @BindView(R.id.gallary_list) ListView gallary_list;

    @BindView(R.id.home_btn) ImageButton home_btn;
    @BindView(R.id.list_btn) ImageButton list_btn;
    @BindView(R.id.play_btn) ImageButton play_btn;
    @BindView(R.id.gallery_btn) ImageButton gallery_btn;
    @BindView(R.id.sns_btn) ImageButton sns_btn;

    GallaryListAdapter gallaryListAdapter;


    public GallaryFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_gallary, container, false);
        ButterKnife.bind(this, layout);
        initSetting();
        //resizeLayout();
        btnSetting();
        return layout;
    }

    public void initSetting(){
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(play_btn);
        if(ApplicationStatus.isPlaying)
            Glide.with(getContext()).load(R.raw.mn_equalizer).into(imageViewTarget);
        else
            Glide.with(getContext()).load(R.drawable.mn_play_off2).into(imageViewTarget);

        gallaryListAdapter = new GallaryListAdapter(getContext());
        gallaryListAdapter.addItem(0, "신현희와김루트 싸우지 말자 '작업일기'", R.drawable.gallary1);
        gallaryListAdapter.addItem(1, "[M/V] 신현희와김루트 '오빠야'", R.drawable.video1, "OmjN_b07syo");
//        gallaryListAdapter.addItem(0, "음반작업 현장", R.drawable.galleryimg_03);
        gallary_list.setAdapter(gallaryListAdapter);
    }

    public void btnSetting(){
        home_btn.setOnClickListener(view -> setViewPagerTabListener.setTab(0));
        list_btn.setOnClickListener(view -> setViewPagerTabListener.setTab(1));
        play_btn.setOnClickListener(view -> setViewPagerTabListener.setTab(2));
        gallery_btn.setOnClickListener(view -> setViewPagerTabListener.setTab(3));
        sns_btn.setOnClickListener(view -> setViewPagerTabListener.setTab(4));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){ // 유저가 화면을 보고있을 때
            if(this.layout != null){
                DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(play_btn);
                if(ApplicationStatus.isPlaying){
                    Glide.with(getContext()).load(R.raw.mn_equalizer).into(imageViewTarget);
                    return;
                }
                else{
                    Glide.with(getContext()).load(R.drawable.mn_play_off2).into(imageViewTarget);
                    return;
                }
            }
            return;
        }
        else
            Log.d("SetUserHint","Cover OFF");
    }



}
