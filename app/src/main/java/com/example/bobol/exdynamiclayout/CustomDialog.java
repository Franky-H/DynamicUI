package com.example.bobol.exdynamiclayout;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


/**
 * Created by bobol on 2018-02-20.
 */

public class CustomDialog extends Dialog {



   public   WebView webView;
   public int iconWidth = DisplayMetrics(getContext()).widthPixels* 2/ 100; //왼쪽 메뉴의 가로&세로 길이 의 값을 정의. Badge 의 크기와 Badge 위치 정의에 사용함


    public CustomDialog(final Context context,String JsonString) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen); // 다이얼로그 제목을 없애고, 전체화면

        setCanceledOnTouchOutside(false); //외부 터치시 다이얼로그 종료를 방지

        /*  iconWidth 는 iconLayout 의 width 를 정하는 값과 동시에 BadgeView 의 크기와 위치를 설정하는 변수  */
        /* 최대 최소 값을 설정(임의로 설정), ConstraintLayout 을 활용 하여 범위 설정가능..(?)  */
        if (iconWidth >= (DisplayMetrics(getContext()).widthPixels * 21 / 100)) {
            iconWidth = DisplayMetrics(getContext()).widthPixels * 20 / 100;
        }

        if (iconWidth <= (DisplayMetrics(getContext()).widthPixels * 6 / 100)) {
            iconWidth = DisplayMetrics(getContext()).widthPixels * 7 / 100;
        }

        /** JSON 파싱 및 데이터 저장 & 불러오기 */
        /*WebView 호출 및 정의 */
        webView = new WebView(context);
        webView.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        Values values = new Values(); // Values 를 호출 하여 JsonString 을 넘겨주고 parsing 한 data 를 받아오기 위함.
        values.JsonParsing(context, JsonString, iconWidth, webView); //JsonParsing 이라는 함수를 통해 JSON 을 파싱하고 각각의 데이터를 뽑아옴

        /* 레이아웃들의 Parameter 정의
        *
        * RelativeLayout 안에 LinearLayout 의 Parameter 는 RelativeLayout.Parameter 로 정의 해야 한다...
        * Parent 와 Child 의 이해
        * */
        /** Layout 정의  */
        RelativeLayout.LayoutParams bigLayoutParams = new RelativeLayout.LayoutParams((DisplayMetrics(context).widthPixels), (DisplayMetrics(context).heightPixels));
        RelativeLayout.LayoutParams menuParams = new RelativeLayout.LayoutParams((DisplayMetrics(context).widthPixels * 85 / 100), (DisplayMetrics(context).heightPixels * 80 / 100)); // 각각의 레이아웃의 parameter 를 정의
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(iconWidth, LinearLayout.LayoutParams.MATCH_PARENT);


        RelativeLayout bigLayout = new RelativeLayout(context);  // 다이얼로그의 전체 화면이 되는 Layout
        LinearLayout menuLayout = new LinearLayout(context); // Menu 틀이 되는 Layout
        LinearLayout iconLayout = new LinearLayout(context); // icon 들이 들어가는 Layout


        /* 다이얼로그의 전체 화면이 되는 Layout 정의 */
        bigLayout.setLayoutParams(bigLayoutParams);
        bigLayout.setBackgroundColor(Color.BLACK);
        bigLayout.getBackground().setAlpha(130);
        bigLayout.addView(menuLayout);


        /*  메뉴창 레이아웃 정의   (menuLayout 에서 iconLayout 을 제외하고 나머지부분은 webView 를 Load 함 */
        menuLayout.setOrientation(LinearLayout.HORIZONTAL);
        menuLayout.addView(iconLayout);
        menuLayout.addView(webView);
        menuParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        menuLayout.setLayoutParams(menuParams);
        menuLayout.setId(1);// exitButton 을 상대적으로 배치하기 위해 ID 값을 부여함
        menuLayout.setBackgroundResource(R.drawable.ilovemoa_shadow);// 메뉴창에 shadow 이미지를 BackgroundResource 로 정의


        /* 아이콘이 들어가는 Layout 정의*/
        iconLayout.setLayoutParams(iconParams);
        iconLayout.setOrientation(LinearLayout.VERTICAL);
        iconLayout.setBackgroundColor(Color.parseColor(values.defaultBackgroundColor)); // icon 이 들어가는 Layout 에 DeFaultBackgroundColor 로 정의


        /* 닫기 버튼 정의*/
        ImageView exitButton = new ImageView(context); //닫기 버튼을 정의함
        exitButton.setImageResource(R.drawable.ilovemoa_exit);
        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams((DisplayMetrics(context).widthPixels * 6 / 100), (DisplayMetrics(context).heightPixels * 6 / 100));// 이미지 버튼의 width 와 height 를 모든 기종에 맞게 비율로 처리함.

        buttonParams.addRule(RelativeLayout.ALIGN_RIGHT, 1); //subject 1 은 menuLayout 의 ID
        buttonParams.addRule(RelativeLayout.ALIGN_TOP, 1);   //menuLayout 의 위의 오른쪽 끝에 배치하기 위함.

        exitButton.setTranslationX(-(DisplayMetrics(context).heightPixels / 24)); // 그림자만큼 위치 조정
        exitButton.setTranslationY(-(DisplayMetrics(context).heightPixels / 30));
        exitButton.setLayoutParams(buttonParams);

        bigLayout.addView(exitButton);

        int size =values.IconInformation.size(); // Icon 의 정보가 담긴 ArrayList 의 사이즈 정의

        for(int i = 0; i< size; i++){
           iconLayout.addView(values.IconInformation.get(i).getIcon()); // icon 의 갯수만큼 iconLayout 에 addView
        }

        values.IconInformation.get(values.defaultIndex).getIcon().performClick();//  performClick 설정

        setContentView(bigLayout); // menuLayout 을 ContentView 함

        exitButton.setOnClickListener(new View.OnClickListener() { //닫기 버튼 클릭시 dialog 종료
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

    }


    /** 기종별 displayMetrics Data 를 뽑는 메소드 */
    public static DisplayMetrics DisplayMetrics(Context context){ // Device 마다 display 정보가 다르기 때문에 쓰는 method

        DisplayMetrics metrics = new DisplayMetrics(); //metrics 에 기종에 따른 display 정보가 저장된다.
        try{
            WindowManager winMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);


            winMgr.getDefaultDisplay().getMetrics(metrics);

        }catch(Exception e){metrics.density=1;}
        return metrics;
    }
}

/** 구조 변경 후 줄인것 */
/*
     Id 갯수 만큼 FrameLayout 과 BadgeView 를 생성 함

      frameLayouts = new FrameLayout[size]; // id 의 갯수만큼 frameLayout 과 BadgeView 를 정의 함.
       badgeViews   = new BadgeView  [size];

        for (int i = 0; i < size; i++) { // Id 의 갯수만큼 이미지를 addView 하는 루프

           frameLayouts[i] = new FrameLayout(context);
           frameLayouts[i].setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, iconWidth));


           iconLayout.addView(frameLayouts[i]);// iconLayout 에 생성된 FrameLayout 을 addView 함

           frameLayouts[i].addView(values.IconInformation.get(i).iCon); // FrameLayout 에 아이콘이 되는 imageViews 를 addView 함


           if (values.IconInformation.get(i).getBadge()) {
               badgeViews[i] = BadgeView.setBadge(context,(iconWidth)); // BadgeView 의 크기와 set 위치를 정의함.
               frameLayouts[i].addView(badgeViews[i]); // FrameLayout 에 BadgeView 를 addView 함
           }
            values.IconInformation.get(i).iCon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

       }

    public void imageViewSetting(final Context context, final ImageView imageView, final int index){ // index 번째 URL 이나, resourceId를 ImageView 에 심어줌

        imageView.setBackgroundColor(Color.parseColor(values.defaultBackgroundColor));
        imageView.setColorFilter(Color.parseColor(values.defaultIconColor),PorterDuff.Mode.SRC_ATOP);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,iconWidth)); //index 번째 ImageView 에 대한 setting 을 하는 과정

         *//*

 resourceId 에 따라 icon 설정

         if(TextUtils.isEmpty(values.IconInformation.get(index).getResourcesId())){
            Toast.makeText(context,index+"번째 resourceId 가 존재 하지 않습니다.",Toast.LENGTH_SHORT).show();
         }
         else{
             imageView.setImageResource(trans(context,values.IconInformation.get(index).getResourcesId()));// resourceIds.get(index) 번째 값은 String 이기때문에 setImageResource 에 적합한 int 형태로 변환하기 위해 trans 라는 method 를 만들어서 사용.
         }

        imageView.setPadding(iconWidth/4,iconWidth/4,iconWidth/4,iconWidth/4); // icon 의 크기를 줄이기 위하여 Padding 값을 주었음. iconWidth / 5  는 딱 보기좋은 크기(?)

        imageView.setOnClickListener(new View.OnClickListener() { // 이미지(icon) 을 눌렀을때 정의
            @Override
            public void onClick(View v) { // 이미지를 클릭했을시

                values.pressIconColor(imageViews,imageView); // IconClick 메소드를 통해 imageView 들의

                webView.setWebViewClient(new noPlayBrow()); // 웹뷰에서 URL 을 처리 하도록 유도 ( 웹뷰에서 URL 이 Load 되지않고 크롬을 통해 Load 되는 것을 방지

                if(TextUtils.isEmpty(values.IconInformation.get(index).getUrl()))
                { Toast.makeText(context,index+"번째 URL 이 존재 하지 않습니다.",Toast.LENGTH_SHORT).show();}
                else
                { webView.loadUrl(values.IconInformation.get(index).getUrl());} // 이미지를 눌렀을시 해당 URL 을 WebView 에 Load 함

                if(badgeViews[index]!=null){ // 이미지(아이콘)을 클릭 했을때 해당 BadgeView 를 없앰
                    badgeViews[index].hide();
                }
                여기까지 imageView 를 클릭했을시 설정

            }
        });

    }
class noPlayBrow extends WebViewClient { // 웹뷰에서 Load 시 브라우져를 통해 열리는 것을 방지하는 Class

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) { // 현재 웹뷰에서 처리하도록 하는 method...
        view.loadUrl(url);
        return true;
    }
}


    public int trans (Context context,String string){ // String 형태로 받아온 resourceId를 drawable int 타입으로 변환   ex) int 형태인 R.drawable.ilovemoa_exit   대신 String 형태의 resourceId 로 호출 가능.
        return context.getResources().getIdentifier(""+string,"drawable",context.getPackageName()); // 받은 String 형태를 drawable int 형태로 변환 하여 return 함
    }
*/



