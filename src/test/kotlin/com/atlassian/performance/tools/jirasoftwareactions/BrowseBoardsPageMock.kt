package com.atlassian.performance.tools.jirasoftwareactions

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import java.util.*

internal class BrowseBoardsPageMock {
    private val boardsFilter = WebElementMapping(
        By.cssSelector("#ghx-manage-boards-filter a"),
        listOf(
            object : WebElementMock(emptyMap()) {
                override fun click() {
                }
            }
        )
    )
    private val scrumFilter = WebElementMapping(
        By.className("type-filter-scrum"),
        listOf(
            object : WebElementMock(mapOf("data-board-id" to "1")) {
                override fun click() {
                    filterScrumBoards()
                }
            }
        )
    )
    private val initialBoards = listOf(
        StaleElementMock(WebElementMock(mapOf("data-board-id" to "1"))),
        StaleElementMock(WebElementMock(mapOf("data-board-id" to "2"))),
        StaleElementMock(WebElementMock(mapOf("data-board-id" to "3"))),
        StaleElementMock(WebElementMock(mapOf("data-board-id" to "4"))),
        StaleElementMock(WebElementMock(mapOf("data-board-id" to "5")))
    )
    private var boardsList = WebElementMapping(By.cssSelector(".boards-list tr"), initialBoards)
    private val driver: DriverMock = DriverMock(
        mutableMapOf(
            boardsFilter.selector to boardsFilter.elements,
            scrumFilter.selector to scrumFilter.elements,
            boardsList.selector to boardsList.elements
        )
    )

    fun getDriver(): WebDriver {
        return driver
    }

    private fun filterScrumBoards() {
        initialBoards.last().isStale = true
        Timer().schedule(
            object : TimerTask() {
                override fun run() {
                    for (staleElementMock in initialBoards) {
                        staleElementMock.isStale = true
                    }
                    driver.mappings[By.cssSelector(".boards-list tr")] = listOf(WebElementMock(mapOf("data-board-id" to "1")))
                }
            },
            50
        )
    }
}

private data class WebElementMapping(val selector: By, val elements: List<WebElement>)