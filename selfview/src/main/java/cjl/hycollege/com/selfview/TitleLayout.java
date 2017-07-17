package cjl.hycollege.com.selfview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by xiaolong on 2017/7/16.
 */

public class TitleLayout extends LinearLayout {
    public TitleLayout(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title_layout, this);
        Button bt_back = (Button) findViewById(R.id.bt_back);
        Button bt_edit = (Button) findViewById(R.id.bt_edit);
        bt_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)getContext()).finish();
            }
        });
        bt_edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"点击了编辑",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
