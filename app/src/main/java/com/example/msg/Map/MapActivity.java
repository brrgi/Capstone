package com.example.msg.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.msg.R;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapView mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

//        Intent intent = getIntent();
//        Double lat=intent.getExtras().getDouble("mLat");
//        Double lng=intent.getExtras().getDouble("mLng");
        //위도와 경도로 위치를 나타낼 수 있다.
        //그렇다면 주소를 입력해 위치와 경도를 구하는 api를 사용해야한다.
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(37.5546254, 126.8376356), 2, true);

    }
}
