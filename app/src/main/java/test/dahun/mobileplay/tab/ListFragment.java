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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.PlayListAdapter;
import test.dahun.mobileplay.model.ApplicationStatus;

import static test.dahun.mobileplay.adapter.ViewPagerAdapter.setViewPagerTabListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    @BindView(R.id.play_list) ListView play_list;

    @BindView(R.id.home_btn) ImageButton home_btn;
    @BindView(R.id.list_btn) ImageButton list_btn;
    @BindView(R.id.play_btn) ImageButton play_btn;
    @BindView(R.id.gallery_btn) ImageButton gallery_btn;
    @BindView(R.id.sns_btn) ImageButton sns_btn;

    RelativeLayout layout = null;
    PlayListAdapter playListAdapter;

    // 임시저장
    ArrayList<String> titles;
    ArrayList<Integer> heart_nums;

    public ListFragment() {
        super();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, layout);

        // 임시저장
        makeData();

        initSetting();
        btnSetting();
        //resizeLayout();
        return layout;
    }

    public void initSetting(){
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(play_btn);
        if(ApplicationStatus.isPlaying)
            Glide.with(getContext()).load(R.raw.mn_equalizer).into(imageViewTarget);
        else
            Glide.with(getContext()).load(R.drawable.mn_play_off).into(imageViewTarget);

        playListAdapter = new PlayListAdapter(getContext());
        for(int i=0; i<titles.size(); i++){
            if ( i == 0 )
                playListAdapter.addItem(0, i+1, R.drawable.like_off, heart_nums.get(i), titles.get(i), "검정치마", 1);
            else
                playListAdapter.addItem(0, i+1, R.drawable.like_off, heart_nums.get(i), titles.get(i), "검정치마", 0);
        }
        playListAdapter.addItem(1, "안녕하세요 앤츠(Ants) 입니다.", "컨텐츠...");
        play_list.setAdapter(playListAdapter);
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

    public void resizeLayout(){
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        float context_height = pxToDp(dm.heightPixels);
        final int layout_height = dpToPx(context_height - 276);
        play_list.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams position = new LinearLayout.LayoutParams(
                        play_list.getWidth(), layout_height
                );
                play_list.setLayoutParams(position);
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

    // 임시저장
    public void makeData(){
        titles = new ArrayList<>();
        heart_nums = new ArrayList<>();
        titles.add("Big Love");      heart_nums.add(13);
        titles.add("좋아해줘");      heart_nums.add(1789);
        titles.add("Dientes");       heart_nums.add(542);
        titles.add("Stand Still");   heart_nums.add(486);
        titles.add("상아");           heart_nums.add(992);
        titles.add("강아지");         heart_nums.add(96);
        titles.add("Antifreeze");    heart_nums.add(9);
        titles.add("Kiss And Tell"); heart_nums.add(75);
        titles.add("LE Fou Muet");   heart_nums.add(123);
        titles.add("Diamond");       heart_nums.add(47);
        titles.add("난 아니에요");    heart_nums.add(7);
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
