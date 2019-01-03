package test.dahun.mobileplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import test.dahun.mobileplay.R;

public class ThanksToAdapter extends BaseAdapter {
    LayoutInflater inflater;

    private ArrayList<ThanksToItem> mItems = new ArrayList<>();
    private TextView intro_title;
    private TextView intro_singer;
    private TextView intro_content;

    ThanksToItem current_item;

    public ThanksToAdapter(Context context){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public ThanksToItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        current_item = getItem(position);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.singer_intro, parent, false);
        }

        intro_title = (TextView) convertView.findViewById(R.id.intro_title);
        intro_singer = (TextView) convertView.findViewById(R.id.intro_singer);
        intro_content = (TextView) convertView.findViewById(R.id.intro_content);


        if(current_item != null){
            intro_title.setText(current_item.getTitle());
            intro_singer.setText(current_item.getSinger());
            intro_content.setText(current_item.getContent());
        }

        return convertView;
    }

    public void addItem(String title, String singer, String content) {
        ThanksToItem mItem = new ThanksToItem();

        mItem.setTitle(title);
        mItem.setSinger(singer);
        mItem.setContent(content);

        mItems.add(mItem);

    }
}
