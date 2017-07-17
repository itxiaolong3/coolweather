package cjl.hycollege.com.account;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cjl.hycollege.com.account.adapter.vpadpter;

public class Guide extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    ViewPager viewpager;
    private List<View> viewp=new ArrayList<>();
    //小圆点
    ImageView[] imgs;
    int[] imgId=new int[]{R.id.img_dot1,R.id.img_dot2,R.id.img_dot3};

    //最后一页的按钮
    LinearLayout lin_btn;

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
        imgs=new ImageView[imgId.length];

        for (int i = 0; i <imgId.length ; i++) {
            imgs[i]= (ImageView) findViewById(imgId[i]);
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
        lin_btn= (LinearLayout) view3.findViewById(R.id.lin_btn);
        viewp.add(view1);
        viewp.add(view2);
        viewp.add(view3);
        viewpager= (ViewPager) findViewById(R.id.vp);
        viewpager.setAdapter(new vpadpter(viewp,this));
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
     * @param position 当前页面的索引
     */
    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i <imgId.length ; i++) {
            if (position==i){
               imgs[i].setImageResource(R.drawable.login_point_selected);
               // Log.i("Guide","选择序号为"+i);
            }else{
               imgs[i].setImageResource(R.drawable.login_point);
                //Log.i("Guide","不选择序号为"+i);
            }

        }
        if (position==2){
            //透明显示出来
            Animation mshowbtn= AnimationUtils.loadAnimation(this,R.anim.btn_alpha);
            lin_btn.setVisibility(View.VISIBLE);
            lin_btn.startAnimation(mshowbtn);
        }else{
            lin_btn.setVisibility(View.INVISIBLE);
        }
    }
    public void Letgo(View view){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //Utils.showToast(getApplicationContext(), "请再次进入授权读取内存卡权限，否则本应用使用不了");
            toSetPermisson(Guide.this,getPackageName());
        }else{
            intoMainActivity();
        }

    }
    private void intoMainActivity(){
        Intent intent=new Intent(Guide.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    /**
     * 调出系统设置界面，手动给应用设置权限
     */
    public void toSetPermisson(Context context, final String getPackageName) {
        new AlertDialog.Builder(context)
                .setTitle("紧危提示")
                .setMessage("请手动给本应用授权。打开应用-->本地影音-->权限--打开储存")
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //跳转到安全设置页面
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (Build.VERSION.SDK_INT>=9){
                            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                            intent.setData(Uri.fromParts("package",getPackageName,null));
                        }
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("算了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

}
