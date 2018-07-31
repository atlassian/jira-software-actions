package com.atlassian.performance.tools.jirasoftwareactions.page

import com.atlassian.performance.tools.jiraactions.page.wait
import org.openqa.selenium.By.className
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
        return driver
            .findElements(className("ghx-issue"))
            .map { it.getAttribute("data-issue-key") }
    }
}