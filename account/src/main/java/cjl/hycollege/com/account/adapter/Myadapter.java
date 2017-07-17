package cjl.hycollege.com.account.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import cjl.hycollege.com.account.R;
import cjl.hycollege.com.account.bean.Mybean;

/**
 * Created by xiaolong on 2017/6/20.
 */

public class Myadapter extends BaseAdapter{
    private ArrayList<Mybean> list;
    private LayoutInflater mInflater;
    public Myadapter(Context ct,ArrayList<Mybean> list){
        this.list=list;
        this.mInflater=LayoutInflater.from(ct);
    }
    public void refresh(ArrayList<Mybean> list){
        this.list=list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        //Log.i("Myadapter","数据条数="+list.size());
        return list.size();

    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder=null;
        if (view==null){
            holder=new ViewHolder();
            view=mInflater.inflate(R.layout.list_item,null);
            view.setTag(holder);
            holder.tv_time= (TextView) view.findViewById(R.id.tv_list_time);
            holder.tv_money= (TextView) view.findViewById(R.id.tv_list_money);
            holder.tv_des= (EditText) view.findViewById(R.id.tv_list_dec);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        Mybean mybean=list.get(position);
        //Log.i("Myadapter","适配器中获取时间="+mybean.money);
        //Log.i("Myadapter","适配器中获取位置"+position);
        holder.tv_time.setText(mybean.getTime());

        holder.tv_money.setText(mybean.getMoney());
        holder.tv_des.setText(mybean.getDes());
        return view;
    }
    public final class ViewHolder{
        TextView tv_time,tv_money,tv_des;
    }
}
