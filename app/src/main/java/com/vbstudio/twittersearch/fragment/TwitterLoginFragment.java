package com.vbstudio.twittersearch.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebIconDatabase;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.vbstudio.twittersearch.R;
import com.vbstudio.twittersearch.widgets.TwitterLoginWebViewClient;

public class TwitterLoginFragment extends BaseFragment implements OnClickListener {
	
	private Bundle transactionDetails;
	private WebView webView;
	private WebSettings webPageSettings;
	private String url;


	public static BaseFragment newInstance(Bundle transactionDetails) {
        TwitterLoginFragment paymentFragment = new TwitterLoginFragment();
		paymentFragment.setTransactionDetails(transactionDetails);
		paymentFragment.setUrl(transactionDetails.getString("url"));
		return paymentFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setTitle("Twitter Login");
        setIsLogoutEnabled(false);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
		// inflate and return the layout
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        webView = (WebView) view.findViewById(R.id.webView);
	    
	    return view;
	}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebIconDatabase.getInstance().open(getActivity().getDir("icons", getActivity().MODE_PRIVATE).getPath());

        webPageSettings = webView.getSettings();
        webPageSettings.setJavaScriptEnabled(true);
        webPageSettings.setLoadWithOverviewMode(true);
        webPageSettings.setUseWideViewPort(true);
        webPageSettings.setSupportZoom(true);
        webPageSettings.setBuiltInZoomControls(true);
        webPageSettings.setDomStorageEnabled(true);
        webPageSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new TwitterLoginWebViewClient(getActivity()));
        webView.loadUrl(url);
    }

	@Override
	public void onResume() {
	    super.onResume();
	}
	
	@Override
	public void onClick(View view) {
		int id = view.getId();
		
	}

    /***************************/
	
	public Bundle getTransactionDetails() {
		return transactionDetails;
	}

	public void setTransactionDetails(Bundle transactionDetails) {
		this.transactionDetails = transactionDetails;
	}
	
	public WebView getWebView() {
		return webView;
	}

	public void setWebView(WebView webView) {
		this.webView = webView;
	}

	public WebSettings getWebPageSettings() {
		return webPageSettings;
	}

	public void setWebPageSettings(WebSettings webPageSettings) {
		this.webPageSettings = webPageSettings;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
