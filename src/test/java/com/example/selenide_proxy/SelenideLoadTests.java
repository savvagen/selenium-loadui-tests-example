package com.example.selenide_proxy;

import com.browserup.bup.BrowserUpProxy;
import com.browserup.bup.proxy.CaptureType;
import com.browserup.harreader.model.Har;
import com.browserup.harreader.model.HarPage;
import com.codeborne.selenide.Configuration;
import com.example.utilities.HarWriter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.WebDriverRunner.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.codeborne.selenide.Selenide.*;


public class SelenideLoadTests {

    @BeforeEach
    public void setUpTest(){
        Configuration.browser = "chrome";
        Configuration.baseUrl = "https://react-redux.realworld.io";
        Configuration.startMaximized = true;
        Configuration.proxyEnabled = true;
        open();
    }

    @AfterEach
    public void tearDownTest(){
        getWebDriver().close();
    }


    @Test
    public void shouldTestMainPageLoadTime() throws IOException {
        BrowserUpProxy proxy = getSelenideProxy().getProxy();
        proxy.setHarCaptureTypes(CaptureType.getAllContentCaptureTypes());

        proxy.newHar("home_page");
        open("/#/");
        $(byText("Sign in")).shouldBe(visible);
        Har har = proxy.endHar();
        HarWriter.writeHAR(new File("hars/home_page.har"), har.getLog());
        HarPage page = har.getLog().getPages().get(0);
        assertAll(()->{
            assertTrue(page.getPageTimings().getOnLoad() < 5000, "Load time should be less than 5000 ms");
            assertTrue(page.getPageTimings().getOnLoad() > 0, "Load time should be more than 0 ms");
        });
    }


    @Test
    public void shouldCheckLoginPage() throws IOException {
        getSelenideProxy().getProxy().newHar("login_page");
        open("/#/login");
        $("button[type='submit']").shouldBe(visible);
        Har har = getSelenideProxy().getProxy().endHar();
        HarWriter.writeHAR(new File("hars/login_page.har"), har.getLog());
        HarPage page = har.getLog().getPages().get(0);
        assertAll(()->{
            assertTrue(page.getPageTimings().getOnLoad() < 5000, "Load time should be less than 5000 ms");
            assertTrue(page.getPageTimings().getOnLoad() > 0, "Load time should be more than 0 ms");
        });
    }

    @Test
    public void shouldTestMainPageAfterLogin() throws IOException {
        open("/#/login");
        $("input[type='email']").setValue(System.getenv("AUTH_USERNAME")).pressTab();
        $("input[type='password']").setValue(System.getenv("AUTH_PASSWORD")).pressEnter();
        $("a[href='#settings']").shouldBe(visible);
        // Start new Har
        getSelenideProxy().getProxy().newHar("main_page2");
        open("/#/");
        $("a[href='#settings']").shouldBe(visible);
        Har har = getSelenideProxy().getProxy().endHar();
        HarWriter.writeHAR(new File("hars/login_page2.har"), har.getLog());
        HarPage page = har.getLog().getPages().get(0);
        assertAll(()->{
            assertTrue(page.getPageTimings().getOnLoad() < 5000, "Load time should be less than 5000 ms");
            assertTrue(page.getPageTimings().getOnLoad() > 0, "Load time should be more than 0 ms");
        });
    }



    @ParameterizedTest
    @CsvSource({
            "main_page, /#/, a[href='#login']",
            "login_page, /#/login, button[type='submit']",
            "register_page, /#/register, button[type='submit']"

    })
    @Tag("load_selenide")
    public void verifyAllPages(String pageName, String pageUrl, String selector) throws IOException {
        Configuration.pageLoadStrategy = "normal";

        getSelenideProxy().getProxy().newHar(pageName);

        open(pageUrl);
        $(selector).shouldBe(visible);

        Har har = getSelenideProxy().getProxy().endHar();
        HarWriter.writeHAR(new File("hars/" + pageName + ".har"), har.getLog());
        HarPage page = har.getLog().getPages().stream().filter(p -> p.getTitle().equals(pageName)).collect(Collectors.toCollection(ArrayList::new)).get(0);
        assertAll(()->{
            assertTrue(page.getPageTimings().getOnLoad() < 5000, "Load time should be less than 5000 ms");
            assertTrue(page.getPageTimings().getOnLoad() > 0, "Load time should be more than 0 ms");
        });

    }



}
