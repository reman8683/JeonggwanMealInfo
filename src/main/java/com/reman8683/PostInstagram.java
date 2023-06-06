package com.reman8683;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.instagram4j.instagram4j.models.highlights.Highlight;
import com.github.instagram4j.instagram4j.requests.highlights.HighlightsCreateReelRequest;
import com.github.instagram4j.instagram4j.requests.highlights.HighlightsDeleteReelRequest;
import com.github.instagram4j.instagram4j.requests.highlights.HighlightsUserTrayRequest;
import com.github.instagram4j.instagram4j.responses.IGResponse;
import com.github.instagram4j.instagram4j.responses.highlights.HighlightsCreateReelResponse;
import com.github.instagram4j.instagram4j.responses.highlights.HighlightsUserTrayResponse;
import com.github.instagram4j.instagram4j.responses.media.MediaResponse;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.reman8683.Main.client;

public class PostInstagram extends TimerTask {
    public void run() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMdd");

        String serialDate = timeFormat.format(calendar.getTime());
        System.out.println(serialDate);

        try {
            List<String> mealData = new GetMeal().GetMealFromNeis(serialDate);
            String title = new GetMeal().SerialDateToGeneralDate(serialDate);

            ByteArrayOutputStream outputStream = new DrawImage().GenerateImage(title, mealData);

            CompletableFuture<HighlightsUserTrayResponse> highlightsUserTray = new HighlightsUserTrayRequest(client.getSelfProfile().getPk()).execute(client);

            List<Highlight> highlightId = highlightsUserTray.get().getTray();
            if (!highlightId.isEmpty()) {
                IGResponse deleteResponse = new HighlightsDeleteReelRequest(highlightId.get(0).getId()).execute(client).join();
                System.out.println(deleteResponse.getStatus());
            }

            MediaResponse.MediaConfigureToStoryResponse storyResponse = client.actions()
                    .story()
                    .uploadPhoto(outputStream.toByteArray(), Collections.emptyList())
                    .join();

            HighlightsCreateReelResponse response =
                    new HighlightsCreateReelRequest(title, storyResponse.getMedia().getId()).execute(client).join();

            System.out.println(response.getStatus());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
