package com.reman8683;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

public class DrawImage {
    /**
     * @param title [String] 이미지의 타이틀
     * @param mealData [List<String>] 급식 메뉴
     * @return [ByteArrayOutputStream] 급식 메뉴 이미지
     * @throws IOException
     * @throws FontFormatException
     * @throws URISyntaxException
     */
    public ByteArrayOutputStream GenerateImage(String title, List<String> mealData) throws IOException, FontFormatException, URISyntaxException {
        BufferedImage bufferedImage = new BufferedImage(1080, 1920, BufferedImage.TYPE_INT_RGB);

        Graphics graphic = bufferedImage.getGraphics();

        Graphics2D graphics = (Graphics2D) graphic;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //배경
        graphics.setColor(new Color(60, 72, 107));
        graphics.fillRect(0, 0, 1080, 1920);

        //폰트
        InputStream titlefontIs = DrawImage.class.getClassLoader().getResourceAsStream("BlackHanSans-Regular.ttf");
        InputStream mainfontIs = DrawImage.class.getClassLoader().getResourceAsStream("S-Core_Dream_OTF/SCDream4.otf");


        assert titlefontIs != null;
        Font titlefont = Font.createFont(Font.TRUETYPE_FONT, titlefontIs);

        assert mainfontIs != null;
        Font mainfont = Font.createFont(Font.TRUETYPE_FONT, mainfontIs);

        graphics.setFont(titlefont.deriveFont(175f));
        FontMetrics titlefontMetrics = graphics.getFontMetrics();

        //타이틀 위치
        int titleX = (1080 - titlefontMetrics.stringWidth(title)) / 2;
        int titleY = (int) ((1920 - titlefontMetrics.getHeight()) / 2 + titlefontMetrics.getAscent()
                - (mealData.size() * graphics.getFontMetrics(titlefont.deriveFont(175f)).getHeight()
                - titlefontMetrics.getHeight()) / 4 * 1.5);

        //타이틀
        graphics.setColor(new Color(249, 217, 73));
        graphics.drawString(title, titleX, titleY);

        //메인
        graphics.setFont(mainfont.deriveFont(75f));
        FontMetrics mainfontMetrics = graphics.getFontMetrics();
        int offsetY = 0;

        graphics.setColor(new Color(240, 240, 240));
        for (String element : mealData) {
            offsetY += mainfontMetrics.getAscent() * 1.5;
            String draw = element.replaceAll("\\(.*\\)", "");
            System.out.println(draw);
            graphics.drawString(draw, (1080 - mainfontMetrics.stringWidth(draw)) / 2, titleY + offsetY);

        }


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", outputStream);
        return outputStream;
    }
}
