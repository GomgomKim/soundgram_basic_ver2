
package test.dahun.mobileplay.tab;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.CoverCustomPagerAdapter;
import test.dahun.mobileplay.adapter.ViewPagerAdapter;
import test.dahun.mobileplay.ui.VerticalViewPager;

/**
 * Created by jeongdahun on 2017. 9. 11..
 */

public class CoverFragment extends Fragment
{
   // @BindView(R.id.mainImage) ImageView mainImage;
    @BindView(R.id.maingBottomImage) ImageView mainBottomImage;
    @BindView(R.id.cover_text) TextView maintext;
    @BindView(R.id.cover_top) ImageView mainTopImage;
    @BindView(R.id.ic_equalizerBtn) Button ic_equalizerBtn;


    @BindView(R.id.coverpager)
    VerticalViewPager coverPager;

    //
  //  @BindView(R.id.handle) Button SlidingButton;
  //  @BindView(R.id.slidingDrawer1) SlidingDrawer slidingdrawer;
    boolean state= true;//커버

    final String TAG="CoverFragment";
    LinearLayout layout;

    public CoverFragment() {
        super();
    }


    //
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_cover, container, false);

        ButterKnife.bind(this, layout);

        initSetting();

        return layout;
    }

    public void initSetting() {
       // Glide.with(getContext()).load(R.drawable.main_1_bg).into(mainImage);
       // Glide.with(getContext()).load(R.drawable.bg_main_bottom).into(mainBottomImage);
       // Glide.with(getContext()).load(R.drawable.main_3_info).into(mainTopImage);

       // mainImage.setImageResource(R.drawable.main_1_bg);
        ic_equalizerBtn.setBackgroundResource(R.drawable.ic_equalizer);

        //equalbtn
        ic_equalizerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPagerAdapter.setViewPagerTabListener.setTab(1);
            }
        });
        //
        String txt ="문문 (MoonMoon)\n" +
                "[LIFE IS BEAUTY FULL]\n" +
                "\n" +
                "MoonMoon입니다.\n" +
                "\n" +
                "노래를 만드는 일은 밤을 채워주기 때문에 좋아합니다.\n" +
                "나의 알뜰히 적어내린 이야기로\n" +
                "당신의 밤이 가난하지 않기를 이불같길.\n" +
                "\n" +
                "2016년 가을.\n" +
                "\n" +
                "‘비행운’가사 중 ‘나는 자라 겨우 내가 되겠지’는 소설\n" +
                "‘비행운’의 일부를 인용 하였습니다.";
        maintext.setText(txt);

        //
        coverPager.setAdapter(new CoverCustomPagerAdapter(getContext()));

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
            Log.d("SetUserHint","Cover ON");
        else
            Log.d("SetUserHint","Cover OFF");


    }

}