package com.example.bobol.exdynamiclayout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by bobol on 2018-03-07.
 */

public class IconInfo {

    private FrameLayout iCon; // Icon 의 정보들을 해당 클래스 밖에서 set 할수 없게끔 private 형태로 변수 선언
    private int defaultIndex;
    private String defaultIconColor;
    private String pressedIconColor;
    private String defaultBackgroundColor;
    private String pressedBackgroundColor;
    private String resourceId;
    private String url;
    private Boolean badge;
    private int iConWidth;
    private ImageView imageView;
    private BadgeView badgeView;


/* icon 한개 한개의 대한 정의를 함.
 *  구조 변경 전 key: Value 에서 Values 값만 ArrayList 로 저장했을 경우에, 중간에 빈값이 들어오면 crash
 *  list 에서 id:0 번째의 모든정보를 담아 ArrayList<IconInfo> 형태로 저장함.
 **/

    public IconInfo () {

    }

    public void defaultInfo(int defaultIndex, String pressedIconColor, String pressedBackgroundColor,String defaultBackgroundColor,String defaultIconColor) {
        this.defaultIndex = defaultIndex;
        this.pressedIconColor = pressedIconColor;
        this.pressedBackgroundColor = pressedBackgroundColor;
        this.defaultBackgroundColor = defaultBackgroundColor;
        this.defaultIconColor=defaultIconColor;
    }

    public void collectInfo (Context context,String resourcesId, String url, Boolean badge,int iConWidth) {
        this.url= url;
        this.badge=badge;
        this.iConWidth=iConWidth;
        this.resourceId=resourcesId;
        this.iCon=init(context);
    }

    public FrameLayout init(Context context) {
        imageView = new ImageView (context);
        iCon= new FrameLayout(context);// iCon 은 FrameLayout
        badgeView =new BadgeView(context);

        badgeView.setBadgeLocationAndWidth(iConWidth);
        badgeView.show(context);

        iCon.setLayoutParams(new FrameLayout.LayoutParams(iConWidth,iConWidth));

        setTheme(false);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(iConWidth, iConWidth));
        imageView.setPadding(iConWidth/4 , iConWidth/4 , iConWidth/4 , iConWidth/4 );
        imageView.setImageResource(trans(context, resourceId));

        iCon.addView(imageView);

        if(badge) iCon.addView(badgeView);
        return iCon;
    }

    public int trans(Context context, String string) { // String 형태로 받아온 resourceId를 drawable int 타입으로 변환   ex) int 형태인 R.drawable.ilovemoa_exit   대신 String 형태의 resourceId 로 호출 가능.
        return context.getResources().getIdentifier("" + string, "drawable", context.getPackageName()); // 받은 String 형태를 drawable int 형태로 변환 하여 return 함
    }

    public void setTheme(boolean isClicked) {


        if(isClicked) { //눌렸을때 BackgroundColor 지정

             imageView.setBackgroundColor(Color.parseColor(pressedBackgroundColor));
             imageView.setColorFilter(Color.parseColor(pressedIconColor),PorterDuff.Mode.SRC_ATOP);
             badgeView.hide();
        }
        else { // 눌리지 않았을때 BackgroundColor 지정
             imageView.setBackgroundColor(Color.parseColor(defaultBackgroundColor));   // 안눌린 컬러 지정
             imageView.setColorFilter(Color.parseColor(defaultIconColor),PorterDuff.Mode.SRC_ATOP);
        }
    }

    public String getDefaultIconColor(){return this.defaultIconColor;}
    public String getDefaultBackgroundColor(){return this.defaultBackgroundColor;}
    public String getResourceId(){return this.resourceId;}
    public boolean getBadge(){return this.badge;}
    public String getUrl(){return this.url;}
    public FrameLayout getIcon() {return this.iCon;}
    public Integer getDefaultIndex(){return this.defaultIndex;}
}
