package io.ap1.beaconsdkandroid;

/**
 * Created by richthofen80 on 10/1/15.
 */
public class UrlContent {
    private String webPageContent;

    public UrlContent (String content){
        setUrlContent(content);
    }

    public String getUrlContent(){
        return webPageContent;
    }

    public void setUrlContent(String content){
        webPageContent = content;
    }
}
