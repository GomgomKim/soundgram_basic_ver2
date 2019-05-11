package test.dahun.mobileplay.adapter;

import android.content.Context;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import test.dahun.mobileplay.R;
import test.dahun.mobileplay.events.EndFragEvent;
import test.dahun.mobileplay.events.PositionEvent;
import test.dahun.mobileplay.events.SelectSongEvent;
import test.dahun.mobileplay.interfaces.HeartNumInterface;
import test.dahun.mobileplay.main.MainActivity;
import test.dahun.mobileplay.services.BusProvider;
import test.dahun.mobileplay.tab.ListFragment;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static test.dahun.mobileplay.adapter.ViewPagerAdapter.setViewPagerTabListener;

public class PlayListAdapter extends BaseAdapter {
    LayoutInflater inflater;

    private ArrayList<PlayListItem> mItems = new ArrayList<>();
    private LinearLayout heart_touch_area;
    private LinearLayout song_touch_area;
    private TextView index;
    private ImageView heart;
    private TextView heart_num;
    private ImageView title_img;
    private TextView title;
    private TextView singer;
    PlayListItem current_item;

    private TextView intro_title;
    private TextView intro_content;

    ListFragment listFragment;

    public PlayListAdapter(Context context, ListFragment listFragment){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listFragment = listFragment;
        BusProvider.getInstance().register(listFragment.getContext());
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public PlayListItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 뷰 종류
    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

    // 뷰 여러개 붙이기
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        current_item = getItem(position);
        int res = 0;

        if(convertView == null){
            res = getItemViewType(position);
            switch (res){
                case 0:
                    res = R.layout.play_lists;
                    break;
                case 1:
                    res = R.layout.play_lists_bottom;
                    break;
            }

            convertView = inflater.inflate(res, parent, false);
        }

        res = getItemViewType(position);
        switch (res){
            case 0: // 곡 목록
                index = (TextView) convertView.findViewById(R.id.index);
                heart_touch_area = (LinearLayout) convertView.findViewById(R.id.heart_touch_area);
                song_touch_area = (LinearLayout) convertView.findViewById(R.id.song_touch_area);
                heart = (ImageView) convertView.findViewById(R.id.heart);
                heart_num = (TextView) convertView.findViewById(R.id.heart_num);
                title_img = (ImageView) convertView.findViewById(R.id.title_img);
                title = (TextView) convertView.findViewById(R.id.title);
                singer = (TextView) convertView.findViewById(R.id.singer);

                if(current_item != null){
                    index.setText(current_item.getIndex());
//                    heart.setBackgroundResource(current_item.getHeart());
                    if(HeartNumInterface.getIsHeart(position) == 0) heart.setBackgroundResource(R.drawable.like_off);
                    else if(HeartNumInterface.getIsHeart(position) == 1) heart.setBackgroundResource(R.drawable.like_on);
                    heart_num.setText(setHeartNum(HeartNumInterface.getHeartNum(position)));
//                    heart_num.setText(setHeartNum(current_item.getHeart_num()));
                    title.setText(current_item.getTitle());
                    singer.setText(current_item.getSinger());

                    //set Image
                    title_img.setImageResource(R.drawable.bl_title);

                    if(current_item.getIsTitle() == 1){
                        title_img.setVisibility(View.VISIBLE);
                    } else if(current_item.getIsTitle() == 0){
                        title_img.setVisibility(View.GONE);
                    }

                    // 하트 터치
                    heart_touch_area.setOnClickListener(view -> {
                        ConnectivityManager connectivityManager = (ConnectivityManager) listFragment.getActivity().getSystemService(CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                        if(networkInfo != null && networkInfo.isConnected()){

                            int is_heart = mItems.get(position).getHeart();
                            if(is_heart == R.drawable.like_off){
                                mItems.get(position).setHeart(R.drawable.like_on);
                                mItems.get(position).setHeart_num(mItems.get(position).getHeart_num()+1);
                                HeartNumInterface.setHeartNum(position, mItems.get(position).getHeart_num());
                                HeartNumInterface.setIsHeart(position, 1);
//                            listFragment.viewGif();
                            } else if (is_heart == R.drawable.like_on) {
                                mItems.get(position).setHeart(R.drawable.like_off);
                                mItems.get(position).setHeart_num(mItems.get(position).getHeart_num()-1);
                                HeartNumInterface.setHeartNum(position, mItems.get(position).getHeart_num());
                                HeartNumInterface.setIsHeart(position, 0);
                            }

                        } else{
                            // alert
                            AlertDialog.Builder alert = new AlertDialog.Builder(listFragment.getContext());
                            alert.setPositiveButton("확인", (dialog, which) -> dialog.dismiss());
                            alert.setMessage("네트워크가 연결되지 않았습니다. Wi-Fi 또는 데이터를 활성화 해주세요.");
                            alert.show();
                        }

                        notifyDataSetChanged();
                    });

                    // 곡명 터치
                    song_touch_area.setOnClickListener(view -> {
                        MainActivity.setPosition(position);
                        MainActivity.setState(0);
                        setViewPagerTabListener.setTab(2);

//                        BusProvider.getInstance().post(new SelectSongEvent(position));
                    });

                }
                break;
            case 1: // 앨범 소개
                intro_title = (TextView) convertView.findViewById(R.id.intro_title);
                intro_content = (TextView) convertView.findViewById(R.id.intro_content);
                intro_title.setText(current_item.getIntro_title());
                intro_content.setText(current_item.getIntro_content());
                break;
        }


        return convertView;
    }

    // 하트갯수 수정
    public String setHeartNum(int current_like_count){
        String heart_count = "";
        if(current_like_count >= 1000)  {
            int thousand = current_like_count/1000;
            int rest = current_like_count - thousand*1000;
            heart_count = (current_like_count/1000)+"."+(rest/100)+"k";
        }
        else heart_count = String.valueOf(current_like_count);
        return heart_count;
    }

    public void addItem(int type, int index, int heart, int heart_num, String title, String singer, int isTitle) {
        PlayListItem mItem = new PlayListItem();

        mItem.setType(type);
        mItem.setIndex(String.format("%02d", index));
        mItem.setHeart(heart);
        mItem.setHeart_num(heart_num);
        mItem.setTitle(title);
        mItem.setSinger(singer);
        mItem.setIsTitle(isTitle);

        mItems.add(mItem);

    }

    public void addItem(int type, String title, String content){
        PlayListItem mItem = new PlayListItem();

        mItem.setType(type);
        mItem.setIntro_title(title);
        mItem.setIntro_content(content);

        mItems.add(mItem);
    }
}
