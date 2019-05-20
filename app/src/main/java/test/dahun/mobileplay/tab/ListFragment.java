package test.dahun.mobileplay.tab;


import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.squareup.otto.Subscribe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dahun.mobileplay.R;
import test.dahun.mobileplay.adapter.PlayListAdapter;
import test.dahun.mobileplay.database.DBOpenHelper;
import test.dahun.mobileplay.database.SongData;
import test.dahun.mobileplay.events.AddLikeFinishEvent;
import test.dahun.mobileplay.events.SongInfoEvent;
import test.dahun.mobileplay.interfaces.ButtonInterface;
import test.dahun.mobileplay.interfaces.GetSongDataInterface;
import test.dahun.mobileplay.interfaces.HeartNumInterface;
import test.dahun.mobileplay.services.BusProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    @BindView(R.id.like_gif) ImageView like_gif;

    @BindView(R.id.play_list) ListView play_list;

    @BindView(R.id.list_bg) ImageView list_bg;

    RelativeLayout layout = null;
    PlayListAdapter playListAdapter;

    // 임시저장
    ArrayList<String> titles;
    ArrayList<Integer> heart_nums;

    // DB 정보 저장
    ArrayList<SongData> song_data_DB;

    // 하트 눌렀는지
    ArrayList<Integer> is_like_arr;

    public ListFragment() {
        super();
    }

    public void eventBus(){
        BusProvider.getInstance().register(this);
    }

    // DB곡 정보 이벤트
    @Subscribe
    public void FinishLoad(SongInfoEvent mEvent) {
        // 사용자가 보고있는 위치 저장
        int index = play_list.getFirstVisiblePosition();
        View v = play_list.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - play_list.getPaddingTop());

        // 새로 세팅
        is_like_arr.removeAll(is_like_arr);

        song_data_DB = new ArrayList<>();
        song_data_DB = mEvent.getSong_arr();

        Log.i("is_likeSQLTest", "loaded");
        is_like_arr.addAll(((GetSongDataInterface)getContext()).getIsLike());
        adapterSetting();

        // 저장한 위치 불러오기
        play_list.setSelectionFromTop(index, top);
    }

    // like add 후 DB내용에따라 업데이트
    @Subscribe
    public void FinishLoad(AddLikeFinishEvent mEvent) {
        ((GetSongDataInterface)getContext()).getSongdata();
    }


    @Override
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, layout);
        eventBus();
        initSetting();

        // 임시저장
        makeData();
        return layout;
    }

    public void initSetting(){
        Glide.with(getContext()).load(R.drawable.list_bg).into(list_bg);

        is_like_arr = new ArrayList<>();
    }

    public void adapterSetting(){
        Log.i("is_likeSQLTest", "adapter");

        playListAdapter = new PlayListAdapter(getContext(), this);
        for(int i=0; i<titles.size(); i++){
            if ( i == 1 ){
                if(is_like_arr.get(i) == 0) playListAdapter.addItem(0, i+1, R.drawable.like_off, song_data_DB.get(i).getLikeCount(), titles.get(i), "신현희와김루트", 1, song_data_DB.get(i).getSong_id());
                else if(is_like_arr.get(i) == 1) playListAdapter.addItem(0, i+1, R.drawable.like_on, song_data_DB.get(i).getLikeCount(), titles.get(i), "신현희와김루트", 1, song_data_DB.get(i).getSong_id());
            }
            else{
                if(is_like_arr.get(i) == 0) playListAdapter.addItem(0, i+1, R.drawable.like_off, song_data_DB.get(i).getLikeCount(), titles.get(i), "신현희와김루트", 0, song_data_DB.get(i).getSong_id());
                else if(is_like_arr.get(i) == 1) playListAdapter.addItem(0, i+1, R.drawable.like_on, song_data_DB.get(i).getLikeCount(), titles.get(i), "신현희와김루트", 0, song_data_DB.get(i).getSong_id());
            }
        }
        play_list.setAdapter(playListAdapter);
//        playListAdapter.notifyDataSetChanged();
    }



    // 임시저장
    public void makeData(){
        titles = new ArrayList<>();
        heart_nums = new ArrayList<>();
        titles.add("신현희와김루트");
        titles.add("오빠야");
        titles.add("Cap Song");
        titles.add("집 비던날");
        titles.add("편한노래");
        titles.add("날개");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){ // 유저가 화면을 보고있을 때
            if(this.layout != null){
                ((ButtonInterface)getContext()).reset();
                ((ButtonInterface)getContext()).listOn();

                if(playListAdapter != null) playListAdapter.notifyDataSetChanged();
            }
            return;
        }
        else
            Log.d("SetUserHint","Cover OFF");
    }



}
