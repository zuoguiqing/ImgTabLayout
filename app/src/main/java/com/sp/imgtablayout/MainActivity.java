package com.sp.imgtablayout;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.sp.imgtablibrary.ImgTabFragmentPagerAdapter;
import com.sp.imgtablibrary.ImgTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zgq
 * @version 1.0.0
 * @describe -- 自定义可以设置图片的TabLayout
 * @date 2019/3/22
 */
public class MainActivity extends AppCompatActivity {

    private ImgTabFragmentPagerAdapter adapter, adapter2, adapter3, adapter4, adapter5;
    private ViewPager vp, vp2, vp3, vp4, vp5;
    private ImgTabLayout tabLayout, tabLayout2, tabLayout3, tabLayout4, tabLayout5;
    private List<CharSequence> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout2 = findViewById(R.id.tabLayout2);
        tabLayout3 = findViewById(R.id.tabLayout3);
        tabLayout4 = findViewById(R.id.tabLayout4);
        tabLayout5 = findViewById(R.id.tabLayout5);
        vp = findViewById(R.id.vp);
        vp2 = findViewById(R.id.vp2);
        vp3 = findViewById(R.id.vp3);
        vp4 = findViewById(R.id.vp4);
        vp5 = findViewById(R.id.vp5);
        adapter = new ImgTabFragmentPagerAdapter(getSupportFragmentManager());
        adapter2 = new ImgTabFragmentPagerAdapter(getSupportFragmentManager());
        adapter3 = new ImgTabFragmentPagerAdapter(getSupportFragmentManager());
        adapter4 = new ImgTabFragmentPagerAdapter(getSupportFragmentManager());
        adapter5 = new ImgTabFragmentPagerAdapter(getSupportFragmentManager());

        tabLayout.setOnTabLayoutItemSelectListener(new ImgTabLayout.OnTabLayoutItemSelectListener() {
            @Override
            public void onTabLayoutItemSelect(int position) {
                Toast.makeText(MainActivity.this, data.get(position), Toast.LENGTH_SHORT).show();
                tabLayout2.setCurrentItem(position);
                tabLayout3.setCurrentItem(position);
                tabLayout4.setCurrentItem(position);
            }
        });

        setData();
        setData2();
        setData3();
        setData4();
        setData5();
    }

    private void setData() {
        data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(i + "水果" + i);
        }
        for (int i = 0; i < data.size(); i++) {
            TabFragment fragment = new TabFragment();
            fragment.setContent(data.get(i) + "");
            adapter.addFrag(fragment, data.get(i));
        }

        //设置viewPager
        vp.setAdapter(adapter);
        tabLayout.setViewPager(vp);
    }

    private void setData2() {
        for (int i = 0; i < data.size(); i++) {
            TabFragment fragment = new TabFragment();
            fragment.setContent(data.get(i) + "");
            adapter2.addFrag(fragment, data.get(i));
        }
        vp2.setAdapter(adapter2);
        tabLayout2.setViewPager(vp2);
    }

    private void setData3() {
        for (int i = 0; i < data.size(); i++) {
            TabFragment fragment = new TabFragment();
            fragment.setContent(data.get(i) + "");
            adapter3.addFrag(fragment, data.get(i));
        }
        tabLayout3.setTextGravity(Gravity.CENTER);
        vp3.setAdapter(adapter3);
        tabLayout3.setViewPager(vp3);
    }

    private void setData4() {
        for (int i = 0; i < data.size(); i++) {
            TabFragment fragment = new TabFragment();
            fragment.setContent(data.get(i) + "");
            adapter4.addFrag(fragment, data.get(i));
        }
        tabLayout4.setTextGravity(Gravity.CENTER);
        vp4.setAdapter(adapter4);
        tabLayout4.setViewPager(vp4);
    }

    private void setData5() {
        List<CharSequence> list = new ArrayList<>();
        list.add("这是苹果");
        list.add("那是香蕉");
        list.add("还有橘子");
        list.add("望着榴莲");
        tabLayout5.setViewWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        tabLayout5.setAverageTab(true, getResources().getDisplayMetrics().widthPixels);
        //只设置tab
        tabLayout5.setTabData(list, 0);
    }

}
