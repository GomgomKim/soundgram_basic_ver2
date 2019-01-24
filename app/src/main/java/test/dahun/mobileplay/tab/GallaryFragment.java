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
import test.dahun.mobileplay.model.ApplicationStatus;

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
            Glide.with(getContext()).load(R.drawable.mn_play_off).into(imageViewTarget);

        gallaryListAdapter = new GallaryListAdapter(getContext());
        gallaryListAdapter.addItem(0, "검정치마 '조휴일'", R.drawable.galleryimg_01);
        gallaryListAdapter.addItem(1, "한국대중음악상이 사랑한 인디밴드", R.drawable.galleryimg_02, "aCj1Igctb8s");
//        gallaryListAdapter.addItem(0, "음반작업 현장", R.drawable.galleryimg_03);
        gallary_list.setAdapter(gallaryListAdapter);
    }

    public void resizeLayout(){
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        float context_height = pxToDp(dm.heightPixels);
        final int layout_height = dpToPx(context_height - 132);
        gallary_list.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams position = new LinearLayout.LayoutParams(
                        gallary_list.getWidth(), layout_height
                );
                gallary_list.setLayoutParams(position);
            }
        });

    }

    //dp를 px로
    public int dpToPx(float dp){
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
        return px;
    }

    //px를 dp로
    public float pxToDp(float px){
        float density = getContext().getResources().getDisplayMetrics().density;

        if(density == 1.0) density *= 4.0;
        else if(density == 1.5) density *= (8/3);
        else if(density == 2.0) density *= 2.0;

        float dp = px/density;
        return dp;
    }

    public void btnSetting(){
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewPagerTabListener.setTab(0);
            }
        });
        list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewPagerTabListener.setTab(1);
            }
        });
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewPagerTabListener.setTab(2);
            }
        });
        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewPagerTabListener.setTab(3);
            }
        });
        sns_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewPagerTabListener.setTab(4);
            }
        });
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
                    Glide.with(getContext()).load(R.drawable.mn_play_off).into(imageViewTarget);
                    return;
                }
            }
            return;
        }
        else
            Log.d("SetUserHint","Cover OFF");
    }



}
