package cjl.hycollege.com.coolweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cjl.hycollege.com.coolweather.R;
import cjl.hycollege.com.coolweather.apdate.vpadpter;

public class Guide extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    ViewPager viewpager;
    private List<View> viewp = new ArrayList<>();
    //小圆点
    ImageView[] imgs;
    int[] imgId = new int[]{R.id.img_dot1, R.id.img_dot2, R.id.img_dot3};

    //最后一页的按钮
    private Button bt_enter;
    private LinearLayout lin_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        initDot();
    }

    /**
     * 用for循环初始化小圆点
     */
    private void initDot() {
        imgs = new ImageView[imgId.length];

        for (int i = 0; i < imgId.length; i++) {
            imgs[i] = (ImageView) findViewById(imgId[i]);
            final int finalI = i;
            imgs[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewpager.setCurrentItem(finalI);
                }
            });
        }


    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(this);

        View view1 = inflater.inflate(R.layout.activity_one, null);
        View view2 = inflater.inflate(R.layout.activity_two, null);
        View view3 = inflater.inflate(R.layout.activity_three, null);
        bt_enter= (Button) view3.findViewById(R.id.bt_enter);
        lin_btn= (LinearLayout) view3.findViewById(R.id.lin_btn);
        viewp.add(view1);
        viewp.add(view2);
        viewp.add(view3);
        viewpager = (ViewPager) findViewById(R.id.vp);
        viewpager.setAdapter(new vpadpter(viewp, this));
        viewpager.addOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //Log.i("Guide","偏移量"+positionOffsetPixels);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 当前页面被选中状态
     *
     * @param position 当前页面的索引
     */
    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < imgId.length; i++) {
            if (position == i) {
                imgs[i].setImageResource(R.drawable.login_point_selected);
                // Log.i("Guide","选择序号为"+i);
            } else {
                imgs[i].setImageResource(R.drawable.login_point);
                //Log.i("Guide","不选择序号为"+i);
            }

        }
        if (position == 2) {
            //透明显示出来
            Animation mshowbtn = AnimationUtils.loadAnimation(this, R.anim.btn_alpha);
            lin_btn.setVisibility(View.VISIBLE);
            lin_btn.startAnimation(mshowbtn);
            bt_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences perfs = getSharedPreferences("xiaozhun", MODE_PRIVATE);
                    SharedPreferences.Editor editor = perfs.edit();
                    editor.putBoolean("isFirstIn", false);
                    editor.commit();
                    intoMainActivity();
                }
            });
        } else {
            lin_btn.setVisibility(View.INVISIBLE);
        }
    }

    private void intoMainActivity() {
        Intent intent = new Intent(Guide.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

}
