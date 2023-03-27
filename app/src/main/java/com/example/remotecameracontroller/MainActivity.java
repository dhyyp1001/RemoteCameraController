package com.example.remotecameracontroller;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

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

        //-- db 연결시 해당내용 변경
        data.add(new MainListData("자동기 사무실 내부", "rtsp://admin:wkehdrl6320@61.37.102.154:554/profile2/media.smp", "61.37.102.154"));
        data.add(new MainListData("외부 시설 도로", "rtsp://admin:Tldosdptmdk2@223.171.40.250:554/profile2/media.smp", "61.37.102.154"));
        //--

        adapter = new MainListAdapter(this, data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            //리스트 중복선택 지연
            if (SystemClock.elapsedRealtime() - mLastClickTime > 1000) {

                //클릭시 이벤트
                MainListData selectedData = (MainListData) adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, DataDetailActivity.class);
                intent.putExtra("url", selectedData.getUrl());
                intent.putExtra("modUrl", selectedData.getModUrl());

                startActivity(intent);
            }
            mLastClickTime = SystemClock.elapsedRealtime();
        });
    }
}