package com.grad.information.news;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.grad.databinding.ActivityNewsDetailBinding;
import com.grad.service.UserService;

public class NewsDetailActivity extends AppCompatActivity {
    ActivityNewsDetailBinding mBinding;
    private String mHtml;
    private String mNewsId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHtml = getIntent().getStringExtra("html");
        mNewsId = getIntent().getStringExtra("news_id");
        mBinding = ActivityNewsDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initView();
    }

    private void initView(){
        mBinding.webView.getSettings().setJavaScriptEnabled(true);
        mBinding.webView.getSettings().setLoadWithOverviewMode(true);
        mBinding.webView.getSettings().setDomStorageEnabled(true);
        mBinding.webView.getSettings().setSupportMultipleWindows(true);
        mBinding.webView.getSettings().setDomStorageEnabled(true);
        mBinding.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mBinding.webView.getSettings().setLoadsImagesAutomatically(true);
        mBinding.webView.setWebViewClient(new MyWebViewClient());
        mBinding.webView.loadData(mHtml, "text/html", "utf-8");
        UserService.increaseNewsViewCnt(mNewsId);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mBinding.webView.canGoBack()) {
            mBinding.webView.goBack();
            return true;
        }     // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            injectJavascript();
        }
    }

    private void injectJavascript() {
        mBinding.webView.loadUrl("javascript:(function() { " +
                "var imgs = document.getElementsByTagName('img');" +
                "for (var i = 0; i < imgs.length; i++) {" +
                "    imgs[i].onclick = function() { " +
                "        var imgUrl = this.src || this.getAttribute('data-src');" +
                "        if (!imgUrl) return;" +
                "        var div = document.createElement('div');" +
                "        div.setAttribute('style', 'position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,255); z-index: 999;');" +
                "        div.setAttribute('id', 'enlargedImgDiv');" +
                "        var img = document.createElement('img');" +
                "        img.setAttribute('style', 'max-width: 100%; max-height: 100%; margin: auto; position: absolute; top: 0; left: 0; right: 0; bottom: 0;');" +
                "        img.setAttribute('src', imgUrl);" +
                "        div.appendChild(img);" +
                "        document.body.appendChild(div);" +
                "        div.onclick = function() { " +
                "            document.body.removeChild(div);" +
                "        } " +
                "    }" +
                "}" +
                "})()");
    }

}