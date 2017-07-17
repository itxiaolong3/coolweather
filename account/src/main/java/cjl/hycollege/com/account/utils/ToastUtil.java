package cjl.hycollege.com.account.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by xiaolong on 2017/6/4.
 */

public class ToastUtil {
    /**
     *
     * @param context 传入上下文
     * @param s 需要显示的字符串
     */
    public static void showToast(Context context, String s) {
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }
}
