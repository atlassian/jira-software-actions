package com.atlassian.performance.tools.jirasoftwareactions.api

import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.action.*
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.adaptive.AdaptiveIssueKeyMemory
import com.atlassian.performance.tools.jiraactions.api.memories.adaptive.AdaptiveIssueMemory
import com.atlassian.performance.tools.jiraactions.api.memories.adaptive.AdaptiveJqlMemory
import com.atlassian.performance.tools.jiraactions.api.memories.adaptive.AdaptiveProjectMemory
import com.atlassian.performance.tools.jiraactions.api.scenario.Scenario
import com.atlassian.performance.tools.jirasoftwareactions.ActionShuffler
import com.atlassian.performance.tools.jirasoftwareactions.api.actions.BrowseBoardsAction
import com.atlassian.performance.tools.jirasoftwareactions.api.actions.ViewBacklogAction
import com.atlassian.performance.tools.jirasoftwareactions.api.actions.ViewBoardAction
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.AgileBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.ScrumBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.AdaptiveBoardMemory

class JiraSoftwareScenario : Scenario {
    /**
     * Different actions have different proportions. The goal is to make the traffic more realistic.
     */
    override fun getActions(
        jira: WebJira,
        seededRandom: SeededRandom,
        meter: ActionMeter
    ): List<Action> {
        val projectMemory = AdaptiveProjectMemory(random = seededRandom)
        val jqlMemory = AdaptiveJqlMemory(seededRandom)
        val issueKeyMemory = AdaptiveIssueKeyMemory(seededRandom)
        val issueMemory = AdaptiveIssueMemory(issueKeyMemory, seededRandom)
        val agileBoardMemory = AdaptiveBoardMemory<AgileBoard>(seededRandom)
        val scrumBoardMemory = AdaptiveBoardMemory<ScrumBoard>(seededRandom)
        val scenario: MutableList<Action> = mutableListOf()
        val createIssue = CreateIssueAction(
            jira = jira,
            meter = meter,
            seededRandom = seededRandom,
            projectMemory = projectMemory
        )
        val searchWithJql = SearchJqlAction(
            jira = jira,
            meter = meter,
            jqlMemory = jqlMemory,
            issueKeyMemory = issueKeyMemory
        )
        val findInitialIssues = ActionShuffler.findIssueKeysWithJql(jira, meter, issueKeyMemory)

        val viewIssue = ViewIssueAction(
            jira = jira,
            meter = meter,
            issueKeyMemory = issueKeyMemory,
            issueMemory = issueMemory,
            jqlMemory = jqlMemory
        )
        val projectSummary = ProjectSummaryAction(
            jira = jira,
            meter = meter,
            projectMemory = projectMemory
        )
        val viewDashboard = ViewDashboardAction(
            jira = jira,
            meter = meter
        )
        val editIssue = EditIssueAction(
            jira = jira,
            meter = meter,
            issueMemory = issueMemory
        )
        val addComment = AddCommentAction(
            jira = jira,
            meter = meter,
            issueMemory = issueMemory
        )
        val browseProjects = BrowseProjectsAction(
            jira = jira,
            meter = meter,
            projectMemory = projectMemory
        )

        val jiraSoftware = WebJiraSoftware(jira)
        val viewBoard = ViewBoardAction(
            jiraSoftware = jiraSoftware,
            meter = meter,
            boardMemory = agileBoardMemory,
            issueKeyMemory = issueKeyMemory
        ) { it.issuesOnBoard != 0 }
        val browseBoards = BrowseBoardsAction(
            jiraSoftware = jiraSoftware,
            meter = meter,
            boardsMemory = agileBoardMemory,
            scrumBoardsMemory = scrumBoardMemory
        )
        val viewBacklog = ViewBacklogAction(
            jiraSoftware = jiraSoftware,
            meter = meter,
            boardMemory = scrumBoardMemory
        ) { it.issuesInBacklog != 0 }
        val actionProportions = mapOf(
            createIssue to 5,
            searchWithJql to 19,
            viewIssue to 55,
            projectSummary to 5,
            viewDashboard to 10,
            editIssue to 5,
            addComment to 2,
            browseProjects to 5,
            viewBoard to 10,
            viewBacklog to 10,
            browseBoards to 2
        )

        return ActionShuffler.createRandomisedScenario(seededRandom, actionProportions, findInitialIssues)
    }
}
