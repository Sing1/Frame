package sing.myapplication.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import sing.f.FragMainTab;
import sing.myapplication.R;

public class AA extends FragMainTab {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        handler.sendEmptyMessageDelayed(1,1000);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            onCurrent();
        }
    };

    @Override
    protected void onInit() {
        TextView tv = (TextView) getView().findViewById(R.id.tv_aaa);
        tv.setText("qqqqq");
    }
}