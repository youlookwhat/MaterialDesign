package com.example.jingbin.materialdesign;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
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

import com.example.jingbin.materialdesign.activity.BottomNavigatorActivity;
import com.example.jingbin.materialdesign.activity.FullscreenActivity;
import com.example.jingbin.materialdesign.activity.LoginActivity;
import com.example.jingbin.materialdesign.activity.ScrollingActivity;
import com.example.jingbin.materialdesign.activity.SettingsActivity;
import com.example.jingbin.materialdesign.activity.TabbedActivity;
import com.example.jingbin.materialdesign.list.ItemListActivity;
import com.example.jingbin.materialdesign.main.MyFragment;
import com.example.jingbin.materialdesign.main.adapter.MyViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

/**
 * Created by jingbin on 16/9/10.
 */
public class MainActivity extends AppCompatActivity
        implements ViewPager.OnPageChangeListener {

    //初始化各种控件，照着xml中的顺序写
    private DrawerLayout mDrawerLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FloatingActionButton mFloatingActionButton;
    private NavigationView mNavigationView;

    // TabLayout中的tab标题
    private String[] mTitles;
    // 填充到ViewPager中的Fragment
    private List<Fragment> mFragments;
    // ViewPager的数据适配器
    private MyViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initData();
//        initView();
        configViews();
    }


    private void initData() {

        // Tab的标题采用string-array的方法保存，在res/values/arrays.xml中写
        mTitles = getResources().getStringArray(R.array.tab_titles);

        //初始化填充到ViewPager中的Fragment集合
        mFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            Bundle mBundle = new Bundle();
            mBundle.putInt("flag", i);
            MyFragment mFragment = new MyFragment();
            mFragment.setArguments(mBundle);
            mFragments.add(i, mFragment);
        }
    }

    private void initViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.id_coordinatorlayout);
//        mAppBarLayout = (AppBarLayout) findViewById(R.id.id_appbarlayout);
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.id_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mNavigationView = (NavigationView) findViewById(R.id.id_navigationview);
//        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.id_floatingactionbutton);
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
        mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), mTitles, mFragments);
        mViewPager.setAdapter(mViewPagerAdapter);
        // 设置ViewPager最大缓存的页面个数
        mViewPager.setOffscreenPageLimit(5);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mViewPager.addOnPageChangeListener(this);

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
        if (id == R.id.action_settings) {
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
     * @param mNav
     */
    private void onNavigationViewMenuItemSelected(NavigationView mNav) {
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_login) {
                    /** 登录*/
                    // Handle the camera action
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //execute the task
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        }
                    }, 100);

                } else if (id == R.id.nav_gallery) {
                    /** 滚动title置顶的scroolview*/
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //execute the task
                            startActivity(new Intent(MainActivity.this, ScrollingActivity.class));
                        }
                    }, 100);

                } else if (id == R.id.nav_slideshow) {
                    /** listview*/
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //execute the task
                            startActivity(new Intent(MainActivity.this, ItemListActivity.class));
                        }
                    }, 100);
                } else if (id == R.id.nav_manage) {
                    /** 设置*/
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //execute the task
                            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        }
                    }, 100);

                } else if (id == R.id.nav_share) {
                    /** TabbedActivity*/
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //execute the task
                            startActivity(new Intent(MainActivity.this, TabbedActivity.class));
                        }
                    }, 100);

                } else if (id == R.id.nav_send) {
                    /** BottomNavigatorActivity*/
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //execute the task
                            startActivity(new Intent(MainActivity.this, BottomNavigatorActivity.class));
                        }
                    }, 100);

                }else if (id == R.id.nav_full) {
                    /** BottomNavigatorActivity*/
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //execute the task
                            startActivity(new Intent(MainActivity.this, FullscreenActivity.class));
                        }
                    }, 100);
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);// “right” -- end  "left" -- start
                return true;
            }
        });
    }

}
