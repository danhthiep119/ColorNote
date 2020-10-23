package com.example.colornote.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;

import com.example.colornote.R;

public class WebView extends AppCompatActivity {
    String url;
    android.webkit.WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getDataFromIntent();
        AddControls();
        AddEvents();
    }

    private void getDataFromIntent() {
        Intent intent=getIntent();
        url = intent.getStringExtra("PUSHDATA");
    }


    private void AddEvents() {
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);
    }

    private void AddControls() {
        webView=findViewById(R.id.web_view);
    }
}