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

    private  var isDepartureMode = true //출발역 선택 모드 여부를 나타내는 변수
    // 현재 선택된 검색창 상태 (true: 출발역, false: 도착역)
    //출발역 선택할때는 true, 도착역 선택할때는 false
    private var departureStation: String? = null
    private var destinationStation: String? = null

    @SuppressLint("JavascriptInterface", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webview)
        editTextDeparture = findViewById(R.id.editTextDeparture)
        editTextDestination = findViewById(R.id.editTextDestination)

        //웹뷰 설정 코드
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
        webView.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                addOnClickToElements()
            }
        }

        // 자바스크립트 인터페이스 등록
        webView.addJavascriptInterface(JavaScriptInterface(), "AndroidInterface")
        webView.loadUrl("file:///android_res/raw/mapimage.svg") // 앱의 raw 폴더에 있는 SVG 파일 로드

        //출발역 EditText 클릭이벤트 처리
        editTextDeparture.setOnClickListener {
            isDepartureMode = true //출발역 선택 모드로 변경
            editTextDestination.text.clear()
            Toast.makeText(this,"출발역을 선택하세요.",Toast.LENGTH_SHORT).show()
        }
        //도착역 EditText 클릭이벤트 처리
        editTextDestination.setOnClickListener {
            isDepartureMode = false //도착역 선택모드로 변경
            editTextDeparture.text.clear()
            Toast.makeText(this,"도착역을 선택하세요",Toast.LENGTH_SHORT).show()
        }
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
    inner class JavaScriptInterface {
        @android.webkit.JavascriptInterface
        fun handleTextClick(stationName: String) {
            runOnUiThread {
                setStationName(stationName)
            }
        }

        private fun setStationName(stationName: String) {
            Log.d("webViewEvent", "역이름클릭:$stationName")

            if (isDepartureMode) {
                // 출발역 검색창이 선택된 상태일 때
                departureStation = stationName
                runOnUiThread {
                    // 출발역 EditText에 역 이름 설정
                    editTextDeparture.setText(stationName)

                    // 도착역 검색으로 변경
                    isDepartureMode = false
                }
            } else {
                // 도착역 검색창이 선택된 상태일 때
                destinationStation = stationName
                runOnUiThread {
                    // 도착역 EditText에 역 이름 설정
                    editTextDestination.setText(stationName)

                    // 출발역 검색으로 변경
                    isDepartureMode = true
                }
            }

            // 선택한 역 이름 토스트 메시지로 표시
            Toast.makeText(applicationContext, "선택한 역: $stationName", Toast.LENGTH_LONG).show()
        }
    }

}