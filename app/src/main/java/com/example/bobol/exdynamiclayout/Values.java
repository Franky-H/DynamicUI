package com.example.bobol.exdynamiclayout;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by bobol on 2018-02-21.
 */

public class Values {


    Integer defaultIndex = null;
    String defaultBackgroundColor = null;
    String pressedBackgroundColor = null;
    String defaultIconColor = null;
    String pressedIconColor = null;
    ArrayList<IconInfo> IconInformation = new ArrayList<>();
    IconInfo iconInfo = new IconInfo();


    public void JsonParsing(final Context context, String JsonString, final int index, final WebView webView) {  // JSONArray 는 [](대괄호) 이하 의 key 값과 Value 값을 저장한 Array .. , JSONObject 는 {}(중괄호) 이하의 key 값, value 값 라고 생각하자.

        try {// JSON 파싱 시 반드시 필요함

            JSONObject jsonObject = new JSONObject(JsonString); // 받아오는 JSON String 을 jsonObject 에 저장 함.
            JSONObject menus_helpObject = new JSONObject(jsonObject.optString("menus_help")); // jsonObject 에서 menus_help 의 Key 값과 Value 값을 menus_helpObject 에 저장
            JSONArray listArray = new JSONArray(menus_helpObject.optString("list")); // menus_helpObject 에서 list 의 key 값과 Value 값이 담긴  array 를 jsonArray 에 저장함
            defaultIndex = menus_helpObject.optInt("defaultIndex");  //(defaultIndex 의 Value 값을 찾아 저장함) 각각의 해당하는 Value 값을 찾아서 String 으로 정의함.
            defaultBackgroundColor = menus_helpObject.optString("defaultBackgroundColor");
            pressedBackgroundColor = menus_helpObject.optString("pressedBackgroundColor");
            defaultIconColor = menus_helpObject.optString("defaultIconColor");
            pressedIconColor = menus_helpObject.optString("pressedIconColor");

            for (int i = 0; i < listArray.length(); i++) { // id 와 resourceId, Badge , Url 은 저장 한 갯수만큼 Data 가  있기 때문에 for 문 안에서 배열로 저장함.

                menus_helpObject = listArray.optJSONObject(i);

                iconInfo = new IconInfo();
                iconInfo.defaultInfo(defaultIndex, pressedIconColor, pressedBackgroundColor,defaultBackgroundColor,defaultIconColor); // "menus_help" 의 key, Values 값을 먼저 받아서 저장
                iconInfo.collectInfo(context, menus_helpObject.optString("resourceId"), menus_helpObject.optString("url"), menus_helpObject.optBoolean("badge"),index); // "list" 의 key 와 Values 값을 받아서 저장
                IconInformation.add(iconInfo); // IconInfo 가 담긴 ArrayList IconInformation 에 저장함
                final int  Clicked = i; // clicked 는 i 번째 index
                IconInformation.get(i).getIcon().setOnClickListener(new View.OnClickListener() { // Icon 클릭시 이벤트 정의함.
                    @Override
                    public void onClick(View v) {

                        webView.setWebViewClient(new noPlayBrow()); // WebView 에서 Load 하기 위함.( setWebViewClient 를 통해 set 하지 않으면 Chrome Browser 로 Load 됨. )
                        webView.loadUrl(IconInformation.get(Clicked).getUrl());
                        setTheme(true,Clicked);

                    }
                });
            }

        } catch (JSONException e) { // Json 을 파싱할땐 (jsonObject 사용) 반드시 예외처리.(안하면 Error)
            e.printStackTrace();
        }


    }
    public void setTheme(Boolean change, int index) {
        for (int k = 0; k < IconInformation.size(); k++) {
            IconInformation.get(k).setTheme(false);// IconInformation 의 setTheme 에서 false - deFaultBackgroundColor 가 setting 되로록
        }
        if (change) {
            IconInformation.get(index).setTheme(true); // setTheme 에서 true 가 넘어오면 PressBackgroundColor 가 setting 되록 함

        }
    }
    class noPlayBrow extends WebViewClient { // 웹뷰에서 Load 시 브라우져를 통해 열리는 것을 방지하는 Class

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { // 현재 웹뷰에서 처리하도록 하는 method...
            view.loadUrl(url);
            return true;
        }
    }


}



