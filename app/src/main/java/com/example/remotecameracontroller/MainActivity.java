package com.example.remotecameracontroller;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import network_resurce.PostLoginData;

public class MainActivity extends AppCompatActivity {
    List<MainListData> data;
    MainListAdapter adapter;
    ListView listView;
    Long mLastClickTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
        data = new ArrayList<>();

        Gson gson = new Gson();
        data = gson.fromJson(PostLoginData.listValues, new TypeToken<ArrayList<MainListData>>(){}.getType());

        adapter = new MainListAdapter(this, data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            //리스트 중복선택 지연
            if (SystemClock.elapsedRealtime() - mLastClickTime > 1000) {

                //클릭시 이벤트
                MainListData selectedData = (MainListData) adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, DataDetailActivity.class);
                intent.putExtra("mediaUri", selectedData.getMediaUri());
                intent.putExtra("modIP", selectedData.getModIP());

                startActivity(intent);
            }
            mLastClickTime = SystemClock.elapsedRealtime();
        });
    }
}