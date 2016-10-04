package com.example.rxexample.chapter02;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rxexample.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class BottomFragment extends Fragment {
    TextView _tapEventTxtShow;
    TextView _tapEventCountShow;
    private RxBus _rxBus;
    private Disposable _disposable1;
    private Disposable _disposable2;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_bottom, container, false);
        _tapEventTxtShow = (TextView) layout.findViewById(R.id.demo_rxbus_tap_txt);
        _tapEventCountShow = (TextView) layout.findViewById(R.id.demo_rxbus_tap_count);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _rxBus = RxBus.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();

        //将普通的Observable转换为可连接的Observable
        Flowable<TapEvent> tapEventEmitter = _rxBus.toFlowable(TapEvent.class);

        _disposable1 = tapEventEmitter
                .subscribe(new Consumer<TapEvent>() { //一个一旦被触发就会显示TapText的监听者
                    @Override
                    public void accept(TapEvent event) {
                            _showTapText();
                    }
                });
       _disposable2 = tapEventEmitter.buffer(tapEventEmitter.debounce(1, TimeUnit.SECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TapEvent>>() {
                               @Override
                               public void accept(List<TapEvent> tapEvents) throws Exception {
                                   _showTapCount(tapEvents.size());
                               }
                           });
    }

    @Override
    public void onStop() {
        super.onStop();
        _disposable1.dispose();
        _disposable2.dispose();
    }

    // -----------------------------------------------------------------------------------
    // Helper to show the text via an animation

    /**
     * 显示TapText
     */
    private void _showTapText() {
        _tapEventTxtShow.setVisibility(View.VISIBLE);
        _tapEventTxtShow.setAlpha(1f);
        ViewCompat.animate(_tapEventTxtShow).alphaBy(-1f).setDuration(400);
    }

    private void _showTapCount(int size) {
        _tapEventCountShow.setText(String.valueOf(size));
        _tapEventCountShow.setVisibility(View.VISIBLE);
        _tapEventCountShow.setScaleX(1f);
        _tapEventCountShow.setScaleY(1f);
        ViewCompat.animate(_tapEventCountShow)
                .scaleXBy(-1f)
                .scaleYBy(-1f)
                .setDuration(800)
                .setStartDelay(100);
    }
}
