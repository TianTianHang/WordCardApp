package com.mao.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.mao.adapter.WordAdapter;
import com.mao.event.DBEvent;
import com.mao.event.HttpEvent;
import com.mao.fragment.DrawerFragment;
import com.mao.fragment.MainFragment;
import com.mao.fragment.SettingsFragment;
import com.mao.fragment.WordCardFragment;
import com.mao.model.WordItem;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 订阅事件
        EventBus.getDefault().register(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        registerDrawerListener(drawer);
        toggle.syncState();
        init();
    }
    private void init(){
        initMain();
        initDrawer();
    }
    private void initMain(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainFragment mainFragment=new MainFragment();
        fragmentTransaction.replace(R.id.main_container,mainFragment);
        fragmentTransaction.commit();

    }
    private void initDrawer(){
        DrawerFragment drawerFragment = new DrawerFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.drawer_container, drawerFragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void registerDrawerListener(DrawerLayout drawer) {
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(HttpEvent event) {
        String message = event.message;
        // 处理事件
        Log.d("d", message);
        if (event.what == 1) {
            WordCardFragment wordCardFragment = new WordCardFragment((WordItem)event.obj);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_container, wordCardFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DBEvent event) {
        String type = event.type;
        // 处理事件
        Log.d("d", type);
        switch (type) {
            case "insert":
                initDrawer();
                break;
            case "delete":
                initDrawer();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.all_menu,menu);
        menu.setGroupVisible(R.id.group1,false);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit_menu_item:
                finish();
                return true;
            case R.id.setting_item:
                SettingsFragment settingsFragment=new SettingsFragment();
                FragmentManager fragmentManager=getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, settingsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}