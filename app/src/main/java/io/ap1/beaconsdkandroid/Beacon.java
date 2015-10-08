package io.ap1.beaconsdkandroid;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Date;

@DatabaseTable(tableName = "beacon")
public class Beacon {

    private final String ID = "id";
    private final String CID = "cid";
    private final String CREATEDON = "createdOn";
    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private final String MACADDRESS = "macAddress";
    private final String UUID = "uuid";
    private final String MAJOR = "major";
    private final String MINOR = "minor";
    private final String NICKNAME = "nickname";
    private final String RSSI = "rssi";
    private final String UNIT = "unit";
    private final String URL = "url";
    private final String X = "x";
    private final String Y = "y";
    private final String Z = "z";
    private final String URLCONTENT = "urlContent";

    @DatabaseField(generatedId=true, useGetSet=true, columnName=ID)
    private int id;
    @DatabaseField(useGetSet = true, columnName = CID)
    private String cid;
    @DatabaseField(useGetSet = true, columnName = CREATEDON)
    private Date createdOn;
    @DatabaseField(useGetSet = true, columnName = LATITUDE)
    private String latitude;
    @DatabaseField(useGetSet = true, columnName = LONGITUDE)
    private String longitude;
    @DatabaseField(useGetSet = true, columnName = MACADDRESS)
    private String macAddress;
    @DatabaseField(useGetSet = true, columnName = UUID)
    private String uuid;
    @DatabaseField(useGetSet = true, columnName = MAJOR)
    private String major;
    @DatabaseField(useGetSet = true, columnName = MINOR)
    private String minor;
    @DatabaseField(useGetSet = true, columnName = NICKNAME)
    private String nickName;
    @DatabaseField(useGetSet = true, columnName = RSSI)
    private String rssi;
    @DatabaseField(useGetSet = true, columnName = UNIT)
    private String unit;
    @DatabaseField(useGetSet = true, columnName = URL)
    private String url;
    @DatabaseField(useGetSet = true, columnName = X)
    private String x;  //what's this?
    @DatabaseField(useGetSet = true, columnName = Y)
    private String y;  //and this?
    @DatabaseField(useGetSet = true, columnName = Z)
    private String z;  //and this?
    @DatabaseField(useGetSet = true, columnName = URLCONTENT)
    private UrlContent urlContent;

    public Beacon(){
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getCid(){
        return cid;
    }

    public void setCid(String cid){
        this.cid = cid;
    }

    public Date getCreatedOn(){
        return createdOn;
    }

    public void setCreatedOn(Date createdOn){
        this.createdOn = createdOn;
    }

    public String getLatitude(){
        return latitude;
    }

    public void setLatitude(String latitude){
        this.latitude = latitude;
    }

    public String getLongitude(){
        return longitude;
    }

    public void setLongitude(String longitude){
        this.longitude = longitude;
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

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
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

    public UrlContent getUrlContent(){
        return urlContent;
    }

    public void setUrlContent(UrlContent urlContent){
        this.urlContent = urlContent;
    }

}
