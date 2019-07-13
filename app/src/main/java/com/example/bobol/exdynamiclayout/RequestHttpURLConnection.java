package com.example.bobol.exdynamiclayout;


import android.content.ContentValues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
/**
 * Created by bobol on 2018-02-21.
 */

public class RequestHttpURLConnection {


    public String request(String _url,ContentValues _params) {

        HttpURLConnection urlConn = null;
        StringBuffer sbParams = new StringBuffer(); //URL 뒤에 붙여서 보낼 parameter 문자열 Buffer

        /**   StringBuffer 에 parameter 연결 *보낼 데이터가 있을시 설정(이번 경우에는 없음....) */
        if (_params == null) sbParams.append("");// 보낼 데이터가 없으면 parameter 를 ("") 비운다.

        else {;//보낼 데이터가 있으면..
            boolean isAnd = false; //parameter 연결할 스위칭 변수 isAnd
            String key;
            String value;


            //Map 은 Key 와 Value 를 가진 집합이며 중복을 허용하지 않는다. 즉 한개의 키에 한개의 value 값만 매칭된다. _params 의 valueSet()은 Entry 에 대응하는(?) 값을 지정된 값에 옮겨놓을때 까지..?
            for (Map.Entry<String, Object> parameter : _params.valueSet()) {//mapping 과정..
                key = parameter.getKey(); // parameter 에서 key 값을 가져와 저장
                value = parameter.getValue().toString(); //parameter 에서 value 값을 가져와 저장


                if (isAnd) //parameter 가 2개 이상일 때 &를 넣는다.
                {
                    sbParams.append("&");// parameter 사이에 & 를 넣음

                    sbParams.append(key).append("=").append(value); //

                }
                if (!isAnd) //parameter 가 2개 이상일 때 isAnd 를 true 로 바꾸고 다음 루프부터 &를 붙인다.
                {
                    if (_params.size() >= 2)
                        isAnd = true;
                }

            }

        }
        /** HttpURLConnection 을 통해 web 의 데이터를 가져온다.(HttpURLConnection 은 Stream 기반이다.)*/
        try {
            URL url = new URL(_url);
            urlConn = (HttpURLConnection) url.openConnection();// openConnection 메소드를 통해 생성.
            //urlConnection 설정
            urlConn.setRequestMethod("POST"); // Method 형태 설정. GET 과 POST 등등 형식이 있음. 차이는 간단하게 GET 은 URL 에 해당 정보 노출 , POST 는 노출되지 않아 보안 측면에서 약간(?) 좋다고 함. Limit 의 차이도 존재함
            urlConn.setRequestProperty("token","GOOGLE_SIMPLE_AHGYMOBILETK"); //Header Setting (Field 와 value 셋팅)  헤더는 특정값이 필요한 경우 setting 해주어야 하며, 응답 Header 에서 내려오는 특정값이 필요한 경우 해당 값을 가져와야 한다.

            //parameter 전달 및 데이터 읽어옴
            String strParams = sbParams.toString();
            OutputStream os = urlConn.getOutputStream(); //OutputStream 정의 Data 를 비워 버려야 모두 도착지점에 저장된다.( 새로운 OutputStream 에 urlConnection 의 outputStream 을 넣는 과정)
            os.write(strParams.getBytes("UTF-8"));// UTF-8 형식으로 인코딩 된 byte 단위로 전송
            os.flush(); //StringBuffer 에 저장 되어 있는 데이터들을 강제적으로 출력 시킨다.(기본적으로 출력 스트림은 버퍼에 데이터가 꽉차야 출력하는데 flush 는 관계없이 출력한다.) 비운다는 개념
            os.close(); //

            //HttpURLConnection 이 실패시(성공이 아닐시) 메서드를 종료.
            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            /** web 정보를 parsing 하는 부분 */
            // OutputStream 으로 부터 받은 출력을 BufferReader 로 받음.( BufferedReader 는 문자 Buffer 를 입력 하고 라인을 해석함.)
            // 결국 reader 는 byte Stream 인 urlConn.getInputStream 을  InputStreamReader 를 통해 문자 Stream 으로 변환 후 BufferedReader 가 문자 buffer 를 입력받아 라인을 해석함.

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8")); // InputStream 에 문자열 setting 함. ( InputStreamReader 는 byte Stream 을 문자 Stream 으로 변환)
            String line ;
            String page = "";
            //라인을 받아와 "" 와 합친다.
            while ((line = reader.readLine()) != null) { // Stream 에서 한줄씩 읽어들이는 readLine()
                page += line;
            }
            return page;
        } catch (MalformedURLException e) { //URL Protocol 이 잘못되었다는 예외 (http://가 없을시)
            e.printStackTrace();
        } catch (IOException e) { //openConnection 에 대한 예외 처리
            e.printStackTrace();
        } finally { // finally 이하 예외발생 유무나 catch 유무 상관없이 반드시 실행된다.
            if (urlConn != null)
                urlConn.disconnect();

        }

        return null;
    }
}








