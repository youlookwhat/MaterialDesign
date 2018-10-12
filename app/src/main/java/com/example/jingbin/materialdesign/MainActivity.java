package com.example.jingbin.materialdesign;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.jingbin.materialdesign.activity.BottomNavigatorActivity;
import com.example.jingbin.materialdesign.activity.FullscreenActivity;
import com.example.jingbin.materialdesign.activity.ItemListDialogFragment;
import com.example.jingbin.materialdesign.activity.LoginActivity;
import com.example.jingbin.materialdesign.activity.PlusOneFragment;
import com.example.jingbin.materialdesign.activity.ScrollingActivity;
import com.example.jingbin.materialdesign.activity.SettingsActivity;
import com.example.jingbin.materialdesign.activity.TabbedActivity;
import com.example.jingbin.materialdesign.list.ItemListActivity;
import com.example.jingbin.materialdesign.main.MyFragment;
import com.example.jingbin.materialdesign.main.adapter.MyViewPagerAdapter;
import com.example.jingbin.materialdesign.main.utils.DateUtils;
import com.example.jingbin.materialdesign.main.utils.SnackbarUtil;
import com.example.jingbin.materialdesign.notification.NewMessageNotification;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;
import static com.example.jingbin.materialdesign.main.utils.DateUtils.FORMAT_D;
import static com.example.jingbin.materialdesign.main.utils.DateUtils.FORMAT_M;
import static com.example.jingbin.materialdesign.main.utils.DateUtils.FORMAT_Y;

/**
 * Created by jingbin on 16/9/10.
 */
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, ItemListDialogFragment.Listener, PlusOneFragment.OnFragmentInteractionListener {

    //初始化各种控件，照着xml中的顺序写
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private NavigationView mNavigationView;

    // TabLayout中的tab标题
    private String[] mTitles;
    // 填充到ViewPager中的Fragment
    private List<Fragment> mFragments;
    //选择时间
    protected int mYear;
    protected int mMonth;
    protected int mDay;
    protected String days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initData();
        configViews();
    }


    private void initData() {
        // Tab的标题采用string-array的方法保存，在res/values/arrays.xml中写
        mTitles = getResources().getStringArray(R.array.tab_titles);

        //初始化填充到ViewPager中的Fragment集合
        mFragments = new ArrayList<>();
        int length = mTitles.length;
        for (int i = 0; i < length; i++) {
            Bundle mBundle = new Bundle();
            mBundle.putInt("flag", i);
            MyFragment mFragment = new MyFragment();
            mFragment.setArguments(mBundle);
            mFragments.add(i, mFragment);

            if (i == length - 1) {
                mFragments.remove(length - 1);
                PlusOneFragment plusOneFragment = PlusOneFragment.newInstance("md", "md");
                mFragments.add(length - 1, plusOneFragment);
            }
//            else if (i == length - 2) {
//                mFragments.remove(length - 2);
//                ItemFragment itemFragment = ItemFragment.newInstance(3);
//                mFragments.add(length - 2, itemFragment);
//            }
        }
    }

    private void initViews() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToolbar = findViewById(R.id.id_toolbar);
        mTabLayout = findViewById(R.id.id_tablayout);
        mViewPager = findViewById(R.id.id_viewpager);
        mNavigationView = findViewById(R.id.id_navigationview);

        //设置日期选择器初始日期
        mYear = Integer.parseInt(DateUtils.getCurYear(FORMAT_Y));
        mMonth = Integer.parseInt(DateUtils.getCurMonth(FORMAT_M)) - 1;
        mDay = Integer.parseInt(DateUtils.getCurMonth(FORMAT_D));
        //设置当前 日期
        days = DateUtils.getCurDateStr("yyyy-MM-dd");
//        SnackbarUtil.showLong(this, days);
    }

    private void configViews() {
        // 设置显示Toolbar
        setSupportActionBar(mToolbar);

        // 设置Drawerlayout开关指示器，即Toolbar最左边的那个icon
        ActionBarDrawerToggle mActionBarDrawerToggle =
                new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();

        //给NavigationView填充顶部区域，也可在xml中使用app:headerLayout="@layout/header_nav"来设置
        mNavigationView.inflateHeaderView(R.layout.nav_header_main);
        //给NavigationView填充Menu菜单，也可在xml中使用app:menu="@menu/menu_nav"来设置
        mNavigationView.inflateMenu(R.menu.activity_main_drawer);

        // 自己写的方法，设置NavigationView中menu的item被选中后要执行的操作
        onNavigationViewMenuItemSelected(mNavigationView);
//        mNavigationView.setNavigationItemSelectedListener(this);

        // 初始化ViewPager的适配器，并设置给它
        MyViewPagerAdapter mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), mTitles, mFragments);
        mViewPager.setAdapter(mViewPagerAdapter);
        // 设置ViewPager最大缓存的页面个数
        mViewPager.setOffscreenPageLimit(5);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mViewPager.addOnPageChangeListener(this);
        // 可滚动
        mTabLayout.setTabMode(MODE_SCROLLABLE);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mTabLayout.setupWithViewPager(mViewPager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);

        // 设置FloatingActionButton的点击事件
//        mFloatingActionButton.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_calendar) {
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    mYear = i;
                    mMonth = i1;
                    mDay = i2;
                    if (mMonth + 1 < 10) {
                        if (mDay < 10) {
                            days = new StringBuffer().append(mYear).append("-").append("0").
                                    append(mMonth + 1).append("-").append("0").append(mDay).toString();
                        } else {
                            days = new StringBuffer().append(mYear).append("-").append("0").
                                    append(mMonth + 1).append("-").append(mDay).toString();
                        }

                    } else {
                        if (mDay < 10) {
                            days = new StringBuffer().append(mYear).append("-").
                                    append(mMonth + 1).append("-").append("0").append(mDay).toString();
                        } else {
                            days = new StringBuffer().append(mYear).append("-").
                                    append(mMonth + 1).append("-").append(mDay).toString();
                        }

                    }
                    SnackbarUtil.showLong(MainActivity.this, days);
                }
            }, mYear, mMonth, mDay).show();
            return true;
        } else if (id == R.id.action_notify) {
            // 通知权限要打开才能显示
            NewMessageNotification.notify(this, "这是一条新信息", 1);
            return true;
        } else if (id == R.id.action_dialog) {
            ItemListDialogFragment itemListDialogFragment = ItemListDialogFragment.newInstance(50);
            itemListDialogFragment.show(getSupportFragmentManager(), "MainActivity");
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mToolbar.setTitle(mTitles[position]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 设置NavigationView中menu的item被选中后要执行的操作
     *
     * @param mNav NavigationView
     */
    private void onNavigationViewMenuItemSelected(NavigationView mNav) {
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                final int id = item.getItemId();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (id) {
                            case R.id.nav_login:
                                /** 登录*/
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                break;
                            case R.id.nav_gallery:
                                /** 滚动title置顶的scrollview*/
                                startActivity(new Intent(MainActivity.this, ScrollingActivity.class));
                                break;
                            case R.id.nav_slideshow:
                                /** ListView*/
                                startActivity(new Intent(MainActivity.this, ItemListActivity.class));
                                break;
                            case R.id.nav_manage:
                                /** 设置*/
                                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                                break;
                            case R.id.nav_share:
                                /** TabbedActivity*/
                                startActivity(new Intent(MainActivity.this, TabbedActivity.class));
                                break;
                            case R.id.nav_send:
                                /** BottomNavigatorActivity*/
                                startActivity(new Intent(MainActivity.this, BottomNavigatorActivity.class));
                                break;
                            case R.id.nav_full:
                                /** 全屏*/
                                startActivity(new Intent(MainActivity.this, FullscreenActivity.class));
                                break;
                            default:
                                break;
                        }
                    }

                }, 100);

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);// “right” -- end  "left" -- start
                return true;
            }
        });
    }

    /**
     * ItemListDialogFragment 的实现接口
     */
    @Override
    public void onItemClicked(int position) {
        Toast.makeText(this, "点中了第" + position + "个", Toast.LENGTH_LONG).show();
    }

    /**
     * PlusOneFragment 的实现接口
     */
    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, "uri:" + uri, Toast.LENGTH_LONG).show();
    }
}
