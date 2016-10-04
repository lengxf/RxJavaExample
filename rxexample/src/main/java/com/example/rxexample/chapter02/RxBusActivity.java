package com.example.rxexample.chapter02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rxexample.R;

public class RxBusActivity extends AppCompatActivity {


    RxBus _rxBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_bus);
        _rxBus=RxBus.getInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.demo_rxbus_frag_1, new TopFragment())
                .replace(R.id.demo_rxbus_frag_2, new BottomFragment())
                .commit();
    }
}
