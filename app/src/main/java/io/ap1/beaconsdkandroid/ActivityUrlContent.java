package io.ap1.beaconsdkandroid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

public class ActivityUrlContent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_content);


        WebView wv_url_content = (WebView) findViewById(R.id.wv_url_content);
        wv_url_content.loadUrl("file:///" + getIntent().getStringExtra("filePath"));
    }

}
