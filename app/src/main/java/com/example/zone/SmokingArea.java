package com.example.zone;

import org.json.JSONException;
import org.json.JSONObject;

public class SmokingArea {
    private String _AreaName;
    private String _AreaDesc;
    private String _AreaRegDate;
    private String _AreaRegUser;
    private Double _Point;
    private Double _AreaLat;
    private Double _AreaLng;
    private int _ChkAircondition;
    private int _ChkRoof;
    private int _ChkBench;
    private int _ChkInside;
    private int _Report;
    private int _AreaType;


    public SmokingArea(JSONObject SmokingAreaInfo){
        try {
            _AreaName=SmokingAreaInfo.get("name").toString();
            _AreaDesc=SmokingAreaInfo.get("desc").toString();
            _AreaRegDate=SmokingAreaInfo.get("reg_date").toString();
            _AreaRegUser=SmokingAreaInfo.get("reg_user").toString();
            _AreaLat=SmokingAreaInfo.getDouble("lat");
            _AreaLng=SmokingAreaInfo.getDouble("lng");
            _Point=SmokingAreaInfo.getDouble("point");

//            _ChkAircondition=Integer.parseInt(SmokingAreaInfo.getString("vtl"));
//            _ChkRoof=Integer.parseInt(SmokingAreaInfo.getString("roof"));
//            _ChkBench=Integer.parseInt(SmokingAreaInfo.getString("bench"));
//            _ChkInside=(int)SmokingAreaInfo.get("inside");//서버쪽에 추가필요
            _AreaType=Integer.parseInt(SmokingAreaInfo.getString("type"));
            _Report=Integer.parseInt(SmokingAreaInfo.getString("report"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getSmokingAreaName(){ return _AreaName; }

    public String getSmokingAreaDesc(){ return _AreaDesc; }

    public Double getSmokingAreaPoint(){ return _Point;}

    public Double getSmokingAreaLat(){ return _AreaLat; }

    public Double getSmokingAreaLng(){ return _AreaLng; }

    public int getSmokinAreaType(){ return _AreaType; }
}