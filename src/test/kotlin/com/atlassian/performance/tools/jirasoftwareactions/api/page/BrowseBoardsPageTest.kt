package com.atlassian.performance.tools.jirasoftwareactions.api.page

import com.atlassian.performance.tools.jirasoftwareactions.DriverMock
import com.atlassian.performance.tools.jirasoftwareactions.WebElementMock
import org.assertj.core.api.Assertions.*
import org.junit.Test
import org.openqa.selenium.*

class BrowseBoardsPageTest {

    @Test
    fun shouldNotReturnNullValuesWhenBoardListHasNoAttributes() {
        val browseBoardsPage = BrowseBoardsPage(
            DriverMock(
                mapOf(By.cssSelector(".boards-list tr") to listOf(WebElementMock(emptyMap())))
            )
        )

        val boardIds = browseBoardsPage.getBoardIds()

        assertThat(boardIds).doesNotContainNull()
    }
}