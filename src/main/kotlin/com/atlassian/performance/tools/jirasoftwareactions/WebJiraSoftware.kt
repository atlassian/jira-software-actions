package com.atlassian.performance.tools.jirasoftwareactions

import com.atlassian.performance.tools.jiraactions.WebJira
import com.atlassian.performance.tools.jirasoftwareactions.page.BrowseBoardsPage
import com.atlassian.performance.tools.jirasoftwareactions.page.ViewBoardPage

class WebJiraSoftware(
    private val jira: WebJira
) {
    fun goToViewBoard(
        agileBoardId: String
    ): ViewBoardPage {
        jira.navigateTo("secure/RapidBoard.jspa?rapidView=$agileBoardId")
        return ViewBoardPage(jira.driver)
    }

    fun goToBrowseBoards(): BrowseBoardsPage {
        jira.navigateTo("secure/ManageRapidViews.jspa")
        return BrowseBoardsPage(jira.driver)
    }
}