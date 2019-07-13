package com.example.bobol.exdynamiclayout;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private CustomDialog dialog ; // customDialog 를 전역변수 로  선언 하고.. 버튼 클릭시 dialog.show() 호출



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // onCreate 의 Bundle 변수명 savedInstanceState 기본적으로 현 상태를 저장해놓은 상태값이라고 생각. 화면을 가로 세로로 변환하였을때 사실은 onDestroy 호출 후 다시 onCreate 되는 현상 일종의 ReCreate 현상

        View decorView = getWindow().getDecorView(); // decorView 란 Top-Level Window 라고 개념
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN); // status bar ( 상단 상태창 ) 을 가려주는 Flag

        String url = "https://dgameapi.ilovegame.co.kr/init"; //NetworkTask 에서 execute 를 통해 HttpURLConnection 수행

        NetworkTask networkTask = new NetworkTask(url, null);
        networkTask.execute(); // networkTask 실행 result 는 onPostExecute 에...

        final FrameLayout mainLayout = new FrameLayout(this); //MainActivity 의 레이아웃 과 버튼 정의

        Button Help = new Button(this);

        Help.setText("도움말");
        FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mainLayout.setLayoutParams(layoutParams);
        Help.setLayoutParams(buttonParams);

        buttonParams.gravity=Gravity.CENTER;
        mainLayout.addView(Help);

        Help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        dialog.show();
                }
            });

            setContentView(mainLayout);

    }

    /**
     * -AsyncTask (비동기적 처리) 는 메인 쓰레드의 작업을 좀더 효율적이게 해줄수 있는 백그라운드 병렬적 처리 기법
     * -onPreExecute() 작업이 실행되기 전에 UI 스레드에서 호출된다.
     * -doInBackground , onPreExecute, onProgressUpdate, onPostExecute 가 Override
     * -doInBackground 는 새로 만든 thread 에서 작업을 수행함 execute() 를 호출 할때 받은 parameter 를 배열로 전달 받음
     * -onPreExecute 는 Background 작업을 수행하기 전에 호출함. 초기화 작업에 사용
     * -onProgressUpdate 는 Background 작업의 수행 상태를 표시하기 위해 호출됨 작업 중간중간에 UI에 접근하는 경우
     * -onPostExecute 는 Background 작업이 끝난 후에 호출되며 결과값을 반환함.
     * -작업 순서는 onPreExecute - doInBackground - onPostExecute 순
     */

    @SuppressLint("StaticFieldLeak")
    class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) { // Url 과 보낼 Values 를 정의 함.. 이번경우에 values 는 없음
            this.url = url;
            this.values = values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected String doInBackground(Void... params) { //onPreExecute()실행 후 백그라운드 스레드에서 호출됨 가장 핵심적인 작업을 수행함. execute() 메소드를 호출할때 사용된 파라미터를 배열로 전달받음

          RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            return requestHttpURLConnection.request(url, values);// 요청 결과를 반환해줌
        }

        @Override //doInBackground()의 리턴값(result)을 변수로 받아 처리한다.
        protected void onPostExecute(String JsonString) {
            super.onPostExecute(JsonString); //결과값 JsonString

            if(TextUtils.isEmpty(JsonString))
                Toast.makeText(getApplication(),"네트워크 연결 상태를 확인해주세요.",Toast.LENGTH_LONG).show(); //JsonString 이 "" 이거나, null 값 일때 Toast 호출
            else {// 위치를 바꾼 이유(?) 버튼을 누를때마다 CustomDialog 를 new 로 생성했다. onPostExecute 안에 new CustomDialog 를 호출하여 한번 생성..
                dialog = new CustomDialog(MainActivity.this, JsonString); // 다이얼로그 불러온 후 parameter 정의 함. 이후에 show() 를 통해 불러옴
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 다이얼로그를 FullScreen 으로 띄움
                Toast.makeText(getApplicationContext(), "준비완료", Toast.LENGTH_LONG).show();
            }
        }
    }

}




