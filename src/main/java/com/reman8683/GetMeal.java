package com.reman8683;

import com.reman8683.utils.ReadXML;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetMeal {
    public List<String> GetMealFromNeis(String date) throws IOException {
        String data = Jsoup.connect("https://open.neis.go.kr/hub/mealServiceDietInfo?ATPT_OFCDC_SC_CODE=C10&SD_SCHUL_CODE=7150580&MMEAL_SC_CODE=2&MLSV_YMD=" + date).get().html();
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

    public String SerialDateToGeneralDate(String SerialDate) {
        return SerialDate.substring(4,6)
                .replaceFirst("^0*", "")
                + "월"
                + SerialDate.substring(6, 8)
                .replaceFirst("^0*", "")
                + "일 급식";
    }
}
