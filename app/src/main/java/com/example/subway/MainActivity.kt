package com.example.subway

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var editTextDeparture: EditText
    private lateinit var editTextDestination: EditText

    @SuppressLint("JavascriptInterface", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webview)
        editTextDeparture = findViewById(R.id.editTextDeparture)
        editTextDestination = findViewById(R.id.editTextDestination)

        val webSettings: WebSettings = webView.settings

        webSettings.javaScriptEnabled = true //JavaScript 사용 가능하도록 설정
        // 웹뷰가 전체 화면에 꽉 차도록 설정
        webSettings.loadWithOverviewMode = true
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.useWideViewPort = true
        webSettings.setSupportZoom(true)

        // 웹뷰가 처음에는 일부만 보이고, 사용자가 원하는 만큼 확대/축소할 수 있도록 초기 확대 비율 설정
        val initialScalePercent = 150 // 원하는 비율 (예: 120%)
        webView.setInitialScale(initialScalePercent)

//        // 줌 기능 활성화
//        webView.settings.setBuiltInZoomControls(true)
//        webView.settings.displayZoomControls = false
//
//        // 줌 인/아웃을 가능하도록 터치 제스처를 활성화
//        webView.settings.builtInZoomControls = true
//        webView.settings.setSupportZoom(true)

        webView.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                addOnClickToElements()
            }
        }
        // 자바스크립트 인터페이스 등록
        webView.addJavascriptInterface(JavaScriptInterface(), "AndroidInterface")
        webView.loadUrl("file:///android_res/raw/mapimage.svg") // 앱의 raw 폴더에 있는 SVG 파일 로드
    }
    private fun addOnClickToElements(){
            val javascriptCode = """
                 (function() {
                    var elements = document.querySelectorAll('[class^="st37 st28"]');
                    for (var i = 0; i < elements.length; i++) {
                        var element = elements[i];
                        var stationName = element.textContent; // 해당 역 이름
                        element.setAttribute('onclick', 'handleTextClick(\"' + stationName + '\")');
                    }
                })()
            """.trimIndent()
            webView.evaluateJavascript(javascriptCode,null)
    }
    inner class JavaScriptInterface{
        @android.webkit.JavascriptInterface
        fun handleTextClick(stationName: String){
            Log.d("webViewEvent","역이름클릭:$stationName")
            runOnUiThread {
                if (TextUtils.isEmpty(editTextDeparture.text)) {
                    editTextDeparture.setText(stationName)
                } else {
                    editTextDestination.setText(stationName)
                }
            }
            Toast.makeText(applicationContext,"Clicked:$stationName",Toast.LENGTH_LONG).show()
        }
    }
}