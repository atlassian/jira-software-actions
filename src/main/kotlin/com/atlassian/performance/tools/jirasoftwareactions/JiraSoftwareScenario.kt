package com.atlassian.performance.tools.jirasoftwareactions

import com.atlassian.performance.tools.jiraactions.SeededRandom
import com.atlassian.performance.tools.jiraactions.WebJira
import com.atlassian.performance.tools.jiraactions.action.*
import com.atlassian.performance.tools.jiraactions.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.memories.adaptive.AdaptiveIssueKeyMemory
import com.atlassian.performance.tools.jiraactions.memories.adaptive.AdaptiveIssueMemory
import com.atlassian.performance.tools.jiraactions.memories.adaptive.AdaptiveJqlMemory
import com.atlassian.performance.tools.jiraactions.memories.adaptive.AdaptiveProjectMemory
import com.atlassian.performance.tools.jiraactions.scenario.Scenario
import com.atlassian.performance.tools.jiraactions.scenario.addMultiple
import com.atlassian.performance.tools.jirasoftwareactions.actions.BrowseBoardsAction
import com.atlassian.performance.tools.jirasoftwareactions.actions.ViewBoardAction
import com.atlassian.performance.tools.jirasoftwareactions.memories.AdaptiveBoardIdMemory

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
        val agileBoardIdMemory = AdaptiveBoardIdMemory(seededRandom)
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
            boardIdMemory = agileBoardIdMemory,
            issueKeyMemory = issueKeyMemory
        )
        val browseBoards = BrowseBoardsAction(
            jiraSoftware = jiraSoftware,
            meter = meter,
            boardsMemory = agileBoardIdMemory
        )
        val actionProportions = mapOf(
            createIssue to 5,
            searchWithJql to 20,
            viewIssue to 55,
            projectSummary to 5,
            viewDashboard to 10,
            editIssue to 5,
            addComment to 2,
            browseProjects to 5,
            viewBoard to 10,
            browseBoards to 2
        )
        actionProportions.entries.forEach { scenario.addMultiple(element = it.key, repeats = it.value) }
        scenario.shuffle(seededRandom.random)
        return scenario
    }
}