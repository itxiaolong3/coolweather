package cjl.hycollege.com.coolweather.selfview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**自定义开关
 * Android 的界面绘制流程
 *   测量        摆放       绘制
 * onMeasure  ->onLayout  ->onDraw 重写这些方法，实现自定义控件
 *      对应的方法
 *    ||            ||         ||
 *   onMeasure()   onLayout()    onDraw(){绘制自动的内容}
 *   这些方法在onResume()之后执行
 *   view 中没有onlayout之说 onMeasure()     onDraw(){绘制自动的内容}
 *   viewGroup才有  onMeasure()（指定自己的宽高，所有子View的宽高）   onLayout()（摆放所有的子view）    onDraw(){绘制自动的内容}
 * Created by xiaolong on 2017/5/23.
 */

public class ToggleViews extends View {
    private Bitmap switchBackgroundBitmap;
    private Bitmap slideBitmap;
    private Paint paint;
    private boolean siwthState=false;//默认开关状态为false
    private float getCurrentX;
    private boolean isTouch=false;//判断是否开始滑动
    private OnSwitchStateUpdateLinster onSwitchStateUpdateLinstener;

    /**
     * 用于代码创建控件
     * @param context
     */
    public ToggleViews(Context context) {
        super(context);
        initPaint();
    }

    public boolean isSiwthState() {
        return siwthState;
    }

    private void initPaint() {
        //初始化画笔
        paint = new Paint();
    }

    /**
     * 用于在XML中使用，可指定自定义属性
     * @param context
     * @param attrs 设置属性
     */
    public ToggleViews(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        //获取自定义控件的属性
        String namespace="http://schemas.android.com/apk/res-auto";
        int switchBackgroundResoure = attrs.getAttributeResourceValue(namespace, "switch_background", -1);
        int slideButtonResource = attrs.getAttributeResourceValue(namespace, "slide_button", -1);
        siwthState = attrs.getAttributeBooleanValue(namespace, "switch_state", false);

        //设置自定义控件属性
        setSwitchBackgroundResoure(switchBackgroundResoure);
        setSlideButtonResoure(slideButtonResource);

    }

    /**
     *  用于在XML中使用，可指定自定义属性和风格
     * @param context
     * @param attrs 属性
     * @param defStyleAttr 风格
     */
    public ToggleViews(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(switchBackgroundBitmap.getWidth(),switchBackgroundBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        canvas.drawBitmap(switchBackgroundBitmap,0,0,paint);

        //绘制滑块,根据滑块的状态来在对应的位置画
        //根据滑块的动态具体位置来画按钮的
        if (isTouch){
            int newLeft= (int) (getCurrentX-slideBitmap.getWidth()/2.0f);
            int maxleft=switchBackgroundBitmap.getWidth()-slideBitmap.getWidth();
            //限制滑块的移动距离
            if (newLeft<0){
                newLeft=0;
            }else if (newLeft>maxleft){
                newLeft=maxleft;
            }
            canvas.drawBitmap(slideBitmap,newLeft,0,paint);
        }else{
            if (siwthState){
                int newleft=switchBackgroundBitmap.getWidth()-slideBitmap.getWidth();
                canvas.drawBitmap(slideBitmap,newleft,0,paint);
            }else{
                canvas.drawBitmap(slideBitmap,0,0,paint);
            }
        }

    }

    //设置背景图
    public void setSwitchBackgroundResoure(int switch_background) {
        switchBackgroundBitmap = BitmapFactory.decodeResource(getResources(), switch_background);
    }
    //设置滑块图片资源
    public void setSlideButtonResoure(int slide_button) {
        slideBitmap = BitmapFactory.decodeResource(getResources(), slide_button);
    }
    //设置按钮的状态
    public void setSwichState(boolean siwthState) {
        this.siwthState=siwthState;
    }

    //重写onTouchEvent方法
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                System.out.println("当前按下的x位置："+event.getX());
                float currentX=event.getX();
                getCurrentX=currentX;
                isTouch=true;
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("当前移动时的x位置："+event.getX());
               getCurrentX=event.getX();
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("当前抬起时的x位置："+event.getX());
                getCurrentX=event.getX();
                isTouch=false;
                //根据滑块是否滑到一半来判断滑块的状态是否为关或者开
                boolean state=getCurrentX>switchBackgroundBitmap.getWidth()/2.0f;
                if (state!=siwthState&&onSwitchStateUpdateLinstener!=null){
                    onSwitchStateUpdateLinstener.onStateUpdate(state);
                }
                siwthState=state;
                break;
        }
        //重绘制画面，此方法会重新调用OnDraw().
        invalidate();
        return true;
    }
    //状态回调，把当前的状态传出去
    public interface OnSwitchStateUpdateLinster{
        void onStateUpdate(boolean state);
    }
    public void setOnSwitchStateUpdateLinstener(OnSwitchStateUpdateLinster onSwitchStateUpdateLinstener){
        this.onSwitchStateUpdateLinstener=onSwitchStateUpdateLinstener;
    }
}
