package com.example.android.recyclerview;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class WebviewActivity extends AppCompatActivity {
    private WebView mWebView;
    final static String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mWebView = (WebView) findViewById(R.id.wv_video);
        mWebView.getSettings().setJavaScriptEnabled(true);
        //mWebView.setWebChromeClient(new WebChromeClient() {});
        Intent intent = getIntent();
        String key = intent.getStringExtra(DetailActivity.VIDEO_KEY);
        String youtubeURL = YOUTUBE_URL + key;
        mWebView.loadUrl(youtubeURL);
    }
}
