package sing.frame;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import sing.SlidingTabPagerAdapter;
import sing.bean.MainBean;
import sing.f.FragMainTab;

public class TabAdapter extends SlidingTabPagerAdapter {

    private List<MainBean> list = new ArrayList<>();

    @Override
    public int getCacheCount() {
        return list.size();
    }

    public TabAdapter(FragmentManager fm, Context context, List<MainBean> list) {
        super(fm, list.size(), context.getApplicationContext());
        this.list = list;
        for (MainBean tab : list) {
            try {
                FragMainTab fragment = null;
                List<Fragment> fs = fm.getFragments();
                if (fs != null) {
                    for (Fragment f : fs) {
                        if (f.getClass() == tab.clazz) {
                            fragment = (FragMainTab) f;
                            break;
                        }
                    }
                }

                if (fragment == null) {
                    fragment = tab.clazz.newInstance();
                }

                fragment.attachTabData(tab);

                fragments[tab.index] = fragment;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        MainBean tab = fromTabIndex(position);
        int resId = tab != null ? tab.resId : 0;
        return resId != 0 ? context.getText(resId) : "";
    }

    private MainBean fromTabIndex(int tabIndex) {
        for (MainBean value : list) {
            if (value.index == tabIndex) {
                return value;
            }
        }
        return null;
    }
}