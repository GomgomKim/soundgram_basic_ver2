package test.dahun.mobileplay.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import test.dahun.mobileplay.R;
import test.dahun.mobileplay.tab.ImageActivity;
import test.dahun.mobileplay.tab.VideoActivity;

public class GallaryListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;

    private ArrayList<GallaryListItem> mItems = new ArrayList<>();
    private ImageButton gallary_img;
    private TextView gallary_img_title;

    GallaryListItem current_item;

    private ImageView gallary_video_img;
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
                    Glide.with(context).load(current_item.getImg_resource())
                            .apply(new RequestOptions().fitCenter()).into(gallary_img);
                    gallary_img_title.setText(current_item.getTitle());
                }

                final String image_title = current_item.getTitle();
                final int img_resource = current_item.getImg_resource();

                gallary_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ImageActivity.class);
                        intent.putExtra("image_title", image_title);
                        intent.putExtra("img_resource", img_resource);
                        context.startActivity(intent);
                    }
                });
                break;
            case 1: // 동영상
                gallary_video_img = (ImageView) convertView.findViewById(R.id.gallary_video_img);
                gallary_video_play = (ImageButton) convertView.findViewById(R.id.gallary_video_play);
                gallary_video_title = (TextView) convertView.findViewById(R.id.gallary_video_title);
                final String video_id = current_item.getVideo_code();
                final String video_title = current_item.getTitle();

                Glide.with(context).load(current_item.getImg_resource())
                        .apply(new RequestOptions().fitCenter()).into(gallary_video_img);

                // play 버튼
                gallary_video_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, VideoActivity.class);
                        intent.putExtra("video_id", video_id);
                        intent.putExtra("video_title", video_title);
                        context.startActivity(intent);
                    }
                });
                gallary_video_title.setText(video_title);
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
