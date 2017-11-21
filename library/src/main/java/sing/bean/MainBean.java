package sing.bean;

import java.io.Serializable;

import sing.f.FragMainTab;


public class MainBean implements Serializable {

    public int index;
    public Class<? extends FragMainTab> clazz;
    public int resId;
    public int fragmentId;
    public int layoutId;

    public MainBean(int index, Class<? extends FragMainTab> clazz, int resId, int layoutId) {
        this.index = index;
        this.clazz = clazz;
        this.resId = resId;
        this.fragmentId = index;
        this.layoutId = layoutId;
    }
}
