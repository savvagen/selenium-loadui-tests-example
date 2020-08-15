package com.example.selenium_mob_up_proxy;


import com.browserup.bup.BrowserUpProxy;
import com.browserup.bup.BrowserUpProxyServer;
import com.browserup.bup.client.ClientUtil;
import com.browserup.bup.proxy.CaptureType;
import com.browserup.harreader.model.Har;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.example.utilities.HarWriter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.io.IOException;


public class BrowserUpProxyExample {


    public static BrowserUpProxy proxy;

    @BeforeAll
    public static void setup() throws IOException {
        //Files.createDirectory(new File("hars").toPath());
        //FileUtils.cleanDirectory(new File("hars"));

        proxy = new BrowserUpProxyServer();
        proxy.start();

        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        // configure it as a desired capability
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

        //proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
        proxy.setHarCaptureTypes(CaptureType.getAllContentCaptureTypes());
        proxy.setHarCaptureTypes(CaptureType.getHeaderCaptureTypes());

        Configuration.baseUrl = "http://google.com";
        Configuration.browserCapabilities = capabilities;

        //Configuration.proxyEnabled = true;
        //Configuration.proxyPort = 4444;
        //Configuration.proxyHost = "127.0.0.1";
    }

    @Test
    public void shouldOpenSearchPage() throws IOException {
        proxy.newHar("google");
        Selenide.open("/");
        Har har =  proxy.endHar();
        HarWriter.writeHAR(new File("hars/test2.har"), har.getLog());
    }


}
