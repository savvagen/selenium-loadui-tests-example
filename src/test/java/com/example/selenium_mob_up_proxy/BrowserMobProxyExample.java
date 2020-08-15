package com.example.selenium_mob_up_proxy;

import com.codeborne.selenide.Configuration;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.io.IOException;
import static com.codeborne.selenide.Selenide.open;


public class BrowserMobProxyExample {


    public static BrowserMobProxy bmp;

    @BeforeAll
    public static void setup() throws IOException {
        //Files.createDirectory(new File("hars").toPath());
        //FileUtils.cleanDirectory(new File("hars"));

        bmp = new BrowserMobProxyServer();
        bmp.setTrustAllServers(true);
        bmp.setHarCaptureTypes(CaptureType.getAllContentCaptureTypes());
        bmp.setHarCaptureTypes(CaptureType.getHeaderCaptureTypes());
        bmp.start(4444);

        Configuration.baseUrl = "http://google.com";
        Configuration.pageLoadStrategy = "normal";
    }

    @AfterAll
    public static void tearDown(){ bmp.stop(); }


    @Test
    public void shouldOpenLoginPage() throws IOException {
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(bmp);

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        Configuration.browserCapabilities = desiredCapabilities;

        bmp.newHar();
        open("/");
        Har har = bmp.endHar();

        /*List<net.lightbody.bmp.core.har.HarEntry> entries = har.getLog().getEntries();
        for (net.lightbody.bmp.core.har.HarEntry entry: entries){
            System.out.println(entry.getResponse().getStatus());
        }*/
        har.writeTo(new File("hars/test.har"));
    }
}
