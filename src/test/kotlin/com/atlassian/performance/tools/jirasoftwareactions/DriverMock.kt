package com.atlassian.performance.tools.jirasoftwareactions

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import java.lang.Exception

internal class DriverMock(
    val mappings: MutableMap<By, List<WebElement>>
) : WebDriver {

    override fun getCurrentUrl(): String {
        return "http://some.url/"
    }

    override fun getPageSource(): String {
        return "some html"
    }

    override fun quit() {
        throw Exception("unexpected call")
    }

    override fun close() {
        throw Exception("unexpected call")
    }

    override fun switchTo(): WebDriver.TargetLocator {
        throw Exception("unexpected call")
    }

    override fun get(p0: String?) {
        throw Exception("unexpected call")
    }

    override fun manage(): WebDriver.Options {
        throw Exception("unexpected call")
    }

    override fun navigate(): WebDriver.Navigation {
        throw Exception("unexpected call")
    }

    override fun getWindowHandle(): String {
        throw Exception("unexpected call")
    }

    override fun findElement(by: By): WebElement {
        return findElements(by).single()
    }

    override fun getWindowHandles(): MutableSet<String> {
        throw Exception("unexpected call")
    }

    override fun findElements(by: By): List<WebElement> {
        return mappings[by].orEmpty()
    }

    override fun getTitle(): String {
        throw Exception("unexpected call")
    }
}