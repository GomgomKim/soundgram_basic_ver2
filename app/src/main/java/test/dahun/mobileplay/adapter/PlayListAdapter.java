package test.dahun.mobileplay.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import test.dahun.mobileplay.R;

public class PlayListAdapter extends BaseAdapter {

    private ArrayList<PlayListItem> mItems = new ArrayList<>();
    private TextView index;
    private ImageView heart;
    private TextView heart_num;
    private ImageView title_img;
    private TextView title;
    private TextView singer;
    PlayListItem current_item;

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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.play_lists, parent, false);
        }

        index = (TextView) convertView.findViewById(R.id.index);
        heart = (ImageButton) convertView.findViewById(R.id.heart);
        heart_num = (TextView) convertView.findViewById(R.id.heart_num);
        title_img = (ImageView) convertView.findViewById(R.id.title_img);
        title = (TextView) convertView.findViewById(R.id.title);
        singer = (TextView) convertView.findViewById(R.id.singer);

        current_item = getItem(position);

        index.setText(current_item.getIndex());
        heart_num.setText(String.valueOf(current_item.getHeart_num()));
        title.setText(current_item.getTitle());
        singer.setText(current_item.getSinger());

        if(mItems.get(position).getIsTitle() == 1)
            title_img.setVisibility(View.VISIBLE);

        if(heart.getTag().equals("off")){
            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    heart.setImageResource(R.drawable.like_on);
                    heart.setTag("on");
                    mItems.get(position).setHeart_num(mItems.get(position).getHeart_num() + 1);
                }
            });
        } else if (heart.getTag().equals("on")){
            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    heart.setImageResource(R.drawable.like_off);
                    heart.setTag("off");
                    mItems.get(position).setHeart_num(mItems.get(position).getHeart_num() - 1);
                }
            });
        }

        return convertView;
    }

    public void addItem(int index, int heart_num, String title, String singer, int isTitle) {
        PlayListItem mItem = new PlayListItem();

        mItem.setIndex(String.format("%02d", index));
        mItem.setHeart_num(heart_num);
        mItem.setTitle(title);
        mItem.setSinger(singer);
        mItem.setIsTitle(isTitle);

        mItems.add(mItem);

    }
}
