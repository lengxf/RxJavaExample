package com.example.rxexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    String[] chapters = {"chapter01", "chapter02","rxbus"};

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list_view);
        List<HashMap<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < chapters.length; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("chapter", chapters[i]);
            list.add(map);
        }

        SimpleAdapter arrayAdapter = new SimpleAdapter(this, list, R.layout.chapter_item, new String[]{"chapter"}, new int[]{R.id.chapter_btn});

        mListView.setAdapter(arrayAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(chapters[position]);
                startActivity(intent);
            }
        });
    }
}
