package sing.f;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sing.bean.MainBean;
import sing.frame.R;

public abstract class FragMainTab extends Fragment {

    private boolean loaded = false;
    private MainBean bean;

    protected abstract void onInit();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_tab_fragment_container, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void attachTabData(MainBean bean) {
        this.bean = bean;
    }

    public void onCurrent() {
        if (!loaded && loadRealLayout()) {
            loaded = true;
            onInit();
        }
    }

    private boolean loadRealLayout() {
        ViewGroup root = (ViewGroup) getView();
        if (root != null) {
            try {
                root.removeAllViewsInLayout();
                View.inflate(root.getContext(), bean.layoutId, root);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return root != null;
    }

    /**
     * leave current page
     */
    public void onLeave() {
    }

    /**
     * notify current scrolled
     */
    public void onCurrentScrolled() {
    }

    public void onCurrentTabClicked() {
    }

    public void onCurrentTabDoubleTap(){
    }
}
