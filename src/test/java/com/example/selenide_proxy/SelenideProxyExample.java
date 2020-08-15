package com.example.selenide_proxy;

import com.browserup.bup.proxy.CaptureType;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.proxy.SelenideProxyServer;
import com.example.utilities.HarWriter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;


import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.WebDriverRunner.*;

public class SelenideProxyExample {



    @BeforeAll
    public static void setup() {
        Configuration.baseUrl = "http://google.com";
        Configuration.proxyEnabled = true;
        //Configuration.proxyPort = 4444;
        //Configuration.proxyHost = "127.0.0.1";

        SelenideProxyServer proxy = getSelenideProxy();
        proxy.getProxy().enableHarCaptureTypes(CaptureType.getAllContentCaptureTypes());

        /*proxy.addRequestFilter("display.all.requests", (request, contents, messageInfo) -> {
            System.out.println(request.getUri());
            return null;
        });

        proxy.addResponseFilter("my.response.filter", (response, contents, messageInfo) -> {
            if (messageInfo.getOriginalUrl().endsWith("api/tags")){
                System.out.println(contents.getContentType());
            }
        });*/
    }

    @Test
    public void shouldOpenSearchPage() throws IOException {
        Selenide.open("/");
        HarWriter.writeHAR(new File("hars/google.har"), getSelenideProxy().getProxy().getHar().getLog());
    }


    @Test
    public void shouldOpenSearchPageWithTimingApi(){
        Selenide.open("/");
        /*Map<String, Long> timing = executeJavaScript("return window.performance.timing;");
        timing.forEach((key, value)->{
            System.out.println(key + ": " + (timing.get(key))/1000 + " seconds.");
        });
        System.out.println(new ObjectMapper().writeValueAsString(timing));
        */
        Long loadEventEnd = executeJavaScript("return window.performance.timing.loadEventEnd;");
        // Получаем Navigation Event Start (начало перехода)
        Long navigationStart = executeJavaScript("return window.performance.timing.navigationStart;");
        // Разница между Load Event End и Navigation Event Start - это время загрузки страницы
        System.out.println("Page Load Time is " + (loadEventEnd - navigationStart) + " ms.");
    }





}
