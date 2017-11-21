# Frame 
 
![](./app/src/main/res/mipmap-xhdpi/demo.gif "")   
这是一个常见的底部切换封装，即主框架：ViewPager+PagerSlidingTabStrip。

### Maven：
```XML
<dependency>
    <groupId>sing.frame</groupId>
    <artifactId>library</artifactId>
    <version>1.0.1</version>
    <type>pom</type>
</dependency>
```
### Gradle：
```XML
dependencies {
    ...
    compile 'sing.frame:library:1.0.1'
}
```

### Ivy：
```XML
<dependency org='sing.frame' name='library' rev='1.0.1'>
    <artifact name='library' ext='pom' ></artifact>
</dependency>
```
### 具体使用：
首先是XML，很简单的 ViewPager + PagerSlidingTabStrip：

```XML
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <android.support.v4.view.ViewPager
        android:id="@+id/main_tab_pager"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"/>

    <sing.PagerSlidingTabStrip
        android:id="@+id/tabs"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:background="#ffffff"/>
</LinearLayout>
```
然后是设置 ViewPager 相关：

```JAVA
private int scrollState;
private TabAdapter adapter;// 引用中的adapter

// 设置viewPager
private void setupPager() {
    List<MainBean> list = new ArrayList<>();
    // 准备好4个继承与FragMainTab的Fragment和对应的布局
    // MainBean，位置，类，底部显示文字，布局
    list.add(new MainBean(0, AA.class, R.string.a, R.layout.aaa));
    list.add(new MainBean(1, BA.class, R.string.b, R.layout.bbb));
    list.add(new MainBean(2, CA.class, R.string.c, R.layout.ccc));
    list.add(new MainBean(3, DA.class, R.string.d, R.layout.ddd));

    adapter = new TabAdapter(getSupportFragmentManager(), this, list);
    // 缓存页面的个数
    pager.setOffscreenPageLimit(adapter.getCacheCount());
    // 转场动画，带一个FadeInOutPageTransformer渐变
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

private void selectPage(int page) {
    if (scrollState == ViewPager.SCROLL_STATE_IDLE) {
        adapter.onPageSelected(pager.getCurrentItem());
    }
}
```
然后设置TAB相关：

```JAVA
private void setupTabs() {
	 // 底部显示的文字，与list中相同，以数组的形式传
    int str[] = new int[]{R.string.a, R.string.b, R.string.c, R.string.d};
    // 底部显示的图片，以数组的形式传
    int icon[] = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d};
    // 选中的底部显示的图片，以数组的形式传
    int iconPress[] = new int[]{R.drawable.a_p, R.drawable.b_p, R.drawable.c_p, R.drawable.d_p};
    // 传给tabs，文字与图片的个数一定要相等
    tabs.setTabValue(str, icon, iconPress);

    tabs.setOnCustomTabListener(new PagerSlidingTabStrip.OnCustomTabListener() {
        @Override
        public int getTabLayoutResId(int position) {
            return R.layout.tab_layout_main;// tab的布局，文字的id为tab_title，图的id为tab_image，有空再优化
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
```
而4个Fragment需要继承于FragMainTab，需要复写`onInit()`方法,用于初始化：

```JAVA
@Override
protected void onInit() {
	TextView tv = (TextView) getView().findViewById(R.id.tv_aaa);
	tv.setText("qqqqq");
}    
```
如果想提交加载的话只需要执行一下`FragMainTab`中的`onCurrent()`方法即可。