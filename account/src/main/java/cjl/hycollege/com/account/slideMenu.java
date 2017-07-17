package cjl.hycollege.com.account;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import cjl.hycollege.com.account.utils.ScreenUtils;

/**
 * Created by xialong on 2017/6/20.
 */

public class slideMenu extends HorizontalScrollView {
    private static final String Tag = "slideMenu";
    //屏幕宽度
    private int mScreenWidth;
    //菜单宽度
    private int mMenuWidth;
    private int mHalMenufWidth;
    //菜单右边距
    private int mMenuRightPadding = 200;
    private boolean isOpen;

    //开始测量布局宽高标志
    private boolean isone;

    public slideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScreenWidth = ScreenUtils.getScreenWidth(context);
        Log.i(Tag, "mScreenWidth==" + mScreenWidth);
    }

    public slideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.slideMenu, defStyleAttr, 0);
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.slideMenu_rightPadding:
                    //50转换为50dp
                    typedArray.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                    50f, getResources().getDisplayMetrics())
                    );
                    break;
            }
        }
        typedArray.recycle();
    }

    /**
     * 测量布局
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (!isone) {
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
            ViewGroup menu = (ViewGroup) wrapper.getChildAt(0);
            ViewGroup content = (ViewGroup) wrapper.getChildAt(1);
            //转化像素单位dp--px
            mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    mMenuRightPadding, content.getResources().getDisplayMetrics());
            //菜单的宽度
            mMenuWidth = mScreenWidth - mMenuRightPadding;
            mHalMenufWidth = mMenuWidth / 2;
            menu.getLayoutParams().width = mMenuWidth;
            content.getLayoutParams().width = mScreenWidth;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 布局的定位
     *
     * @param changed 位置改变
     * @param l       左
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            //隐藏菜单
            this.scrollTo(mMenuWidth, 0);
            isone = true;
        }
    }

    /**
     * 手触事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            //鼠标抬起时判断菜单的显示是否过半，如果过半就全部显示
            case MotionEvent.ACTION_UP:
                int getScreentX = getScrollX();
                //Log.i(Tag, "当前滑动的距离==" + getScreentX);
                /**
                 * 这里的滑动距离得重点理解，整个屏幕设为-1 0  1 显示的主题部分为0，闭合时的菜单为-1，
                 * 滑动的距离是从-1开始计算的，也就是菜单最左边发生的位移进行判断，当菜单全部显示出来时
                 * 再先右滑动就滑动不了，此时已经完全显示了
                 *
                 */
                if (getScreentX > mHalMenufWidth) {
                    //展开
                    this.smoothScrollTo(mMenuWidth, 0);
                    isOpen=false;
                   // Log.i(Tag, "mMenuWidth==" + mMenuWidth);
                   // Log.i(Tag, "mHalMenufWidth==" + mHalMenufWidth);
                } else {
                    //关闭
                    this.smoothScrollTo(0, 0);
                    isOpen=true;
                }
                return true;
            case MotionEvent.ACTION_DOWN:
               // Log.i(Tag, "按下的位置" + ev.getX());
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 打开菜单
     */
    public void openMenu() {
        if (isOpen)
            return;
        this.smoothScrollTo(0, 0);
        isOpen = true;
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (isOpen) {
            this.smoothScrollTo(mMenuWidth, 0);
            isOpen = false;
        }
    }

    /**
     * 切换菜单状态
     */
    public void toggle() {
        if (isOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }

}
