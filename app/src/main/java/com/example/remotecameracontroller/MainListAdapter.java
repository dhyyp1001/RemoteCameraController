package com.example.remotecameracontroller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MainListAdapter extends BaseAdapter {

    private Context mContext;
    private List mData;


    public MainListAdapter(Context context, List<MainListData> data){
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //아이템 한개의 뷰를 완성하는 곳
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //최초 로드 시
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_adapter_main, parent, false);
        }

        //view 가져오기
        TextView location = convertView.findViewById(R.id.data_location);
        TextView url = convertView.findViewById(R.id.data_url_address);

        //data 불러오기
        MainListData data = (MainListData) getItem(position);

        //view 에 data 삽입
        location.setText(data.getsName());
        //url.setText("url : "+data.getRtspUrl());

        return convertView;
    }
}
