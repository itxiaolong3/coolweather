package cjl.hycollege.com.account.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaolong on 2017/6/17.
 */

public class vpadpter extends PagerAdapter {

  private List<View> list_adapter=new ArrayList<>();
  private Context context;
    public vpadpter(List<View> vplist, Context ct){
        this.context=ct;
        this.list_adapter=vplist;
    }
    @Override
    public int getCount() {
        return list_adapter.size();
    }
    /**
     *相当listview中的getitem
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list_adapter.get(position));
        return list_adapter.get(position);
    }
    /**
     * 销毁不显示的view,比如说0-3，当前显示2，则就把0清除掉
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       // super.destroyItem(container, position, object);
        container.removeView(list_adapter.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
