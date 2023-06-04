package com.reman8683.utils;

import com.reman8683.PostInstagram;

import java.util.Scanner;

public class Console {
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String input;

        input = scanner.nextLine();
        if (input.equals("stop")) {
            System.out.println("프로그램을 종료합니다.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.exit(0);
        }
        if (input.equals("run")) {
            new PostInstagram().run();
        }
    }
}
