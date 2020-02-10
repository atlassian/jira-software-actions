package com.atlassian.performance.tools.jirasoftwareactions.api.page

import com.atlassian.performance.tools.jiraactions.api.page.JiraErrors
import com.atlassian.performance.tools.jiraactions.api.page.wait
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions.*
import java.time.Duration

class BrowseBoardsPage(
    private val driver: WebDriver
) {
    fun waitForBoardsList(): BrowseBoardsPage {
        val jiraErrors = JiraErrors(driver)
        driver.wait(
            Duration.ofSeconds(30),
            or(
                and(
                    textToBePresentInElementLocated(By.cssSelector("#ghx-header h2"), "Boards"),
                    presenceOfElementLocated(By.cssSelector("#ghx-content-main table.aui"))
                ),
                jiraErrors.anyCommonError()
            )
        )
        jiraErrors.assertNoErrors()
        return this
    }

    fun getBoardIds(): Collection<String> =
        driver.findElements(By.cssSelector(".boards-list tr"))
            .mapNotNull { it.getAttribute("data-board-id") }

    fun getScrumBoardIds(): Collection<String> {
        val boardsBeforeFiltering = driver.findElements(By.cssSelector(".boards-list tr"))
        if (boardsBeforeFiltering.isEmpty()) {
            return emptyList()
        }
        driver.wait(
            Duration.ofSeconds(5), 
            elementToBeClickable(By.cssSelector("#ghx-manage-boards-filter a"))
        ).click()

        driver.wait(
            Duration.ofSeconds(5),
            elementToBeClickable(By.className("type-filter-scrum"))
        ).click()
        
        driver.wait(
            Duration.ofSeconds(15),
            and(
                *boardsBeforeFiltering.map { stalenessOf(it) }.toTypedArray()
            )
        )
        return getBoardIds()
    }
}