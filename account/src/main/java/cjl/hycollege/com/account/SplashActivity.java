package cjl.hycollege.com.account;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cjl.hycollege.com.account.bean.UpdateMsg;
import cjl.hycollege.com.account.utils.StreamUtil;
import cjl.hycollege.com.account.utils.ToastUtil;


/**
 * Created by xiaolong on 2017/5/1.
 */

public class SplashActivity extends AppCompatActivity {
    public static final String tag = "SplashActivity";
    //进入引导界面
    private static final int GUIDE = 0000;
    //进入主页
    private static final int MAIN = 1111;
    //延迟时间
    private static final int TIME = 2000;

    //进入主界面的状态码
    private static final int ENTER_VERSION = 101;
    //联网异常状态码
    private static final int ERRO_VERSION = 102;
    private static final int SAVAMSG = 103;
    //本地版本号
    private int mlocalVersion;
    //网络获取更新版本信息
    private String mversionName;
    private String mversionDes;
    private String mVersionUrl;
    private String mversionCode;
    private SharedPreferences sp;
    private boolean isFrist;
    List<UpdateMsg> updateMsgs=new ArrayList<>();
    UpdateMsg upMsg;
    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GUIDE:
                    intoGuide();
                    //mhandler.removeCallbacksAndMessages(null);
                    break;
                case MAIN:
                    //enterhome();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//如果不是6.0以上的就直接不用动态申请
                        enterhome();
                    }else{
                        getPermison();
                    }

                    break;
                case SAVAMSG:
                    //联网成功后才更新
                    sp.edit().putBoolean("isUpdateVersion",true).commit();
                    sp.edit().putString("UpdateUri",upMsg.getDownUrl()).commit();
                    break;
                case ERRO_VERSION:
                    ToastUtil.showToast(getApplicationContext(),"服务器有误");
                    mhandler.removeCallbacksAndMessages(null);
                    break;
            }
        }
    };


    public void initDate() {
        getLocalVersion();
        checkVersion();
        sp = getSharedPreferences("FristState", Context.MODE_PRIVATE);
        isFrist = sp.getBoolean("IsFrist", true);
        if (isFrist) {
            mhandler.sendEmptyMessageDelayed(GUIDE, TIME);
            sp.edit().putBoolean("IsFrist", false).commit();
        } else {
            mhandler.sendEmptyMessageDelayed(MAIN, TIME);
        }
    }

    private void getPermison() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//如果不是6.0以上的就直接不用动态申请

        } else {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //Utils.showToast(getApplicationContext(), "授权成功");
                    ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    new AlertDialog.Builder(SplashActivity.this)
                            .setTitle("温馨提示")
                            .setMessage("没有读取内存卡权限，我显示不了小视频哦。请把权限赐给我吧！！！")
                            .setPositiveButton("你懂得，给你", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                                }
                            }).setNegativeButton("不给！", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).show();
                }

            } else {
                //querysVidio();
                enterhome();
            }
        }

    }

    protected void enterhome() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void intoGuide() {

        Intent i = new Intent(SplashActivity.this, Guide.class);
        startActivity(i);
        finish();
        getPermison();
    }
    /**
     * 获取本地版本号
     *
     * @return
     */
    public int getLocalVersion() {
        mlocalVersion = BuildConfig.VERSION_CODE;
        return mlocalVersion;
    }
    /**
     * 检测版本号，联网操作
     */
    private void checkVersion() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                //http://165831q58j.imwork.net:40347/update.json
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    //1、封装地址
                    URL url = new URL("http://165831q58j.imwork.net:40347/update.json");
                    //2、开启一个链接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //3、设置常见参数
                    //默认请求是GET请求
                    // connection.setRequestMethod("POST");
                    connection.setConnectTimeout(3000);
                    connection.setReadTimeout(3000);
                    //4、获取响应码
                    int code = connection.getResponseCode();
                    if (code==200){
                        //5、以流的方式将数据读取下来
                        InputStream is = connection.getInputStream();
                        //6、将流转换为json格式的字符串
                        String getjson = StreamUtil.streamToString(is);
                        Log.i(tag,getjson);
                        //7、解析json
                        JSONObject jsonObject = new JSONObject(getjson);

                        mversionCode = jsonObject.getString("versionCode");
                        mversionDes = jsonObject.getString("versionDes");
                        mVersionUrl = jsonObject.getString("downloadUrl");
                        upMsg=new UpdateMsg();
                        upMsg.setDownUrl(mVersionUrl);
                        upMsg.setVersionCode(mversionCode);
                        Log.i(tag,"得到的更新码是："+mversionCode);
                        updateMsgs.add(upMsg);
                        Log.i(tag,jsonObject.getString("versionName"));
                        Log.i(tag,mversionDes);
                        Log.i(tag,mversionCode);
                        Log.i(tag,"欢迎页的下载地址"+upMsg.getDownUrl());
                        Log.i(tag,"updateMsgs.get(0)得到的地址="+updateMsgs.get(0));
                        //8、比较版本号
                        if (mlocalVersion<Integer.parseInt(mversionCode)){
                            //通过弹出对话框通知用户更新软件，这就涉及到消息机制了。
                            msg.what=SAVAMSG;
                            Log.i(tag,"保存的更新值"+sp.getBoolean("isUpdateVersion",false));
                        }
                    }else {
                        //Toast.makeText(getApplicationContext(),"服务器请求有误！",Toast.LENGTH_SHORT).show();
                        Log.i(tag,"联网超时----");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what=ERRO_VERSION;

                }finally {
                    //网络请求时间大约4秒后不做任何处理，如果小于4秒就让子线程睡到4秒
                    long endTime=System.currentTimeMillis();
                    Log.i(tag,"请求时间"+(endTime-startTime));
                    if (endTime-startTime<4000){
                        try {
                            Thread.sleep(4000-(endTime-startTime));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mhandler.sendMessage(msg);
                }
            }
        }.start();
    }
    //权限回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enterhome();
            } else {
                Log.i("SplashActivity", "权限问题显示");
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    SplashActivity.this.finish();
                    Log.i("SplashActivity", "权限问题显示+true时");
                } else {
                    Log.i("SplashActivity", "权限问题显示+false是");
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //当用户点击了拒绝并且把不再提示打上了，就得调用如下方法直接开启权限设置页面
                        toSetPermisson(SplashActivity.this,getPackageName());
                    } else {
                        finish();
                    }

                }
            }
        }
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initDate();
        getLocalVersion();
    }
}
