package cjl.hycollege.com.account;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**侧滑面板控制
 * Created by xiaolong on 2017/5/27.
 */

public class SlideMeui extends ViewGroup {

    private View leftLayout;//左边面板
    private View mainLayout;//右边面板
    private float downX;
    private float moveX;
    private float downY;//按下时的y坐标
    public static final int MAIN_STATE=0;
    public static final int MENU_STATE=1;
    private int currentState;//当前状态
    Scroller scroller;//滚动器，数值模拟器
    public SlideMeui(Context context) {
        super(context);
        init();
    }

    public SlideMeui(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideMeui(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化滚动器
     */
    private void init() {
        scroller = new Scroller(getContext());
    }

    /**
     * 测量并设置所有子view的宽高
     * @param widthMeasureSpec 当前控件的宽度测量规则
     * @param heightMeasureSpec 当前控件的高度测量规则
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //指定左边面板的宽高
        leftLayout = getChildAt(0);
        leftLayout.measure(leftLayout.getLayoutParams().width,heightMeasureSpec);

        //指定主面板的宽高
        mainLayout = getChildAt(1);
        mainLayout.measure(widthMeasureSpec,heightMeasureSpec);
		
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     *
     * @param changed 当前控件的尺寸大小，位置是否发生了变化
     * @param l 左边距
     * @param t 上边距
     * @param r 右边距
     * @param b 下边距
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //摆放左板
        leftLayout.layout(-leftLayout.getMeasuredWidth(),0,0,b);
        //摆放主面板
        mainLayout.layout(l,t,r,b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                //将要发生的偏移量或者变化量
                int scrollX= (int) (downX-moveX);
                //设置滑动的限制距离
                //将要滚动到的位置
                int newScollPosition=getScrollX()+scrollX;

                //限制左边界
                if (newScollPosition<-getChildAt(0).getMeasuredWidth()){
                    scrollTo(-getChildAt(0).getMeasuredWidth(),0);
                }else if (newScollPosition>0){//限制右边界
                   scrollTo(0,0);
                }else{
                    scrollBy(scrollX,0);//scrollBy()这个方法是移动屏幕，不是移动控件
                }
                downX=moveX;//每次移动之后就把值赋给按下的值
                break;
            case MotionEvent.ACTION_UP:
                int leftCenter= (int) (-getChildAt(0).getMeasuredWidth()/2.0f);
                if (getScrollX()<leftCenter){
                    //打开状态，切换为菜单面板
                    currentState=MENU_STATE;
                    updateCurrentContent();
                }else{
                    //关闭，切换到主页面
                    currentState=MAIN_STATE;
                    updateCurrentContent();
                }
                break;
        }
        return true;//这里的消费事件得看是否为完全自定义的。如果是完全自定义的就返回true
    }

    /**
     * 根据当前的状态进行关闭或者开启的动画
     */
    private void updateCurrentContent() {
        int stratX=getScrollX();
        int dx=0;
        if (currentState==MENU_STATE){//打开
            //scrollTo(-getChildAt(0).getMeasuredWidth(),0);
            dx=-getChildAt(0).getMeasuredWidth()-stratX;
        }else{
            scrollTo(0,0);
            dx=0-stratX;
        }

        int durations=Math.abs(dx*2);//120*10=1200毫秒的滚动时间,这里随便改，越小越快
        /**
         * stratX 开始移动位置，startY开始的y值
         * dx 将要发生的位移，dy同理
         * durations 是执行时间
         */
        scroller.startScroll(stratX,0,dx,0,durations);
        //重绘界面
        invalidate();//重绘界面->drawChild() ->computeScroll();
    }

    /**
     * 维持动画的继续
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        //表示动画正在执行
        if (scroller.computeScrollOffset()){//小于500毫秒就为true,大于500毫秒就为false

            //执行过程中的模拟位移变化，比如重-300移动到-400，在移动过程中有-301,-302...这些就是模拟位移值
            int currX = scroller.getCurrX();
            scrollTo(currX,0);
            invalidate();//再次调用此方法来重绘界面，不断循环执行
        }
    }
    /**
     * 点击主界面的按钮时进行界面的切换，根据状态进行切换
     */
    public void open(){
        currentState=MENU_STATE;
        updateCurrentContent();
    }
    public void close(){
        currentState=MAIN_STATE;
        updateCurrentContent();
    }
    public void swithState(){
        if (currentState==MAIN_STATE){
            open();
        }else{
            close();
        }
    }
    public int getCurrentState(){
        return  currentState;
    }

    //触摸事件机制的传递，拦截判断
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX=ev.getX();
                downY=ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //偏移量
                float xoffSet=Math.abs(ev.getX()-downX);
                float yoffSet=Math.abs(ev.getY()-downY);
                if (xoffSet>yoffSet&&xoffSet>15){//横向移动,其中的xoffSet>15表示水平方向偏移15像素才生效拦截
                    return true;//拦截此次的触摸事件，使界面滚动
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        //父类默认返回false,表示不拦截事件
        return super.onInterceptTouchEvent(ev);
    }
}
