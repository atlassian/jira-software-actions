package com.atlassian.performance.tools.jirasoftwareactions.api

import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jirasoftwareactions.api.page.BrowseBoardsPage
import com.atlassian.performance.tools.jirasoftwareactions.api.page.ViewBoardPage

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