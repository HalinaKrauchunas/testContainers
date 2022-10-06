package com.example.testconatainersdemo.selenide

import com.codeborne.selenide.WebDriverRunner
import com.example.testconatainersdemo.BaseEnd2EndTest
import org.openqa.selenium.By
import org.openqa.selenium.remote.RemoteWebDriver
import spock.lang.Shared
import spock.lang.Unroll

import static com.codeborne.selenide.Selenide.$
import static com.codeborne.selenide.Selenide.open
import static com.codeborne.selenide.Selenide.sleep
import static com.example.testconatainersdemo.service.ResponseServiceTest.getBodyAfterDisarmDevice
import static com.example.testconatainersdemo.service.ResponseServiceTest.getBodyAfterGetStateForDevice

class SelenideTest extends BaseEnd2EndTest {

    @Shared
    def myGoldenDevUrl = 'https://mygoldendev.arlo.com/'
    @Shared
    def loginHome = $(By.className("login-button-home"))
    @Shared
    def email = $(By.id("email"))
    @Shared
    def password = $(By.id("password"))
    @Shared
    def login = $(By.className("login-button"))
    @Shared
    def location = $(By.id("header-location-group-id"))
    @Shared
    def office = $(By.id("location-item-0-id"))
    @Shared
    def close = $(By.className("close"))
    @Shared
    def routines = $(By.id("automation_action_nav_routines"))
    @Shared
    def standBy = $(By.id("standby-ID"))
    @Shared
    def armAway = $(By.id("armAway-ID"))
    @Shared
    def ms = 10000L


    def setup() {
        RemoteWebDriver driver = chrome.getWebDriver()
        WebDriverRunner.setWebDriver(driver)
    }

    @Unroll
    def 'the device in the location should change the mode when the button is clicked'() {
        given: 'disarm device using hmsgoogleapi'
            getBodyAfterDisarmDevice(userId, deviceId, pin) == expectResponse(deviceId, false)
        and: 'log in to the site'
            open(myGoldenDevUrl)
            loginHome.click()
            email.sendKeys(accEmail)
            password.sendKeys(accPassword)
            login.click()
            sleep(ms)
        and: 'choose location'
            location.click()
            office.click()
            close.click()
        and:
            routines.click()

        when: 'set arm mode on the site'
            armAway.click()
            sleep(ms)

        then: 'device state changes to arm'
            getBodyAfterGetStateForDevice(userId, deviceId) == expectResponse(deviceId, true)

        when: 'set disarm mode on the site'
            standBy.click()
            sleep(ms)

        then: 'device state changes to disarm'
            getBodyAfterGetStateForDevice(userId, deviceId) == expectResponse(deviceId, false)
    }


    def expectResponse(deviceId, isArmed) {
        """{"success":true,"data":{"${deviceId}":{"online":true,"isArmed":${isArmed},"status":"SUCCESS"}}}"""
    }
}
