package com.reman8683;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.reman8683.utils.Console;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {
    public static IGClient client;

    public static void main(String[] args) {
        try {
            client = IGClient.builder()
                    .username("today.jeonggwanhs.meal")
                    .password("rlaxoghks07")
                    .login();
        } catch (IGLoginException e) {
            throw new RuntimeException(e);
        }


        Calendar today = Calendar.getInstance(Locale.KOREA); // 오늘
        Calendar scheduledTime = Calendar.getInstance(Locale.KOREA); // 실행 시간
        scheduledTime.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        scheduledTime.set(Calendar.HOUR_OF_DAY, 14); //2PM
        scheduledTime.set(Calendar.MINUTE, 0);
        scheduledTime.set(Calendar.SECOND, 0);

        if (scheduledTime.before(today)) {
            scheduledTime.add(Calendar.DATE, 1);  // 다음 날로 설정
        }

        Timer timer = new Timer();
        timer.schedule(new PostInstagram(),
                scheduledTime.getTime(),
                TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // period: 하루

        while (true) {
            new Console().run(); // stop & run
        }
    }
}