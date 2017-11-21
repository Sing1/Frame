package sing.bean;

import java.io.Serializable;

import sing.f.FragMainTab;


public class MainBean implements Serializable {

    public int index;// position
    public Class<? extends FragMainTab> clazz;// Fragment
    public int resId;// 文字，即底部显示的文字
    public int fragmentId;// index一样
    public int layoutId;// 布局id

    public MainBean(int index, Class<? extends FragMainTab> clazz, int resId, int layoutId) {
        this.index = index;
        this.clazz = clazz;
        this.resId = resId;
        this.fragmentId = index;
        this.layoutId = layoutId;
    }
}
