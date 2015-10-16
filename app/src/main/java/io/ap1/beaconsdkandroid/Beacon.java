package io.ap1.beaconsdkandroid;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Date;

@DatabaseTable(tableName = "beacon")
public class Beacon {

    private final String LID = "lid";
    private final String ID = "id";
    private final String UUID = "uuid";
    private final String MAJOR = "major";
    private final String MINOR = "minor";
    private final String LATITUDE = "lat";
    private final String LONGITUDE = "lng";
    private final String X = "x";
    private final String Y = "y";
    private final String Z = "z";
    private final String UNIT = "unit";
    private final String NICKNAME = "nickName";
    private final String RSSI = "rssi";
    private final String TYPE = "type";
    private final String CID = "cID";
    private final String MACADDRESS = "macAddress";
    private final String CREATEDDATE = "createdDate";
    private final String LOCATION_FK = "location_fk";
    private final String BEACONURL = "URL";

    @DatabaseField(generatedId=true, useGetSet=true, columnName=LID)
    private int lID; // this field is for local auto increment
    @DatabaseField(useGetSet=true, columnName=ID)
    private String id;
    @DatabaseField(useGetSet = true, columnName = UUID)
    private String uuid;
    @DatabaseField(useGetSet = true, columnName = MAJOR)
    private String major;
    @DatabaseField(useGetSet = true, columnName = MINOR)
    private String minor;
    @DatabaseField(useGetSet = true, columnName = LATITUDE)
    private String lat;
    @DatabaseField(useGetSet = true, columnName = LONGITUDE)
    private String lng;
    @DatabaseField(useGetSet = true, columnName = X)
    private String x;  // for floor
    @DatabaseField(useGetSet = true, columnName = Y)
    private String y;  // for floor
    @DatabaseField(useGetSet = true, columnName = Z)
    private String z;  // for floor
    @DatabaseField(useGetSet = true, columnName = UNIT)
    private String unit;
    @DatabaseField(useGetSet = true, columnName = NICKNAME)
    private String nickName;
    @DatabaseField(useGetSet = true, columnName = RSSI)
    private String rssi;
    @DatabaseField(useGetSet = true, columnName = TYPE)
    private String type;
    @DatabaseField(useGetSet = true, columnName = CID)
    private String cID;
    @DatabaseField(useGetSet = true, columnName = MACADDRESS)
    private String macAddress;
    @DatabaseField(useGetSet = true, columnName = CREATEDDATE)
    private String createdDate;
    @DatabaseField(useGetSet = true, columnName = LOCATION_FK)
    private String location_fk;
    @DatabaseField(useGetSet = true, columnName = BEACONURL)
    private String URL;

    public Beacon(){
    }

    public int getLID(){
        return lID;
    }

    public void setLID(int lID){
        this.lID = lID;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getCID(){
        return cID;
    }

    public void setCID(String cID){
        this.cID = cID;
    }

    public String getCreatedDate(){
        return createdDate;
    }

    public void setCreatedDate(String createdOn){
        this.createdDate = createdOn;
    }

    public String getLat(){
        return lat;
    }

    public void setLat(String latitude){
        this.lat = latitude;
    }

    public String getLng(){
        return lng;
    }

    public void setLng(String longitude){
        this.lng = longitude;
    }

    public String getMacAddress(){
        return macAddress;
    }

    public void setMacAddress(String macAddress){
        this.macAddress = macAddress;
    }

    public String getUuid(){
        return uuid;
    }

    public void setUuid(String uuid){
        this.uuid = uuid;
    }

    public String getMajor(){
        return major;
    }

    public void setMajor(String major){
        this.major = major;
    }

    public String getMinor(){
        return minor;
    }

    public void setMinor(String minor){
        this.minor = minor;
    }

    public String getNickName(){
        return nickName;
    }

    public void setNickName(String nickName){
        this.nickName = nickName;
    }

    public String getRssi(){
        return rssi;
    }

    public void setRssi(String rssi){
        this.rssi = rssi;
    }

    public String getUnit(){
        return unit;
    }

    public void setUnit(String unit){
        this.unit = unit;
    }

    public String getX(){
        return x;
    }

    public void setX(String x){
        this.x = x;
    }

    public String getY(){
        return y;
    }

    public void setY(String Y){
        this.y = y;
    }

    public String getZ(){
        return z;
    }

    public void setZ(String z){
        this.z = z;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getLocation_fk(){
        return location_fk;
    }

    public void setLocation_fk(String location_fk){
        this.location_fk = location_fk;
    }

    public String getURL(){
        return URL;
    }

    public void setURL(String url){
        this.URL = url;
    }
}
