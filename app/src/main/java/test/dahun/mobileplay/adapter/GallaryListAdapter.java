package test.dahun.mobileplay.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import org.w3c.dom.Text;

import java.util.ArrayList;

import test.dahun.mobileplay.R;
import test.dahun.mobileplay.tab.VideoActivity;

public class GallaryListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;

    private ArrayList<GallaryListItem> mItems = new ArrayList<>();
    private ImageButton gallary_img;
    private TextView gallary_img_title;

    GallaryListItem current_item;

    private FrameLayout gallary_video_img;
    private ImageButton gallary_video_play;
    private TextView gallary_video_title;

    public GallaryListAdapter(Context context){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public GallaryListItem getItem(int position) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        current_item = getItem(position);
        int res = 0;

        if(convertView == null){
            res = getItemViewType(position);
            switch (res){
                case 0:
                    res = R.layout.gallary_img;
                    break;
                case 1:
                    res = R.layout.gallary_video;
                    break;
            }
            convertView = inflater.inflate(res, parent, false);
        }

        res = getItemViewType(position);
        switch (res){
            case 0: // 사진
                gallary_img = (ImageButton) convertView.findViewById(R.id.gallary_img);
                gallary_img_title = (TextView) convertView.findViewById(R.id.gallary_img_title);

                if(current_item != null){
                    gallary_img.setBackgroundResource(current_item.getImg_resource());
                    gallary_img_title.setText(current_item.getTitle());
                }

                // 이미지 팝업
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View popupView = inflater.inflate(R.layout.popup_gallary_img, null);
                final PopupWindow popup = new PopupWindow(popupView,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        true);

                final String popup_title = current_item.getTitle();
                final int img_resource = current_item.getImg_resource();



                gallary_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 팝업 윈도우 생성
                        popup.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                        // elements 불러오기
                        final RelativeLayout top_layout = (RelativeLayout) popupView.findViewById(R.id.top_layout);
                        final RelativeLayout bottom_layout = (RelativeLayout) popupView.findViewById(R.id.bottom_layout);
                        TextView popup_text = (TextView) popupView.findViewById(R.id.popup_text);
                        ImageButton btn_close = (ImageButton) popupView.findViewById(R.id.btn_close);
                        PhotoView popup_img = (PhotoView) popupView.findViewById(R.id.popup_img);

                        // title 넣기
                        popup_text.setText(popup_title);

                        // 이미지 넣기
                        popup_img.setBackgroundResource(img_resource);

                        // title 사라지기
                        popupView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(top_layout.getVisibility() == View.VISIBLE){
                                    TranslateAnimation animation = new TranslateAnimation(
                                            0, 0, 0, -200
                                    );
                                    animation.setDuration(1000);
                                    top_layout.startAnimation(animation);
                                    top_layout.setVisibility(View.GONE);
                                } else if(top_layout.getVisibility() == View.GONE){
                                    top_layout.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                        // X 버튼
                        btn_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popup.dismiss();
                            }
                        });
                    }
                });
                break;
            case 1: // 동영상
                gallary_video_img = (FrameLayout) convertView.findViewById(R.id.gallary_video_img);
                gallary_video_play = (ImageButton) convertView.findViewById(R.id.gallary_video_play);
                gallary_video_title = (TextView) convertView.findViewById(R.id.gallary_video_title);
                final String video_id = current_item.getVideo_code();

                gallary_video_img.setBackgroundResource(current_item.getImg_resource());

                // play 버튼
                gallary_video_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, VideoActivity.class);
                        intent.putExtra("video_id", video_id);
                        context.startActivity(intent);
                    }
                });
                gallary_video_title.setText(current_item.getTitle());
                break;
        }


        return convertView;
    }

    public void addItem(int type, String title, int img_resource) {
        GallaryListItem mItem = new GallaryListItem();

        mItem.setType(type);
        mItem.setTitle(title);
        mItem.setImg_resource(img_resource);

        mItems.add(mItem);

    }

    public void addItem(int type, String title, int img_resource, String video_code){
        GallaryListItem mItem = new GallaryListItem();

        mItem.setType(type);
        mItem.setTitle(title);
        mItem.setImg_resource(img_resource);
        mItem.setVideo_code(video_code);

        mItems.add(mItem);
    }
}
