package com.reman8683;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.reman8683.utils.ReadXML;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetMeal {
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

    public JsonArray GetMealFromNeisJson(String date) throws IOException {
        String data = Jsoup.connect(
                        "https://open.neis.go.kr/hub/mealServiceDietInfo?KEY=50cf8994e2ef4b77b11efd3df124036a&Type=json&ATPT_OFCDC_SC_CODE=C10&SD_SCHUL_CODE=7150580&MMEAL_SC_CODE=2&MLSV_YMD=" + date)
                .get().text();

        Gson gson = new Gson();
        JsonObject jsonData = gson.fromJson(data, JsonObject.class);

        String[] MealDataIncludeAllergy = jsonData.get("mealServiceDietInfo").getAsJsonArray()
                .get(1).getAsJsonObject()
                .get("row").getAsJsonArray()
                .get(0).getAsJsonObject()
                .get("DDISH_NM").getAsString().replace(" ", "")

                .replaceAll("\\(.*?\\)", ",")
                .replaceAll(",+", ",")
                .replaceAll("[`~!@#$%^&*|\\\\'\";:/?\\-+].*?,", ",")
                .split(",");
        System.out.println(Arrays.toString(MealDataIncludeAllergy));
        return null;
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
