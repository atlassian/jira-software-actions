package com.atlassian.performance.tools.jirasoftwareactions.api.page

import com.atlassian.performance.tools.jiraactions.api.page.wait
import com.atlassian.performance.tools.jirasoftwareactions.webdriver.JavaScriptUtils
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated
import org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElementLocated
import java.time.Duration

class ViewBacklogPage(
    private val driver: WebDriver
) {
    private val sprintsLocator = By.cssSelector(".ghx-sprint-group .ghx-backlog-container")

    fun waitForBacklog(): WebElement? {
        return driver.wait(
            Duration.ofSeconds(30),
            presenceOfElementLocated(By.cssSelector("#ghx-backlog[data-rendered]:not(.browser-metrics-stale)"))
        )
    }

    fun getIssueKeys(): List<String> {
        return JavaScriptUtils.executeScript(
            driver,
            "return Array.from(document.getElementsByClassName('js-issue'), i => i.getAttribute('data-issue-key'))"
        )
    }

    fun listSprints(): List<SprintOnBacklog> = driver
        .findElements(sprintsLocator)
        .map { SprintOnBacklog(driver, it) }

    fun validateSprintName(expectedSprintName: String) {
        driver.wait(
            Duration.ofSeconds(30),
            textToBePresentInElementLocated(By.cssSelector(".ghx-backlog-container .ghx-name"), expectedSprintName)
        )
    }
}
