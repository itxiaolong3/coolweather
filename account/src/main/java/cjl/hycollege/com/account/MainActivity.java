package cjl.hycollege.com.account;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import cjl.hycollege.com.account.adapter.Myadapter;
import cjl.hycollege.com.account.bean.Mybean;
import cjl.hycollege.com.account.db.SqlHelper;
import cjl.hycollege.com.account.utils.ToastUtil;

public class MainActivity extends AppCompatActivity {
    /**
     * 下载更新
     */
    //更新新版本的状态码
    private static final int UPDATE_VERSION = 100;
    private static final int TODOWN = 1000;
    //获取版本名称
    private String mversionName;
    //更新功能描述信息
    private String mversionDes;
    //更新地址
    private String mVersionUrl;
    //本地版本号
    private int mlocalVersion;
    //下载进度条
    private ProgressBar pb;
    private SharedPreferences sp;

    private SlideMeui slideme;
    private ListView listView;
    private ArrayList<Mybean> mbeanlist = new ArrayList<Mybean>();
    private SqlHelper helper = null;
    private LinearLayout login, dele;
    private Myadapter myadapter;
    private View view;
    public static final String tag = "MainActivity";
    private TextView tv_show_login_state;
    Handler mhande = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TODOWN:
                    showUpdateDialog();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        initView();
        initDate();
    }

    private void initView() {
        slideme = (SlideMeui) findViewById(R.id.activity_main);
        listView = (ListView) findViewById(R.id.list_view);
        dele = (LinearLayout) findViewById(R.id.lin_thre_dele);
        login = (LinearLayout) findViewById(R.id.lin_one_login);
        pb= (ProgressBar) findViewById(R.id.pb);
        tv_show_login_state= (TextView) findViewById(R.id.tv_show_login_state);
    }

    private void initDate() {
        helper = new SqlHelper(this);
        myadapter = new Myadapter(MainActivity.this, mbeanlist);
        query();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "登录", Toast.LENGTH_SHORT).show();
                showLogin();
                query();
            }
        });
        dele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "点击格式化", Toast.LENGTH_SHORT).show();
                delete();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //ToastUtil.showToast(getApplicationContext(), "长按了"+id);
                deleteItem(position);
                return true;
            }
        });
        //版本更新
        getVerSionName();
        //getLocalVersion();
        sp = getSharedPreferences("FristState", Context.MODE_PRIVATE);
        boolean isUpdateVersion = sp.getBoolean("isUpdateVersion", false);
        mVersionUrl = sp.getString("UpdateUri", "");
        Log.i("MainActivity", "是否更新：" + isUpdateVersion);
        if (isUpdateVersion) {
            //更新版本
            // showUpdateDialog();
            mhande.sendEmptyMessage(TODOWN);
        } else {
            ToastUtil.showToast(getApplicationContext(), "没更新");
        }
       /*测试进度条显示效果
       pb.setVisibility(View.VISIBLE);
        pb.setMax(100);
        pb.setProgress(50);*/

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void add(View view) {
        showAdd();
    }

    private void query() {
        SQLiteDatabase database = helper.getReadableDatabase();
        String sql1 = "select*from info";
        Cursor cursor = database.rawQuery(sql1, null);
        mbeanlist.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            Log.i("MaintAactvity", "id值=" + id);
            String money = cursor.getString(cursor.getColumnIndex("money"));
            String dec = cursor.getString(cursor.getColumnIndex("dec"));
            Mybean bean = new Mybean();
            bean.setTime(time);
            bean.setMoney(money);
            bean.setDes(dec);
            mbeanlist.add(bean);
        }
        cursor.close();
        database.close();
       /* for (Mybean b : mbeanlist) {
            //Log.i("MainACtivity","查询得到的所有数据="+b+"\n");
        }*/
        listView.setAdapter(myadapter);
        myadapter.refresh(mbeanlist);

    }

    /**
     * 格式化数据库
     */
    private void delete() {
        SQLiteDatabase database = helper.getReadableDatabase();
        database.delete("info", null, null);
        database.close();
        query();
    }

    private void deleteItem(int position) {
        SQLiteDatabase database = helper.getReadableDatabase();
        //查询数据库中的id
        String sql = "select _id from info";
        Cursor cursor_id = database.rawQuery(sql, null);
        cursor_id.moveToPosition(position);
        int getid = cursor_id.getInt(cursor_id.getColumnIndex("_id"));

        //查询数据库中的各数据,以获取到的id再次进行查询
        String sql1 = "select*from info where _id=" + "'" + getid + "'";
        Cursor cursor_value = database.rawQuery(sql1, null);
        //通过点击查询到的id获取到的信息
        String getmoney_dele = null;
        String gettime_dele = null;
        String getdec_dele = null;
        while (cursor_value.moveToNext()) {
            gettime_dele = cursor_value.getString(cursor_value.getColumnIndex("time"));
            getmoney_dele = cursor_value.getString(cursor_value.getColumnIndex("money"));
            getdec_dele = cursor_value.getString(cursor_value.getColumnIndex("dec"));
        }
        Log.i("MainActivtiy", "得到数据库的钱===" + getmoney_dele);
        //弹出确定删除的对话框
        showDelete(gettime_dele,getmoney_dele,getdec_dele,getid);
        database.close();
        query();
    }

    public void btmenu(View view) {
        slideme.swithState();
    }

    //显示添加框
    private void showAdd() {
        final SQLiteDatabase database = helper.getReadableDatabase();
        //自定义弹出框。
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        view = View.inflate(this, R.layout.add_view, null);
        //让对话框显示自己定义的界面
        dialog.setView(view);
        dialog.show();
        Button bt_submit = (Button) view.findViewById(R.id.bt_add_post);
        //获取系统时间
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        Log.i("MainActivity", "当前是月=" + month + "月");
        int day = c.get(Calendar.DAY_OF_MONTH);
        final String gettime = year + "年" + month + "月" + day + "日";
        EditText time = (EditText) view.findViewById(R.id.et_add_time);
        time.setText(gettime);
        //点击确定按钮
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //对输入内容进行判断
                EditText money = (EditText) view.findViewById(R.id.et_add_money);
                EditText des = (EditText) view.findViewById(R.id.et_add_des);
                String getMoney = money.getText().toString().trim();
                String getDes = des.getText().toString().trim();
                if (!TextUtils.isEmpty(getMoney) && !TextUtils.isEmpty(getDes)) {
                    // String sql="insert into info (name,phone,age) values('zhangsan','1341111111',20)";
                    String sql = "insert into info(time,money,dec)values(" + "'" + gettime + "'" + "," + "'" + getMoney + "'" + "," + "'" + getDes + "'" + ")";
                   // Log.i("MainActivity", "添加的数据有：" + gettime + "\n" + getMoney + "\n" + getDes);
                    database.execSQL(sql);
                    database.close();
                    dialog.dismiss();
                    query();
                    ToastUtil.showToast(getApplicationContext(),"添加成功");
                } else {
                    ToastUtil.showToast(getApplicationContext(), "输入不可为空");
                }
            }
        });
        //点击取消按钮
        Button bt_cancel = (Button) view.findViewById(R.id.bt_add_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    //显示添加框
    private void showDelete(final String times, final String moneys, final String dess,
                            final int _id) {
        final SQLiteDatabase database = helper.getReadableDatabase();
        //自定义弹出框。
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        view = View.inflate(this, R.layout.delete_view, null);
        //让对话框显示自己定义的界面
        dialog.setView(view);
        dialog.show();
        Button bt_submit = (Button) view.findViewById(R.id.bt_delete_post);
        //对输入内容进行判断
        TextView money = (TextView) view.findViewById(R.id.tv_delete_money);
        TextView des = (TextView) view.findViewById(R.id.tv_delete_des);
        TextView time = (TextView) view.findViewById(R.id.tv_delete_time);
        Log.i("MainActivtiy", "删除显示的钱===" + moneys);
        money.setText(moneys);
        time.setText(times);
        des.setText(dess);
        //点击确定按钮
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase database = helper.getReadableDatabase();
                String deleSql = "delete from info where _id=" + "'" + _id + "'";
                database.execSQL(deleSql);
                dialog.dismiss();
                query();
                ToastUtil.showToast(getApplicationContext(),"删除成功");

            }
        });
        //点击取消按钮
        Button bt_cancel = (Button) view.findViewById(R.id.bt_delete_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    //显示登录框
    private void showLogin() {

        //自定义弹出框。
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        view = View.inflate(this, R.layout.activity_login, null);
        //让对话框显示自己定义的界面
        dialog.setView(view);
        dialog.show();
        Button bt_submit = (Button) view.findViewById(R.id.bt_login);
        //对输入内容进行判断
        final EditText loginName = (EditText) view.findViewById(R.id.et_user);
        final EditText loginPassw = (EditText) view.findViewById(R.id.et_passwork);

        //点击确定按钮
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String getName=loginName.getText().toString().trim();
                 String getPassw=loginPassw.getText().toString().trim();
                Log.i(tag,"登录账号=="+getName) ;
                if (!TextUtils.isEmpty(getName) && !TextUtils.isEmpty(getPassw)) {
                    tv_show_login_state.setText(getName+"您好");
                    ToastUtil.showToast(getApplicationContext(),"登录成功");
                    dialog.dismiss();
                } else {
                    ToastUtil.showToast(getApplicationContext(), "输入不可为空");
                }

            }
        });

    }
    /**
     * 获取版本名称
     *
     * @return
     */
    public String getVerSionName() {
        mversionName = BuildConfig.VERSION_NAME;
        return mversionName;
    }


    /**
     * 弹出要更新的对话框
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("版本更新");
        builder.setMessage(mversionDes);
        //点击更新按钮
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downApk();
            }
        });
        //点击取消更新按钮
        builder.setNegativeButton("先不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //enterMainActivity();
                ToastUtil.showToast(getApplicationContext(), "您取消了更新");
            }
        });
        //用户点击了返回键
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // enterMainActivity();
                ToastUtil.showToast(getApplicationContext(), "您取消了更新");
                dialog.dismiss();
            }
        });
        builder.show();
    }
    /**
     * 下载更新的apk方法
     */
    private void downApk() {

        //1、判断SD卡是否插上
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //2、获取SD卡路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "360soft2.apk";
            //Log.i(tag, "保存在" + path);
            //3、发送请求，获取apk安装包并且保存到指定位置
            HttpUtils httpUtils = new HttpUtils();
            //4、发送请求，传递参数，第三个参数表示请求的回调方法，判断成功与否
            Log.i(tag, "获取到的下载地址为=" + mVersionUrl);
            Log.i(tag, "获取到的保存apk地址为=" + path);
            httpUtils.download(mVersionUrl, path, new RequestCallBack<File>() {

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载成功
                    File file = responseInfo.result;
                    Log.i(tag, "下载成功");
                    //提示用户安装
                    installapk(file);
                    pb.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    //下载失败
                    Log.i(tag, "下载失败");
                    Log.i(tag, "下载失败的原因=" + e.getMessage());
                    pb.setVisibility(View.GONE);
                }

                //刚刚开始下载
                @Override
                public void onStart() {
                    super.onStart();
                    Log.i(tag, "开始下载");


                }

                /**
                 *
                 * @param total 下载文件的总大小
                 * @param current 当前下载位置
                 * @param isUploading 是否正在下载中
                 */
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    Log.i(tag, "正在下载--" + current);
                    setProgress((int) ((current*100/total)));
                    pb.setVisibility(View.VISIBLE);
                    Log.i(tag, "总长度--" + Formatter.formatFileSize(getApplicationContext(),total));
                    pb.setMax(100);
                    pb.setProgress((int) (current*100/total));
                    Log.i(tag, "下载进度--" +""+(current*100/total));
                }
            });
        }
    }

    //提示用户安装
    private void installapk(File file) {
        //配置apk安装入口，查看源码的主配置可知
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        //文件作为数据源，且设置安装的类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        // startActivity(intent);
        startActivityForResult(intent, 0);
    }
}
