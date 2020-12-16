package com.passta.a2ndproj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.passta.a2ndproj.main.DataVO.CoronaInformation_VO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.*;

import java.io.*;
import java.net.*;
import java.util.*;


import org.w3c.dom.*;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.CircleIndicator2;
import me.relex.circleindicator.CircleIndicator3;

public class CoronaInformationActivity extends AppCompatActivity {

    private ImageView backButton;
    private ViewPager2 viewPager;
    private int currentPage = 0;
    private Timer timer;
    //delay in milliseconds before task is to be executed
    private final long DELAY_MS = 500;
    // time in milliseconds between successive task executions.
    private final long PERIOD_MS = 2000;

    private TextView domestic;
    private TextView abroad;
    private TextView DECIDE_CNT;//확진자 수
    private TextView CLEAR_CNT;//격리해제 수
    private TextView EXAM_CNT;//검사진행 수
    private TextView DEATH_CNT;//사망자 수
    private String date;
    private String today;
    private String beforeWeek;
    private ArrayList<CoronaVO> coronaList;

    ArrayList<String> day;


    //Handler CoronaHandler;
    final Bundle Coronabundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corona_informaion);
        setStatusBar();


        //initialize view
        domestic = (TextView) findViewById(R.id.domestic_txt);
        abroad = (TextView) findViewById(R.id.abroad_txt);
        DECIDE_CNT = (TextView) findViewById(R.id.DECIDE_CNT);
        CLEAR_CNT = (TextView) findViewById(R.id.CLEAR_CNT);
        EXAM_CNT = (TextView) findViewById(R.id.EXAM_CNT);
        DEATH_CNT = (TextView) findViewById(R.id.DEATH_CNT);
        backButton = findViewById(R.id.back_coronainfo_activity);

        //set listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //set datetime
        Date currentTime = Calendar.getInstance().getTime();

        String check_day = new SimpleDateFormat("HH", Locale.getDefault()).format(currentTime);
        Log.i("모은","check_day : "+check_day);
        if( Integer.parseInt(check_day) < 10){
            Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DATE, -1);
            today = new SimpleDateFormat("yyyyMMdd").format(yesterday.getTime());
            Log.i("모은", "날짜 : " + today);

        }
        else{
            today = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(currentTime);
            Log.i("모은", "날짜 : " + today);
        }


        Calendar week = Calendar.getInstance();
        week.add(Calendar.DATE, -7);
        beforeWeek = new SimpleDateFormat("yyyyMMdd").format(week.getTime());
        Log.i("모은", "일주일 전 날짜 : " + beforeWeek);


        day = new ArrayList<String>();
        for (int i = 1; i <= 7; i++) {
            Calendar cal_day = Calendar.getInstance();
            cal_day.add(Calendar.DATE, -i);
            String bar_day = new SimpleDateFormat("MM/dd").format(cal_day.getTime());
            day.add(bar_day);
            Log.i("모은", "bar_day :" + bar_day);

        }


        // set thread
        new Thread() {
            public void run() {
                CrawlingTodayCorona();
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    APiTodayCorona();
                } catch (IOException | SAXException | ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        }.start();


        //set viewpage ( + indicator/ handler)
        viewPager = findViewById(R.id.viewPager);
        ArrayList<CoronaInformation_VO> list = new ArrayList<>();
        list.add(new CoronaInformation_VO("코로나 19 정부 사이트\n바로 가기", R.drawable.slide_image1, "http://ncov.mohw.go.kr/"));
        list.add(new CoronaInformation_VO("질병 관리 본부 사이트\n바로 가기", R.drawable.slide_image2, "http://www.kdca.go.kr/"));
        list.add(new CoronaInformation_VO("행정 안전부 사이트\n바로 가기", R.drawable.slide_image3, "https://www.mois.go.kr/frt/a01/frtMain.do"));
        viewPager.setAdapter(new ViewPagerAdapter(list));

        CircleIndicator3 circleIndicator = (CircleIndicator3) findViewById(R.id.indicator);
        circleIndicator.setViewPager(viewPager);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if (currentPage == 3) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

    }

    Handler CoronaHandler = new Handler(new Handler.Callback() {
        @SuppressLint("SetTextI18n")
        @Override
        public boolean handleMessage(Message msg) {
            // todo
            Bundle bundle = msg.getData();
            domestic.setText(bundle.getString("domestic") + "명");
            abroad.setText(bundle.getString("abroad") + "명");
            return true;
        }
    });

    Handler APIHandler = new Handler(new Handler.Callback() {
        @SuppressLint("SetTextI18n")
        @Override
        public boolean handleMessage(Message msg) {
            // todo

            DecimalFormat myFormatter = new DecimalFormat("###,###");


            DECIDE_CNT.setText(myFormatter.format(Integer.parseInt(coronaList.get(0).decide_cnt)) + "명");
            CLEAR_CNT.setText(myFormatter.format(Integer.parseInt(coronaList.get(0).clear_cnt)) + "명");
            EXAM_CNT.setText(myFormatter.format(Integer.parseInt(coronaList.get(0).exam_cnt ))+ "명");
            DEATH_CNT.setText(myFormatter.format(Integer.parseInt(coronaList.get(0).death_cnt)) + "명");
            drawBarChart();
            for (int i = 0; i < coronaList.size(); i++) {
                Log.i("모은", "코로나리스트 " + i + " " + coronaList.get(i).getDate());
            }
            return true;
        }
    });

    public void APiTodayCorona() throws IOException, SAXException, ParserConfigurationException {


        StringBuilder urlBuilder = new StringBuilder("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19InfStateJson"); /*URL*/

        try {
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=y1hjUzPWOK7R6uEVHpQx492VRktHBQiZP%2B0UhSzGqbAAB9E1J2njbwrAhSXs6SCj4ldloVYk0yIfVWuX9bpLyQ%3D%3D"); /*Service Key*/

            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("startCreateDt", "UTF-8") + "=" + URLEncoder.encode(beforeWeek, "UTF-8")); /*검색할 생성일 범위의 시작*/
            urlBuilder.append("&" + URLEncoder.encode("endCreateDt", "UTF-8") + "=" + URLEncoder.encode(today, "UTF-8")); /*검색할 생성일 범위의 종료*/


            java.net.URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            Log.i("모은", "Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            Log.i("모은", sb.toString());//xml 출력

            //xml to json
            XmlToJson xmlToJson = new XmlToJson.Builder(sb.toString()).build();

            JSONObject jsonObject = xmlToJson.toJson();
            Log.i("모은", "json -> " + jsonObject.toString());
            jsonObject = jsonObject.getJSONObject("response");
            jsonObject = jsonObject.getJSONObject("body");
            jsonObject = jsonObject.getJSONObject("items");

            JSONArray jsonArray = jsonObject.getJSONArray("item");

            String decide_cnt;
            String clear_cnt;
            String exam_cnt;
            String death_cnt;
            coronaList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObject = (JSONObject) jsonArray.get(i);
                Log.i("모은", jObject.toString());
                Log.i("모은", jObject.getString("decideCnt"));
                decide_cnt = jObject.getString("decideCnt");
                clear_cnt = jObject.getString("clearCnt");
                exam_cnt = jObject.getString("examCnt");
                death_cnt = jObject.getString("deathCnt");
                date = jObject.getString("stateDt").replaceAll("-", "");


                if (i == 0) {
                    coronaList.add(new CoronaVO(decide_cnt, clear_cnt, exam_cnt, death_cnt, date));
                } else if (!coronaList.get(coronaList.size() - 1).getDate().equals(date)) {
                    coronaList.add(new CoronaVO(decide_cnt, clear_cnt, exam_cnt, death_cnt, date));
                }
//                else if (!coronaList.get(coronaList.size()-2).getDate().equals(date)) {
//                    coronaList.add(new CoronaVO(decide_cnt, clear_cnt, exam_cnt, death_cnt, date));
//                }

            }


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            Message msg = APIHandler.obtainMessage();
            APIHandler.sendMessage(msg);
        }


    }

    public void CrawlingTodayCorona() {

        String URL = "http://ncov.mohw.go.kr/";

        try {
            Document rawData = Jsoup.connect(URL)
                    .timeout(5000)
                    .get();

            Elements elements = rawData.select("div[class=datalist]").select("ul").select("li").select(".data");

            String domestic = elements.get(0).ownText();
            String abroad = elements.get(1).ownText();

            Log.i("모은 domestic", domestic);
            Log.i("모은 abroad", abroad);

            Coronabundle.putString("domestic", domestic);
            Coronabundle.putString("abroad", abroad);

            Message msg = CoronaHandler.obtainMessage();
            msg.setData(Coronabundle);
            CoronaHandler.sendMessage(msg);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @SuppressLint({"ResourceType", "SimpleDateFormat"})
    private void drawBarChart() {
        //막대 그래프
        BarChart barChart = (BarChart) findViewById(R.id.chart_corona_coronainfo_activity);//layout의 id;

        //확대 불가능
        barChart.setPinchZoom(true);
        barChart.setScaleXEnabled(false);
        barChart.setScaleYEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);

        barChart.animateY(2000);

        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(xAxisFormatter);

        xAxis.setTextColor(Color.parseColor("#000000"));
        xAxis.setGranularity(1f);

        YAxis yAxisL = barChart.getAxisLeft();
        YAxis yAxisR = barChart.getAxisRight();
        yAxisL.setAxisMinimum(0f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawLabels(true);
        yAxisL.setDrawAxisLine(false);
        yAxisL.setDrawLabels(false);
        yAxisL.setDrawZeroLine(true);
        yAxisR.setDrawGridLines(false);
        yAxisR.setDrawAxisLine(false);
        yAxisR.setDrawLabels(false);

        //remove horizontal lines
        AxisBase axisBase = barChart.getAxisLeft();
        axisBase.setDrawGridLines(false);

        List<BarEntry> entry_chart = new ArrayList<>();


        String bar_date = today;
        for (int i = 1, j = 7; i <= 7; i++, j--) {
            Calendar week = Calendar.getInstance();
            week.add(Calendar.DATE, -i - 1);
            bar_date = new SimpleDateFormat("yyyyMMdd").format(week.getTime());
//            Log.i("모은", "bar_date : " + bar_date);
//            Log.i("모은", "계산 " + String.valueOf(Integer.parseInt(coronaList.get(i - 1).getDecide_cnt()) - Integer.parseInt(coronaList.get(i).getDecide_cnt())));
            entry_chart.add(new BarEntry(j, Integer.parseInt(coronaList.get(i - 1).getDecide_cnt()) - Integer.parseInt(coronaList.get(i).getDecide_cnt())));
            Log.i("모은", "j : " + j);

        }


        BarDataSet barDataSet = new BarDataSet(entry_chart, "");
        barDataSet.setColors(Color.parseColor(getString(R.color.twitterBlue)));
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        barDataSet.setValueFormatter(new MyValueFormatter());
        barDataSet.setValueTextColor(Color.parseColor("#000000"));
        barChart.setData(barData);
        barChart.setDescription(null);

        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

        barData.setValueTextSize(11);
        barChart.setBackgroundColor(Color.parseColor("#00000000"));

        barChart.invalidate();
    }

    private void setStatusBar() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));//색 지정
    }

    public class DayAxisValueFormatter extends ValueFormatter {
        private final BarLineChartBase<?> chart;


        public DayAxisValueFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value) {

            switch ((int) value) {
                case 1:
                    return day.get(6);
                case 2:
                    return day.get(5);
                case 3:
                    return day.get(4);
                case 4:
                    return day.get(3);
                case 5:
                    return day.get(2);
                case 6:
                    return day.get(1);
                case 7:
                    return day.get(0);

                default:
                    return "0";
            }

        }
    }

    public class MyValueFormatter extends ValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,###"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value) {
            // write your logic here
            return mFormat.format(value) + " 명"; // e.g. append a dollar-sign
        }
    }

}
