package com.atlassian.performance.tools.jirasoftwareactions.api.page

import com.atlassian.performance.tools.jiraactions.api.page.wait
import com.atlassian.performance.tools.jirasoftwareactions.webdriver.JavaScriptUtils
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated
import java.time.Duration

class ViewBacklogPage(
    private val driver: WebDriver
) {
    fun waitForBacklog() = driver.wait(
        Duration.ofSeconds(30),
        presenceOfElementLocated(By.cssSelector("#ghx-backlog[data-rendered]:not(.browser-metrics-stale)")),
        Duration.ofMillis(100)
    )

    fun getIssueKeys(): List<String> {
        return JavaScriptUtils.executeScript(driver,
            "return Array.from(document.getElementsByClassName('js-issue'), i => i.getAttribute('data-issue-key'))"
        )
    }
}
