package com.atlassian.performance.tools.jirasoftwareactions

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

internal class DriverMock(
    val mappings: MutableMap<By, List<WebElement>>
) : WebDriver, JavascriptExecutor {

    @Volatile
    private var executeScriptReturnValue: Any? = null

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

    override fun executeScript(p0: String?, vararg p1: Any?): Any? {
        return executeScriptReturnValue
    }

    override fun executeAsyncScript(p0: String?, vararg p1: Any?): Any {
        throw RuntimeException("not implemented")
    }
    
    fun setExecuteScriptReturnValue(value: Any?) {
        executeScriptReturnValue = value
    }
}