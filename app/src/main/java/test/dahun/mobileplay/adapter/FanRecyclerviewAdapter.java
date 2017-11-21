package test.dahun.mobileplay.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import test.dahun.mobileplay.R;
import test.dahun.mobileplay.model.Fan;

/**
 * Created by gonghojin on 2017. 11. 8..
 */

public class FanRecyclerviewAdapter extends RecyclerView.Adapter<FanRecyclerviewAdapter.ItemViewHolder> {
//////
    ArrayList<Fan> mItems;

    public FanRecyclerviewAdapter(ArrayList<Fan> items){
        mItems = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fansay_view,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.idTv.setText(mItems.get(position).getid());
        holder.phoneTv.setText(mItems.get(position).getphone());
        holder.dateTv.setText(mItems.get(position).getdate());
        holder.textTv.setText(mItems.get(position).gettext());

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView idTv;
        private TextView phoneTv;
        private TextView dateTv;
        private TextView textTv;

        public ItemViewHolder(View itemview){
            super(itemview);
            idTv = (TextView)itemview.findViewById(R.id.fanid);
            phoneTv = (TextView)itemview.findViewById(R.id.fanphone);
            dateTv = (TextView)itemview.findViewById(R.id.fandate);
            textTv = (TextView)itemview.findViewById(R.id.fantext);

        }
    }
}
