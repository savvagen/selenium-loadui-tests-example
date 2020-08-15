package com.example.selenium_mob_up_proxy;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarPage;
import net.lightbody.bmp.proxy.CaptureType;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class BrowseMobProxyLoadTests {


    public static BrowserMobProxy bmp;

    @BeforeAll
    public static void setUp() {
        bmp = new BrowserMobProxyServer();
        bmp.setTrustAllServers(true);
        bmp.setHarCaptureTypes(CaptureType.getAllContentCaptureTypes());
        bmp.setHarCaptureTypes(CaptureType.getHeaderCaptureTypes());
        bmp.start(4444);

        //Configuration.baseUrl = "http://google.com";
        //Configuration.pageLoadStrategy = "normal";
    }

    @AfterAll
    public static void tearDown() throws IOException {
        bmp.stop();
    }


    @BeforeEach
    public void setUpTest(){
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(bmp);

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

        Configuration.baseUrl = "http://google.com";
        Configuration.pageLoadStrategy = "normal";
        Configuration.browserCapabilities = desiredCapabilities;
    }


    @AfterEach
    public void tearDownTest(){
        Selenide.closeWindow();
    }


    @Test
    public void shouldOpenLoginPage() throws IOException {
        bmp.newHar("google");
        open("/");
        Har har = bmp.endHar();
        /*List<HarEntry> entries = har.getLog().getEntries();
        for (HarEntry entry: entries){
            System.out.println(entry.getResponse().getStatus());
        }*/
        har.writeTo(new File("hars/google.har"));
        HarPage page = har.getLog().getPages().get(0);
        assertAll(()->{
            assertTrue(page.getPageTimings().getOnLoad() < 5000, "Load time should be less than 5000 ms");
            assertTrue(page.getPageTimings().getOnLoad() > 0, "Load time should be more than 0 ms");
        });
    }


    @Test
    public void shouldOpenLoginPage2() throws IOException {
        bmp.newHar("google1");
        open("/");
        Har har = bmp.endHar();
        har.writeTo(new File("hars/google1.har"));
        HarPage page = har.getLog().getPages().get(0);
        assertAll(()->{
            assertTrue(page.getPageTimings().getOnLoad() < 5000, "Load time should be less than 5000 ms");
            assertTrue(page.getPageTimings().getOnLoad() > 0, "Load time should be more than 0 ms");
        });
    }



    @Test
    public void shouldOpenLoginPage3() throws IOException {
        bmp.newHar("google2");
        open("/");
        Har har = bmp.endHar();
        har.writeTo(new File("hars/google2.har"));
        HarPage page = har.getLog().getPages().get(0);
        assertAll(()->{
            assertTrue(page.getPageTimings().getOnLoad() < 5000, "Load time should be less than 5000 ms");
            assertTrue(page.getPageTimings().getOnLoad() > 0, "Load time should be more than 0 ms");
        });
    }




}
