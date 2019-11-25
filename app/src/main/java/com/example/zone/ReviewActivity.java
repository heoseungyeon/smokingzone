package com.example.zone;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class ReviewActivity extends AppCompatActivity {
    private TextView smokingarea_name;
    private TextView smokingarea_rating_avg;
    private CheckBox bench;
    private CheckBox roof;
    private CheckBox vtl;
    private RatingBar ratingbar;
    private TextView ratingValue;
    private EditText ed_review_comment;
    private Button bt_reg_rating;
    private Button bt_reg_comment;
    private ActionBar actionBar;


    private JSONArray mArray;  //서버로부터 JSON Array를 받아 저장할 변수
    ListView listView; //리뷰화면 댓글  ListView 레이아웃 형성을 위한 객체 생성
    ReviewListViewAdapter adapter; // 뷰에 넣을 데이터들을 어떠한 형식과 어떠한 값들로 구성할지 정하는 adapter 객체

    //흡연구역데이터 배열 선언
    String smoking_area_data[];

    //각각의 데이터가 들어갈 ArrayList 생성
    ArrayList<String> arrayregDate = new ArrayList<String>();
    ArrayList<String> arrayregUser = new ArrayList<String>();
    ArrayList<String> arrayctnt = new ArrayList<String>();

    ArrayList<ReviewModel> arrayList = new ArrayList<ReviewModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        //액션바 가져오기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.custom_bar_review);
        actionBar.setTitle("리뷰 화면");



        //메뉴바에 '<' 버튼이 생긴다.(두개는 항상 같이다닌다)
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //각 해당하는 변수의 xml 컴포넌트를 등록
        smokingarea_name = findViewById(R.id.review_smokingarea);
        smokingarea_rating_avg = findViewById(R.id.avg_point);
        bench = findViewById(R.id.bench);
        roof = findViewById(R.id.roof);
        vtl = findViewById(R.id.vtl);
        ratingbar = findViewById(R.id.ratingbar);
        ratingValue = findViewById(R.id.ratingvalue);
        ed_review_comment = findViewById((R.id.edit_review_comment));
        bt_reg_rating = findViewById((R.id.ratingregbutton));
        bt_reg_comment = findViewById((R.id.comment_reg_button));


        //흡연구역 정보를 intent를 통해 받음
        Intent intent = getIntent();
        smoking_area_data =intent.getExtras().getStringArray("arr");

        //받아온 정보를 각 항목에 설정
        smokingarea_name.setText(smoking_area_data[3]);
        smokingarea_rating_avg.setText(smoking_area_data[5]);
        if(smoking_area_data[0].charAt(0)=='1')
        {
            bench.setChecked(true);
        }
        else{
            bench.setChecked(false);
        }
        if(smoking_area_data[1].charAt(0)=='1')
        {
            roof.setChecked(true);
        }
        else{
            roof.setChecked(false);
        }
        if(smoking_area_data[2].charAt(0)=='1')
        {
            vtl.setChecked(true);
        }
        else{
            vtl.setChecked(false);
        }
        //3가지 항목 체크박스 선택할 수 없도록 하기
        bench.setClickable(false);
        roof.setClickable(false);
        vtl.setClickable(false);

        //댓글 등록버튼을 눌렀을 때 리스너 등록
        bt_reg_comment.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                //JSONObject에 서버에 보내줄 댓글 데이터 담아줌
                JSONObject sbParam = new JSONObject();
                try {
                    sbParam.put("smoking_area_no",Integer.parseInt(smoking_area_data[6]));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    sbParam.put("smoking_review_reg_user", "wow");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    sbParam.put("smoking_review_ctnt", ed_review_comment.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    sbParam.put("smoking_review_point", ratingValue.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //서버로 댓글 입력 클래스 생성 및 실행
                NetworkTaskWrite networkTaskWrite = new NetworkTaskWrite(sbParam.toString());
                Log.d("data",sbParam.toString());
                networkTaskWrite.execute();


            }
        });
        //------------------
        /*흡연구역 정보 담기*/
        //------------------


        //리뷰 액티비티 networkTask 클래스 생성 및 실행
        ReviewActivity.NetworkTask networkTask = new ReviewActivity.NetworkTask(this,smoking_area_data[6]);
        networkTask.execute();

        //---------------------
        /*더미 데이터 생성 파트*/
        //---------------------
        /*
        //더미 배열 생성
        String[] dummyuser = new String[]{"박지성", "손흥민", "황희찬", "이강인", "남태희"};
        String[] dummydate = new String[]{"2019/09/30", "2019/00/00", "2019/00/00", "2019/00/00", "2019/00/00"};
        String[] dummyctnt = new String[]{"Battery detail...", "Cpu detail...", "Display detail...", "Memory detail...", "Sensor detail..."};

        //더미 데이터 입력
        for (int i = 0; i < 5; i++) {
            arrayregUser.add(dummyuser[i]);
            arrayregDate.add(dummydate[i]);
            arrayctnt.add(dummyctnt[i]);
        }


        //더미 데이터 입력 (Json Parsing)
        String Test = "[{\"smoking_area_no\":\"1\",\"smoking_review_reg_user\":\"user\",\"smoking_review_ctnt\":\"ctntsadasdasdsada\",\"smoking_review_point\":\"15.5\"}]";

        try {
            mArray = new JSONArray(Test);
        } catch (JSONException e) {
            //TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (int i = 0; i < mArray.length(); i++) {
            try {
                JSONObject jsonObject = mArray.getJSONObject(i);
                // Pulling items from the array
                arrayregDate.add(jsonObject.getString("reg_date"));
                arrayregUser.add(jsonObject.getString("reg_user"));
                arrayctnt.add(jsonObject.getString("ctnt"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //listView에 xml의 listView를 가져와서 넣어줌
        listView = findViewById(R.id.listView);

        //Model에 데이터를 넣어주고 arrayList<BoardModel>에 넣어줌
        for (int i = 0; i < arrayregUser.size(); i++) {
            ReviewModel reviewModel = new ReviewModel(arrayregDate.get(i), arrayregUser.get(i), arrayctnt.get(i));
            //bind all strings in an array
            arrayList.add(reviewModel);
        }

        //listViewAdapter클래스에 결과를 넘겨줌
        adapter = new ReviewListViewAdapter(this, arrayList);

        //bind the adapter to the listview
        listView.setAdapter(adapter);
        */

        //별점 ratingbar 리스너
        ratingbar.setOnRatingBarChangeListener(new RatingbarListener());
    }

    class RatingbarListener implements RatingBar.OnRatingBarChangeListener {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            ratingbar.setRating(rating);
            ratingValue.setText(Float.toString(ratingbar.getRating()));
            ratingValue.setTypeface(null, Typeface.BOLD);


        }
    }//Ratingbarlistenr class

    //해당 흡연구역의 댓글을 뿌려주는 NetworkTask 클래스
    public class NetworkTask extends AsyncTask<Void, Void, String> {


        String values;
        Context mcontext;

        NetworkTask(Context mcontext, String values) {
            this.mcontext = mcontext;
            this.values = values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress bar를 보여주는 등등의 행위
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";

            try {
                result = ServerReviewData(values);
                Log.d("result",result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
        }

        @Override
        protected void onPostExecute(String result) {
            // 통신이 완료되면 호출됩니다.
            // 결과에 따른 UI 수정 등은 여기서 합니다.
            //더미 데이터 입력 (Json Parsing)
            //String Test = "[{\"smoking_area_no\":\"1\",\"smoking_review_reg_user\":\"user\",\"smoking_review_ctnt\":\"ctntsadasdasdsada\",\"smoking_review_point\":\"15.5\"}]";
            if (result != "") {
                try {
                    mArray = new JSONArray(result);
                } catch (JSONException e) {
                    //TODO Auto-generated catch block
                    e.printStackTrace();
                }
                for (int i = 0; i < mArray.length(); i++) {
                    try {
                        JSONObject jsonObject = mArray.getJSONObject(i);
                        // Pulling items from the array
                        arrayregDate.add(jsonObject.getString("reg_date"));
                        arrayregUser.add(jsonObject.getString("reg_user"));
                        arrayctnt.add(jsonObject.getString("ctnt"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //listView에 xml의 listView를 가져와서 넣어줌
                listView = findViewById(R.id.listView);

                //Model에 데이터를 넣어주고 arrayList<BoardModel>에 넣어줌
                for (int i = 0; i < arrayregUser.size(); i++) {
                    ReviewModel reviewModel = new ReviewModel(arrayregDate.get(i), arrayregUser.get(i), arrayctnt.get(i));
                    //bind all strings in an array
                    arrayList.add(reviewModel);
                }

                //listViewAdapter클래스에 결과를 넘겨줌
                adapter = new ReviewListViewAdapter(mcontext, arrayList);

                //bind the adapter to the listview
                listView.setAdapter(adapter);
            }//result not null
            else {
                Log.d("data:", "게시글 없음!");
            }
        }
    }

    //서버로 해당 흡연구역 정보를 넘겨주고 댓글 정보를 받는 함수
    public String ServerReviewData(String values) throws JSONException {

        String result = "";
        try {
            //--------------------------
            //   URL 설정하고 접속하기
            //--------------------------
            URL url = new URL("http://18.222.175.17:8080/SmokingArea/SmokingArea/smokingAreaReview.jsp");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();   // 접속
            //--------------------------
            //   전송 모드 설정 - 기본적인 설정이다
            //--------------------------
            http.setDefaultUseCaches(false);
            http.setDoInput(true);                         // 서버에서 읽기 모드 지정
            http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST");         // 전송 방식은 POST

            // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");            //--------------------------
            //   서버로 값 전송
            //--------------------------
            StringBuffer buffer = new StringBuffer();
            String smokingAreaReviewValue="smokingAreaReviewValue="+values;

            buffer.append(smokingAreaReviewValue);                 // php 변수에 값 대입

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();

            //--------------------------
            //   서버에서 전송받기
            //--------------------------
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;

            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
            }
            result = builder.toString();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
       // System.out.println(result);
        return result;
    } // HttpPostDat

    // 리뷰화면 댓글 입력 클래스 (클라이언트 -> 서버 , 서버 -> 클라이언트)
    public class NetworkTaskWrite extends AsyncTask<Void, Void, String> {

        String values;

        NetworkTaskWrite(String values) {
            this.values = values;
        }//생성자

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress bar를 보여주는 등등의 행위
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            try {
                //클라이언트로 받은 값들을 result에 넣어줌
                result = sendReviewWrite(values);
                Log.d("result2",result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
        }

        @Override
        protected void onPostExecute(String result) {
            // 통신이 완료되면 호출됩니다.
            // 결과에 따른 UI 수정 등은 여기서 합니다.
            Intent intent = new Intent(ReviewActivity.this, ReviewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.putExtra("arr",smoking_area_data);
            startActivity(intent);
        }
    }
    //서버로 댓글을 JSONObject 형태의 String 값으로 댓글보내고 리턴값을 받는 함수.
    public String sendReviewWrite(String values) throws JSONException {

        String result = "";
        try {
            //--------------------------
            //   URL 설정하고 접속하기
            //--------------------------
            URL url = new URL("http://18.222.175.17:8080/SmokingArea/SmokingArea/insertSmokingReview.jsp");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();   // 접속
            //--------------------------
            //   전송 모드 설정 - 기본적인 설정이다
            //--------------------------
            http.setDefaultUseCaches(false);
            http.setDoInput(true);                         // 서버에서 읽기 모드 지정
            http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST");         // 전송 방식은 POST

            // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");            //--------------------------
            //   서버로 값 전송
            //--------------------------
            StringBuffer buffer = new StringBuffer();
            String regdata = "json_smokingReviewValue=" + values;
            buffer.append(regdata);                 // php 변수에 값 대입

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();

            //--------------------------
            //   서버에서 전송받기
            //--------------------------
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;

            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
            }
            result = builder.toString();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        System.out.println(result);
        return result;
    } // HttpPostDat
}

