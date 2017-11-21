package sing.myapplication.fragment;

import android.os.Bundle;
import android.widget.TextView;

import sing.f.FragMainTab;
import sing.myapplication.R;

public class BA extends FragMainTab {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        onCurrent(); // 触发onInit，提前加载
    }

    @Override
    protected void onInit() {
        TextView tv = getView().findViewById(R.id.tv_aaa);
        tv.setText("qqqqq");
    }
}
