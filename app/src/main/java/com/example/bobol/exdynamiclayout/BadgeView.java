package com.example.bobol.exdynamiclayout;

/**
 * Created by bobol on 2018-02-26.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by bobol on 2018-02-26.
 */
public class BadgeView extends View {

    private Paint backgroundPaint;
//    public Drawable drawable;


    public BadgeView(Context context) { // BadgeView 생성자 init() 에서 BadgeView 의 초기값 정의
        super(context);
        init();

    }
/*    public BadgeView BadgeViewDrawable(Context context,String string){

        drawable = getResources().getDrawable(trans(context,string));
        drawable.setBounds(0,0,30,30);


        return this;
    }*/
    /**
     * View 를 상속받아 onDraw 를 Override 한다..
     *
     * canvas 는 보통 2D 이미지를 그리기 위해 사용된다. 원하는 화면을 구성하여 그림을 그릴 수 있다.
     *               paint 객체는 그리기를 위한 도구
     */

    public void init(){ // 초기화 BadgeView 의 초기 setting
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 안티 알리아싱은 도형이나 글꼴 주변배경과 부드럽게 잘 어울리게 하는 기법
        backgroundPaint.setColor(Color.RED);
        backgroundPaint.setStyle(Paint.Style.FILL); // 색상이 채워지고 테두리는 그려지지 않는다.
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT); // Canvas 가 View 위에 그림을 그리기 위해 Parameter 정의
        setLayoutParams(params);

    }




    @Override  // View 상속받아 override
    protected void onDraw(Canvas canvas) { //View 위에 canvas 가 원을 그려준다고 이해. ex)drawRect,drawLine .....
        super.onDraw(canvas);
           canvas.drawCircle(getMeasuredWidth() / 2f, getMeasuredHeight() / 2f, getMeasuredWidth() / 2, backgroundPaint); // 입력받게 될 Parameter 가로세로 절반, 즉 중간 위치에 backgroundColor 로 반지름(radius)이 입력받은 int 값의 1/2 크기로 그려짐

    }

    public boolean hide() {
        if (getParent() != null) { // 아이콘에서 뱃지가 제거 되지 않았을 시.
            ((ViewGroup) getParent()).removeView(this); // Parent ViewGroup 에서 this(BadgeView) 를 제거함.
            return true;
        }
        return false; // 이곳의 예외 처리는 BadgeView 가 제거 된 후 다시 한번 hide()가 호출 되었을시를 고려함
    }

    public BadgeView setBadgeLocationAndWidth(int index) { // 입력받는 index 를 통해서 BadgeView 의 크기 와 위치를 설정하는 함수

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams(); // BadgeView 의 parameter 를 setting 함
        params.gravity=Gravity.CENTER;
        params.width = index/5;
        params.height = index/5;
        params.setMargins(index/6,0,0,index/6); // 아이콘의 우측 위에 달기 위해서 Bottom 과 Left 에 margin 을 주었음.
        setLayoutParams(params);
        return this;

    }


    public BadgeView show(Context context) { //Badge 가 Contain 되어있는 FrameLayout 의 Parameter 를 정의함.

        return new BadgeView(context);

    }

}


