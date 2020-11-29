package com.passta.a2ndproj.main.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.passta.a2ndproj.MainActivity;
import com.passta.a2ndproj.R;
import com.passta.a2ndproj.main.DataVO.Msg_VO;
import com.passta.a2ndproj.main.Callback.DialogDeleteListener;

import java.util.ArrayList;
import java.util.Arrays;


public class MsgInfoDialog extends Dialog {

    private TextView tag, location, time, categoty, http;
    private ImageView img;
    private Button deleteMsg, confirm;
    private ArrayList<String> categoryArray;
    private MainActivity mainActivity;
    private String tagString;
    private Msg_VO msgVo;
    private int imgNumber;
    private String httpString;

    public MsgInfoDialog(@NonNull Context context, Msg_VO msgVo, MainActivity mainActivity, String timeString,int chlidPosition) {
        super(context);
        this.mainActivity = mainActivity;
        this.msgVo = msgVo;

        categoryArray = new ArrayList<String>(Arrays.asList("코로나 동선", "코로나 발생/방역", "코로나 안전수칙", "재난/날씨", "경제/금융"));

        setContentView(R.layout.dialogue_msg_info);
        tag = findViewById(R.id.tag_name_msg_info);
        location = findViewById(R.id.location_msg_info);
        time = findViewById(R.id.time_msg_info);
        categoty = findViewById(R.id.category_msg_info);
        http = findViewById(R.id.http_msg_info);
        img = findViewById(R.id.img_msg_info);
        confirm = findViewById(R.id.confirm_msg_info);
        deleteMsg = findViewById(R.id.delete_msg_info);

        // 태그의 이름을 찾아서 저장.
        setHashtag();
        // 레벨에따른 이미지
        getImg();
        //http 주소
        setHttpString();

        //디바이스크기에맞게 가로사이즈 지정하기위함
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Window window = this.getWindow();
        int x = (int) (size.x * 0.9f);
        int y = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setLayout(x, y);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setDimAmount(0.81f);

        tag.setText("#" + tagString);
        if(msgVo.getSenderLocation().equals("중대본 전체"))
            location.setText("중앙 대책 본부");
        else
            location.setText(msgVo.getSenderLocation());
        time.setText(timeString);
        categoty.setText(categoryArray.get(msgVo.getCategroyIndex()));
        img.setImageResource(imgNumber);
        http.setPaintFlags(http.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        http.setText(httpString);

        http.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(httpString));
                context.startActivity(intent);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        deleteMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckDeleteMsgDialog checkDeleteMsgDialog = new CheckDeleteMsgDialog(context, mainActivity, msgVo, chlidPosition,
                        new DialogDeleteListener() {
                            @Override
                            public void dissmissListener() {
                                dismiss();
                            }
                        });
            }
        });

        show();


    }

    public void setHashtag() {

        if(msgVo.getSenderLocation().equals("중대본 전체")){
            tagString = "중앙 대책 본부";
            return;
        }

        for (int i = 0; i < mainActivity.userList.size(); i++) {
            String userLocation = mainActivity.userList.get(i).getLocation_si() + " " + mainActivity.userList.get(i).getLocation_gu();
            if (userLocation.equals(msgVo.getSenderLocation()) || (
                    mainActivity.userList.get(i).getLocation_si().equals(msgVo.getSenderLocation().split(" ")[0]) &&
                            msgVo.getSenderLocation().split(" ")[1].equals("전체")
            )) {
                tagString = mainActivity.userList.get(i).getTag();
                break;
            }
        }

        //널로 반환되면 같은시의 전체 문자를 찾는다.
        if(tagString == null){
            for (int i = 0; i < mainActivity.userList.size(); i++) {
                if(mainActivity.userList.get(i).getLocation_si().equals(msgVo.getSenderLocation().split(" ")[0]) &&
                        mainActivity.userList.get(i).getLocation_gu().equals("전체")){
                    tagString = mainActivity.userList.get(i).getTag();
                    break;
                }
            }
        }

    }

    public void setHttpString() {
        String si = msgVo.getSenderLocation().split(" ")[0];
        String gu = msgVo.getSenderLocation().split(" ")[1];

        if (si.equals("서울특별시")) {
            if (gu.equals("전체"))
                httpString = "https://www.seoul.go.kr/main/indexM.jsp";
            else if (gu.equals("도봉구"))
                httpString = "http://m.dobong.go.kr/Contents.asp?code=10007859";
            else if (gu.equals("금천구"))
                httpString = "https://www.geumcheon.go.kr/portal/intro.do";
            else if (gu.equals("노원구"))
                httpString = "https://www.nowon.kr/corona19/index.do";
            else if (gu.equals("강북구"))
                httpString = "https://www.geumcheon.go.kr/portal/intro.do";
            else if (gu.equals("은평구"))
                httpString = "https://www.ep.go.kr/CmsWeb/viewPage.req?idx=PG0000004987";
            else if (gu.equals("성북구"))
                httpString = "http://www.sb.go.kr/PageLink.do?link=forward:use/cop/bbs/selectBoardList.do?bbsId=B0050_main&tempParam1=&menuNo=05000000&subMenuNo=05140000&thirdMenuNo=05142100&fourthMenuNo=";
            else if ( gu.equals("중랑구"))
                httpString = "https://www.jungnang.go.kr/intro.jsp";
            else if (gu.equals("종로구"))
                httpString = "https://www.jongno.go.kr/Main.do?menuId=400516&menuNo=400516";
            else if (gu.equals("동대문구"))
                httpString = "http://m.ddm.go.kr/life/covid19/bbs/flowList.jsp";
            else if (gu.equals("서대문구"))
                httpString = "http://www.sdm.go.kr/news/corona19/coronaInfo.do?mode=view&sdmBoardSeq=236109";
            else if (gu.equals("중구"))
                httpString = "http://www.junggu.seoul.kr/covid.jsp#!";
            else if (gu.equals("성동구"))
                httpString = "http://www.sd.go.kr/sd/intro.do";
            else if (gu.equals("광진구"))
                httpString = "https://www.gwangjin.go.kr/portal/bbs/B0000259/list.do?menuNo=201389";
            else if (gu.equals("강동구"))
                httpString = "https://www.gangdong.go.kr/site/contents/corona/index.html#rollup-title1";
            else if (gu.equals("송파구"))
                httpString = "http://www.songpa.go.kr/route/index.jsp";
            else if (gu.equals("강남구"))
                httpString = "https://www.gangnam.go.kr/path.htm";
            else if (gu.equals("서초구"))
                httpString = "https://www.seocho.go.kr/html/notice/main.jsp?con=16#con16";
            else if (gu.equals("금천구"))
                httpString = "https://www.geumcheon.go.kr/portal/selectBbsNttList.do?bbsNo=150301&key=3227";
            else if (gu.equals("동작구"))
                httpString = "http://www.dongjak.go.kr/portal/bbs/B0001349/list.do?menuNo=201327";
            else if (gu.equals("용산구"))
                httpString = "https://www.yongsan.go.kr/index.htm";
            else if (gu.equals("마포구"))
                httpString = "http://www.mapo.go.kr/html/corona/intro.htm#n";
            else if (gu.equals("강서구"))
                httpString = "http://www.gangseo.seoul.kr/popup/main.jsp";
            else if (gu.equals("양천구"))
                httpString = "https://www.yangcheon.go.kr/site/yangcheon/coronaStatusList.do";
            else if (gu.equals("구로구"))
                httpString = "https://www.guro.go.kr/corona2.jsp";
            else if (gu.equals("관악구"))
                httpString = "http://www.gwanak.go.kr/site/health/ex/bbs/List.do?cbIdx=587";
            else
                httpString = "https://www.seoul.go.kr/main/indexM.jsp";

        } else if(si.equals("경기도")){
            if (gu.equals("전체"))
                httpString = "https://www.gg.go.kr/";
            else if (gu.equals("가평군"))
                httpString = "https://www.gp.go.kr/intro_gp.jsp";
            else if (gu.equals("고양시"))
                httpString = "http://www.goyang.go.kr/www/emergencyPopup/BD_selectCoronaPopupRoute.do";
            else if (gu.equals("과천시"))
                httpString = "https://m.gccity.go.kr/smart/board/bbs.do?mCode=F100010000&cfgIdx=237";
            else if (gu.equals("광명시"))
                httpString = "http://www.gm.go.kr/#n";
            else if (gu.equals("광주시"))
                httpString = "https://www.gjcity.go.kr/corona_index.jsp";
            else if (gu.equals("구리시"))
                httpString = "http://www.guri.go.kr/main/cityhall#";
            else if (gu.equals("군포시"))
                httpString = "https://www.gunpo.go.kr/intro.jsp";
            else if (gu.equals("김포시"))
                httpString = "https://www.gimpo.go.kr/portal/contents.do?key=4844";
            else if (gu.equals("남양주시"))
                httpString = "https://www.nyj.go.kr/";
            else if (gu.equals("동두천시"))
                httpString = "http://www.ddc.go.kr/";
            else if (gu.equals("부천시"))
                httpString = "http://www.bucheon.go.kr/site/main/corona";

            else if (gu.equals("성남시"))
                httpString = "http://corona.seongnam.go.kr/coronaIndex.do";
            else if (gu.equals("수원시"))
                httpString = "https://www.suwon.go.kr/web/safesuwon/corona/PD_index.do";
            else if (gu.equals("시흥시"))
                httpString = "https://www.siheung.go.kr/corona_policy.jsp";
            else if (gu.equals("안산시"))
                httpString = "https://www.ansan.go.kr/www/coronaBoardN.do";
            else if (gu.equals("안성시"))
                httpString = "https://www.anseong.go.kr/corona_index.jsp";
            else if (gu.equals("안양시"))
                httpString = "https://www.anyang.go.kr/corona5.jsp";
            else if (gu.equals("양주시"))
                httpString = "https://www.yangju.go.kr/intro.jsp";
            else if (gu.equals("양평군"))
                httpString = "https://www.yp21.go.kr/";
            else if (gu.equals("여주시"))
                httpString = "https://www.yeoju.go.kr/corona.jsp";

            else if (gu.equals("연천군"))
                httpString = "https://www.yeoncheon.go.kr/index.yeoncheon?menuCd=DOM_000000141000000000";
            else if (gu.equals("오산시"))
                httpString = "https://www.osan.go.kr/corona_index.jsp";
            else if (gu.equals("용인시"))
                httpString = "https://corona.yongin.go.kr/index.do?q_dgnssInfo=y";
            else if (gu.equals("의왕시"))
                httpString = "https://www.uiwang.go.kr/humanframe/theme/uiwang/html/corona_index/index.html";
            else if (gu.equals("의정부시"))
                httpString = "https://www.ui4u.go.kr/corona_index.jsp";
            else if (gu.equals("이천시"))
                httpString = "https://www.icheon.go.kr/portal/contents.do?key=3567";
            else if (gu.equals("파주시"))
                httpString = "https://m.paju.go.kr/m/index.do";
            else if (gu.equals("평택시"))
                httpString = "https://www.pyeongtaek.go.kr/pyeongtaek/corona_route.jsp";
            else if (gu.equals("포천시"))
                httpString = "http://www.pocheon.go.kr/intro.jsp";
            else if (gu.equals("하남시"))
                httpString = "https://www.hanam.go.kr/intro3.jsp";
            else if (gu.equals("화성시"))
                httpString = "https://www.hscity.go.kr/www/corona/corona.do";
            else
                httpString = "https://www.gg.go.kr/";
        } else if(si.equals("부산광역시")){
            httpString = "http://www.busan.go.kr/covid19/Corona19/travelhist.do";
        }else if(si.equals("대구광역시")){
            httpString = "http://covid19.daegu.go.kr/00937400.html";
        }else if(si.equals("인천광역시")){
            httpString = "https://www.incheon.go.kr/health/HE020409";
        }else if(si.equals("광주광역시")){
            httpString = "https://www.gwangju.go.kr/intro/index.html";
        }else if(si.equals("대전광역시")){
            httpString = "https://www.daejeon.go.kr/corona19/index.do";
        }else if(si.equals("울산광역시")){
            httpString = "https://www.ulsan.go.kr/corona.jsp";
        }else if(si.equals("세종특별자치시")){
            httpString = "https://www.sejong.go.kr/bbs/R3273/list.do?cmsNoStr=18839";
        }else if(si.equals("강원도")){
            httpString = "http://www.provin.gangwon.kr/covid-19.html";
        }else if(si.equals("충청북도")){
            httpString = "http://www1.chungbuk.go.kr/covid-19/index.do";
        }else if(si.equals("충청남도")){
            httpString = "http://www.chungnam.go.kr/coronaStatus.do";
        }else if(si.equals("전라북도")){
            httpString = "https://www.jeonbuk.go.kr/board/list.jeonbuk?boardId=BBS_0000105&menuCd=DOM_000000110001000000&contentsSid=1219&cpath=";
        }else if(si.equals("전라남도")){
            httpString = "https://www.jeonnam.go.kr/coronaMainPage.do";
        }else if(si.equals("경상북도")){
            httpString = "https://www.gb.go.kr/corona_main.htm";
        }else if(si.equals("경상남도")){
            httpString = "http://xn--19-q81ii1knc140d892b.kr/main/main.do";
        }else if(si.equals("제주특별자치도")){
            httpString = "https://www.jeju.go.kr/corona19.jsp";
        }
        else{
            httpString = "http://www.kdca.go.kr/";
        }
    }

    public void getImg() {
        switch (msgVo.getLevel()) {
            case 1:
                imgNumber = R.drawable.level1;
                break;
            case 2:
                imgNumber = R.drawable.level2;
                break;
            case 3:
                imgNumber = R.drawable.level3;
                break;
        }
    }
}
