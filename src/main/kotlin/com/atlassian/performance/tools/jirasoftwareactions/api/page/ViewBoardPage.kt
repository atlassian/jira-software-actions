package com.atlassian.performance.tools.jirasoftwareactions.api.page

import com.atlassian.performance.tools.jiraactions.api.page.wait
import com.atlassian.performance.tools.jirasoftwareactions.webdriver.JavaScriptUtils
import org.openqa.selenium.By.cssSelector
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated
import java.time.Duration

class ViewBoardPage(
    private val driver: WebDriver
) {
    fun waitForBoard() = driver.wait(
        Duration.ofSeconds(30),
        presenceOfElementLocated(cssSelector(".ghx-column"))
    )

    fun getIssueKeys(): List<String> {
        return JavaScriptUtils.executeScript(driver,
            "return Array.from(document.getElementsByClassName('ghx-issue'), i => i.getAttribute('data-issue-key'))"
        )
    }
}
