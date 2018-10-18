package com.atlassian.performance.tools.jirasoftwareactions.api.page

import com.atlassian.performance.tools.jiraactions.api.page.wait
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated
import org.openqa.selenium.WebDriver
import java.time.Duration

class ViewBacklogPage(
    private val driver: WebDriver
) {
    fun waitForBacklog() = driver.wait(
        Duration.ofSeconds(30),
        presenceOfElementLocated(By.cssSelector("#ghx-backlog[data-rendered]:not(.browser-metrics-stale)"))
    )

    fun getIssueKeys(): List<String> {
        return driver
            .findElements(By.className("js-issue"))
            .map { it.getAttribute("data-issue-key") }
    }
}