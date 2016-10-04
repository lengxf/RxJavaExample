package com.example.rxexample.chapter02;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rxexample.R;


public class TopFragment extends Fragment {
    private RxBus _rxBus;
    private Button mTapBtn;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_top, container, false);
        mTapBtn = (Button) layout.findViewById(R.id.btn_demo_rxbus_tap);
        mTapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapButtonClicked();
            }
        });
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _rxBus =RxBus.getInstance();
    }

    public void onTapButtonClicked() {
        _rxBus.postEvent(new TapEvent());
    }
}
