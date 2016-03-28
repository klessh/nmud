package com.iillyyaa2033.mud.editor.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView webview = new WebView(this);

		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new WebViewClient() {
				public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
					Toast.makeText(HelpActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
				}
			});
		webview.setWebContentsDebuggingEnabled(true);
		webview.loadUrl("file:///android_asset/test.html");
		setContentView(webview);
	}
	
}
