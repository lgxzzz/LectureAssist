package com.lecture.assist;

import android.app.Application;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.avos.avoscloud.AVOSCloud;
import com.lecture.assist.navi.GeoSearchMgr;
import com.lecture.assist.navi.LocationMgr;
import com.lecture.assist.navi.PoiSearchMgr;

/**
 * Created by lgx on 2019/4/24.
 */

public class LectureAssistApplication extends Application {
    //搜索模块
    private PoiSearchMgr mPoiSearchMgr;
    //定位模块
    private LocationMgr mLocationMgr;
    //逆地理编码
    private GeoSearchMgr mGeoSearchMgr;
    @Override
    public void onCreate() {
        super.onCreate();

        AVOSCloud.initialize(this,"PAVyPpTl9PQcqgvmQLmRDx0Q-gzGzoHsz","QiXmWNDNvykFuCrPuL677dgC");
        AVOSCloud.setDebugLogEnabled(true);

        mLocationMgr = new LocationMgr(this);
        getPosition();
    }


    //获取定位信息并且查询当前的POI点周边
    public void getPosition(){
        mLocationMgr.getLonLat(this, new LocationMgr.LonLatListener() {
            @Override
            public void getLonLat(AMapLocation aMapLocation) {

            }
        });
    }
}
