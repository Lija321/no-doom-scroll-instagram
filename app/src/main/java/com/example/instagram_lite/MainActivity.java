package com.example.instagram_lite;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebBackForwardList;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final boolean debug = false;

    private WebView webView;
    final String insta_url = "https://www.instagram.com/";
    //final String insta_url="https://www.google.com/";

    private Button button;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webview);
        button  = (Button) findViewById(R.id.button);

        Handler handler = new Handler();


        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                postReelEscape();
            }
        };
        handler.postDelayed(myRunnable, 1000);


        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);
        webView.loadUrl(insta_url);
        CookieManager.getInstance().flush();

        button.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, webView.getUrl(), Toast.LENGTH_SHORT).show();
        });

        if(!debug){
            button.setVisibility(View.GONE);
            Log.i("DEBUG", "Button is deactivated");
        }
    }

    private void postReelEscape(){
        String url = webView.getUrl();
        if(url.contains("reel")){
            Log.i("DEBUG", "Reel detected");
            if(webView.canGoBack()){
                Log.i("DEBUG", "Going back");
                webView.goBack();
            }else {
                webView.loadUrl(insta_url);
            }
        }
        else if(url.contains("/p/")){
            WebBackForwardList history = webView.copyBackForwardList();
            int index = history.getCurrentIndex();
            String lastUrl = history.getItemAtIndex(index-1).getUrl();
            if(lastUrl.contains("explore")){
                Log.i("DEBUG", "explore detected");
                if(webView.canGoBack()){
                    Log.i("DEBUG", "Going back");
                    webView.goBack();
                }else {
                    webView.loadUrl(insta_url);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
