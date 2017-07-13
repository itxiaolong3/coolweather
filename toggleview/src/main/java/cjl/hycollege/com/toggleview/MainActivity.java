package cjl.hycollege.com.toggleview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ToggleViews toggleViews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggleViews= (ToggleViews) findViewById(R.id.bt_toggleview);

        /*//代码格式进行设置
        toggleViews.setSwitchBackgroundResoure(R.drawable.switch_background);
        toggleViews.setSlideButtonResoure(R.drawable.slide_button);
        toggleViews.setSwichState(false);*/
        //toggleViews.setSwichState(true);
        //自定义实现的监听器方法
        toggleViews.setOnSwitchStateUpdateLinstener(new ToggleViews.OnSwitchStateUpdateLinster() {
            @Override
            public void onStateUpdate(boolean state) {
                Toast.makeText(getApplicationContext(),"当前状态为："+state,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
