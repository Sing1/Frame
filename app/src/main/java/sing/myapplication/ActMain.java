package sing.myapplication;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import sing.FadeInOutPageTransformer;
import sing.PagerSlidingTabStrip;
import sing.bean.MainBean;
import sing.frame.TabAdapter;
import sing.myapplication.fragment.AA;
import sing.myapplication.fragment.BA;
import sing.myapplication.fragment.CA;
import sing.myapplication.fragment.DA;

public class ActMain extends AppCompatActivity {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private int scrollState;
    private TabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.main_tab_pager);

        setupPager();
        setupTabs();
    }

    private void selectPage(int page) {
        if (scrollState == ViewPager.SCROLL_STATE_IDLE) {
            adapter.onPageSelected(pager.getCurrentItem());
        }
    }

    /**
     * 设置viewPager
     */
    private void setupPager() {
        List<MainBean> list = new ArrayList<>();
        list.add(new MainBean(0, AA.class, R.string.aaa, R.layout.aaa));
        list.add(new MainBean(1, BA.class, R.string.bbb, R.layout.bbb));
        list.add(new MainBean(2, CA.class, R.string.ccc, R.layout.ccc));
        list.add(new MainBean(3, DA.class, R.string.ddd, R.layout.ddd));

        adapter = new TabAdapter(getSupportFragmentManager(), this, list);
        pager.setOffscreenPageLimit(adapter.getCacheCount());
        pager.setPageTransformer(true, new FadeInOutPageTransformer());
        pager.setAdapter(adapter);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabs.onPageScrolled(position, positionOffset, positionOffsetPixels);
                adapter.onPageScrolled(position);
            }

            @Override
            public void onPageSelected(int position) {
                tabs.onPageSelected(position);
                selectPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                tabs.onPageScrollStateChanged(state);

                scrollState = state;

                selectPage(pager.getCurrentItem());
            }
        });
    }

    /**
     * 设置tab条目
     */
    private void setupTabs() {
        int str[] = new int[]{R.string.aaa, R.string.bbb, R.string.ccc, R.string.ddd};
        int icon[] = new int[]{R.drawable.yx_message, R.drawable.yx_friend, R.drawable.yx_group, R.drawable.yx_setting};
        int iconPress[] = new int[]{R.drawable.yx_message_rest, R.drawable.yx_friend_rest, R.drawable.yx_group_rest, R.drawable.yx_setting_set};
        tabs.setTabValue(str, icon, iconPress);

        tabs.setOnCustomTabListener(new PagerSlidingTabStrip.OnCustomTabListener() {
            @Override
            public int getTabLayoutResId(int position) {
                return R.layout.tab_layout_main;
            }

            @Override
            public boolean screenAdaptation() {
                return true;
            }
        });
        tabs.setViewPager(pager);
        tabs.setOnTabClickListener(adapter);
        tabs.setOnTabDoubleTapListener(adapter);
    }
}