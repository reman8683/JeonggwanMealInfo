package com.reman8683;

import com.google.gson.*;
import com.reman8683.utils.ReadXML;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetMeal {
    /**
     * @param date [String] 불러올 급식의 날짜 {yyyy/MM/dd}
     * @return [List&lt;String&gt;] 급식 메뉴
     * @throws IOException
     */
    public List<String> GetMealFromNeis(String date) throws IOException {
        String data = Jsoup.connect(
                "https://open.neis.go.kr/hub/mealServiceDietInfo?KEY=50cf8994e2ef4b77b11efd3df124036a&ATPT_OFCDC_SC_CODE=C10&SD_SCHUL_CODE=7150580&MMEAL_SC_CODE=2&MLSV_YMD=202306" + date)
                .get().html();
        Map<String, String> resultMap = new ReadXML().convertXMLToMap(data);
        System.out.println(resultMap);
        List<String> MealData = new ArrayList<>();
        if (resultMap.get("CODE").equals("INFO-000")) {
            MealData = Arrays.stream(
                    Arrays.stream(resultMap.get("row")
                                    .replace(" ", "")
                                    .split("\n")).toList().get(9)
                            .split("<br/>")).toList();
        }
        else MealData.add("급식이 없습니다!");
        return MealData;
    }

    /**
     * @param date [String] 불러올 급식의 날짜 {yyyy/MM/dd}
     * @return [List&lt;String&gt;] 급식 메뉴
     * @throws IOException
     */
    public List<String> GetMealFromBusanEdu(String date) throws IOException {
        String data = Jsoup.connect(
                        "https://school.busanedu.net/jeonggwan-h/dv/dietView/selectDvList.do")
                .data("dietTy", "중식")
                .data("sysId", "jeonggwan-h")
                .data("monthFirst", date)
                .data("monthEnmt", date)
                .post().text();

        Gson gson = new Gson();

        //전송받은 데이터를 [gson] JsonObject 형태로
        JsonArray jsonArray = gson.fromJson(data, JsonArray.class);
        JsonObject jsonMenu = jsonArray.get(jsonArray.size() - 1).getAsJsonObject();

        //불러온 정보가 급식이 아닌경우
        if (jsonMenu.has("bgnde")) {
            return List.of("급식이 없습니다!");
        }

        //메뉴
        String menu = jsonMenu.get("dietCn").getAsString();

        //알레르기 정보 제거, List<String>화
        List<String> AllergyRemovedMenuArray = Arrays.stream(
                menu.replaceAll("\\(.*?\\)", "").split("\n")
        ).toList();

        return AllergyRemovedMenuArray;
    }

    /**
     * @param SerialDate [String] yyyy/MM/dd
     * @return [String] M월d일
     */
    public String SerialDateToGeneralDate(String SerialDate) {
        return SerialDate.substring(5,7)
                .replaceFirst("^0*", "")
                + "월"
                + SerialDate.substring(8)
                .replaceFirst("^0*", "")
                + "일 급식";
    }
}
